/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.util.MailService;
import br.org.ipti.guigoh.util.translator.Translator;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author IPTIPC100
 */
@ViewScoped
@Named
public class ReviewViewBean implements Serializable {

    private Integer pendingObjectsLimit, deactivatedObjectsLimit;
    private Boolean hasPendingObjects, hasDeactivatedObjects;
    private String search;
    
    private SocialProfile mySocialProfile;
    private Translator trans;
    
    private List<EducationalObject> pendingEducationalObjectList, deactivatedEducationalObjectList;
    
    private SocialProfileJpaController socialProfileJpaController;
    private EducationalObjectJpaController educationalObjectJpaController;
    
    public void init() throws IOException {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
        }
    }
    
    public void rejectEducationalObject(EducationalObject educationalObject) throws NonexistentEntityException, RollbackFailureException, Exception {
        educationalObject.setStatus("DE");
        educationalObjectJpaController.edit(educationalObject);
        pendingEducationalObjectList.remove(educationalObject);
        deactivatedEducationalObjectList.add(educationalObject);
        String mailSubject = "Objeto Educacional negado";
        String mailText = "Seu objeto educacional no Arte com Ciência foi negado por um administrador.";
        trans.setLocale(educationalObject.getSocialProfileId().getLanguageId().getAcronym());
        mailSubject = trans.getWord(mailSubject);
        mailText = trans.getWord(mailText);
        mailText += "\n\n" + educationalObject.getName() + "\n\n" + educationalObject.getInactiveReason(); 
        MailService.sendMail(mailText, mailSubject, new String[] {educationalObject.getSocialProfileId().getUsers().getUsername()});
        trans.setLocale(CookieService.getCookie("locale"));
        hasPendingObjects = !pendingEducationalObjectList.isEmpty();
        hasDeactivatedObjects = !deactivatedEducationalObjectList.isEmpty();
    }

    public void acceptEducationalObject(EducationalObject educationalObject) throws NonexistentEntityException, RollbackFailureException, Exception {
        educationalObject.setStatus("AC");
        educationalObject.setInactiveReason(null);
        educationalObjectJpaController.edit(educationalObject);
        deactivatedEducationalObjectList.remove(educationalObject);
        pendingEducationalObjectList.remove(educationalObject);
        String mailSubject = "Objeto Educacional aceito";
        String mailText = "Seu objeto educacional no Arte com Ciência foi aceito por um administrador.";
        trans.setLocale(educationalObject.getSocialProfileId().getLanguageId().getAcronym());
        mailSubject = trans.getWord(mailSubject);
        mailText = trans.getWord(mailText);
        mailText += "\n\n" + educationalObject.getName();
        MailService.sendMail(mailText, mailSubject, new String[] {educationalObject.getSocialProfileId().getUsers().getUsername()});
        trans.setLocale(CookieService.getCookie("locale"));
        hasPendingObjects = !pendingEducationalObjectList.isEmpty();
        hasDeactivatedObjects = !deactivatedEducationalObjectList.isEmpty();
    }

    public void increaseLimit(String type) {
        switch (type) {
            case "PO":
                pendingObjectsLimit += 9;
                break;
            case "DO":
                deactivatedObjectsLimit += 9;
                break;
        }
    }
    
    public void searchEvent(String type) {
        switch (type) {
            case "PO":
                if (!search.equals("")) {
                    pendingEducationalObjectList = educationalObjectJpaController.findEducationalObjects(search, null, null, null, "PE");
                } else {
                    pendingEducationalObjectList = educationalObjectJpaController.getPendingEducationalObjects();
                }
                break;
            case "DO":
                if (!search.equals("")) {
                    deactivatedEducationalObjectList = educationalObjectJpaController.findEducationalObjects(search, null, null, null, "DE");
                } else {
                    deactivatedEducationalObjectList = educationalObjectJpaController.getInactiveEducationalObjects();
                }
                break;
        }
    }
    
    private void initGlobalVariables() throws IOException {
        socialProfileJpaController = new SocialProfileJpaController();
        educationalObjectJpaController = new EducationalObjectJpaController();

        mySocialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));

        if (mySocialProfile.getUsers().getInterestsCollection().isEmpty()) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/home.xhtml");
        } else {
            pendingObjectsLimit = deactivatedObjectsLimit = 9;

            pendingEducationalObjectList = educationalObjectJpaController.getPendingEducationalObjectsForReviewers(mySocialProfile.getTokenId());
            deactivatedEducationalObjectList = educationalObjectJpaController.getInactiveEducationalObjectsForReviewers(mySocialProfile.getTokenId());

            hasPendingObjects = !pendingEducationalObjectList.isEmpty();
            hasDeactivatedObjects = !deactivatedEducationalObjectList.isEmpty();

            trans = new Translator();
            trans.setLocale(CookieService.getCookie("locale"));

            search = "";
        }
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

    public Boolean getHasPendingObjects() {
        return hasPendingObjects;
    }

    public void setHasPendingObjects(Boolean hasPendingObjects) {
        this.hasPendingObjects = hasPendingObjects;
    }

    public Boolean getHasDeactivatedObjects() {
        return hasDeactivatedObjects;
    }

    public void setHasDeactivatedObjects(Boolean hasDeactivatedObjects) {
        this.hasDeactivatedObjects = hasDeactivatedObjects;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
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
