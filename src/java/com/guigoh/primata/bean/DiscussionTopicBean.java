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
import com.guigoh.primata.entity.DiscussionTopic;
import com.guigoh.primata.entity.DiscussionTopicFiles;
import com.guigoh.primata.entity.Interests;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.Tags;
import com.guigoh.primata.entity.Users;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Joe
 */
@SessionScoped
@ManagedBean(name = "discussionTopicBean")
public class DiscussionTopicBean {

    final TimeZone timeZone = TimeZone.getDefault();
    public static final char ACTIVE = 'A';
    public static final char DISABLED = 'D';
    public static final char WARNING = 'W';
    public static final char TOPIC = 'T';
    public static final char MSG = 'M';
    private DiscussionTopic discussionTopic;
    private List<Tags> tags;
    private int themesID;
    private Users user;
    private SocialProfile socialProfile;
    private Interests theme;
    private List<DiscussionTopicFiles> listDiscussionTopicFiles;
    private int files;
    private Tags tag;
    private String tagInput;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            if (discussionTopic == null) {
                discussionTopic = new DiscussionTopic();
                discussionTopic.setTitle("Um título que descreva o assunto tratado no seu tópico...");
            }
            if (listDiscussionTopicFiles == null) {
                listDiscussionTopicFiles = new ArrayList<DiscussionTopicFiles>();
            }
            tagInput = "#";
            if (tags == null) {
                tags = new ArrayList<Tags>();
            }
            tag = new Tags();
            
            user = new Users();
            loadUserCookie();
            SocialProfileBO spBO = new SocialProfileBO();
            socialProfile = spBO.findSocialProfile(user.getToken());
            InterestsBO interestsBO = new InterestsBO();

            theme = interestsBO.findInterestsByID(themesID);

            loadSessionFiles();
        }
    }

    private void loadUserCookie() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().trim().equalsIgnoreCase("user")) {
                    user.setUsername(cookie.getValue());
                } else if (cookie.getName().trim().equalsIgnoreCase("token")) {
                    user.setToken(cookie.getValue());
                }
            }
        }
    }

    private void loadSessionFiles() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        boolean isCrateTopicPage = facesContext.getViewRoot().getViewId().lastIndexOf("createTopic") > -1 ? true : false;
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession session = request.getSession();
        if (files == 1) {
            listDiscussionTopicFiles = (List<DiscussionTopicFiles>) session.getAttribute("listDiscussionTopicFiles");
        } else {
            session.removeAttribute("listDiscussionTopicFiles");
            listDiscussionTopicFiles = new ArrayList<DiscussionTopicFiles>();
        }
    }

    public void holdDiscussionTopic() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillTag() {
        tag.setName(tagInput);
    }

    public void addTag() {
        if (tags.size() <= 3) {
            if (!tag.getName().equals("#")) {
                tags.add(tag);
                tag = new Tags();
                tagInput = "#";
            }
        }
    }

    public boolean isTagsFilled() {
        return tags.size() == 3 ? true : false;
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
            for (Tags tag : tags) {
                tagsBO.create(tag);
                tagsBO.createTagsDiscussionTopic(tag, discussionTopic);
            }
            if (listDiscussionTopicFiles != null) {
                DiscussionTopicFilesBO discussionTopicFilesBO = new DiscussionTopicFilesBO();
                for (DiscussionTopicFiles discussionTopicFiles : listDiscussionTopicFiles) {
                    discussionTopicFiles.setFkType(TOPIC);
                    discussionTopicFiles.setFkId(discussionTopic.getId());
                    discussionTopicFilesBO.create(discussionTopicFiles);
                }
            }

            discussionTopic = new DiscussionTopic();
            tags = new ArrayList<Tags>();
            listDiscussionTopicFiles = new ArrayList<DiscussionTopicFiles>();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            HttpSession session = request.getSession();
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

    public int getThemesID() {
        return themesID;
    }

    public void setThemesID(int themesID) {
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

    public String getTagInput() {
        return tagInput;
    }

    public void setTagInput(String tagInput) {
        this.tagInput = tagInput;
    }

    public List<DiscussionTopicFiles> getListDiscussionTopicFiles() {
        loadSessionFiles();
        return listDiscussionTopicFiles;
    }

    public void setListDiscussionTopicFiles(List<DiscussionTopicFiles> listDiscussionTopicFiles) {
        this.listDiscussionTopicFiles = listDiscussionTopicFiles;
    }

    public int getFiles() {
        return files;
    }

    public void setFiles(int files) {
        this.files = files;
    }
}
