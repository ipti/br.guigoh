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
    
    public static List<InterestsType> findInterestsType() {
        try {
            InterestsTypeDAO interestsTypeDAO = new InterestsTypeDAO();
            return interestsTypeDAO.findInterestsTypeEntities();
        } catch (Exception e) {
        }
        return null;
    }

    public static InterestsType findInterestsTypeByName(String typeName) {
        try {
            InterestsTypeDAO interestsTypeDAO = new InterestsTypeDAO();
            return interestsTypeDAO.findInterestsTypeByName(typeName);
        } catch (Exception e) {
        }
        return null;

    }
}
