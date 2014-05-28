/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.StateDAO;
import com.guigoh.primata.entity.State;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class StateBO implements Serializable {

    private StateDAO stateDAO;

    public StateBO() {
        stateDAO = new StateDAO();
    }

    public List<State> getAll() {
        try {
            return stateDAO.findStateEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<State> findStatesByCountryId(Integer id) {
        try {
            return stateDAO.findStatesByCountryId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public State getStateByName(String stateName) {
        try {
            return stateDAO.findStateByName(stateName);
        } catch (Exception e) {
            e.printStackTrace();
            return new State();
        }
    }
}
