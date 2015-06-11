/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.EducationsNameDAO;
import com.guigoh.entity.EducationsName;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class EducationsNameBO implements Serializable {

    public static void create(EducationsName educationsName) {
        try {
            EducationsNameDAO educationsNameDAO = new EducationsNameDAO();
            educationsNameDAO.create(educationsName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static EducationsName findEducationsNameByName(EducationsName nameId) {
        try {
            EducationsNameDAO educationsNameDAO = new EducationsNameDAO();
            return educationsNameDAO.findEducationsByName(nameId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<EducationsName> getAll() {
        try {
            EducationsNameDAO educationsNameDAO = new EducationsNameDAO();
            return educationsNameDAO.findEducationsNameEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
