/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.PreexistingEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.model.entity.Friends;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.FriendsJpaController;
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

    private List<Friends> acceptedList, pendingList;

    private String friendInputSearch;
    private boolean hasFriend;

    private FriendsJpaController friendsJpaController;

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
        hasFriend = acceptedList.isEmpty();
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
        if (friendInputSearch.length() == 0) {
            getFriends();
        } else {
            acceptedList = friendsJpaController.findFriendSearchList(user.getToken(), friendInputSearch);
            organizeFriendList(acceptedList);
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

        friendInputSearch = "";

        user = new Users();

        user.setUsername(CookieService.getCookie("user"));
        user.setToken(CookieService.getCookie("token"));
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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public boolean isHasFriend() {
        return hasFriend;
    }

    public void setHasFriend(boolean hasFriend) {
        this.hasFriend = hasFriend;
    }
}
