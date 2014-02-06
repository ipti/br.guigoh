/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.OccupationsTypeDAO;
import com.guigoh.primata.entity.OccupationsType;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class OccupationsTypeBO implements Serializable {
    public List<OccupationsType> getAll(){
        OccupationsTypeDAO occupationsDAO = new OccupationsTypeDAO();
        return occupationsDAO.findOccupationsTypeEntities();
    }
    
}
