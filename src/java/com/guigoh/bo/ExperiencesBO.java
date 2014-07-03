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

    private ExperiencesDAO experiencesDAO;

    public ExperiencesBO() {
        experiencesDAO = new ExperiencesDAO();
    }

    public List<Experiences> findExperiencesByTokenId(String token_id) {
        try {
            return experiencesDAO.findExperiencesByTokenId(token_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Experiences> getAll() {
        try {
            return experiencesDAO.findExperiencesEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void create(Experiences experiences) {
        try {
            experiencesDAO.create(experiences);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createInsert(Experiences experiences) {
        try {
            experiencesDAO.createInsert(experiences);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeExperience(Experiences exp) {
        try {
            experiencesDAO.destroy(exp.getExperiencesPK());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
