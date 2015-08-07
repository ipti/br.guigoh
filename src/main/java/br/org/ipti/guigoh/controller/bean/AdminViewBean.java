/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.util.MailService;
import br.org.ipti.guigoh.util.translator.Translator;
import br.org.ipti.guigoh.model.entity.UserAuthorization;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UserAuthorizationJpaController;
import br.org.ipti.guigoh.model.jpa.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.mail.EmailException;

/**
 *
 * @author Joe
 */
@ViewScoped
@ManagedBean(name = "adminViewBean")
public class AdminViewBean implements Serializable {

    public static final String ACTIVE_ACCESS = "AC", INACTIVE_ACCESS = "IC",
    FIRST_ACCESS = "FC", PENDING_ACCESS = "PC", ADMIN = "AD", REVISER = "RE",
    ACCEPTED = "AC", REJECTED = "RE", DEACTIVATED = "DE";

    private boolean admin, reviser;

    private UserAuthorization authorization;
    private SocialProfile socialProfile;
    private Translator trans;

    private List<SocialProfile> socialProfileList;
    private List<UserAuthorization> pendingUserList, activeUserList, inactiveUserList;
    //private List<UserAuthorization> authorizationList;
    private List<EducationalObject> pendingEducationalObjectList, activeEducationalObjectList, inactiveEducationalObjectList;

    private Map<Integer, Boolean> checked;

    private EducationalObjectJpaController educationalObjectJpaController;
    private UserAuthorizationJpaController userAuthorizationJpaController;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
            
            getLoggedSocialProfile();
            getUserRole();
            checkAuthorization();
            getPendingUsers();
            getActiveUsers();
            getInactiveUsers();
            getPendingEducationalObjects();
            getActiveEducationalObjects();
            getInactiveEducationalObjects();
        }
    }

    private void getLoggedSocialProfile() {
        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
        socialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));
    }

    private void getUserRole() {
        authorization = userAuthorizationJpaController.findAuthorization(socialProfile.getTokenId());
        if (authorization.getRoles().equals(ADMIN)) {
            admin = true;
        }
        if (authorization.getRoles().equals(REVISER)) {
            reviser = true;
        }
    }

    private void checkAuthorization() {
        if (!admin && !reviser) {
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "islogged");
        }
    }

//    private void getPendingUsers() {
//        pendingUserList = authorizationBO.getAll();
//        if (socialProfile.getSubnetworkId() != null) {
//            authorizationList = authorizationBO.findAuthorizationByActive(socialProfile.getSubnetworkId().getId());
//        }
//    }
    private void getPendingUsers() {
        pendingUserList = userAuthorizationJpaController.getPendingUsers();
    }

    private void getActiveUsers() {
        activeUserList = userAuthorizationJpaController.getActiveUsers();
    }

    private void getInactiveUsers() {
        inactiveUserList = userAuthorizationJpaController.getInactiveUsers();
    }

    private void getPendingEducationalObjects() {
        pendingEducationalObjectList = educationalObjectJpaController.getPendingEducationalObjects();
    }

    private void getActiveEducationalObjects() {
        activeEducationalObjectList = educationalObjectJpaController.getActiveEducationalObjects();
    }

    private void getInactiveEducationalObjects() {
        inactiveEducationalObjectList = educationalObjectJpaController.getInactiveEducationalObjects();
    }

    public void acceptUser(String token) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            UserAuthorization user = userAuthorizationJpaController.findAuthorization(token);
            user.setStatus(FIRST_ACCESS);
            userAuthorizationJpaController.edit(user);
            String mailSubject = "Cadastro aceito";
            String mailText = "Bem-vindo!\n\nSeu cadastro no Arte com Ciência foi aceito por um administrador.\n\n"
                    + "Clique no link abaixo para começar a utilizar sua conta.\n\n";
            trans.setLocale(user.getUsers().getSocialProfile().getLanguageId().getAcronym());
            mailSubject = trans.getWord(mailSubject);
            mailText = trans.getWord(mailText);
            mailText += "http://artecomciencia.guigoh.com/login/auth.xhtml";
