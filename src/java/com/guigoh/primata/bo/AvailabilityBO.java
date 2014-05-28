/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.AvailabilityDAO;
import com.guigoh.primata.entity.Availability;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class AvailabilityBO implements Serializable {

    private AvailabilityDAO availabilityDAO;

    public AvailabilityBO() {
        availabilityDAO = new AvailabilityDAO();
    }

    public List<Availability> getAll() {
        try {
            return availabilityDAO.findAvailabilityEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
