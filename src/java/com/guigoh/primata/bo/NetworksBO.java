/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.NetworksDAO;
import com.guigoh.primata.entity.Networks;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class NetworksBO implements Serializable{
    public List<Networks> getAll(){
        NetworksDAO networksDAO = new NetworksDAO();
        return networksDAO.findNetworksEntities();
    }
}
