/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.AuthorizationBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.entity.Authorization;
import com.guigoh.primata.entity.SocialProfile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

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
    private boolean admin;
    private boolean reviser;
    private Authorization authorization;
    private SocialProfile socialProfile;
    private List<SocialProfile> listSocialProfile;
    private List<Authorization> authorizationList;
    private Map<Integer, Boolean> checked;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            authorization = new Authorization();
            socialProfile = new SocialProfile();
            listSocialProfile = new ArrayList<SocialProfile>();
            authorizationList = new ArrayList<Authorization>();
            checked = new HashMap<Integer, Boolean>();
            admin = false;
            reviser = false;
            holdSocialProfile();
            holdAuthorization();
            securityAdmin();
            getAllUsers();
        }
    }

    private void holdSocialProfile() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().trim().equalsIgnoreCase("token")) {
                    socialProfile.setTokenId(cookie.getValue());
                    break;
                }
            }
            SocialProfileBO socialProfileBO = new SocialProfileBO();
            socialProfile = socialProfileBO.findSocialProfile(socialProfile.getTokenId());
        }
    }

    private void holdAuthorization() {
        AuthorizationBO authorizationBO = new AuthorizationBO();
        authorization = authorizationBO.findAuthorizationByTokenId(socialProfile.getTokenId());
        if (authorization.getRoles().equals(ADMIN)){
            admin = true;
        }
        if (authorization.getRoles().equals(REVISER)){
            reviser = true;
        }
    }

    private void securityAdmin() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        NavigationHandler nh = facesContext.getApplication().getNavigationHandler();

        if (!authorization.getRoles().equals(ADMIN) && !authorization.getRoles().equals(REVISER)) {
            nh.handleNavigation(facesContext, null, "islogged");
        }
    }

    private void getAllUsers() {
        AuthorizationBO authorizationBO = new AuthorizationBO();
        authorizationList = authorizationBO.findAuthorizationByActive(socialProfile.getSubnetworkId().getId());
    }

    public void submitSelections() {
        try {
            List<Authorization> checkedItems = new ArrayList<Authorization>();
            for (Authorization authorizations : authorizationList) {
                if (checked.get(authorizations.getUsers().getSocialProfile().getSocialProfileId())) {
                    checkedItems.add(authorizations);
                }
            }
            checked.clear();
            AuthorizationBO authorizationBO = new AuthorizationBO();
            for (Authorization authorizations : checkedItems) {
                authorizations.setStatus(FIRST_ACCESS);
                authorizationBO.edit(authorizations);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        getAllUsers();
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
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

    public List<Authorization> getAuthorizationList() {
        return authorizationList;
    }

    public void setAuthorizationList(List<Authorization> authorizationList) {
        this.authorizationList = authorizationList;
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
