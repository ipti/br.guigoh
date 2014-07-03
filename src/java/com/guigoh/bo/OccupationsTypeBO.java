/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.OccupationsTypeDAO;
import com.guigoh.entity.OccupationsType;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class OccupationsTypeBO implements Serializable {

    private OccupationsTypeDAO occupationsTypeDAO;

    public OccupationsTypeBO() {
        occupationsTypeDAO = new OccupationsTypeDAO();
    }

    public List<OccupationsType> getAll() {
        try {
            return occupationsTypeDAO.findOccupationsTypeEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
