/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.FriendsBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.dao.exceptions.PreexistingEntityException;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import com.guigoh.primata.entity.Friends;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

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
            loadUserCookie();
            SocialProfileBO spBO = new SocialProfileBO();
            userSocialProfile = spBO.findSocialProfile(user.getToken());
            loadFriends();
        }
    }

    public void loadFriends() {
        FriendsBO friendsBO = new FriendsBO();
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
            /*friend.getTokenFriend1().getSocialProfile().setName(
                        friend.getTokenFriend1().getSocialProfile().getName().split(" ")[0]);
            friend.getTokenFriend2().getSocialProfile().setName(
                        friend.getTokenFriend2().getSocialProfile().getName().split(" ")[0]);*/
            if (user.getToken().equals(friend.getTokenFriend2().getToken())){
                Users user = friend.getTokenFriend1();
                friend.setTokenFriend1(friend.getTokenFriend2());
                friend.setTokenFriend2(user);
            }
        }
    }
            
    public void searchFriendEvent() {
        FriendsBO friendBO = new FriendsBO();
        acceptedList = new ArrayList<Friends>();
        acceptedList = friendBO.loadFriendSearchList(user.getToken(), friendInputSearch);
        
        organizeFriendList(acceptedList);
    }

    public void searchUsersEvent() {
        FriendsBO friendBO = new FriendsBO();
        socialProfileList = new ArrayList<SocialProfile>();
        if (!userInputSearch.equals("")) {
            socialProfileList = friendBO.loadUserSearchList(userInputSearch);
            
        }
    }

    private void loadUserCookie() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().trim().equalsIgnoreCase("user")) {
                    user.setUsername(cookie.getValue());
                } else if (cookie.getName().trim().equalsIgnoreCase("token")) {
                    user.setToken(cookie.getValue());
                }
            }
        }
    }

    public void removeFriend(Integer id) throws PreexistingEntityException, RollbackFailureException, Exception {
        FriendsBO friendsBO = new FriendsBO();
        friendsBO.removeFriend(user, id);
        loadFriends();
    }

    public void acceptFriend(Integer id) throws PreexistingEntityException, RollbackFailureException, Exception {
        FriendsBO friendsBO = new FriendsBO();
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
    
