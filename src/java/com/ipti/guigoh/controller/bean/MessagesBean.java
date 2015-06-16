/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipti.guigoh.controller.bean;

import com.guigoh.bo.MessengerMessagesBO;
import com.guigoh.bo.SocialProfileBO;
import com.ipti.guigoh.util.CookieService;
import com.ipti.guigoh.model.entity.MessengerMessages;
import com.ipti.guigoh.model.entity.SocialProfile;
import com.ipti.guigoh.model.entity.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author IPTI
 */
@ViewScoped
@ManagedBean(name = "messagesBean")
public class MessagesBean implements Serializable{

    private Users user;
    private List<SocialProfile> contactsList;
    private List<MessengerMessages> messagesList;
    private SocialProfile contactSocialProfile;
    private SocialProfile socialProfile;
    private Boolean isCurriculum;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            user = new Users();
            contactSocialProfile = new SocialProfile();
            messagesList = new ArrayList<>();
            getCookie();
            socialProfile = SocialProfileBO.findSocialProfile(user.getToken());
            loadContacts();
        }
    }

    private void loadContacts() {
        contactsList = new ArrayList<>();
        contactsList = MessengerMessagesBO.getAllContacts(SocialProfileBO.findSocialProfile(user.getToken()).getSocialProfileId());
    }

    private void getCookie() {
        user.setUsername(CookieService.getCookie("user"));
        user.setToken(CookieService.getCookie("token"));
    }
    
    public void getCurriculumMessages(){
        isCurriculum = true;
        messagesList = MessengerMessagesBO.getAllCurriculumMessages(SocialProfileBO.findSocialProfile(user.getToken()).getSocialProfileId());
    }
    
    public String goToProfile(Integer id) {
        return "/profile/viewProfile.xhtml?id=" + id;
    }

    public void getMessages(Integer socialProfileId) {
        isCurriculum = false;
        messagesList = MessengerMessagesBO.getAllMessages(SocialProfileBO.findSocialProfile(user.getToken()).getSocialProfileId(), socialProfileId);
        contactSocialProfile = SocialProfileBO.findSocialProfileBySocialProfileId(socialProfileId);
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
