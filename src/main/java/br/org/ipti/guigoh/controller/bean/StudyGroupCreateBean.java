/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.DiscussionTopic;
import br.org.ipti.guigoh.model.entity.DiscussionTopicFiles;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.Tags;
import br.org.ipti.guigoh.model.jpa.controller.DiscussionTopicFilesJpaController;
import br.org.ipti.guigoh.model.jpa.controller.DiscussionTopicJpaController;
import br.org.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.TagsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UtilJpaController;
import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.util.UploadService;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.Part;

/**
 *
 * @author iptipc008
 */
@ViewScoped
@Named
public class StudyGroupCreateBean implements Serializable {

    private SocialProfile mySocialProfile;
    private Interests interest;
    private DiscussionTopic discussionTopic;

    private String tag;
    private Integer interestId;

    private List<Interests> interestList;
    private List<Tags> tagList;

    private transient Part file;
    private transient List<Part> fileList;

    private InterestsJpaController interestsJpaController;
    private SocialProfileJpaController socialProfileJpaController;

    public void init() throws IOException {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
            if (mySocialProfile.getRoleId() != null && mySocialProfile.getRoleId().getName().equals("Visitante")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../home.xhtml");
            }
        }
    }

    public void addTag() {
        if (tagList.size() < 3) {
            boolean exists = false;
            for (Tags t : tagList) {
                if (t.getName().equals(tag)) {
                    exists = true;
                }
            }
            if (!exists) {
                Tags tag = new Tags();
                tag.setName(this.tag);
                tagList.add(tag);
                this.tag = "";
            }
        }
    }

    public void removeTag(int index) {
        tagList.remove(index);
    }

    public void addMedia() throws IOException {
        if (fileList.size() < 3) {
            if (!file.getSubmittedFileName().equals("") && file.getSubmittedFileName().contains(".")) {
                fileList.add(file);
            }
        }
    }

    public void removeMedia(Part media) {
        fileList.remove(media);
    }

    public void createTopic() throws Exception {
        UtilJpaController utilJpaController = new UtilJpaController();
        discussionTopic.setData(utilJpaController.getTimestampServerTime());
        discussionTopic.setSocialProfileId(mySocialProfile);
        discussionTopic.setStatus('A');
        discussionTopic.setThemeId(interest);
        discussionTopic.setViews(BigInteger.ZERO);
        DiscussionTopicJpaController discussionTopicJpaController = new DiscussionTopicJpaController();
        TagsJpaController tagsJpaController = new TagsJpaController();
        discussionTopicJpaController.create(discussionTopic);
        for (Tags t : tagList) {
            Tags tagDB = tagsJpaController.findTagByName(t.getName());
            if (tagDB == null) {
                tagsJpaController.create(t);
                tagsJpaController.createTagsXDiscussionTopic(t, discussionTopic);
            } else {
                tagsJpaController.createTagsXDiscussionTopic(tagDB, discussionTopic);
            }
        }
        if (!fileList.isEmpty()) {
            DiscussionTopicFilesJpaController discussionTopicFilesJpaController = new DiscussionTopicFilesJpaController();
            for (Part part : fileList) {
                String filePath = File.separator + "home" + File.separator + "www" + File.separator + "com.guigoh.cdn"
                        + File.separator + "guigoh" + File.separator + "discussionFiles" + File.separator + "topic"
                        + File.separator + discussionTopic.getId() + File.separator;
                UploadService.uploadFile(part, filePath, null);
                DiscussionTopicFiles discussionTopicFiles = new DiscussionTopicFiles();
                String[] fileSplit = part.getSubmittedFileName().split("\\.");
                discussionTopicFiles.setFileName(part.getSubmittedFileName().replace("." + fileSplit[fileSplit.length - 1], ""));
                discussionTopicFiles.setFileType(fileSplit[fileSplit.length - 1]);
                discussionTopicFiles.setFilepath("cdn.guigoh.com/guigoh/discussionFiles/topic/" + discussionTopic.getId() + "/" + part.getSubmittedFileName());
                discussionTopicFiles.setFkType('T');
                discussionTopicFiles.setFkId(discussionTopic.getId());
                discussionTopicFilesJpaController.create(discussionTopicFiles);
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("/study-group/library.xhtml?id=" + interestId);
    }

    private void initGlobalVariables() throws IOException {
        if (interestId == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/home.xhtml");
        } else {
            interestsJpaController = new InterestsJpaController();
            socialProfileJpaController = new SocialProfileJpaController();

            fileList = new ArrayList<>();

            mySocialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));
            interest = interestsJpaController.findInterests(interestId);
            discussionTopic = new DiscussionTopic();

            interestList = interestsJpaController.findInterestsEntities();
            tagList = new ArrayList<>();
        }
    }

    public SocialProfile getMySocialProfile() {
        return mySocialProfile;
    }

    public void setMySocialProfile(SocialProfile mySocialProfile) {
        this.mySocialProfile = mySocialProfile;
    }

    public List<Part> getFileList() {
        return fileList;
    }

    public void setFileList(List<Part> fileList) {
        this.fileList = fileList;
    }

    public Integer getInterestId() {
        return interestId;
    }

    public void setInterestId(Integer interestId) {
        this.interestId = interestId;
    }

    public List<Interests> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<Interests> interestList) {
        this.interestList = interestList;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public Interests getInterest() {
        return interest;
    }

    public void setInterest(Interests interest) {
        this.interest = interest;
    }

    public DiscussionTopic getDiscussionTopic() {
        return discussionTopic;
    }

    public void setDiscussionTopic(DiscussionTopic discussionTopic) {
        this.discussionTopic = discussionTopic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<Tags> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tags> tagList) {
        this.tagList = tagList;
    }
}
