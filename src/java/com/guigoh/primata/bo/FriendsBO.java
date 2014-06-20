/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.FriendsDAO;
import com.guigoh.primata.dao.SocialProfileDAO;
import com.guigoh.primata.dao.UsersDAO;
import com.guigoh.primata.dao.exceptions.NonexistentEntityException;
import com.guigoh.primata.dao.exceptions.PreexistingEntityException;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import com.guigoh.primata.entity.Friends;
import com.guigoh.primata.entity.FriendsPK;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IPTI
 */
public class FriendsBO implements Serializable{

    private FriendsDAO friendsDAO;

    public FriendsBO() {
        friendsDAO = new FriendsDAO();
    }

    public List<Friends> findFriendsByToken(String id) {
        List<Friends> friendList = friendsDAO.findFriendsByToken(id);
        if (friendList == null) {
            return new ArrayList<Friends>();
        }
        return friendList;
    }

    public List<Friends> findPendingFriendsByToken(String id) {
        List<Friends> pendingFriendList = friendsDAO.findPendingFriendsByToken(id);
        if (pendingFriendList == null) {
            return new ArrayList<Friends>();
        }
        return pendingFriendList;
    }

    public List<Friends> loadFriendSearchList(String token, String str) {
        List<Friends> friendsList = friendsDAO.loadFriendSearchList(token, str);
        if (friendsList == null) {
            return new ArrayList<Friends>();
        }
        return friendsList;
    }

    public List<SocialProfile> loadUserSearchList(String str) {
        List<SocialProfile> usersList = friendsDAO.loadUserSearchList(str);
        if (usersList == null) {
            return new ArrayList<SocialProfile>();
        }
        return usersList;
    }

    public void addFriend(Users user, Integer socialProfileId) throws PreexistingEntityException, RollbackFailureException, Exception {
        try {
            SocialProfileDAO socialProfileDAO = new SocialProfileDAO();
            SocialProfile socialProfile = socialProfileDAO.findSocialProfile(socialProfileId);
            Friends friend = new Friends();
            friend.setStatus("PE");
            friend.setTokenFriend1(user);
            friend.setTokenFriend2(socialProfile.getUsers());
            friend.setRecommenders(0);
            friendsDAO.create(friend);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFriend(Users user, Integer socialProfileId) throws PreexistingEntityException, RollbackFailureException, Exception {
        try {
            Friends friend = findFriends(user, socialProfileId);
            friendsDAO.destroy(friend.getFriendsPK());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void acceptFriend(Users user, Integer socialProfileId) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            Friends friend = findFriends(user, socialProfileId);
            friend.setStatus("AC");
            friendsDAO.edit(friend);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recommendFriend(Users user, Integer socialProfileId, String receiverUserName, String message) throws PreexistingEntityException, RollbackFailureException, Exception {
        try {
            UsersDAO usersDAO = new UsersDAO();
            Users receiver = usersDAO.findUsers(receiverUserName);
            SocialProfileDAO socialProfileDAO = new SocialProfileDAO();
            SocialProfile socialProfile = socialProfileDAO.findSocialProfile(socialProfileId);
            FriendsPK friendsPK = new FriendsPK();
            friendsPK.setTokenFriend1(socialProfile.getTokenId());
            friendsPK.setTokenFriend2(receiver.getToken());
            Friends friend = friendsDAO.findFriends(friendsPK);
            if (friend == null) {
                friend = new Friends();
                friend.setStatus("PE");
                friend.setTokenFriend1(socialProfile.getUsers());
                friend.setTokenFriend2(receiver);
                friend.setTokenRecommender(user);
                friend.setMessage(message);
                friend.setRecommenders(1);
                friendsDAO.create(friend);
            } else {
                friend.setRecommenders(friend.getRecommenders() + 1);
                friendsDAO.edit(friend);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Friends findFriends(Users user, Integer socialProfileId) {
        FriendsPK friendPK = new FriendsPK();
        SocialProfileDAO socialProfileDAO = new SocialProfileDAO();
        SocialProfile contactAdded = socialProfileDAO.findSocialProfile(socialProfileId);

        friendPK.setTokenFriend1(user.getToken());
        friendPK.setTokenFriend2(contactAdded.getTokenId());

        if (user.getToken().equals(contactAdded.getTokenId())) {
            Friends friend = new Friends();
            friend.setTokenFriend1(user);
            friend.setTokenFriend2(contactAdded.getUsers());
            return friend;
        } else if (friendsDAO.findFriends(friendPK) == null) {
            friendPK.setTokenFriend1(contactAdded.getTokenId());
            friendPK.setTokenFriend2(user.getToken());
            if (friendsDAO.findFriends(friendPK) == null) {
                return null;
            } else {
                return friendsDAO.findFriends(friendPK);
            }
        } else {
            return friendsDAO.findFriends(friendPK);
        }
    }

    public List<SocialProfile> findFriendsOnlineByToken(String id) {
        List<SocialProfile> socialProfile = friendsDAO.findFriendsOnlineByToken(id);
        if (socialProfile == null) {
            return new ArrayList<SocialProfile>();
        }
        return socialProfile;
    }

    public List<SocialProfile> findFriendsOfflineByToken(String id) {
        List<SocialProfile> socialProfile = friendsDAO.findFriendsOfflineByToken(id);
        if (socialProfile == null) {
            return new ArrayList<SocialProfile>();
        }
        return socialProfile;
    }
}
