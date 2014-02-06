/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.InterestsTypeDAO;
import com.guigoh.primata.entity.InterestsType;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class InterestsTypeBO implements Serializable {

    public List<InterestsType> findInterestsType() {
        try {
            InterestsTypeDAO interestsTypeDAO = new InterestsTypeDAO();
            return interestsTypeDAO.findInterestsTypeEntities();
        } catch (Exception e) {
        }
        return null;
    }

    public InterestsType findInterestsTypeByName(String typeName) {
        try {
            InterestsTypeDAO interestsTypeDAO = new InterestsTypeDAO();
            return interestsTypeDAO.findInterestsTypeByName(typeName);
        } catch (Exception e) {
        }
        return null;

    }
}
