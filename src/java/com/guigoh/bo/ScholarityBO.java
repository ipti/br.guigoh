/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.ScholarityDAO;
import com.guigoh.entity.Scholarity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class ScholarityBO implements Serializable {

    public static List<Scholarity> getAll() {
        try {
            ScholarityDAO scholarityDAO = new ScholarityDAO();
            List<Scholarity> scholarityList = scholarityDAO.findScholarityEntities();
            if (scholarityList == null) {
                return new ArrayList<Scholarity>();
            } else {
                return scholarityList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
