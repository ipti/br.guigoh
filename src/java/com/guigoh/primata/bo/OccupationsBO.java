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
    
    public Occupations findSocialProfile(Integer id){
        OccupationsDAO occupationsDAO = new OccupationsDAO();
        return occupationsDAO.findOccupations(id);
    }
    
    public List<Occupations> findOccupationsByType(Integer id){
        OccupationsDAO occupationsDAO = new OccupationsDAO();
        List<Occupations> occupationsList = occupationsDAO.findOccupationsByType(id);
        if (occupationsList == null) {
            return new ArrayList<Occupations>();
        }
        return occupationsList;
    }
    
    public List<Occupations> getAll(){
        OccupationsDAO occupationsDAO = new OccupationsDAO();
        List<Occupations> occupationsList = occupationsDAO.findOccupationsEntities();
        if (occupationsList == null) {
            return new ArrayList<Occupations>();
        }
        return occupationsList;
    }
    
    public Occupations findOccupationsByName(Occupations occupations){
        try {
        OccupationsDAO occupationsDAO = new OccupationsDAO();
        return occupationsDAO.findOccupationsByName(occupations);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Occupations();
    }
    
    public Occupations findOccupationsByNameByType(Occupations occupations){
        try {
            OccupationsDAO occupationsDAO = new OccupationsDAO();
            return occupationsDAO.findOccupationsByNameByType(occupations);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Occupations();
    }
    
    public void create(Occupations occupations){
        try {
            OccupationsDAO occupationsDAO = new OccupationsDAO();
            occupationsDAO.create(occupations);               
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void createInsert(Occupations occupations){
        try {
            OccupationsDAO occupationsDAO = new OccupationsDAO();
            occupationsDAO.createInsert(occupations);               
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
