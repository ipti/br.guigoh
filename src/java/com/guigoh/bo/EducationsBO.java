/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.EducationsJpaController;
import com.ipti.guigoh.model.entity.Educations;
import com.ipti.guigoh.model.entity.EducationsName;
import com.ipti.guigoh.model.entity.Users;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class EducationsBO implements Serializable {

    public static List<Educations> findEducationsByTokenId(String token_id) {
        try {
            EducationsJpaController educationsDAO = new EducationsJpaController();
            return educationsDAO.findEducationsByTokenId(token_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Educations> getAll() {
        try {
            EducationsJpaController educationsDAO = new EducationsJpaController();
            return educationsDAO.findEducationsEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void create(Educations educations) {
        try {
            EducationsJpaController educationsDAO = new EducationsJpaController();
            educationsDAO.create(educations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createInsert(Educations educations) {
        try {
            EducationsJpaController educationsDAO = new EducationsJpaController();
            educationsDAO.createInsert(educations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Educations findEducationsByName(Educations educations) {
        try {
            EducationsJpaController educationsDAO = new EducationsJpaController();
            return educationsDAO.findEducationsByName(educations);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void removeEducation(Educations edu) {
        try {
            EducationsJpaController educationsDAO = new EducationsJpaController();
            educationsDAO.destroy(edu.getEducationsPK());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
