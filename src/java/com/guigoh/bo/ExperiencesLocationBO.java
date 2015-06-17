/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import br.org.ipti.guigoh.model.jpa.controller.ExperiencesLocationJpaController;
import br.org.ipti.guigoh.model.entity.ExperiencesLocation;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class ExperiencesLocationBO implements Serializable {

    public static List<ExperiencesLocation> getAll() {
        try {
            ExperiencesLocationJpaController experiencesLocationDAO = new ExperiencesLocationJpaController();
            return experiencesLocationDAO.findExperiencesLocationEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void create(ExperiencesLocation experiencesLocationt) {
        try {
            ExperiencesLocationJpaController experiencesLocationDAO = new ExperiencesLocationJpaController();
            experiencesLocationDAO.create(experiencesLocationt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ExperiencesLocation findExperiencesLocationByName(ExperiencesLocation locationId) {
        try {
            ExperiencesLocationJpaController experiencesLocationDAO = new ExperiencesLocationJpaController();
            return experiencesLocationDAO.findExperiencesLocationByName(locationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
