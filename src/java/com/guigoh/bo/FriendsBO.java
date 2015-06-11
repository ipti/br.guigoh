/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.FriendsDAO;
import com.guigoh.dao.SocialProfileDAO;
import com.guigoh.dao.UsersDAO;
import com.guigoh.dao.exceptions.NonexistentEntityException;
import com.guigoh.dao.exceptions.PreexistingEntityException;
import com.guigoh.dao.exceptions.RollbackFailureException;
import com.guigoh.entity.Friends;
import com.guigoh.entity.FriendsPK;
import com.guigoh.entity.SocialProfile;
import com.guigoh.entity.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IPTI
 */
public class FriendsBO implements Serializable{
    
    public static List<Friends> findFriendsByToken(String id) {
        FriendsDAO friendsDAO = new FriendsDAO();
        List<Friends> friendList = friendsDAO.findFriendsByToken(id);
        if (friendList == null) {
            return new ArrayList<Friends>();
        }
        return friendList;
    }

    public static List<Friends> findPendingFriendsByToken(String id) {
        FriendsDAO friendsDAO = new FriendsDAO();
        List<Friends> pendingFriendList = friendsDAO.findPendingFriendsByToken(id);
        if (pendingFriendList == null) {
            return new ArrayList<Friends>();
        }
        return pendingFriendList;
    }

    public static List<Friends> loadFriendSearchList(String token, String str) {
        FriendsDAO friendsDAO = new FriendsDAO();
        List<Friends> friendsList = friendsDAO.loadFriendSearchList(token, str);
        if (friendsList == null) {
            return new ArrayList<Friends>();
        }
        return friendsList;
    }

    public static List<SocialProfile> loadUserSearchList(String str) {
        FriendsDAO friendsDAO = new FriendsDAO();
        List<SocialProfile> usersList = friendsDAO.loadUserSearchList(str);
        if (usersList == null) {
            return new ArrayList<SocialProfile>();
        }
        return usersList;
    }

    public static void addFriend(Users user, Integer socialProfileId) throws PreexistingEntityException, RollbackFailureException, Exception {
        try {
            FriendsDAO friendsDAO = new FriendsDAO();
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

    public static void removeFriend(Users user, Integer socialProfileId) throws PreexistingEntityException, RollbackFailureException, Exception {
        try {
            FriendsDAO friendsDAO = new FriendsDAO();
            Friends friend = findFriends(user, socialProfileId);
            friendsDAO.destroy(friend.getFriendsPK());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void acceptFriend(Users user, Integer socialProfileId) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            FriendsDAO friendsDAO = new FriendsDAO();
            Friends friend = findFriends(user, socialProfileId);
            friend.setStatus("AC");
            friendsDAO.edit(friend);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void recommendFriend(Users user, Integer socialProfileId, String receiverUserName, String message) throws PreexistingEntityException, RollbackFailureException, Exception {
        try {
            FriendsDAO friendsDAO = new FriendsDAO();
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

    public static Friends findFriends(Users user, Integer socialProfileId) {
        FriendsDAO friendsDAO = new FriendsDAO();
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

    public static List<SocialProfile> findFriendsOnlineByToken(String id) {
        FriendsDAO friendsDAO = new FriendsDAO();
        List<SocialProfile> socialProfile = friendsDAO.findFriendsOnlineByToken(id);
        if (socialProfile == null) {
            return new ArrayList<SocialProfile>();
        }
        return socialProfile;
    }

    public static List<SocialProfile> findFriendsOfflineByToken(String id) {
        FriendsDAO friendsDAO = new FriendsDAO();
        List<SocialProfile> socialProfile = friendsDAO.findFriendsOfflineByToken(id);
        if (socialProfile == null) {
            return new ArrayList<SocialProfile>();
        }
        return socialProfile;
    }
}
