/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.InterestsTypeDAO;
import com.guigoh.entity.InterestsType;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class InterestsTypeBO implements Serializable {

    private InterestsTypeDAO interestsTypeDAO;
    
    public InterestsTypeBO(){
        interestsTypeDAO = new InterestsTypeDAO();
    }
    
    public List<InterestsType> findInterestsType() {
        try {
            return interestsTypeDAO.findInterestsTypeEntities();
        } catch (Exception e) {
        }
        return null;
    }

    public InterestsType findInterestsTypeByName(String typeName) {
        try {
            return interestsTypeDAO.findInterestsTypeByName(typeName);
        } catch (Exception e) {
        }
        return null;

    }
}
