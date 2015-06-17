/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import br.org.ipti.guigoh.model.jpa.controller.RoleJpaController;
import br.org.ipti.guigoh.model.entity.Role;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author ipti004
 */
public class RoleBO implements Serializable{
    
    public static List<Role> getAll() {
        try {
            RoleJpaController roleDAO = new RoleJpaController();
            return roleDAO.findRoleEntities();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
