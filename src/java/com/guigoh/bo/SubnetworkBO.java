/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.SubnetworkJpaController;
import com.ipti.guigoh.model.entity.Subnetwork;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class SubnetworkBO implements Serializable {

    public static List<Subnetwork> getAll() {
        try {
            SubnetworkJpaController subnetworkDAO = new SubnetworkJpaController();
            return subnetworkDAO.findSubnetworkEntities();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
