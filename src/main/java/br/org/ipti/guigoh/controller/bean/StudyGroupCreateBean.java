/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.DiscussionTopic;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.Tags;
import br.org.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.util.CookieService;
import java.io.IOException;
import java.io.Serializable;
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
    private Tags tag;
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
        }
    }

    public void addTag() {
        if (tagList.size() < 3) {
            boolean exists = false;
            for (Tags t : tagList) {
                if (t.getName().equals(tag.getName())){
                    exists = true;
                }
            }
            if (!exists) {
                tagList.add(tag);
                tag = new Tags();
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
//            Calendar c = Calendar.getInstance(TimeZone.getDefault());
//            discussionTopic.setTitle(new String(discussionTopic.getTitle().getBytes("ISO-8859-1"), "UTF-8"));
//            discussionTopic.setBody(new String(discussionTopic.getBody().getBytes("ISO-8859-1"), "UTF-8"));
//            discussionTopic.setData(c.getTime());
//            discussionTopic.setSocialProfileId(socialProfile);
//            discussionTopic.setStatus(ACTIVE);
//            discussionTopic.setThemeId(theme);
//            DiscussionTopicJpaController discussionTopicJpaController = new DiscussionTopicJpaController();
//            discussionTopicJpaController.create(discussionTopic);
//            TagsJpaController tagsJpaController = new TagsJpaController();
//            for (Tags t : tagList) {
//                Tags tagDB = tagsJpaController.findTagByName(t.getName());
//                if (tagDB == null) {
//                    tagsJpaController.create(t);
//                    tagsJpaController.createTagsXDiscussionTopic(t, discussionTopic);
//                } else {
//                    tagsJpaController.createTagsXDiscussionTopic(tagDB, discussionTopic);
//                }
//            }
//            if (!fileList.isEmpty()) {
//                DiscussionTopicFilesJpaController discussionTopicFilesJpaController = new DiscussionTopicFilesJpaController();
//                for (Part part : fileList) {
//                    String filePath = File.separator + "home" + File.separator + "www" + File.separator + "com.guigoh.cdn" + File.separator + "guigoh" + File.separator + "discussionFiles" + File.separator + "topic" + File.separator + discussionTopic.getId() + File.separator;
//                    UploadService.uploadFile(part, filePath, null);
//                    DiscussionTopicFiles discussionTopicFiles = new DiscussionTopicFiles();
//                    String[] fileSplit = part.getSubmittedFileName().split("\\.");
//                    discussionTopicFiles.setFileName(part.getSubmittedFileName().replace("." + fileSplit[fileSplit.length - 1], ""));
//                    discussionTopicFiles.setFileType(fileSplit[fileSplit.length - 1]);
//                    discussionTopicFiles.setFilepath("http://cdn.guigoh.com/guigoh/discussionFiles/topic/" + discussionTopic.getId() + "/" + part.getSubmittedFileName());
//                    discussionTopicFiles.setFkType(TOPIC);
//                    discussionTopicFiles.setFkId(discussionTopic.getId());
//                    discussionTopicFilesJpaController.create(discussionTopicFiles);
//                }
//            }
//            discussionTopic = new DiscussionTopic();
//            tagList = new ArrayList<>();
//            FacesContext.getCurrentInstance().getExternalContext().redirect("/theme/view.xhtml?id=" + themeID);
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
            tag = new Tags();
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

    public Tags getTag() {
        return tag;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }

    public List<Tags> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tags> tagList) {
        this.tagList = tagList;
    }
}
