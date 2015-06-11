/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.OccupationsDAO;
import com.guigoh.entity.Occupations;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class OccupationsBO implements Serializable {

    public static Occupations findSocialProfile(Integer id) {
        try {
            OccupationsDAO occupationsDAO = new OccupationsDAO();
            return occupationsDAO.findOccupations(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Occupations> findOccupationsByType(Integer id) {
        try {
            OccupationsDAO occupationsDAO = new OccupationsDAO();
            List<Occupations> occupationsList = occupationsDAO.findOccupationsByType(id);
            if (occupationsList == null) {
                return new ArrayList<Occupations>();
            }
            return occupationsList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Occupations> getAll() {
        try {
            OccupationsDAO occupationsDAO = new OccupationsDAO();
            List<Occupations> occupationsList = occupationsDAO.findOccupationsEntities();
            if (occupationsList == null) {
                return new ArrayList<Occupations>();
            }
            return occupationsList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Occupations findOccupationsByName(Occupations occupations) {
        try {
            OccupationsDAO occupationsDAO = new OccupationsDAO();
            return occupationsDAO.findOccupationsByName(occupations);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Occupations();
    }

    public static Occupations findOccupationsByNameByType(Occupations occupations) {
        try {
            OccupationsDAO occupationsDAO = new OccupationsDAO();
            return occupationsDAO.findOccupationsByNameByType(occupations);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Occupations();
    }

    public static void create(Occupations occupations) {
        try {
            OccupationsDAO occupationsDAO = new OccupationsDAO();
            occupationsDAO.create(occupations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createInsert(Occupations occupations) {
        try {
            OccupationsDAO occupationsDAO = new OccupationsDAO();
            occupationsDAO.createInsert(occupations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
