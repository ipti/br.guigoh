/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bean;

import com.guigoh.bo.EducationalObjectBO;
import com.guigoh.entity.EducationalObject;
import com.guigoh.bo.UserAuthorizationBO;
import com.guigoh.bo.SocialProfileBO;
import com.guigoh.bo.util.CookieService;
import com.guigoh.entity.UserAuthorization;
import com.guigoh.entity.SocialProfile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Joe
 */
@SessionScoped
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
    private List<UserAuthorization> authorizationList;
    private List<EducationalObject> pendingEducationalObjectList;
    private List<EducationalObject> activeEducationalObjectList;
    private List<EducationalObject> inactiveEducationalObjectList;
    private Map<Integer, Boolean> checked;
    private UserAuthorizationBO authorizationBO;
    private SocialProfileBO socialProfileBO;
    private EducationalObjectBO educationalObjectBO;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            authorization = new UserAuthorization();
            socialProfile = new SocialProfile();
            listSocialProfile = new ArrayList<>();
            authorizationList = new ArrayList<>();
            checked = new HashMap<>();
            socialProfileBO = new SocialProfileBO();
            authorizationBO = new UserAuthorizationBO();
            educationalObjectBO = new EducationalObjectBO();
            admin = false;
            reviser = false;
            getLoggedSocialProfile();
            getUserRole();
            checkAuthorization();
            getPendingUsers();
            getPendingEducationalObjects();
            getActiveEducationalObjects();
            getInactiveEducationalObjects();
        }
    }

    private void getLoggedSocialProfile(){
        socialProfile = socialProfileBO.findSocialProfile(CookieService.getCookie("token"));
    }
    
    private void getUserRole() {
        authorization = authorizationBO.findAuthorizationByTokenId(socialProfile.getTokenId());
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

    private void getPendingUsers() {
        if (socialProfile.getSubnetworkId() != null) {
            authorizationList = authorizationBO.findAuthorizationByActive(socialProfile.getSubnetworkId().getId());
        }
    }
    
    private void getPendingEducationalObjects(){
        pendingEducationalObjectList = educationalObjectBO.getPendingEducationalObjects();
    }
    
    private void getActiveEducationalObjects(){
        activeEducationalObjectList = educationalObjectBO.getActiveEducationalObjects();
    }
    
    private void getInactiveEducationalObjects(){
        inactiveEducationalObjectList = educationalObjectBO.getInactiveEducationalObjects();
    }
    
    public void acceptEducationalObject(Integer id){
        EducationalObject educationalObject = educationalObjectBO.getEducationalObject(id);
        educationalObject.setStatus(ACCEPTED);
        educationalObjectBO.edit(educationalObject);
        getActiveEducationalObjects();
        getPendingEducationalObjects();
    }
    
    public void declineEducationalObject(Integer id){
        EducationalObject educationalObject = educationalObjectBO.getEducationalObject(id);
        educationalObject.setStatus(REJECTED);
        educationalObjectBO.edit(educationalObject);
        getPendingEducationalObjects();
    }
    
    public void deactivateEducationalObject(Integer id){
        EducationalObject educationalObject = educationalObjectBO.getEducationalObject(id);
        educationalObject.setStatus(DEACTIVATED);
        educationalObjectBO.edit(educationalObject);
        getActiveEducationalObjects();
        getInactiveEducationalObjects();
    }
    
    public void activateEducationalObject(Integer id){
        EducationalObject educationalObject = educationalObjectBO.getEducationalObject(id);
        educationalObject.setStatus(ACCEPTED);
        educationalObjectBO.edit(educationalObject);
        getActiveEducationalObjects();
        getInactiveEducationalObjects();
    }

    public void submitSelections() {
        try {
            List<UserAuthorization> checkedItems = new ArrayList<>();
            for (UserAuthorization authorizations : authorizationList) {
                if (checked.get(authorizations.getUsers().getSocialProfile().getSocialProfileId())) {
                    checkedItems.add(authorizations);
                }
            }
            checked.clear();
            for (UserAuthorization authorizations : checkedItems) {
                authorizations.setStatus(FIRST_ACCESS);
                authorizationBO.edit(authorizations);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getPendingUsers();
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

    public List<SocialProfile> getListSocialProfile() {
        return listSocialProfile;
    }

    public void setListSocialProfile(List<SocialProfile> listSocialProfile) {
        this.listSocialProfile = listSocialProfile;
    }

    public List<UserAuthorization> getAuthorizationList() {
        return authorizationList;
    }

    public void setAuthorizationList(List<UserAuthorization> authorizationList) {
        this.authorizationList = authorizationList;
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
