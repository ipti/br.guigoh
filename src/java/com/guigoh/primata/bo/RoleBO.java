/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.RoleDAO;
import com.guigoh.primata.entity.Role;
import java.util.List;

/**
 *
 * @author ipti004
 */
public class RoleBO {
    
    public List<Role> getAll(){
        RoleDAO roleDAO = new RoleDAO();
        return roleDAO.findRoleEntities();
    }
}
