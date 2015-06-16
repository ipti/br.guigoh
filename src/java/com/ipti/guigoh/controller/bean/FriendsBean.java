/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipti.guigoh.controller.bean;

import com.guigoh.bo.FriendsBO;
import com.guigoh.bo.SocialProfileBO;
import com.ipti.guigoh.util.CookieService;
import com.ipti.guigoh.model.jpa.exceptions.PreexistingEntityException;
import com.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import com.ipti.guigoh.model.entity.Friends;
import com.ipti.guigoh.model.entity.SocialProfile;
import com.ipti.guigoh.model.entity.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author IPTI
 */
@ViewScoped
@ManagedBean(name = "friendsBean")
public class FriendsBean implements Serializable {

    private Users user;
    private SocialProfile userSocialProfile;
    private List<Friends> acceptedList;
    private List<Friends> pendingList;
    private List<SocialProfile> socialProfileList;
    private String friendInputSearch = "";
    private String userInputSearch = "";

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            user = new Users();        
            getCookie();
            userSocialProfile = SocialProfileBO.findSocialProfile(user.getToken());
            loadFriends();
        }
    }
    
    private void getCookie(){
        user.setUsername(CookieService.getCookie("user"));
        user.setToken(CookieService.getCookie("token"));
    }

    public void loadFriends() {
        acceptedList = new ArrayList<>();
        pendingList = new ArrayList<>();
        acceptedList = FriendsBO.findFriendsByToken(user.getToken());
        pendingList = FriendsBO.findPendingFriendsByToken(user.getToken());
        organizeFriendList(acceptedList);
        organizeFriendList(pendingList);
    }

    public String goToProfile(Integer id) {
        return "/profile/viewProfile.xhtml?id=" + id;
    }

    private void organizeFriendList(List<Friends> list){
        for(Friends friend : list){
            if (user.getToken().equals(friend.getTokenFriend2().getToken())){
                Users userFriend = friend.getTokenFriend1();
                friend.setTokenFriend1(friend.getTokenFriend2());
                friend.setTokenFriend2(userFriend);
            }
        }
    }
            
    public void searchFriendEvent() {
        acceptedList = new ArrayList<>();
        acceptedList = FriendsBO.loadFriendSearchList(user.getToken(), friendInputSearch);
        organizeFriendList(acceptedList);
    }

    public void searchUsersEvent() {
        socialProfileList = new ArrayList<>();
        if (!userInputSearch.equals("")) {
            socialProfileList = FriendsBO.loadUserSearchList(userInputSearch);
            
        }
    }

    public void removeFriend(Integer id) throws PreexistingEntityException, RollbackFailureException, Exception {
        FriendsBO.removeFriend(user, id);
        loadFriends();
    }

    public void acceptFriend(Integer id) throws PreexistingEntityException, RollbackFailureException, Exception {
        FriendsBO.acceptFriend(user, id);
        loadFriends();
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
    
