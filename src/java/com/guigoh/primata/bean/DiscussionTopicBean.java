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
import com.guigoh.primata.entity.DiscussionTopic;
import com.guigoh.primata.entity.DiscussionTopicFiles;
import com.guigoh.primata.entity.Interests;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.Tags;
import com.guigoh.primata.entity.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Joe
 */
@SessionScoped
@ManagedBean(name = "discussionTopicBean")
public class DiscussionTopicBean implements Serializable {

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
    private List<DiscussionTopicFiles> discussionTopicFilesList;
    private Integer files;
    private Tags tag;
    private InterestsBO interestsBO;
    private SocialProfileBO socialProfileBO;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            discussionTopic = new DiscussionTopic();
            discussionTopicFilesList = new ArrayList<DiscussionTopicFiles>();
            tags = new ArrayList<Tags>();
            tag = new Tags();
            user = new Users();
            getCookie();
            socialProfileBO = new SocialProfileBO();
            socialProfile = socialProfileBO.findSocialProfile(user.getToken());
            interestsBO = new InterestsBO();
            theme = interestsBO.findInterestsByID(themesID);
            getSessionFiles();
        }
    }

    private void getCookie(){
        user.setUsername(CookieService.getCookie("user"));
        user.setToken(CookieService.getCookie("token"));
    }
    
    private HttpSession getSession(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getSession();
    }

    private void getSessionFiles() {
        HttpSession session = getSession();
        if (files == 1) {
            discussionTopicFilesList = (List<DiscussionTopicFiles>) session.getAttribute("listDiscussionTopicFiles");
        } else {
            session.removeAttribute("listDiscussionTopicFiles");
            discussionTopicFilesList = new ArrayList<DiscussionTopicFiles>();
        }
    }

    public void addTag() {
        if (tags.size() < 3) {
            if (!tag.getName().equals("")) {
                tags.add(tag);
                tag = new Tags();
            }
        }
    }


    public void createTopic() {
        try {
            Calendar c = Calendar.getInstance(TimeZone.getDefault());
            DiscussionTopicBO discussionTopicBO = new DiscussionTopicBO();
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
            if (discussionTopicFilesList != null) {
                DiscussionTopicFilesBO discussionTopicFilesBO = new DiscussionTopicFilesBO();
                for (DiscussionTopicFiles discussionTopicFiles : discussionTopicFilesList) {
                    discussionTopicFiles.setFkType(TOPIC);
                    discussionTopicFiles.setFkId(discussionTopic.getId());
                    discussionTopicFilesBO.create(discussionTopicFiles);
                }
            }
            discussionTopic = new DiscussionTopic();
            tags = new ArrayList<Tags>();
            discussionTopicFilesList = new ArrayList<DiscussionTopicFiles>();
            HttpSession session = getSession();
            session.removeAttribute("listDiscussionTopicFiles");
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

    public List<DiscussionTopicFiles> getListDiscussionTopicFiles() {
        getSessionFiles();
        return discussionTopicFilesList;
    }

    public void setListDiscussionTopicFiles(List<DiscussionTopicFiles> listDiscussionTopicFiles) {
        this.discussionTopicFilesList = listDiscussionTopicFiles;
    }

    public Integer getFiles() {
        return files;
    }

    public void setFiles(Integer files) {
        this.files = files;
    }
}
