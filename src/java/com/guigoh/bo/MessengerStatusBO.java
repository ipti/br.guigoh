/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.MessengerStatusDAO;
import com.guigoh.dao.exceptions.RollbackFailureException;
import com.guigoh.entity.MessengerStatus;
import java.io.Serializable;

/**
 *
 * @author IPTI
 */
public class MessengerStatusBO implements Serializable{
    
    public static Double getServerTime() {
        try {
            MessengerStatusDAO messengerStatusDAO = new MessengerStatusDAO();
            return messengerStatusDAO.getServerTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long getUsersOnline() {
        try {
            MessengerStatusDAO messengerStatusDAO = new MessengerStatusDAO();
            return messengerStatusDAO.getUsersOnline();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void pingUser(MessengerStatus ms) throws RollbackFailureException, Exception {
        try {
            MessengerStatusDAO messengerStatusDAO = new MessengerStatusDAO();
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
