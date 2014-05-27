/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.AuthorizationBO;
import com.guigoh.primata.bo.MessengerStatusBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.bo.UsersBO;
import com.guigoh.primata.bo.util.CookieService;
import com.guigoh.primata.entity.Authorization;
import com.guigoh.primata.entity.SocialProfile;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Joe
 */
@SessionScoped
@ManagedBean(name = "headerBean")
public class HeaderBean implements Serializable {
    
    public static final String ADMIN = "AD";
    public static final String REVISER = "RE";
    private SocialProfile socialProfile;
    private Authorization authorization;
    private Boolean admin;
    private Boolean reviser;
    private Integer registeredUsersCount;
    private Long registeredUsersOnline;
    
    public void init() {
        socialProfile = new SocialProfile();
        admin = false;
        reviser = false;
        loadSocialProfile();
        loadAuthorization();
        getRegisteredUsersQuantity();
    }
    
    private void loadSocialProfile() {
        SocialProfileBO socialProfileBO = new SocialProfileBO();
        socialProfile = socialProfileBO.findSocialProfile(CookieService.getCookie("token"));
        socialProfile.setName(socialProfile.getName().split(" ")[0]);
    }
    
    private void loadAuthorization() {
        AuthorizationBO authorizationBO = new AuthorizationBO();
        authorization = authorizationBO.findAuthorizationByTokenId(socialProfile.getTokenId());
        if (authorization != null) {
            if (authorization.getRoles().equals(ADMIN)) {
                admin = true;
            }
            if (authorization.getRoles().equals(REVISER)) {
                reviser = true;
            }
        }
    }
    
    private void getRegisteredUsersQuantity() {
        UsersBO uBO = new UsersBO();
        MessengerStatusBO msBO = new MessengerStatusBO();
        registeredUsersCount = uBO.getRegisteredUsersQuantity();
        registeredUsersOnline = msBO.getUsersOnline();
        if (registeredUsersOnline == 0) {
            registeredUsersOnline++;
        }
    }
    
    public SocialProfile getSocialProfile() {
        return socialProfile;
    }
    
    public void setSocialProfile(SocialProfile socialProfile) {
        this.socialProfile = socialProfile;
    }
    
    public Authorization getAuthorization() {
        return authorization;
    }
    
    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }
    
    public Boolean getAdmin() {
        return admin;
    }
    
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
    
    public Boolean getReviser() {
        return reviser;
    }
    
    public void setReviser(Boolean reviser) {
        this.reviser = reviser;
    }
    
    public Integer getRegisteredUsersCount() {
        return registeredUsersCount;
    }
    
    public void setRegisteredUsersCount(Integer registeredUsersCount) {
        this.registeredUsersCount = registeredUsersCount;
    }
    
    public Long getRegisteredUsersOnline() {
        return registeredUsersOnline;
    }
    
    public void setRegisteredUsersOnline(Long registeredUsersOnline) {
        this.registeredUsersOnline = registeredUsersOnline;
    }
}
