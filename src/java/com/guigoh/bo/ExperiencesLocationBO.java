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

    public static List<ExperiencesLocation> getAll() {
        try {
            ExperiencesLocationDAO experiencesLocationDAO = new ExperiencesLocationDAO();
            return experiencesLocationDAO.findExperiencesLocationEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void create(ExperiencesLocation experiencesLocationt) {
        try {
            ExperiencesLocationDAO experiencesLocationDAO = new ExperiencesLocationDAO();
            experiencesLocationDAO.create(experiencesLocationt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ExperiencesLocation findExperiencesLocationByName(ExperiencesLocation locationId) {
        try {
            ExperiencesLocationDAO experiencesLocationDAO = new ExperiencesLocationDAO();
            return experiencesLocationDAO.findExperiencesLocationByName(locationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
