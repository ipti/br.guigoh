/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.DiscussionTopicBO;
import com.guigoh.primata.bo.DiscussionTopicFilesBO;
import com.guigoh.primata.bo.InterestsBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.bo.TagsBO;
import com.guigoh.primata.bo.util.CookieService;
import com.guigoh.primata.bo.util.UploadService;
import com.guigoh.primata.entity.DiscussionTopic;
import com.guigoh.primata.entity.DiscussionTopicFiles;
import com.guigoh.primata.entity.Interests;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.Tags;
import com.guigoh.primata.entity.Users;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

/**
 *
 * @author Joe
 */
@ViewScoped
@ManagedBean(name = "createTopicBean")
public class CreateTopicBean implements Serializable {

    public static final char ACTIVE = 'A';
    public static final char DISABLED = 'D';
    public static final char WARNING = 'W';
    public static final char TOPIC = 'T';
    public static final char MSG = 'M';
    private DiscussionTopic discussionTopic;
    private List<Tags> tags;
    private Integer themesID;
    private Users user;
    private SocialProfile socialProfile;
    private Interests theme;
    private Tags tag;
    private String tagInput;
    private Part fileMedia;
    private List<Part> fileList;
    private InterestsBO interestsBO;
    private SocialProfileBO socialProfileBO;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            discussionTopic = new DiscussionTopic();
            tags = new ArrayList<Tags>();
            tag = new Tags();
            tagInput = "";
            user = new Users();
            fileList = new ArrayList<>();
            getCookie();
            socialProfileBO = new SocialProfileBO();
            socialProfile = socialProfileBO.findSocialProfile(user.getToken());
            interestsBO = new InterestsBO();
            theme = interestsBO.findInterestsByID(themesID);
        }
    }

    private void getCookie() {
        user.setUsername(CookieService.getCookie("user"));
        user.setToken(CookieService.getCookie("token"));
    }

    public void addTag() throws UnsupportedEncodingException {
        if (tags.size() < 3) {
            tagInput = new String(tagInput.getBytes("ISO-8859-1"), "UTF-8");
            if (!tagInput.equals("")) {
                tag.setName(tagInput);
                tags.add(tag);
                tag = new Tags();
                tagInput = "";
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

    public void createTopic() {
        try {
            Calendar c = Calendar.getInstance(TimeZone.getDefault());
            DiscussionTopicBO discussionTopicBO = new DiscussionTopicBO();
            discussionTopic.setTitle(new String(discussionTopic.getTitle().getBytes("ISO-8859-1"), "UTF-8"));
            discussionTopic.setBody(new String(discussionTopic.getBody().getBytes("ISO-8859-1"), "UTF-8"));
            discussionTopic.setData(c.getTime());
            discussionTopic.setSocialProfileId(socialProfile);
            discussionTopic.setStatus(ACTIVE);
            discussionTopic.setThemeId(theme);
            TagsBO tagsBO = new TagsBO();
            discussionTopicBO.create(discussionTopic);
            for (Tags t : tags) {
                tagsBO.create(t);
                tagsBO.createTagsDiscussionTopic(t, discussionTopic);
            }
            if (!fileList.isEmpty()) {
                DiscussionTopicFilesBO discussionTopicFilesBO = new DiscussionTopicFilesBO();
                for (Part part : fileList) {
                    String filePath = System.getProperty("user.home") + File.separator + "guigoh" + File.separator + "discussionFiles" + File.separator;
                    UploadService.uploadFile(part, filePath);
                    DiscussionTopicFiles discussionTopicFiles = new DiscussionTopicFiles();
                    String[] fileSplit = part.getSubmittedFileName().split("\\.");
                    discussionTopicFiles.setFileName(part.getSubmittedFileName().replace("."+fileSplit[fileSplit.length - 1], ""));
                    discussionTopicFiles.setFileType(fileSplit[fileSplit.length - 1]);
                    discussionTopicFiles.setFilepath("http://cdn.guigoh.com/discussionFiles/" + part.getSubmittedFileName());
                    discussionTopicFiles.setFkType(TOPIC);
                    discussionTopicFiles.setFkId(discussionTopic.getId());
                    discussionTopicFilesBO.create(discussionTopicFiles);
                }
            }
            discussionTopic = new DiscussionTopic();
            tags = new ArrayList<Tags>();
            FacesContext.getCurrentInstance().getExternalContext().redirect("/primata/theme/theme.xhtml?id=" + themesID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DiscussionTopic getDiscussionTopic() {
        return discussionTopic;
    }

    public void setDiscussionTopic(DiscussionTopic discussionTopic) {
        this.discussionTopic = discussionTopic;
    }

    public Integer getThemesID() {
        return themesID;
    }

    public void setThemesID(Integer themesID) {
        this.themesID = themesID;
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
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
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
