/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.AuthorizationBO;
import com.guigoh.primata.bo.MessengerMessagesBO;
import com.guigoh.primata.bo.MessengerStatusBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.bo.UsersBO;
import com.guigoh.primata.entity.Authorization;
import com.guigoh.primata.entity.SocialProfile;
import java.io.Serializable;
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
@ManagedBean(name = "headerBean")
public class HeaderBean implements Serializable {

    public static final String ADMIN = "AD";
    public static final String REVISER = "RE";
    private SocialProfile socialProfile;
    private Authorization authorization;
    private Boolean adminOK;
    private Boolean reviserOK;
    private Integer registeredUsersCount;
    private Long registeredUsersOnline;

    public void init() {
        socialProfile = new SocialProfile();
        adminOK = false;
        reviserOK = false;
        holdSocialProfile();
        holdAuthorization();
        getRegisteredUsersQuantity();
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
            socialProfile.setName(socialProfile.getName().split(" ")[0]);
        }
    }

    private void holdAuthorization() {
        AuthorizationBO authorizationBO = new AuthorizationBO();
        authorization = authorizationBO.findAuthorizationByTokenId(socialProfile.getTokenId());
        if (authorization != null) {
            if (authorization.getRoles().equals(ADMIN)) {
                adminOK = true;
            }
            if (authorization.getRoles().equals(REVISER)){
                reviserOK = true;
            }
        }
    }
    
    private void getRegisteredUsersQuantity(){
        UsersBO uBO = new UsersBO();
        MessengerStatusBO msBO = new MessengerStatusBO();
        registeredUsersCount = uBO.getRegisteredUsersQuantity();
        registeredUsersOnline = msBO.getUsersOnline();
        if(registeredUsersOnline == 0){
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

    public Boolean getAdminOK() {
        return adminOK;
    }

    public void setAdminOK(Boolean adminOK) {
        this.adminOK = adminOK;
    }

    public Boolean getReviserOK() {
        return reviserOK;
    }

    public void setReviserOK(Boolean reviserOK) {
        this.reviserOK = reviserOK;
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
