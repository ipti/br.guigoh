/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import br.org.ipti.guigoh.model.jpa.controller.ExperiencesJpaController;
import br.org.ipti.guigoh.model.entity.Experiences;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class ExperiencesBO implements Serializable {

    public static List<Experiences> findExperiencesByTokenId(String token_id) {
        try {
            ExperiencesJpaController experiencesDAO = new ExperiencesJpaController();
            return experiencesDAO.findExperiencesByTokenId(token_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Experiences> getAll() {
        try {
            ExperiencesJpaController experiencesDAO = new ExperiencesJpaController();
            return experiencesDAO.findExperiencesEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void create(Experiences experiences) {
        try {
            ExperiencesJpaController experiencesDAO = new ExperiencesJpaController();
            experiencesDAO.create(experiences);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createInsert(Experiences experiences) {
        try {
            ExperiencesJpaController experiencesDAO = new ExperiencesJpaController();
            experiencesDAO.createInsert(experiences);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeExperience(Experiences exp) {
        try {
            ExperiencesJpaController experiencesDAO = new ExperiencesJpaController();
            experiencesDAO.destroy(exp.getExperiencesPK());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
