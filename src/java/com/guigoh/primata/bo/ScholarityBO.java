/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.ScholarityDAO;
import com.guigoh.primata.entity.Scholarity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class ScholarityBO implements Serializable {

    public List<Scholarity> getAll() {
        ScholarityDAO scholarityDAO = new ScholarityDAO();
        List<Scholarity> scholarityList = scholarityDAO.findScholarityEntities();
        if (scholarityList == null) {
            return new ArrayList<Scholarity>();
        }
        return scholarityList;
    }
}
