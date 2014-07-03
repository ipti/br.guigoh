/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bean;

import com.guigoh.bo.FriendsBO;
import com.guigoh.bo.SocialProfileBO;
import com.guigoh.bo.util.CookieService;
import com.guigoh.dao.exceptions.PreexistingEntityException;
import com.guigoh.dao.exceptions.RollbackFailureException;
import com.guigoh.entity.Friends;
import com.guigoh.entity.SocialProfile;
import com.guigoh.entity.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author IPTI
 */
@SessionScoped
@ManagedBean(name = "friendsBean")
public class FriendsBean implements Serializable {

    private Users user;
    private SocialProfile userSocialProfile;
    private List<Friends> acceptedList;
    private List<Friends> pendingList;
    private List<SocialProfile> socialProfileList;
    private String friendInputSearch = "";
    private String userInputSearch = "";
    private SocialProfileBO spBO = new SocialProfileBO();
    private FriendsBO friendsBO = new FriendsBO();

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            user = new Users();        
            getCookie();
            spBO = new SocialProfileBO();
            friendsBO = new FriendsBO();
            userSocialProfile = spBO.findSocialProfile(user.getToken());
            loadFriends();
        }
    }
    
    private void getCookie(){
        user.setUsername(CookieService.getCookie("user"));
        user.setToken(CookieService.getCookie("token"));
    }

    public void loadFriends() {
        acceptedList = new ArrayList<Friends>();
        pendingList = new ArrayList<Friends>();
        acceptedList = friendsBO.findFriendsByToken(user.getToken());
        pendingList = friendsBO.findPendingFriendsByToken(user.getToken());
        organizeFriendList(acceptedList);
        organizeFriendList(pendingList);
    }

    public String goToProfile(Integer id) {
        return "/primata/profile/viewProfile.xhtml?id=" + id;
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
        acceptedList = new ArrayList<Friends>();
        acceptedList = friendsBO.loadFriendSearchList(user.getToken(), friendInputSearch);
        organizeFriendList(acceptedList);
    }

    public void searchUsersEvent() {
        socialProfileList = new ArrayList<SocialProfile>();
        if (!userInputSearch.equals("")) {
            socialProfileList = friendsBO.loadUserSearchList(userInputSearch);
            
        }
    }

    public void removeFriend(Integer id) throws PreexistingEntityException, RollbackFailureException, Exception {
        friendsBO.removeFriend(user, id);
        loadFriends();
    }

    public void acceptFriend(Integer id) throws PreexistingEntityException, RollbackFailureException, Exception {
        friendsBO.acceptFriend(user, id);
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
    
