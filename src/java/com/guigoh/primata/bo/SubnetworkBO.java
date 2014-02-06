/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.LanguageDAO;
import com.guigoh.primata.dao.SubnetworkDAO;
import com.guigoh.primata.entity.Language;
import com.guigoh.primata.entity.Subnetwork;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class SubnetworkBO implements Serializable {
    
    public List<Subnetwork> getAll(){
        SubnetworkDAO subnetworkDAO = new SubnetworkDAO();
        return subnetworkDAO.findSubnetworkEntities();
    }
}
