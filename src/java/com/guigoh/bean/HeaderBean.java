/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bean;

import com.guigoh.bo.UserAuthorizationBO;
import com.guigoh.bo.MessengerStatusBO;
import com.guigoh.bo.SocialProfileBO;
import com.guigoh.bo.UsersBO;
import com.guigoh.bo.util.CookieService;
import com.guigoh.entity.UserAuthorization;
import com.guigoh.entity.SocialProfile;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Joe
 */
@ViewScoped
@ManagedBean(name = "headerBean")
public class HeaderBean implements Serializable {
    
    public static final String ADMIN = "AD";
    public static final String REVISER = "RE";
    private SocialProfile socialProfile;
    private UserAuthorization authorization;
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
        socialProfile = SocialProfileBO.findSocialProfile(CookieService.getCookie("token"));
        socialProfile.setName(socialProfile.getName().split(" ")[0]);
    }
    
    private void loadAuthorization() {
        authorization = UserAuthorizationBO.findAuthorizationByTokenId(socialProfile.getTokenId());
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
        registeredUsersCount = UsersBO.getRegisteredUsersQuantity();
        registeredUsersOnline = MessengerStatusBO.getUsersOnline();
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
    
    public UserAuthorization getAuthorization() {
        return authorization;
    }
    
    public void setAuthorization(UserAuthorization authorization) {
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
