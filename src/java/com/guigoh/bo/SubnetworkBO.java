/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.SubnetworkDAO;
import com.guigoh.entity.Subnetwork;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class SubnetworkBO implements Serializable {

    private SubnetworkDAO subnetworkDAO;

    public SubnetworkBO() {
        subnetworkDAO = new SubnetworkDAO();
    }

    public List<Subnetwork> getAll() {
        try {
            return subnetworkDAO.findSubnetworkEntities();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
