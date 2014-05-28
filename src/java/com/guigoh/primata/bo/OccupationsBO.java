/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.OccupationsDAO;
import com.guigoh.primata.entity.Occupations;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class OccupationsBO implements Serializable {

    private OccupationsDAO occupationsDAO;

    public OccupationsBO() {
        occupationsDAO = new OccupationsDAO();
    }

    public Occupations findSocialProfile(Integer id) {
        try {
            return occupationsDAO.findOccupations(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Occupations> findOccupationsByType(Integer id) {
        try {
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

    public List<Occupations> getAll() {
        try {
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

    public Occupations findOccupationsByName(Occupations occupations) {
        try {
            return occupationsDAO.findOccupationsByName(occupations);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Occupations();
    }

    public Occupations findOccupationsByNameByType(Occupations occupations) {
        try {
            return occupationsDAO.findOccupationsByNameByType(occupations);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Occupations();
    }

    public void create(Occupations occupations) {
        try {
            occupationsDAO.create(occupations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createInsert(Occupations occupations) {
        try {
            occupationsDAO.createInsert(occupations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
