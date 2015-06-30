/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.UserAuthorization;
import br.org.ipti.guigoh.model.jpa.controller.MessengerStatusJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UserAuthorizationJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UsersJpaController;
import br.org.ipti.guigoh.util.CookieService;
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
    
    public static final String ADMIN = "AD", REVISER = "RE";
    
    private SocialProfile socialProfile;
    private UserAuthorization authorization;
    
    private Boolean admin, reviser;
    private Integer registeredUsersCount;
    private Long registeredUsersOnline;
    
    public void init() {
        initGlobalVariables();
        loadSocialProfile();
        loadAuthorization();
        getRegisteredUsersQuantity();
    }
    
    public String logout() {
        CookieService.eraseCookie();
        return "logout";
    }
    
    private void loadSocialProfile() {
        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
        socialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));
        socialProfile.setName(socialProfile.getName().split(" ")[0]);
    }
    
    private void loadAuthorization() {
        UserAuthorizationJpaController userAuthorizationJpaController = new UserAuthorizationJpaController();
        authorization = userAuthorizationJpaController.findAuthorization(socialProfile.getTokenId());
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
        UsersJpaController usersJpaController = new UsersJpaController();
        registeredUsersCount = usersJpaController.getUsersCount();
        MessengerStatusJpaController messengerStatusJpaController = new MessengerStatusJpaController();
        registeredUsersOnline = messengerStatusJpaController.getUsersOnline();
        if (registeredUsersOnline == 0) {
            registeredUsersOnline++;
        }
    }
    
    private void initGlobalVariables() {
        socialProfile = new SocialProfile();
        
        admin = reviser = false;
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
