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

    public static List<OccupationsType> getAll() {
        try {
            OccupationsTypeDAO occupationsTypeDAO = new OccupationsTypeDAO();
            return occupationsTypeDAO.findOccupationsTypeEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
