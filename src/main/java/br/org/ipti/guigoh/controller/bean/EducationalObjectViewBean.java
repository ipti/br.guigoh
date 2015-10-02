/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.EducationalObjectLike;
import br.org.ipti.guigoh.model.entity.EducationalObjectLikePK;
import br.org.ipti.guigoh.model.entity.EducationalObjectMessage;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectLikeJpaController;
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
    private Long likes;
    private String message;
    private Boolean like;

    private EducationalObject educationalObject;
    private SocialProfile mySocialProfile;

    private List<Interests> interestList;

    private EducationalObjectJpaController educationalObjectJpaController;
    private InterestsJpaController interestsJpaController;
    private UsersJpaController usersJpaController;
    private SocialProfileJpaController socialProfileJpaController;
    private UtilJpaController utilJpaController;
    private EducationalObjectLikeJpaController educationalObjectLikeJpaController;

    public void init() throws IOException {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
            educationalObjectJpaController.increaseViews(educationalObject.getId());
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
        EducationalObjectLikeJpaController educationalObjectLikeJpaController = new EducationalObjectLikeJpaController();
        EducationalObjectLike educationalObjectLike = new EducationalObjectLike(
                new EducationalObjectLikePK(educationalObjectId, mySocialProfile.getSocialProfileId()), utilJpaController.getTimestampServerTime(), educationalObject, mySocialProfile);
        if (educationalObjectLikeJpaController.findEducationalObjectLike(educationalObjectLike.getEducationalObjectLikePK()) != null){
            educationalObjectLikeJpaController.destroy(educationalObjectLike.getEducationalObjectLikePK());
            like = false;
        } else {
            educationalObjectLikeJpaController.create(educationalObjectLike);
            like = true;
        }
        likes = educationalObjectLikeJpaController.findEducationalObjectLikesQuantity(educationalObjectId);
    }
    
    public void deactivateEducationalObject() throws IOException, NonexistentEntityException, RollbackFailureException, Exception {
        educationalObject.setStatus("DE");
        educationalObjectJpaController.edit(educationalObject);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/admin/view.xhtml");
    }

    private void initGlobalVariables() throws IOException {
        educationalObjectJpaController = new EducationalObjectJpaController();
        interestsJpaController = new InterestsJpaController();
        usersJpaController = new UsersJpaController();
        socialProfileJpaController = new SocialProfileJpaController();
        utilJpaController = new UtilJpaController();
        educationalObjectLikeJpaController = new EducationalObjectLikeJpaController();

        educationalObject = new EducationalObject();

        if (educationalObjectId == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/home.xhtml");
        } else {
            educationalObject = educationalObjectJpaController.findEducationalObject(educationalObjectId);
            Collections.sort((List<EducationalObjectMessage>) educationalObject.getEducationalObjectMessageCollection(), (c1, c2) -> c2.getDate().compareTo(c1.getDate()));
            interestList = interestsJpaController.findInterestsEntities();
            mySocialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));
            like = educationalObject.getSocialProfileCollection().contains(mySocialProfile);
            likes = educationalObjectLikeJpaController.findEducationalObjectLikesQuantity(educationalObjectId);
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

    public SocialProfile getMySocialProfile() {
        return mySocialProfile;
    }

    public void setMySocialProfile(SocialProfile mySocialProfile) {
        this.mySocialProfile = mySocialProfile;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }
}
