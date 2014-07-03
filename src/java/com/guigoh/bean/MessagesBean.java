/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bean;

import com.guigoh.bo.MessengerMessagesBO;
import com.guigoh.bo.SocialProfileBO;
import com.guigoh.bo.util.CookieService;
import com.guigoh.entity.MessengerMessages;
import com.guigoh.entity.SocialProfile;
import com.guigoh.entity.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author IPTI
 */
@SessionScoped
@ManagedBean(name = "messagesBean")
public class MessagesBean implements Serializable{

    private Users user;
    private List<SocialProfile> contactsList;
    private List<MessengerMessages> messagesList;
    private SocialProfile contactSocialProfile;
    private SocialProfile socialProfile;
    private Boolean isCurriculum;
    private SocialProfileBO spBO;
    private MessengerMessagesBO mmBO;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            user = new Users();
            spBO = new SocialProfileBO();
            mmBO = new MessengerMessagesBO();
            contactSocialProfile = new SocialProfile();
            messagesList = new ArrayList<MessengerMessages>();
            getCookie();
            socialProfile = spBO.findSocialProfile(user.getToken());
            loadContacts();
        }
    }

    private void loadContacts() {
        contactsList = new ArrayList<SocialProfile>();
        contactsList = mmBO.getAllContacts(spBO.findSocialProfile(user.getToken()).getSocialProfileId());
    }

    private void getCookie() {
        user.setUsername(CookieService.getCookie("user"));
        user.setToken(CookieService.getCookie("token"));
    }
    
    public void getCurriculumMessages(){
        isCurriculum = true;
        messagesList = mmBO.getAllCurriculumMessages(spBO.findSocialProfile(user.getToken()).getSocialProfileId());
    }
    
    public String goToProfile(Integer id) {
        return "/primata/profile/viewProfile.xhtml?id=" + id;
    }

    public void getMessages(Integer socialProfileId) {
        isCurriculum = false;
        messagesList = mmBO.getAllMessages(spBO.findSocialProfile(user.getToken()).getSocialProfileId(), socialProfileId);
        contactSocialProfile = spBO.findSocialProfileBySocialProfileId(socialProfileId);
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<SocialProfile> getContactsList() {
        return contactsList;
    }

    public void setContactsList(List<SocialProfile> contactsList) {
        this.contactsList = contactsList;
    }

    public List<MessengerMessages> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(List<MessengerMessages> messagesList) {
        this.messagesList = messagesList;
    }

    public SocialProfile getContactSocialProfile() {
        return contactSocialProfile;
    }

    public void setContactSocialProfile(SocialProfile contactSocialProfile) {
        this.contactSocialProfile = contactSocialProfile;
    }

    public SocialProfile getSocialProfile() {
        return socialProfile;
    }

    public void setSocialProfile(SocialProfile socialProfile) {
        this.socialProfile = socialProfile;
    }

    public Boolean getIsCurriculum() {
        return isCurriculum;
    }

    public void setIsCurriculum(Boolean isCurriculum) {
        this.isCurriculum = isCurriculum;
    }
    
}
