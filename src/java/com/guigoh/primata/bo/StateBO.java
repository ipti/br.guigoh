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
    
    public List<State> getAll(){
        StateDAO stateDAO = new StateDAO();
        return stateDAO.findStateEntities();
    }
    
    public List<State> findStatesByCountryId(Integer id){
        StateDAO stateDAO = new StateDAO();
        return stateDAO.findStatesByCountryId(id);
    }
    
    public State getStateByName(String stateName) {
        try {
            StateDAO stateDAO = new StateDAO();
            return stateDAO.findStateByName(stateName);
        } catch (Exception e) {
            e.printStackTrace();
            return new State();
        }
    }
}
