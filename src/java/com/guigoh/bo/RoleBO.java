/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.RoleDAO;
import com.guigoh.entity.Role;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author ipti004
 */
public class RoleBO implements Serializable{

    private RoleDAO roleDAO;

    public RoleBO() {
        roleDAO = new RoleDAO();
    }

    public List<Role> getAll() {
        try {
            return roleDAO.findRoleEntities();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
