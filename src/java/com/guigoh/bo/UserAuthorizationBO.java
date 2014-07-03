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

    private UserAuthorizationDAO authorizationDAO;

    public UserAuthorizationBO() {
        authorizationDAO = new UserAuthorizationDAO();
    }

    public void create(UserAuthorization authorization) {
        try {
            authorizationDAO.create(authorization);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void edit(UserAuthorization authorization) {
        try {
            authorizationDAO.edit(authorization);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public UserAuthorization findAuthorizationByTokenId(String token_id) {
        try {
            return authorizationDAO.findAuthorization(token_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<UserAuthorization> getAll() {
        try {
            return authorizationDAO.findAuthorizationEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<UserAuthorization> findAuthorizationByActive(Integer subnetwork) {
        try {
            List<UserAuthorization> authorizationList = authorizationDAO.findAuthorizationsBySubnetwork(subnetwork);
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
