/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.ExperiencesLocationDAO;
import com.guigoh.primata.entity.ExperiencesLocation;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class ExperiencesLocationBO implements Serializable {
    public List<ExperiencesLocation> getAll() {
        ExperiencesLocationDAO experiencesLocationDAODAO = new ExperiencesLocationDAO();
        return experiencesLocationDAODAO.findExperiencesLocationEntities();
    }

    public void create(ExperiencesLocation experiencesLocationt) {
         try {
            ExperiencesLocationDAO experiencesLocationDAODAO = new ExperiencesLocationDAO();
           experiencesLocationDAODAO.create(experiencesLocationt);               
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ExperiencesLocation findExperiencesLocationByName(ExperiencesLocation locationId) {
         ExperiencesLocationDAO experiencesLocationDAODAO = new ExperiencesLocationDAO();
        return experiencesLocationDAODAO.findExperiencesLocationByName(locationId);
    }
}
