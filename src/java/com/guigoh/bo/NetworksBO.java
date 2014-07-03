/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.NetworksDAO;
import com.guigoh.entity.Networks;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class NetworksBO implements Serializable {

    private NetworksDAO networksDAO;

    public NetworksBO() {
        networksDAO = new NetworksDAO();
    }

    public List<Networks> getAll() {
        try {
            return networksDAO.findNetworksEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
