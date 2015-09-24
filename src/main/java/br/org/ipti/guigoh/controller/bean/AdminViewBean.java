/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.UserAuthorization;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UserAuthorizationJpaController;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.util.MailService;
import br.org.ipti.guigoh.util.translator.Translator;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Joe
 */
@ViewScoped
@Named
public class AdminViewBean implements Serializable {

    private Integer pendingUsersLimit, deactivatedUsersLimit, pendingObjectsLimit, deactivatedObjectsLimit;

    private SocialProfile mySocialProfile;
    private Translator trans;

    private List<UserAuthorization> pendingUserAuthorizationList, deactivatedUserAuthorizationList;
    private List<EducationalObject> pendingEducationalObjectList, deactivatedEducationalObjectList;

    private SocialProfileJpaController socialProfileJpaController;
    private UserAuthorizationJpaController userAuthorizationJpaController;
    private EducationalObjectJpaController educationalObjectJpaController;

    public void init() throws IOException {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
        }
    }

    public void acceptUser(UserAuthorization userAuthorization) throws NonexistentEntityException, RollbackFailureException, Exception {
        userAuthorization.setStatus("AC");
        userAuthorizationJpaController.edit(userAuthorization);
        String mailSubject = "Cadastro aceito";
        String mailText = "Bem-vindo!\n\nSeu cadastro no Arte com Ciência foi aceito por um administrador.\n\n"
                + "Clique no link abaixo para começar a utilizar sua conta.\n\n";
        trans.setLocale(userAuthorization.getUsers().getSocialProfile().getLanguageId().getAcronym());
        mailSubject = trans.getWord(mailSubject);
        mailText = trans.getWord(mailText);
        mailText += "http://artecomciencia.guigoh.com/login/auth.xhtml";
        MailService.sendMail(mailText, mailSubject, userAuthorization.getUsers().getUsername());
        trans.setLocale(CookieService.getCookie("locale"));
        pendingUserAuthorizationList.remove(userAuthorization);
    }

    public void rejectUser(UserAuthorization userAuthorization) throws NonexistentEntityException, RollbackFailureException, Exception {
        userAuthorization.setStatus("IC");
        userAuthorizationJpaController.edit(userAuthorization);
        String mailSubject = "Cadastro negado";
        String mailText = "Seu cadastro no Arte com Ciência foi negado por um administrador.";
        trans.setLocale(userAuthorization.getUsers().getSocialProfile().getLanguageId().getAcronym());
        mailSubject = trans.getWord(mailSubject);
        mailText = trans.getWord(mailText);
        MailService.sendMail(mailText, mailSubject, userAuthorization.getUsers().getUsername());
        trans.setLocale(CookieService.getCookie("locale"));
        pendingUserAuthorizationList.remove(userAuthorization);
        deactivatedUserAuthorizationList.add(userAuthorization);
    }

    public void activateUser(UserAuthorization userAuthorization) throws NonexistentEntityException, RollbackFailureException, Exception {
        userAuthorization.setStatus("AC");
        userAuthorizationJpaController.edit(userAuthorization);
        deactivatedUserAuthorizationList.remove(userAuthorization);
    }
    
    public void rejectEducationalObject(EducationalObject educationalObject) throws NonexistentEntityException, RollbackFailureException, Exception {
        educationalObject.setStatus("DE");
        educationalObjectJpaController.edit(educationalObject);
        pendingEducationalObjectList.remove(educationalObject);
        deactivatedEducationalObjectList.add(educationalObject);
        
    }
    
    public void acceptEducationalObject(EducationalObject educationalObject) throws NonexistentEntityException, RollbackFailureException, Exception {
        educationalObject.setStatus("AC");
        educationalObjectJpaController.edit(educationalObject);
        deactivatedEducationalObjectList.remove(educationalObject);
        pendingEducationalObjectList.remove(educationalObject);
    }

    public void increaseLimit(String type) {
        switch (type) {
            case "PU":
                pendingUsersLimit += 9;
                break;
            case "DU":
                deactivatedUsersLimit += 9;
                break;
            case "PO":
                pendingObjectsLimit += 9;
                break;
            case "DO":
                deactivatedObjectsLimit += 9;
                break;
        }
    }

    private void initGlobalVariables() throws IOException {
        socialProfileJpaController = new SocialProfileJpaController();
        userAuthorizationJpaController = new UserAuthorizationJpaController();
        educationalObjectJpaController = new EducationalObjectJpaController();

        mySocialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));

        if (mySocialProfile.getUsers().getUserAuthorization().getRoles().equals("DE")) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/home.xhtml");
        } else {
            pendingUsersLimit = deactivatedUsersLimit = pendingObjectsLimit = deactivatedObjectsLimit = 9;
            
            pendingUserAuthorizationList = userAuthorizationJpaController.getPendingUsers();
            deactivatedUserAuthorizationList = userAuthorizationJpaController.getDeactivatedUsers();
            pendingEducationalObjectList = educationalObjectJpaController.getPendingEducationalObjects();
            deactivatedEducationalObjectList = educationalObjectJpaController.getInactiveEducationalObjects();
            
            trans = new Translator();
            trans.setLocale(CookieService.getCookie("locale"));
        }
    }

    public SocialProfile getMySocialProfile() {
        return mySocialProfile;
    }

    public void setMySocialProfile(SocialProfile mySocialProfile) {
        this.mySocialProfile = mySocialProfile;
    }

    public List<UserAuthorization> getPendingUserAuthorizationList() {
        return pendingUserAuthorizationList;
    }

    public void setPendingUserAuthorizationList(List<UserAuthorization> pendingUserAuthorizationList) {
        this.pendingUserAuthorizationList = pendingUserAuthorizationList;
    }

    public List<UserAuthorization> getDeactivatedUserAuthorizationList() {
        return deactivatedUserAuthorizationList;
    }

    public void setDeactivatedUserAuthorizationList(List<UserAuthorization> deactivatedUserAuthorizationList) {
        this.deactivatedUserAuthorizationList = deactivatedUserAuthorizationList;
    }

    public Integer getPendingUsersLimit() {
        return pendingUsersLimit;
    }

    public void setPendingUsersLimit(Integer pendingUsersLimit) {
        this.pendingUsersLimit = pendingUsersLimit;
    }

    public Integer getDeactivatedUsersLimit() {
        return deactivatedUsersLimit;
    }

    public void setDeactivatedUsersLimit(Integer deactivatedUsersLimit) {
        this.deactivatedUsersLimit = deactivatedUsersLimit;
    }

    public Integer getPendingObjectsLimit() {
        return pendingObjectsLimit;
    }

    public void setPendingObjectsLimit(Integer pendingObjectsLimit) {
        this.pendingObjectsLimit = pendingObjectsLimit;
    }

    public Integer getDeactivatedObjectsLimit() {
        return deactivatedObjectsLimit;
    }

    public void setDeactivatedObjectsLimit(Integer deactivatedObjectsLimit) {
        this.deactivatedObjectsLimit = deactivatedObjectsLimit;
    }

    public List<EducationalObject> getPendingEducationalObjectList() {
        return pendingEducationalObjectList;
    }

    public void setPendingEducationalObjectList(List<EducationalObject> pendingEducationalObjectList) {
        this.pendingEducationalObjectList = pendingEducationalObjectList;
    }

    public List<EducationalObject> getDeactivatedEducationalObjectList() {
        return deactivatedEducationalObjectList;
    }

    public void setDeactivatedEducationalObjectList(List<EducationalObject> deactivatedEducationalObjectList) {
        this.deactivatedEducationalObjectList = deactivatedEducationalObjectList;
    }

}
