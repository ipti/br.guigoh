/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.AuthorizationDAO;
import com.guigoh.primata.entity.Authorization;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class AuthorizationBO implements Serializable {

    public void create(Authorization authorization) {
        try {
            AuthorizationDAO authorizationDAO = new AuthorizationDAO();
            authorizationDAO.create(authorization);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void edit(Authorization authorization) {
        try {
            AuthorizationDAO authorizationDAO = new AuthorizationDAO();
            authorizationDAO.edit(authorization);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Authorization findAuthorizationByTokenId(String token_id) {
        try {
            AuthorizationDAO authorizationDAO = new AuthorizationDAO();
            return authorizationDAO.findAuthorization(token_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; 
    }
    
    public List<Authorization> getAll(){
        try {
            AuthorizationDAO authorizationDAO = new AuthorizationDAO();
        return authorizationDAO.findAuthorizationEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Authorization> findAuthorizationByActive(Integer subnetwork) {
        try {
            AuthorizationDAO authorizationDAO = new AuthorizationDAO();
            return authorizationDAO.findExperiencesByTokenId(subnetwork);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; 
    }
    
    
}
