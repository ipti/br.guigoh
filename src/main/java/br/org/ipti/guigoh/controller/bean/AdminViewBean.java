/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.UserAuthorization;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import br.org.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UserAuthorizationJpaController;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
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
 * @author ipti
 */
@ViewScoped
@Named
public class AdminViewBean implements Serializable {

    private Integer pendingUsersLimit, deactivatedUsersLimit, pendingObjectsLimit, deactivatedObjectsLimit;
    private Boolean hasPendingUsers, hasDeactivatedUsers, hasPendingObjects, hasDeactivatedObjects, correctPassword;
    private String search, insertType;

    private SocialProfile mySocialProfile;
    private Interests interestSelected;
    private Translator trans;

    private List<UserAuthorization> pendingUserAuthorizationList, deactivatedUserAuthorizationList, adminList;
    private List<EducationalObject> pendingEducationalObjectList, deactivatedEducationalObjectList;
    private List<SocialProfile> socialProfileList, chosenSocialProfileList;
    private List<Interests> interestList;

    private SocialProfileJpaController socialProfileJpaController;
    private UserAuthorizationJpaController userAuthorizationJpaController;
    private EducationalObjectJpaController educationalObjectJpaController;
    private InterestsJpaController interestsJpaController;

    public void init() throws IOException {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
        }
    }

    public void acceptUser(UserAuthorization userAuthorization) throws NonexistentEntityException, RollbackFailureException, Exception {
        userAuthorization.setStatus("AC");
        userAuthorizationJpaController.edit(userAuthorization);
        pendingUserAuthorizationList.remove(userAuthorization);
        String mailSubject = "Cadastro aceito";
        String mailText = "Bem-vindo!\n\nSeu cadastro no Arte com Ciência foi aceito por um administrador.\n\n"
                + "Clique no link abaixo para começar a utilizar sua conta.\n\n";
        trans.setLocale(userAuthorization.getUsers().getSocialProfile().getLanguageId().getAcronym());
        mailSubject = trans.getWord(mailSubject);
        mailText = trans.getWord(mailText);
        mailText += "http://artecomciencia.guigoh.com/login/auth.xhtml";
        MailService.sendMail(mailText, mailSubject, new String[] {userAuthorization.getUsers().getUsername()});
        trans.setLocale(CookieService.getCookie("locale"));
        hasPendingUsers = !pendingUserAuthorizationList.isEmpty();
    }

    public void rejectUser(UserAuthorization userAuthorization) throws NonexistentEntityException, RollbackFailureException, Exception {
        userAuthorization.setStatus("IC");
        userAuthorizationJpaController.edit(userAuthorization);
        pendingUserAuthorizationList.remove(userAuthorization);
        deactivatedUserAuthorizationList.add(userAuthorization);
        String mailSubject = "Cadastro negado";
        String mailText = "Seu cadastro no Arte com Ciência foi negado por um administrador.";
        trans.setLocale(userAuthorization.getUsers().getSocialProfile().getLanguageId().getAcronym());
        mailSubject = trans.getWord(mailSubject);
        mailText = trans.getWord(mailText);
        mailText += "\n\n" + userAuthorization.getInactiveReason(); 
        MailService.sendMail(mailText, mailSubject, new String[] {userAuthorization.getUsers().getUsername()});
        trans.setLocale(CookieService.getCookie("locale"));
        hasPendingUsers = !pendingUserAuthorizationList.isEmpty();
        hasDeactivatedUsers = !deactivatedUserAuthorizationList.isEmpty();
    }

    public void activateUser(UserAuthorization userAuthorization) throws NonexistentEntityException, RollbackFailureException, Exception {
        userAuthorization.setStatus("AC");
        userAuthorization.setInactiveReason(null);
        userAuthorizationJpaController.edit(userAuthorization);
        deactivatedUserAuthorizationList.remove(userAuthorization);
        String mailSubject = "Cadastro ativado";
        String mailText = "Olá!\n\nSua conta do Arte com Ciência foi ativada por um administrador.\n\n"
                + "Clique no link abaixo para começar a utilizar sua conta.\n\n";
        trans.setLocale(userAuthorization.getUsers().getSocialProfile().getLanguageId().getAcronym());
        mailSubject = trans.getWord(mailSubject);
        System.out.println(mailText);
        mailText = trans.getWord(mailText);
        mailText += "http://artecomciencia.guigoh.com/login/auth.xhtml";
        MailService.sendMail(mailText, mailSubject, new String[] {userAuthorization.getUsers().getUsername()});
        trans.setLocale(CookieService.getCookie("locale"));
        hasDeactivatedUsers = !deactivatedUserAuthorizationList.isEmpty();
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

    public void searchEvent(String type) {
        switch (type) {
            case "PU":
                if (!search.equals("")) {
                    pendingUserAuthorizationList = userAuthorizationJpaController.findUserAuthorizations(search, "PC");
                } else {
                    pendingUserAuthorizationList = userAuthorizationJpaController.getAllPendingUsers();
                }
                break;
            case "DU":
                if (!search.equals("")) {
                    deactivatedUserAuthorizationList = userAuthorizationJpaController.findUserAuthorizations(search, "IC");
                } else {
                    deactivatedUserAuthorizationList = userAuthorizationJpaController.getAllDeactivatedUsers();
                }
                break;
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
            case "AD":
            case "RE":
                if (!search.equals("")) {
                    List<Integer> excludedSocialProfileIdList = new ArrayList<>();
                    chosenSocialProfileList.stream().forEach((socialProfile) -> {
                        excludedSocialProfileIdList.add(socialProfile.getSocialProfileId());
                    });
                    if (type.equals("AD")) {
                        adminList.stream().forEach((admin) -> {
                            excludedSocialProfileIdList.add(admin.getUsers().getSocialProfile().getSocialProfileId());
                        });
                    } else {
                        interestSelected.getUsersCollection().stream().forEach((reviser) -> {
                            excludedSocialProfileIdList.add(reviser.getSocialProfile().getSocialProfileId());
                        });
                    }
                    socialProfileList = socialProfileJpaController.findSocialProfiles(search, mySocialProfile.getTokenId(), true, false, excludedSocialProfileIdList);
                } else {
                    socialProfileList = new ArrayList<>();
                }
                break;
            case "PW":
                if (!search.equals("")) {
                    correctPassword = search.equals("p@s4guigoh");
                } else {
                    correctPassword = false;
                }
                if (correctPassword) {
                    search = "";
                    insertType = "AD";
                }
                break;
        }
    }

    public void resetModal() {
        chosenSocialProfileList = new ArrayList<>();
        socialProfileList = new ArrayList<>();
        search = "";
    }

    public void selectUser(SocialProfile socialProfile) {
        socialProfileList.remove(socialProfile);
        chosenSocialProfileList.add(socialProfile);
    }

    public void removeChosenUser(SocialProfile socialProfile) {
        chosenSocialProfileList.remove(socialProfile);
        searchEvent("AD");
    }

    public void addUsers(String role) throws NonexistentEntityException, RollbackFailureException, Exception {
        if (role.equals("AD")) {
            for (SocialProfile socialProfile : chosenSocialProfileList) {
                socialProfile.getUsers().getUserAuthorization().setRoles("AD");
                userAuthorizationJpaController.edit(socialProfile.getUsers().getUserAuthorization());
                adminList.add(socialProfile.getUsers().getUserAuthorization());
            }
            resetModal();
        } else if (role.equals("RE")) {
            for (SocialProfile socialProfile : chosenSocialProfileList) {
                interestSelected.getUsersCollection().add(socialProfile.getUsers());
                interestsJpaController.edit(interestSelected);
            }
            resetModal();
        }
    }

    public void removeAdmin(UserAuthorization admin) throws NonexistentEntityException, RollbackFailureException, Exception {
        admin.setRoles("DE");
        userAuthorizationJpaController.edit(admin);
        adminList.remove(admin);
    }

    public void removeReviser(Users user) throws NonexistentEntityException, RollbackFailureException, Exception {
        interestSelected.getUsersCollection().remove(user);
        interestsJpaController.edit(interestSelected);
    }

    public void loadRevisersByInterest(Interests interest) {
        interestSelected = interest;
    }

    private void initGlobalVariables() throws IOException {
        socialProfileJpaController = new SocialProfileJpaController();
        userAuthorizationJpaController = new UserAuthorizationJpaController();
        educationalObjectJpaController = new EducationalObjectJpaController();
        interestsJpaController = new InterestsJpaController();

        mySocialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));

        if (mySocialProfile.getUsers().getUserAuthorization().getRoles().equals("DE")) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/home.xhtml");
        } else {
            pendingUsersLimit = deactivatedUsersLimit = pendingObjectsLimit = deactivatedObjectsLimit = 9;

            pendingUserAuthorizationList = userAuthorizationJpaController.getAllPendingUsers();
            deactivatedUserAuthorizationList = userAuthorizationJpaController.getAllDeactivatedUsers();
            pendingEducationalObjectList = educationalObjectJpaController.getPendingEducationalObjects();
            deactivatedEducationalObjectList = educationalObjectJpaController.getInactiveEducationalObjects();
            adminList = userAuthorizationJpaController.getAllAdmins();
            interestList = interestsJpaController.findInterestsEntities();

            interestSelected = interestList.get(0);

            hasPendingUsers = !pendingUserAuthorizationList.isEmpty();
            hasDeactivatedUsers = !deactivatedUserAuthorizationList.isEmpty();
            hasPendingObjects = !pendingEducationalObjectList.isEmpty();
            hasDeactivatedObjects = !deactivatedEducationalObjectList.isEmpty();

            chosenSocialProfileList = socialProfileList = new ArrayList<>();

            trans = new Translator();
            trans.setLocale(CookieService.getCookie("locale"));

            search = "";
            correctPassword = false;
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

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Boolean getHasPendingUsers() {
        return hasPendingUsers;
    }

    public void setHasPendingUsers(Boolean hasPendingUsers) {
        this.hasPendingUsers = hasPendingUsers;
    }

    public Boolean getHasDeactivatedUsers() {
        return hasDeactivatedUsers;
    }

    public void setHasDeactivatedUsers(Boolean hasDeactivatedUsers) {
        this.hasDeactivatedUsers = hasDeactivatedUsers;
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

    public List<UserAuthorization> getAdminList() {
        return adminList;
    }

    public void setAdminList(List<UserAuthorization> adminList) {
        this.adminList = adminList;
    }

    public List<SocialProfile> getSocialProfileList() {
        return socialProfileList;
    }

    public void setSocialProfileList(List<SocialProfile> socialProfileList) {
        this.socialProfileList = socialProfileList;
    }

    public List<SocialProfile> getChosenSocialProfileList() {
        return chosenSocialProfileList;
    }

    public void setChosenSocialProfileList(List<SocialProfile> chosenSocialProfileList) {
        this.chosenSocialProfileList = chosenSocialProfileList;
    }

    public Boolean getCorrectPassword() {
        return correctPassword;
    }

    public void setCorrectPassword(Boolean correctPassword) {
        this.correctPassword = correctPassword;
    }

    public List<Interests> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<Interests> interestList) {
        this.interestList = interestList;
    }

    public Interests getInterestSelected() {
        return interestSelected;
    }

    public void setInterestSelected(Interests interestSelected) {
        this.interestSelected = interestSelected;
    }

    public String getInsertType() {
        return insertType;
    }

    public void setInsertType(String insertType) {
        this.insertType = insertType;
    }
}
