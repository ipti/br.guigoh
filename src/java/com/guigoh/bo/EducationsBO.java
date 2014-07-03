/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.EducationsDAO;
import com.guigoh.entity.Educations;
import com.guigoh.entity.EducationsName;
import com.guigoh.entity.Users;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class EducationsBO implements Serializable {

    private EducationsDAO educationsDAO;

    public EducationsBO() {
        educationsDAO = new EducationsDAO();
    }

    public List<Educations> findEducationsByTokenId(String token_id) {
        try {
            return educationsDAO.findEducationsByTokenId(token_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Educations> getAll() {
        try {
            return educationsDAO.findEducationsEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void create(Educations educations) {
        try {
            educationsDAO.create(educations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createInsert(Educations educations) {
        try {
            educationsDAO.createInsert(educations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Educations findEducationsByName(Educations educations) {
        try {
            return educationsDAO.findEducationsByName(educations);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeEducation(Educations edu) {
        try {
            educationsDAO.destroy(edu.getEducationsPK());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
