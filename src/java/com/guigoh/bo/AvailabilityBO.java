/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.AvailabilityJpaController;
import com.ipti.guigoh.model.entity.Availability;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class AvailabilityBO implements Serializable {

    public static List<Availability> getAll() {
        try {
            AvailabilityJpaController availabilityDAO = new AvailabilityJpaController();
            return availabilityDAO.findAvailabilityEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
