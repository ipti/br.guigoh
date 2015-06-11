/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.UserAuthorizationDAO;
import com.guigoh.entity.UserAuthorization;
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
            UserAuthorizationDAO userAuthorizationDAO = new UserAuthorizationDAO();
            userAuthorizationDAO.create(authorization);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void edit(UserAuthorization authorization) {
        try {
            UserAuthorizationDAO userAuthorizationDAO = new UserAuthorizationDAO();
            userAuthorizationDAO.edit(authorization);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static UserAuthorization findAuthorizationByTokenId(String token_id) {
        try {
            UserAuthorizationDAO userAuthorizationDAO = new UserAuthorizationDAO();
            return userAuthorizationDAO.findAuthorization(token_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<UserAuthorization> findAuthorizationsByRole(String role){
        try {
            UserAuthorizationDAO userAuthorizationDAO = new UserAuthorizationDAO();
            return userAuthorizationDAO.findAuthorizationsByRole(role);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static UserAuthorization getUserAuthorization(String token){
        try {
            UserAuthorizationDAO userAuthorizationDAO = new UserAuthorizationDAO();
            return userAuthorizationDAO.findAuthorization(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<UserAuthorization> getAll() {
        try {
            UserAuthorizationDAO userAuthorizationDAO = new UserAuthorizationDAO();
            return userAuthorizationDAO.findAuthorizationEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<UserAuthorization> getPendingUsers(){
        try {
            UserAuthorizationDAO userAuthorizationDAO = new UserAuthorizationDAO();
            return userAuthorizationDAO.getPendingUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<UserAuthorization> getActiveUsers(){
        try {
            UserAuthorizationDAO userAuthorizationDAO = new UserAuthorizationDAO();
            return userAuthorizationDAO.getActiveUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<UserAuthorization> getInactiveUsers(){
        try {
            UserAuthorizationDAO userAuthorizationDAO = new UserAuthorizationDAO();
            return userAuthorizationDAO.getInactiveUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<UserAuthorization> findAuthorizationByActive(Integer subnetwork) {
        try {
            UserAuthorizationDAO userAuthorizationDAO = new UserAuthorizationDAO();
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
