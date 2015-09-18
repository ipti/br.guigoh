/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.EducationalObjectMessage;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectMessageJpaController;
import br.org.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UsersJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UtilJpaController;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.util.CookieService;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author ipti008
 */
@ViewScoped
@Named
public class EducationalObjectViewBean implements Serializable {

    private Integer educationalObjectId, messagesLimit;
    private String message;
    private Boolean like;

    private EducationalObject educationalObject;
    private SocialProfile mySocialProfile;

    private List<Interests> interestList;

    private EducationalObjectJpaController educationalObjectJpaController;
    private InterestsJpaController interestsJpaController;
    private UsersJpaController usersJpaController;
    private SocialProfileJpaController socialProfileJpaController;

    public void init() throws IOException {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
        }
    }

    public Integer getIdByEmail(String email) {
        Users user = usersJpaController.findUsers(email);
        if (user != null) {
            return user.getSocialProfile().getSocialProfileId();
        } else {
            return null;
        }
    }

    public void publishMessage() throws Exception {
        if (!message.equals("") && message != null) {
            EducationalObjectMessageJpaController educationalObjectMessageJpaController = new EducationalObjectMessageJpaController();
            UtilJpaController utilJpaController = new UtilJpaController();
            EducationalObjectMessage educationalObjectMessage = new EducationalObjectMessage();
            educationalObjectMessage.setEducationalObjectFk(educationalObject);
            educationalObjectMessage.setMessage(message);
            educationalObjectMessage.setSocialProfileFk(mySocialProfile);
            educationalObjectMessage.setDate(utilJpaController.getTimestampServerTime());
            educationalObjectMessageJpaController.create(educationalObjectMessage);
            educationalObject.getEducationalObjectMessageCollection().add(educationalObjectMessage);
            Collections.sort((List<EducationalObjectMessage>) educationalObject.getEducationalObjectMessageCollection(), (c1, c2) -> c2.getDate().compareTo(c1.getDate()));
        }
        message = "";
    }

    public void increaseMessagesLimit() {
        messagesLimit += 5;
    }

    public void likeObject() throws NonexistentEntityException, RollbackFailureException, Exception {
        if (educationalObject.getSocialProfileCollection().contains(mySocialProfile)){
            educationalObject.getSocialProfileCollection().remove(mySocialProfile);
            like = false;
        } else {
            educationalObject.getSocialProfileCollection().add(mySocialProfile);
            like = true;
        }
        educationalObjectJpaController.edit(educationalObject);
        
    }

    private void initGlobalVariables() throws IOException {
        educationalObjectJpaController = new EducationalObjectJpaController();
        interestsJpaController = new InterestsJpaController();
        usersJpaController = new UsersJpaController();
        socialProfileJpaController = new SocialProfileJpaController();

        educationalObject = new EducationalObject();

        if (educationalObjectId == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/home.xhtml");
        } else {
            educationalObject = educationalObjectJpaController.findEducationalObject(educationalObjectId);
            Collections.sort((List<EducationalObjectMessage>) educationalObject.getEducationalObjectMessageCollection(), (c1, c2) -> c2.getDate().compareTo(c1.getDate()));
            interestList = interestsJpaController.findInterestsEntities();
            mySocialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));
            like = educationalObject.getSocialProfileCollection().contains(mySocialProfile);
            messagesLimit = 5;
        }
    }

    public EducationalObject getEducationalObject() {
        return educationalObject;
    }

    public void setEducationalObject(EducationalObject educationalObject) {
        this.educationalObject = educationalObject;
    }

    public Integer getEducationalObjectId() {
        return educationalObjectId;
    }

    public void setEducationalObjectId(Integer educationalObjectId) {
        this.educationalObjectId = educationalObjectId;
    }

    public List<Interests> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<Interests> interestList) {
        this.interestList = interestList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMessagesLimit() {
        return messagesLimit;
    }

    public void setMessagesLimit(Integer messagesLimit) {
        this.messagesLimit = messagesLimit;
    }

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }
}
