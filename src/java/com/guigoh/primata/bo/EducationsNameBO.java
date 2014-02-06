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

    public static void create(EducationsName educationsNamet) {
        try {
           EducationsNameDAO educationsNameDAO = new EducationsNameDAO();
           educationsNameDAO.create(educationsNamet);               
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EducationsName findEducationsNameByName(EducationsName nameId) {
         EducationsNameDAO educationsNameDAO = new EducationsNameDAO();
        return educationsNameDAO.findEducationsByName(nameId);
    }
    public List<EducationsName> getAll() {
        EducationsNameDAO educationsNameDAO = new EducationsNameDAO();
        return educationsNameDAO.findEducationsNameEntities();
    }
}
