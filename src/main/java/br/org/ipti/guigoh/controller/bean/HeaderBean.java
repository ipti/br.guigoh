/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.UserAuthorization;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UserAuthorizationJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UsersJpaController;
import br.org.ipti.guigoh.util.CookieService;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@ViewScoped
@Named 
public class HeaderBean implements Serializable {

    private static final String ADMIN = "AD", REVISER = "RE";

    private SocialProfile socialProfile;
    private UserAuthorization authorization;

    private Boolean admin, reviser;
    private Integer registeredUsersCount;

    public void init() {
        initGlobalVariables();
        getLoggedSocialProfile();
        checkUserAuthorization();
        getRegisteredUsersQuantity();
    }

    public void logout() throws IOException {
        CookieService.eraseCookie();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/login/auth.xhtml");
    }

    private void getLoggedSocialProfile() {
        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
        socialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));
        socialProfile.setName(socialProfile.getName().split(" ")[0]);
    }

    private void checkUserAuthorization() {
        UserAuthorizationJpaController userAuthorizationJpaController = new UserAuthorizationJpaController();
        authorization = userAuthorizationJpaController.findUserAuthorization(socialProfile.getTokenId());
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
}
