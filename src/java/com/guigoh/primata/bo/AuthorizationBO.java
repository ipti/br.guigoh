/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.AuthorizationDAO;
import com.guigoh.primata.entity.Authorization;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class AuthorizationBO implements Serializable {

    private AuthorizationDAO authorizationDAO;

    public AuthorizationBO() {
        authorizationDAO = new AuthorizationDAO();
    }

    public void create(Authorization authorization) {
        try {
            authorizationDAO.create(authorization);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void edit(Authorization authorization) {
        try {
            authorizationDAO.edit(authorization);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Authorization findAuthorizationByTokenId(String token_id) {
        try {
            return authorizationDAO.findAuthorization(token_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Authorization> getAll() {
        try {
            return authorizationDAO.findAuthorizationEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Authorization> findAuthorizationByActive(Integer subnetwork) {
        try {
            List<Authorization> authorizationList = authorizationDAO.findAuthorizationsBySubnetwork(subnetwork);
            if (authorizationList == null) {
                return new ArrayList<Authorization>();
            }
            return authorizationList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Authorization>();
    }
}
