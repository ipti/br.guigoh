/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.UserAuthorizationJpaController;
import com.ipti.guigoh.model.entity.UserAuthorization;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class UserAuthorizationBO implements Serializable {

    public static void create(UserAuthorization authorization) {
        try {
            UserAuthorizationJpaController userAuthorizationDAO = new UserAuthorizationJpaController();
            userAuthorizationDAO.create(authorization);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void edit(UserAuthorization authorization) {
        try {
            UserAuthorizationJpaController userAuthorizationDAO = new UserAuthorizationJpaController();
            userAuthorizationDAO.edit(authorization);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static UserAuthorization findAuthorizationByTokenId(String token_id) {
        try {
            UserAuthorizationJpaController userAuthorizationDAO = new UserAuthorizationJpaController();
            return userAuthorizationDAO.findAuthorization(token_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<UserAuthorization> findAuthorizationsByRole(String role){
        try {
            UserAuthorizationJpaController userAuthorizationDAO = new UserAuthorizationJpaController();
            return userAuthorizationDAO.findAuthorizationsByRole(role);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static UserAuthorization getUserAuthorization(String token){
        try {
            UserAuthorizationJpaController userAuthorizationDAO = new UserAuthorizationJpaController();
            return userAuthorizationDAO.findAuthorization(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<UserAuthorization> getAll() {
        try {
            UserAuthorizationJpaController userAuthorizationDAO = new UserAuthorizationJpaController();
            return userAuthorizationDAO.findAuthorizationEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<UserAuthorization> getPendingUsers(){
        try {
            UserAuthorizationJpaController userAuthorizationDAO = new UserAuthorizationJpaController();
            return userAuthorizationDAO.getPendingUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<UserAuthorization> getActiveUsers(){
        try {
            UserAuthorizationJpaController userAuthorizationDAO = new UserAuthorizationJpaController();
            return userAuthorizationDAO.getActiveUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<UserAuthorization> getInactiveUsers(){
        try {
            UserAuthorizationJpaController userAuthorizationDAO = new UserAuthorizationJpaController();
            return userAuthorizationDAO.getInactiveUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<UserAuthorization> findAuthorizationByActive(Integer subnetwork) {
        try {
            UserAuthorizationJpaController userAuthorizationDAO = new UserAuthorizationJpaController();
            List<UserAuthorization> authorizationList = userAuthorizationDAO.findAuthorizationsBySubnetwork(subnetwork);
            if (authorizationList == null) {
                return new ArrayList<UserAuthorization>();
            }
            return authorizationList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<UserAuthorization>();
    }
}
