/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.MessengerStatusDAO;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import com.guigoh.primata.entity.MessengerStatus;
import java.sql.Timestamp;

/**
 *
 * @author IPTI
 */
public class MessengerStatusBO {
    
    public Double getServerTime() {
        MessengerStatusDAO messengerStatusDAO = new MessengerStatusDAO();
        return messengerStatusDAO.getServerTime();
    }
    
    public Long getUsersOnline(){
        MessengerStatusDAO messengerStatusDAO = new MessengerStatusDAO();
        return messengerStatusDAO.getUsersOnline();
    }
    
    public void pingUser(MessengerStatus ms) throws RollbackFailureException, Exception {
        MessengerStatusDAO messengerStatusDAO = new MessengerStatusDAO();
        if (messengerStatusDAO.findMessengerStatus(ms.getSocialProfileId()) == null) {
            messengerStatusDAO.create(ms);
        } else {
            messengerStatusDAO.edit(ms);
        }
    }
}
