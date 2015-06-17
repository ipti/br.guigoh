/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import br.org.ipti.guigoh.model.jpa.controller.EducationsLocationJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationsNameJpaController;
import br.org.ipti.guigoh.model.entity.Educations;
import br.org.ipti.guigoh.model.entity.EducationsLocation;
import br.org.ipti.guigoh.model.entity.EducationsName;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class EducationsLocationBO implements Serializable {

    public static List<EducationsLocation> getAll() {
        try {
            EducationsLocationJpaController educationsLocationDAO = new EducationsLocationJpaController();
            return educationsLocationDAO.findEducationsLocationEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void create(EducationsLocation educationsLocation) {
        try {
            EducationsLocationJpaController educationsLocationDAO = new EducationsLocationJpaController();
            educationsLocationDAO.create(educationsLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static EducationsLocation findEducationsLocationByName(EducationsLocation locationId) {
        try {
            EducationsLocationJpaController educationsLocationDAO = new EducationsLocationJpaController();
            return educationsLocationDAO.findEducationsByName(locationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
