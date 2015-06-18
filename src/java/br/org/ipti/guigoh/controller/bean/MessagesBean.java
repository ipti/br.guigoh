/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.model.entity.MessengerMessages;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.MessengerMessagesJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
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
    private MessengerMessagesJpaController messengerMessagesJpaController;
    private SocialProfileJpaController socialProfileJpaController;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            user = new Users();
            contactSocialProfile = new SocialProfile();
            messagesList = new ArrayList<>();
            getCookie();
            socialProfileJpaController = new SocialProfileJpaController();
            socialProfile = socialProfileJpaController.findSocialProfile(user.getToken());
            messengerMessagesJpaController = new MessengerMessagesJpaController();
            loadContacts();
        }
    }

    private void loadContacts() {
        contactsList = new ArrayList<>();
        contactsList = messengerMessagesJpaController.getAllContacts(socialProfileJpaController.findSocialProfile(user.getToken()).getSocialProfileId());
    }

    private void getCookie() {
        user.setUsername(CookieService.getCookie("user"));
        user.setToken(CookieService.getCookie("token"));
    }
    
    public void getCurriculumMessages(){
        isCurriculum = true;
        messagesList = messengerMessagesJpaController.getAllCurriculumMessages(socialProfileJpaController.findSocialProfile(user.getToken()).getSocialProfileId());
    }
    
    public String goToProfile(Integer id) {
        return "/profile/viewProfile.xhtml?id=" + id;
    }

    public void getMessages(Integer socialProfileId) {
        isCurriculum = false;
        messagesList = messengerMessagesJpaController.getAllMessages(socialProfileJpaController.findSocialProfile(user.getToken()).getSocialProfileId(), socialProfileId);
        contactSocialProfile = socialProfileJpaController.findSocialProfileBySocialProfileId(socialProfileId);
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
