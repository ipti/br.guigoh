/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.NetworksJpaController;
import com.ipti.guigoh.model.entity.Networks;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class NetworksBO implements Serializable {

    public static List<Networks> getAll() {
        try {
            NetworksJpaController networksDAO = new NetworksJpaController();
            return networksDAO.findNetworksEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
