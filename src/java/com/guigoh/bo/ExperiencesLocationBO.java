/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.ExperiencesLocationDAO;
import com.guigoh.entity.ExperiencesLocation;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class ExperiencesLocationBO implements Serializable {
    
    private ExperiencesLocationDAO experiencesLocationDAO;
    
    public ExperiencesLocationBO(){
        experiencesLocationDAO = new ExperiencesLocationDAO();
    }
    
    public List<ExperiencesLocation> getAll() {
        try{
        return experiencesLocationDAO.findExperiencesLocationEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void create(ExperiencesLocation experiencesLocationt) {
         try {
           experiencesLocationDAO.create(experiencesLocationt);               
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ExperiencesLocation findExperiencesLocationByName(ExperiencesLocation locationId) {
        try{
        return experiencesLocationDAO.findExperiencesLocationByName(locationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
