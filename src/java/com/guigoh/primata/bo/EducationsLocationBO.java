/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.EducationsLocationDAO;
import com.guigoh.primata.dao.EducationsNameDAO;
import com.guigoh.primata.entity.Educations;
import com.guigoh.primata.entity.EducationsLocation;
import com.guigoh.primata.entity.EducationsName;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class EducationsLocationBO implements Serializable {
    public List<EducationsLocation> getAll() {
        EducationsLocationDAO educationsNameDAO = new EducationsLocationDAO();
        return educationsNameDAO.findEducationsLocationEntities();
    }

    public void create(EducationsLocation educationsLocation) {
        try {
           EducationsLocationDAO educationsNameDAO = new EducationsLocationDAO();
           educationsNameDAO.create(educationsLocation);               
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EducationsLocation findEducationsLocationByName(EducationsLocation locationId) {
        EducationsLocationDAO educationsNameDAO = new EducationsLocationDAO();
        return educationsNameDAO.findEducationsByName(locationId);
    }
}
