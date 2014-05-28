/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.MessengerStatusDAO;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import com.guigoh.primata.entity.MessengerStatus;

/**
 *
 * @author IPTI
 */
public class MessengerStatusBO {

    private MessengerStatusDAO messengerStatusDAO;

    public MessengerStatusBO() {
        messengerStatusDAO = new MessengerStatusDAO();
    }

    public Double getServerTime() {
        try {
            return messengerStatusDAO.getServerTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long getUsersOnline() {
        try {
            return messengerStatusDAO.getUsersOnline();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void pingUser(MessengerStatus ms) throws RollbackFailureException, Exception {
        try {
            if (messengerStatusDAO.findMessengerStatus(ms.getSocialProfileId()) == null) {
                messengerStatusDAO.create(ms);
            } else {
                messengerStatusDAO.edit(ms);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
