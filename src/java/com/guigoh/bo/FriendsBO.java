/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.FriendsJpaController;
import com.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import com.ipti.guigoh.model.jpa.controller.UsersJpaController;
import com.ipti.guigoh.model.jpa.exceptions.NonexistentEntityException;
import com.ipti.guigoh.model.jpa.exceptions.PreexistingEntityException;
import com.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import com.ipti.guigoh.model.entity.Friends;
import com.ipti.guigoh.model.entity.FriendsPK;
import com.ipti.guigoh.model.entity.SocialProfile;
import com.ipti.guigoh.model.entity.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IPTI
 */
public class FriendsBO implements Serializable{
    
    public static List<Friends> findFriendsByToken(String id) {
        FriendsJpaController friendsDAO = new FriendsJpaController();
        List<Friends> friendList = friendsDAO.findFriendsByToken(id);
        if (friendList == null) {
            return new ArrayList<Friends>();
        }
        return friendList;
    }

    public static List<Friends> findPendingFriendsByToken(String id) {
        FriendsJpaController friendsDAO = new FriendsJpaController();
        List<Friends> pendingFriendList = friendsDAO.findPendingFriendsByToken(id);
        if (pendingFriendList == null) {
            return new ArrayList<Friends>();
        }
        return pendingFriendList;
    }

    public static List<Friends> loadFriendSearchList(String token, String str) {
        FriendsJpaController friendsDAO = new FriendsJpaController();
        List<Friends> friendsList = friendsDAO.loadFriendSearchList(token, str);
        if (friendsList == null) {
            return new ArrayList<Friends>();
        }
        return friendsList;
    }

    public static List<SocialProfile> loadUserSearchList(String str) {
        FriendsJpaController friendsDAO = new FriendsJpaController();
        List<SocialProfile> usersList = friendsDAO.loadUserSearchList(str);
        if (usersList == null) {
            return new ArrayList<SocialProfile>();
        }
        return usersList;
    }

    public static void addFriend(Users user, Integer socialProfileId) throws PreexistingEntityException, RollbackFailureException, Exception {
        try {
            FriendsJpaController friendsDAO = new FriendsJpaController();
            SocialProfileJpaController socialProfileDAO = new SocialProfileJpaController();
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
            FriendsJpaController friendsDAO = new FriendsJpaController();
            Friends friend = findFriends(user, socialProfileId);
            friendsDAO.destroy(friend.getFriendsPK());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void acceptFriend(Users user, Integer socialProfileId) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            FriendsJpaController friendsDAO = new FriendsJpaController();
            Friends friend = findFriends(user, socialProfileId);
            friend.setStatus("AC");
            friendsDAO.edit(friend);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void recommendFriend(Users user, Integer socialProfileId, String receiverUserName, String message) throws PreexistingEntityException, RollbackFailureException, Exception {
        try {
            FriendsJpaController friendsDAO = new FriendsJpaController();
            UsersJpaController usersDAO = new UsersJpaController();
            Users receiver = usersDAO.findUsers(receiverUserName);
            SocialProfileJpaController socialProfileDAO = new SocialProfileJpaController();
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
        FriendsJpaController friendsDAO = new FriendsJpaController();
        FriendsPK friendPK = new FriendsPK();
        SocialProfileJpaController socialProfileDAO = new SocialProfileJpaController();
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
        FriendsJpaController friendsDAO = new FriendsJpaController();
        List<SocialProfile> socialProfile = friendsDAO.findFriendsOnlineByToken(id);
        if (socialProfile == null) {
            return new ArrayList<SocialProfile>();
        }
        return socialProfile;
    }

    public static List<SocialProfile> findFriendsOfflineByToken(String id) {
        FriendsJpaController friendsDAO = new FriendsJpaController();
        List<SocialProfile> socialProfile = friendsDAO.findFriendsOfflineByToken(id);
        if (socialProfile == null) {
            return new ArrayList<SocialProfile>();
        }
        return socialProfile;
    }
}
