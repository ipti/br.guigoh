/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import br.org.ipti.guigoh.model.jpa.controller.EducationsNameJpaController;
import br.org.ipti.guigoh.model.entity.EducationsName;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class EducationsNameBO implements Serializable {

    public static void create(EducationsName educationsName) {
        try {
            EducationsNameJpaController educationsNameDAO = new EducationsNameJpaController();
            educationsNameDAO.create(educationsName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static EducationsName findEducationsNameByName(EducationsName nameId) {
        try {
            EducationsNameJpaController educationsNameDAO = new EducationsNameJpaController();
            return educationsNameDAO.findEducationsByName(nameId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<EducationsName> getAll() {
        try {
            EducationsNameJpaController educationsNameDAO = new EducationsNameJpaController();
            return educationsNameDAO.findEducationsNameEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
