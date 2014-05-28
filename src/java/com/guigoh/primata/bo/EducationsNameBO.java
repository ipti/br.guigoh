/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.EducationsNameDAO;
import com.guigoh.primata.entity.EducationsName;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class EducationsNameBO implements Serializable {

    private EducationsNameDAO educationsNameDAO;

    public EducationsNameBO() {
        educationsNameDAO = new EducationsNameDAO();
    }

    public void create(EducationsName educationsName) {
        try {
            educationsNameDAO.create(educationsName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EducationsName findEducationsNameByName(EducationsName nameId) {
        try {
            return educationsNameDAO.findEducationsByName(nameId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<EducationsName> getAll() {
        try {
            return educationsNameDAO.findEducationsNameEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
