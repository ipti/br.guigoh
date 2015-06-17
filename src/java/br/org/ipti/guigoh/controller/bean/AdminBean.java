/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import com.guigoh.bo.EducationalObjectBO;
import br.org.ipti.guigoh.model.entity.EducationalObject;
import com.guigoh.bo.UserAuthorizationBO;
import com.guigoh.bo.SocialProfileBO;
import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.util.MailService;
import br.org.ipti.guigoh.util.translator.Translator;
import br.org.ipti.guigoh.model.entity.UserAuthorization;
import br.org.ipti.guigoh.model.entity.SocialProfile;
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
@ManagedBean(name = "adminBean")
public class AdminBean implements Serializable {

    public static final String ACTIVE_ACCESS = "AC";
    public static final String INACTIVE_ACCESS = "IC";
    public static final String FIRST_ACCESS = "FC";
    public static final String PENDING_ACCESS = "PC";
    public static final String ADMIN = "AD";
    public static final String REVISER = "RE";
    public static final String ACCEPTED = "AC";
    public static final String REJECTED = "RE";
    public static final String DEACTIVATED = "DE";
    private boolean admin;
    private boolean reviser;
    private UserAuthorization authorization;
    private SocialProfile socialProfile;
    private List<SocialProfile> listSocialProfile;
    private List<UserAuthorization> pendingUserList;
    private List<UserAuthorization> activeUserList;
    private List<UserAuthorization> inactiveUserList;
    //private List<UserAuthorization> authorizationList;
    private List<EducationalObject> pendingEducationalObjectList;
    private List<EducationalObject> activeEducationalObjectList;
    private List<EducationalObject> inactiveEducationalObjectList;
    private Map<Integer, Boolean> checked;
    private Translator trans;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            authorization = new UserAuthorization();
            socialProfile = new SocialProfile();
            listSocialProfile = new ArrayList<>();
            //authorizationList = new ArrayList<>();
            checked = new HashMap<>();
            admin = false;
            reviser = false;
            trans = new Translator();
            trans.setLocale(CookieService.getCookie("locale"));
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
        socialProfile = SocialProfileBO.findSocialProfile(CookieService.getCookie("token"));
    }

    private void getUserRole() {
        authorization = UserAuthorizationBO.findAuthorizationByTokenId(socialProfile.getTokenId());
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
        pendingUserList = UserAuthorizationBO.getPendingUsers();
    }

    private void getActiveUsers() {
        activeUserList = UserAuthorizationBO.getActiveUsers();
    }

    private void getInactiveUsers() {
        inactiveUserList = UserAuthorizationBO.getInactiveUsers();
    }

    private void getPendingEducationalObjects() {
        pendingEducationalObjectList = EducationalObjectBO.getPendingEducationalObjects();
    }

    private void getActiveEducationalObjects() {
        activeEducationalObjectList = EducationalObjectBO.getActiveEducationalObjects();
    }

    private void getInactiveEducationalObjects() {
        inactiveEducationalObjectList = EducationalObjectBO.getInactiveEducationalObjects();
    }

    public void acceptUser(String token) {
        try {
            UserAuthorization user = UserAuthorizationBO.getUserAuthorization(token);
            user.setStatus(FIRST_ACCESS);
            UserAuthorizationBO.edit(user);
            String mailSubject = "Cadastro aceito";
            String mailText = "Bem-vindo!\n\nSeu cadastro no Arte com Ciência foi aceito por um administrador.\n\n"
                            + "Clique no link abaixo para começar a utilizar sua conta.\n\n";
            trans.setLocale(user.getUsers().getSocialProfile().getLanguageId().getAcronym());
            mailSubject = trans.getWord(mailSubject);
            mailText = trans.getWord(mailText);
            mailText += "http://artecomciencia.guigoh.com/auth/login.xhtml";
//            mailText += "http://rts.guigoh.com:8080/auth/login.xhtml";
            MailService.sendMail(mailText, mailSubject, user.getUsers().getUsername());
            trans.setLocale(CookieService.getCookie("locale"));
            getActiveUsers();
            getPendingUsers();
        } catch (EmailException e) {
        }
    }

    public void declineUser(String token) {
        try {
            UserAuthorization user = UserAuthorizationBO.getUserAuthorization(token);
            user.setStatus(INACTIVE_ACCESS);
            UserAuthorizationBO.edit(user);
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

    public void deactivateUser(String token) {
        UserAuthorization user = UserAuthorizationBO.getUserAuthorization(token);
        user.setStatus(INACTIVE_ACCESS);
        UserAuthorizationBO.edit(user);
        getActiveUsers();
        getInactiveUsers();
    }

    public void activateUser(String token) {
        UserAuthorization user = UserAuthorizationBO.getUserAuthorization(token);
        user.setStatus(ACTIVE_ACCESS);
        UserAuthorizationBO.edit(user);
        getActiveUsers();
        getInactiveUsers();
    }

    public void acceptEducationalObject(Integer id) {
        EducationalObject educationalObject = EducationalObjectBO.getEducationalObject(id);
        educationalObject.setStatus(ACCEPTED);
        EducationalObjectBO.edit(educationalObject);
        getActiveEducationalObjects();
        getPendingEducationalObjects();
    }

    public void declineEducationalObject(Integer id) {
        EducationalObject educationalObject = EducationalObjectBO.getEducationalObject(id);
        educationalObject.setStatus(REJECTED);
        EducationalObjectBO.edit(educationalObject);
        getPendingEducationalObjects();
    }

    public void deactivateEducationalObject(Integer id) {
        EducationalObject educationalObject = EducationalObjectBO.getEducationalObject(id);
        educationalObject.setStatus(DEACTIVATED);
        EducationalObjectBO.edit(educationalObject);
        getActiveEducationalObjects();
        getInactiveEducationalObjects();
    }

    public void activateEducationalObject(Integer id) {
        EducationalObject educationalObject = EducationalObjectBO.getEducationalObject(id);
        educationalObject.setStatus(ACCEPTED);
        EducationalObjectBO.edit(educationalObject);
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

    public List<SocialProfile> getListSocialProfile() {
        return listSocialProfile;
    }

    public void setListSocialProfile(List<SocialProfile> listSocialProfile) {
        this.listSocialProfile = listSocialProfile;
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
