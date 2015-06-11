/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.ExperiencesDAO;
import com.guigoh.entity.Experiences;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class ExperiencesBO implements Serializable {

    public static List<Experiences> findExperiencesByTokenId(String token_id) {
        try {
            ExperiencesDAO experiencesDAO = new ExperiencesDAO();
            return experiencesDAO.findExperiencesByTokenId(token_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Experiences> getAll() {
        try {
            ExperiencesDAO experiencesDAO = new ExperiencesDAO();
            return experiencesDAO.findExperiencesEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void create(Experiences experiences) {
        try {
            ExperiencesDAO experiencesDAO = new ExperiencesDAO();
            experiencesDAO.create(experiences);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createInsert(Experiences experiences) {
        try {
            ExperiencesDAO experiencesDAO = new ExperiencesDAO();
            experiencesDAO.createInsert(experiences);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeExperience(Experiences exp) {
        try {
            ExperiencesDAO experiencesDAO = new ExperiencesDAO();
            experiencesDAO.destroy(exp.getExperiencesPK());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
