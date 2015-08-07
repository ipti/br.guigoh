/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.util.UploadService;
import br.org.ipti.guigoh.model.entity.DiscussionTopic;
import br.org.ipti.guigoh.model.entity.DiscussionTopicFiles;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.Tags;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.DiscussionTopicFilesJpaController;
import br.org.ipti.guigoh.model.jpa.controller.DiscussionTopicJpaController;
import br.org.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.TagsJpaController;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.Part;

/**
 *
 * @author Joe
 */
@ViewScoped
@Named
public class DiscussionTopicCreateBean implements Serializable {

    private static final char ACTIVE = 'A', DISABLED = 'D', WARNING = 'W', TOPIC = 'T', MSG = 'M';

    private DiscussionTopic discussionTopic;
    private Users user;
    private SocialProfile socialProfile;
    private Interests theme;
    private Tags tag;

    private String tagInput;
    private Integer themeID;

    private transient Part fileMedia;
    private transient List<Part> fileList;

    private List<Tags> tagList;

    private InterestsJpaController interestsJpaController;
    private SocialProfileJpaController socialProfileJpaController;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
        }
    }

    public void addTag() throws UnsupportedEncodingException {
        if (tagList.size() < 3) {
            tagInput = new String(tagInput.getBytes("ISO-8859-1"), "UTF-8");
            if (!tagInput.equals("")) {
                boolean exists = false;
                for (Tags t : tagList) {
                    if (t.getName().equals(tagInput)) {
                        exists = true;
                    }
                }
                if (!exists) {
                    tag.setName(tagInput);
                    tagList.add(tag);
                    tag = new Tags();
                    tagInput = "";
                }
            }
        }
    }

    public void addMedia() throws IOException {
        if (fileList.size() < 3) {
            if (!fileMedia.getSubmittedFileName().equals("") && fileMedia.getSubmittedFileName().contains(".")) {
                fileList.add(fileMedia);
            }
        }
    }

    public void removeMedia(Part media) {
        fileList.remove(media);
    }

    public void createTopic() throws Exception {
        try {
            Calendar c = Calendar.getInstance(TimeZone.getDefault());
            discussionTopic.setTitle(new String(discussionTopic.getTitle().getBytes("ISO-8859-1"), "UTF-8"));
            discussionTopic.setBody(new String(discussionTopic.getBody().getBytes("ISO-8859-1"), "UTF-8"));
            discussionTopic.setData(c.getTime());
            discussionTopic.setSocialProfileId(socialProfile);
            discussionTopic.setStatus(ACTIVE);
            discussionTopic.setThemeId(theme);
            DiscussionTopicJpaController discussionTopicJpaController = new DiscussionTopicJpaController();
            discussionTopicJpaController.create(discussionTopic);
            TagsJpaController tagsJpaController = new TagsJpaController();
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
                    String filePath = File.separator + "home" + File.separator + "www" + File.separator + "com.guigoh.cdn" + File.separator + "guigoh" + File.separator + "discussionFiles" + File.separator + "topic" + File.separator + discussionTopic.getId() + File.separator;
                    UploadService.uploadFile(part, filePath);
                    DiscussionTopicFiles discussionTopicFiles = new DiscussionTopicFiles();
                    String[] fileSplit = part.getSubmittedFileName().split("\\.");
                    discussionTopicFiles.setFileName(part.getSubmittedFileName().replace("." + fileSplit[fileSplit.length - 1], ""));
                    discussionTopicFiles.setFileType(fileSplit[fileSplit.length - 1]);
                    discussionTopicFiles.setFilepath("http://cdn.guigoh.com/guigoh/discussionFiles/topic/" + discussionTopic.getId() + "/" + part.getSubmittedFileName());
                    discussionTopicFiles.setFkType(TOPIC);
                    discussionTopicFiles.setFkId(discussionTopic.getId());
                    discussionTopicFilesJpaController.create(discussionTopicFiles);
                }
            }
            discussionTopic = new DiscussionTopic();
            tagList = new ArrayList<>();
            FacesContext.getCurrentInstance().getExternalContext().redirect("/theme/view.xhtml?id=" + themeID);
        } catch (IOException e) {
        }
    }

    private void initGlobalVariables() {
        interestsJpaController = new InterestsJpaController();
        socialProfileJpaController = new SocialProfileJpaController();
        
        tagInput = "";
        
        discussionTopic = new DiscussionTopic();
        tag = new Tags();
        user = new Users();
        
        tagList = new ArrayList<>();
        fileList = new ArrayList<>();
        
        user.setUsername(CookieService.getCookie("user"));
        user.setToken(CookieService.getCookie("token"));
        
        socialProfile = socialProfileJpaController.findSocialProfile(user.getToken());
        theme = interestsJpaController.findInterestsById(themeID);
    }

    public DiscussionTopic getDiscussionTopic() {
        return discussionTopic;
    }

    public void setDiscussionTopic(DiscussionTopic discussionTopic) {
        this.discussionTopic = discussionTopic;
    }

    public Integer getThemeID() {
        return themeID;
    }

    public void setThemeID(Integer themeID) {
        this.themeID = themeID;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public SocialProfile getSocialProfile() {
        return socialProfile;
    }

    public void setSocialProfile(SocialProfile socialProfile) {
        this.socialProfile = socialProfile;
    }

    public Interests getTheme() {
        return theme;
    }

    public void setTheme(Interests theme) {
        this.theme = theme;
    }

    public List<Tags> getTags() {
        return tagList;
    }

    public void setTags(List<Tags> tagList) {
        this.tagList = tagList;
    }

    public Tags getTag() {
        return tag;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }

    public Part getFileMedia() {
        return fileMedia;
    }

    public void setFileMedia(Part fileMedia) {
        this.fileMedia = fileMedia;
    }

    public List<Part> getFileList() {
        return fileList;
    }

    public void setFileList(List<Part> fileList) {
        this.fileList = fileList;
    }

    public String getTagInput() {
        return tagInput;
    }

    public void setTagInput(String tagInput) {
        this.tagInput = tagInput;
    }
}
