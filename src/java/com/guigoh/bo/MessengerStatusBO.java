/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import br.org.ipti.guigoh.model.jpa.controller.MessengerStatusJpaController;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.model.entity.MessengerStatus;
import java.io.Serializable;

/**
 *
 * @author IPTI
 */
public class MessengerStatusBO implements Serializable{
    
    public static Double getServerTime() {
        try {
            MessengerStatusJpaController messengerStatusDAO = new MessengerStatusJpaController();
            return messengerStatusDAO.getServerTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long getUsersOnline() {
        try {
            MessengerStatusJpaController messengerStatusDAO = new MessengerStatusJpaController();
            return messengerStatusDAO.getUsersOnline();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void pingUser(MessengerStatus ms) throws RollbackFailureException, Exception {
        try {
            MessengerStatusJpaController messengerStatusDAO = new MessengerStatusJpaController();
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
