/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.StateDAO;
import com.guigoh.entity.State;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class StateBO implements Serializable {

    public static List<State> getAll() {
        try {
            StateDAO stateDAO = new StateDAO();
            return stateDAO.findStateEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<State> findStatesByCountryId(Integer id) {
        try {
            StateDAO stateDAO = new StateDAO();
            return stateDAO.findStatesByCountryId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static State getStateByName(String stateName) {
        try {
            StateDAO stateDAO = new StateDAO();
            return stateDAO.findStateByName(stateName);
        } catch (Exception e) {
            e.printStackTrace();
            return new State();
        }
    }
}
