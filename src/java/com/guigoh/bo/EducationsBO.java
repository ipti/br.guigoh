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

    public static List<Educations> findEducationsByTokenId(String token_id) {
        try {
            EducationsDAO educationsDAO = new EducationsDAO();
            return educationsDAO.findEducationsByTokenId(token_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Educations> getAll() {
        try {
            EducationsDAO educationsDAO = new EducationsDAO();
            return educationsDAO.findEducationsEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void create(Educations educations) {
        try {
            EducationsDAO educationsDAO = new EducationsDAO();
            educationsDAO.create(educations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createInsert(Educations educations) {
        try {
            EducationsDAO educationsDAO = new EducationsDAO();
            educationsDAO.createInsert(educations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Educations findEducationsByName(Educations educations) {
        try {
            EducationsDAO educationsDAO = new EducationsDAO();
            return educationsDAO.findEducationsByName(educations);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void removeEducation(Educations edu) {
        try {
            EducationsDAO educationsDAO = new EducationsDAO();
            educationsDAO.destroy(edu.getEducationsPK());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
