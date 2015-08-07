/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.model.jpa.exceptions.PreexistingEntityException;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.model.entity.Friends;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.FriendsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author IPTI
 */
@ViewScoped
@Named
public class FriendViewBean implements Serializable {

    private Users user;
    private SocialProfile userSocialProfile;

    private List<Friends> acceptedList, pendingList;
    private List<SocialProfile> socialProfileList;

    private String friendInputSearch, userInputSearch;

    private FriendsJpaController friendsJpaController;
    private SocialProfileJpaController socialProfileJpaController;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
            getFriends();
        }
    }

    private void getFriends() {
        acceptedList = pendingList = new ArrayList<>();
        acceptedList = friendsJpaController.findFriendsByToken(user.getToken());
        pendingList = friendsJpaController.findPendingFriendsByToken(user.getToken());
        organizeFriendList(acceptedList);
        organizeFriendList(pendingList);
    }

    public String goToProfile(Integer id) {
        return "/profile/view.xhtml?id=" + id;
    }

    private void organizeFriendList(List<Friends> list) {
        list.stream()
                .filter((friend) -> (user.getToken().equals(friend.getTokenFriend2().getToken())))
                .forEach((friend) -> {
            Users userFriend = friend.getTokenFriend1();
            friend.setTokenFriend1(friend.getTokenFriend2());
            friend.setTokenFriend2(userFriend);
        });
    }

    public void searchFriendsEvent() {
        acceptedList = new ArrayList<>();
        acceptedList = friendsJpaController.findFriendSearchList(user.getToken(), friendInputSearch);
        organizeFriendList(acceptedList);
    }

    public void searchUsersEvent() {
        socialProfileList = new ArrayList<>();
        if (!userInputSearch.equals("")) {
            socialProfileList = friendsJpaController.findUserSearchList(userInputSearch);

        }
    }

    public void removeFriend(Integer id) throws PreexistingEntityException, RollbackFailureException, Exception {
        friendsJpaController.removeFriend(user, id);
        getFriends();
    }

    public void acceptFriend(Integer id) throws PreexistingEntityException, RollbackFailureException, Exception {
        friendsJpaController.acceptFriend(user, id);
        getFriends();
    }

    private void initGlobalVariables() {
        friendsJpaController = new FriendsJpaController();
        socialProfileJpaController = new SocialProfileJpaController();
        
        friendInputSearch = userInputSearch = "";
        
        user = new Users();
        
        user.setUsername(CookieService.getCookie("user"));
        user.setToken(CookieService.getCookie("token"));
        
        userSocialProfile = socialProfileJpaController.findSocialProfile(user.getToken());
    }

    public List getPendingList() {
        return pendingList;
    }

    public void setPendingList(List pendingList) {
        this.pendingList = pendingList;
    }

    public List getAcceptedList() {
        return acceptedList;
    }

    public void setAcceptedList(List acceptedList) {
        this.acceptedList = acceptedList;
    }

    public String getFriendInputSearch() {
        return friendInputSearch;
    }

    public void setFriendInputSearch(String friendInputSearch) {
        this.friendInputSearch = friendInputSearch;
    }

    public String getUserInputSearch() {
        return userInputSearch;
    }

    public void setUserInputSearch(String userInputSearch) {
        this.userInputSearch = userInputSearch;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<SocialProfile> getSocialProfileList() {
        return socialProfileList;
    }

    public void setSocialProfileList(List<SocialProfile> socialProfileList) {
        this.socialProfileList = socialProfileList;
    }

    public SocialProfile getUserSocialProfile() {
        return userSocialProfile;
    }

    public void setUserSocialProfile(SocialProfile userSocialProfile) {
        this.userSocialProfile = userSocialProfile;
    }
}
