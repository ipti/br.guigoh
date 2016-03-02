/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.Author;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.UserAuthorization;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import br.org.ipti.guigoh.model.jpa.controller.FriendsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UserAuthorizationJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UsersJpaController;
import br.org.ipti.guigoh.util.CookieService;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@ViewScoped
@Named
public class TemplateBean implements Serializable {

    private static final String ADMIN = "AD", REVISER = "RE";

    private SocialProfile socialProfile;
    private UserAuthorization authorization;

    private Boolean admin, reviser;
    private Integer registeredUsersCount, registeredEducationalObjectsCount, interestId, pendingFriendsCount;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
            getLoggedSocialProfile();
            checkUserAuthorization();
            getRegisteredUsersQuantity();
            getRegisteredEducationalObjectsQuantity();
            loadPendingFriendsQuantity();
        }
    }

    public void logout() throws IOException {
        CookieService.eraseCookie();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/login/auth.xhtml");
    }

    private void getLoggedSocialProfile() {
        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
        socialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));
    }

    public void loadPendingFriendsQuantity() {
        FriendsJpaController friendsJpaController = new FriendsJpaController();
        pendingFriendsCount = friendsJpaController.findPendingFriendsByToken(socialProfile.getTokenId()).size();
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

    public String getAuthorsSubnetwork(Collection<Author> authors) {
        UsersJpaController usersJpaController = new UsersJpaController();
        List<String> subnetworkList = new ArrayList<>();
        String subnetwork = "";
        for (Author author : authors) {
            Users user = usersJpaController.findUsers(author.getEmail());
            if (user != null && user.getSocialProfile().getSubnetworkId() != null && !subnetworkList.contains(user.getSocialProfile().getSubnetworkId().getDescription())) {
                subnetworkList.add(user.getSocialProfile().getSubnetworkId().getDescription());
                subnetwork += user.getSocialProfile().getSubnetworkId().getDescription() + " / ";
            }
        }
        if (!subnetwork.equals("")) {
            return subnetwork.substring(0, subnetwork.length() - 3);
        } else {
            return subnetwork;
        }
    }

    private void getRegisteredUsersQuantity() {
        UsersJpaController usersJpaController = new UsersJpaController();
        registeredUsersCount = usersJpaController.getUsersQuantity();
    }
    
    private void getRegisteredEducationalObjectsQuantity() {
        EducationalObjectJpaController educationalObjectJpaController = new EducationalObjectJpaController();
        registeredEducationalObjectsCount = educationalObjectJpaController.getEducationalObjectsQuantity();
    }

    private void initGlobalVariables() {
        socialProfile = new SocialProfile();

        admin = reviser = false;

        InterestsJpaController interestsJpaController = new InterestsJpaController();
        interestId = interestsJpaController.findInterestsEntities(1, 0).get(0).getId();
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

    public Integer getInterestId() {
        return interestId;
    }

    public void setInterestId(Integer interestId) {
        this.interestId = interestId;
    }

    public Integer getPendingFriendsCount() {
        return pendingFriendsCount;
    }

    public void setPendingFriendsCount(Integer pendingFriendsCount) {
        this.pendingFriendsCount = pendingFriendsCount;
    }

    public Integer getRegisteredEducationalObjectsCount() {
        return registeredEducationalObjectsCount;
    }

    public void setRegisteredEducationalObjectsCount(Integer registeredEducationalObjectsCount) {
        this.registeredEducationalObjectsCount = registeredEducationalObjectsCount;
    }
}
