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
    
    public List<Availability> getAll(){
        AvailabilityDAO availabilityDAO = new AvailabilityDAO();
        return availabilityDAO.findAvailabilityEntities();
    }
    
}
