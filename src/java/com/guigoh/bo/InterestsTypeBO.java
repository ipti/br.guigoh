/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.InterestsTypeJpaController;
import com.ipti.guigoh.model.entity.InterestsType;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class InterestsTypeBO implements Serializable {
    
    public static List<InterestsType> findInterestsType() {
        try {
            InterestsTypeJpaController interestsTypeDAO = new InterestsTypeJpaController();
            return interestsTypeDAO.findInterestsTypeEntities();
        } catch (Exception e) {
        }
        return null;
    }

    public static InterestsType findInterestsTypeByName(String typeName) {
        try {
            InterestsTypeJpaController interestsTypeDAO = new InterestsTypeJpaController();
            return interestsTypeDAO.findInterestsTypeByName(typeName);
        } catch (Exception e) {
        }
        return null;

    }
}