//            mailText += "http://rts.guigoh.com:8080/login/auth.xhtml";
            MailService.sendMail(mailText, mailSubject, user.getUsers().getUsername());
            trans.setLocale(CookieService.getCookie("locale"));
            getActiveUsers();
            getPendingUsers();
        } catch (EmailException e) {
        }
    }

    public void declineUser(String token) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            UserAuthorization user = userAuthorizationJpaController.findAuthorization(token);
            user.setStatus(INACTIVE_ACCESS);
            userAuthorizationJpaController.edit(user);
            String mailSubject = "Cadastro negado";
            String mailText = "Seu cadastro no Arte com Ciência foi negado por um administrador.";
            trans.setLocale(user.getUsers().getSocialProfile().getLanguageId().getAcronym());
            mailSubject = trans.getWord(mailSubject);
            mailText = trans.getWord(mailText);
            MailService.sendMail(mailText, mailSubject, user.getUsers().getUsername());
            trans.setLocale(CookieService.getCookie("locale"));
            getInactiveUsers();
            getPendingUsers();
        } catch (EmailException e) {
        }
    }

    public void deactivateUser(String token) throws NonexistentEntityException, RollbackFailureException, Exception {
        UserAuthorization user = userAuthorizationJpaController.findAuthorization(token);
        user.setStatus(INACTIVE_ACCESS);
        userAuthorizationJpaController.edit(user);
        getActiveUsers();
        getInactiveUsers();
    }

    public void activateUser(String token) throws NonexistentEntityException, RollbackFailureException, Exception {
        UserAuthorization user = userAuthorizationJpaController.findAuthorization(token);
        user.setStatus(ACTIVE_ACCESS);
        userAuthorizationJpaController.edit(user);
        getActiveUsers();
        getInactiveUsers();
    }

    public void acceptEducationalObject(Integer id) throws Exception {
        EducationalObject educationalObject = educationalObjectJpaController.findEducationalObject(id);
        educationalObject.setStatus(ACCEPTED);
        educationalObjectJpaController.edit(educationalObject);
        getActiveEducationalObjects();
        getPendingEducationalObjects();
    }

    public void declineEducationalObject(Integer id) throws Exception {
        EducationalObject educationalObject = educationalObjectJpaController.findEducationalObject(id);
        educationalObject.setStatus(REJECTED);
        educationalObjectJpaController.edit(educationalObject);
        getPendingEducationalObjects();
    }

    public void deactivateEducationalObject(Integer id) throws Exception {
        EducationalObject educationalObject = educationalObjectJpaController.findEducationalObject(id);
        educationalObject.setStatus(DEACTIVATED);
        educationalObjectJpaController.edit(educationalObject);
        getActiveEducationalObjects();
        getInactiveEducationalObjects();
    }

    public void activateEducationalObject(Integer id) throws Exception {
        EducationalObject educationalObject = educationalObjectJpaController.findEducationalObject(id);
        educationalObject.setStatus(ACCEPTED);
        educationalObjectJpaController.edit(educationalObject);
        getActiveEducationalObjects();
        getInactiveEducationalObjects();
    }

//    public void submitSelections() {
//        try {
//            List<UserAuthorization> checkedItems = new ArrayList<>();
//            for (UserAuthorization authorizations : authorizationList) {
//                if (checked.get(authorizations.getUsers().getSocialProfile().getSocialProfileId())) {
//                    checkedItems.add(authorizations);
//                }
//            }
//            checked.clear();
//            for (UserAuthorization authorizations : checkedItems) {
//                authorizations.setStatus(FIRST_ACCESS);
//                authorizationBO.edit(authorizations);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        getPendingUsers();
//    }
    private void initGlobalVariables() {
        educationalObjectJpaController = new EducationalObjectJpaController();
        userAuthorizationJpaController = new UserAuthorizationJpaController();
        
        authorization = new UserAuthorization();
        socialProfile = new SocialProfile();
        socialProfileList = new ArrayList<>();
        //authorizationList = new ArrayList<>();
        
        checked = new HashMap<>();
        
        admin = reviser = false;
        
        trans = new Translator();
        trans.setLocale(CookieService.getCookie("locale"));
    }

    public UserAuthorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(UserAuthorization authorization) {
        this.authorization = authorization;
    }

    public SocialProfile getSocialProfile() {
        return socialProfile;
    }

    public void setSocialProfile(SocialProfile socialProfile) {
        this.socialProfile = socialProfile;
    }

    public List<SocialProfile> getSocialProfileList() {
        return socialProfileList;
    }

    public void setSocialProfileList(List<SocialProfile> socialProfileList) {
        this.socialProfileList = socialProfileList;
    }

//    public List<UserAuthorization> getAuthorizationList() {
//        return authorizationList;
//    }
//
//    public void setAuthorizationList(List<UserAuthorization> authorizationList) {
//        this.authorizationList = authorizationList;
//    }
    public List<UserAuthorization> getPendingUserList() {
        return pendingUserList;
    }

    public void setPendingUserList(List<UserAuthorization> pendingUserList) {
        this.pendingUserList = pendingUserList;
    }

    public List<UserAuthorization> getActiveUserList() {
        return activeUserList;
    }

    public void setActiveUserList(List<UserAuthorization> activeUserList) {
        this.activeUserList = activeUserList;
    }

    public List<UserAuthorization> getInactiveUserList() {
        return inactiveUserList;
    }

    public void setInactiveUserList(List<UserAuthorization> inactiveUserList) {
        this.inactiveUserList = inactiveUserList;
    }

    public List<EducationalObject> getPendingEducationalObjectList() {
        return pendingEducationalObjectList;
    }

    public void setPendingEducationalObjectList(List<EducationalObject> educationalObjectList) {
        this.pendingEducationalObjectList = educationalObjectList;
    }

    public List<EducationalObject> getActiveEducationalObjectList() {
        return activeEducationalObjectList;
    }

    public void setActiveEducationalObjectList(List<EducationalObject> educationalObjectList) {
        this.activeEducationalObjectList = educationalObjectList;
    }

    public List<EducationalObject> getInactiveEducationalObjectList() {
        return inactiveEducationalObjectList;
    }

    public void setInactiveEducationalObjectList(List<EducationalObject> educationalObjectList) {
        this.inactiveEducationalObjectList = educationalObjectList;
    }

    public Map<Integer, Boolean> getChecked() {
        return checked;
    }

    public void setChecked(Map<Integer, Boolean> checked) {
        this.checked = checked;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean Admin) {
        this.admin = Admin;
    }

    public boolean isReviser() {
        return reviser;
    }

    public void setReviser(boolean Reviser) {
        this.reviser = Reviser;
    }
}
