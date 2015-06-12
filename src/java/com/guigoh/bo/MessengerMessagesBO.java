/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.MessengerMessagesJpaController;
import com.ipti.guigoh.model.jpa.exceptions.NonexistentEntityException;
import com.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import com.ipti.guigoh.model.entity.MessengerMessages;
import com.ipti.guigoh.model.entity.SocialProfile;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IPTI
 */
public class MessengerMessagesBO implements Serializable{

    public static List<MessengerMessages> getNonReadMessages(Integer socialProfileId) {
        MessengerMessagesJpaController messengerMessagesDAO = new MessengerMessagesJpaController();
        List<MessengerMessages> messagesList = messengerMessagesDAO.getNonReadMessages(socialProfileId);
        if (messagesList == null) {
            return new ArrayList<MessengerMessages>();
        }
        return messagesList;
    }

    public static List<MessengerMessages> getCurriculumMessages(Integer socialProfileId) {
        MessengerMessagesJpaController messengerMessagesDAO = new MessengerMessagesJpaController();
        List<MessengerMessages> messagesList = messengerMessagesDAO.getCurriculumMessages(socialProfileId);
        if (messagesList == null) {
            return new ArrayList<MessengerMessages>();
        }
        return messagesList;
    }

    public static List<MessengerMessages> getAllCurriculumMessages(Integer socialProfileId) {
        MessengerMessagesJpaController messengerMessagesDAO = new MessengerMessagesJpaController();
        List<MessengerMessages> messagesList = messengerMessagesDAO.getAllCurriculumMessages(socialProfileId);
        if (messagesList == null) {
            return new ArrayList<MessengerMessages>();
        }
        return messagesList;
    }

    public static List<MessengerMessages> getLastTenMessages(Integer loggedSocialProfileId, Integer socialProfileId) {
        MessengerMessagesJpaController messengerMessagesDAO = new MessengerMessagesJpaController();
        List<MessengerMessages> messagesList = messengerMessagesDAO.getLastTenMessages(loggedSocialProfileId, socialProfileId);
        if (messagesList == null) {
            return new ArrayList<MessengerMessages>();
        }
        return messagesList;
    }

    public static List<MessengerMessages> getAllMessages(Integer loggedSocialProfileId, Integer socialProfileId) {
        MessengerMessagesJpaController messengerMessagesDAO = new MessengerMessagesJpaController();
        List<MessengerMessages> messagesList = messengerMessagesDAO.getAllMessages(loggedSocialProfileId, socialProfileId);
        if (messagesList == null) {
            return new ArrayList<MessengerMessages>();
        }
        return messagesList;
    }

    public static List<SocialProfile> getAllContacts(Integer loggedSocialProfileId) {
        MessengerMessagesJpaController messengerMessagesDAO = new MessengerMessagesJpaController();
        List<SocialProfile> contactsList = messengerMessagesDAO.getAllContacts(loggedSocialProfileId);
        if (contactsList == null) {
            return new ArrayList<SocialProfile>();
        }
        return contactsList;
    }

    public static void editMessage(MessengerMessages mm) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            MessengerMessagesJpaController messengerMessagesDAO = new MessengerMessagesJpaController();
            messengerMessagesDAO.edit(mm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createMessage(MessengerMessages mm) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            MessengerMessagesJpaController messengerMessagesDAO = new MessengerMessagesJpaController();
            messengerMessagesDAO.create(mm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Timestamp getServerTime() {
        try {
            MessengerMessagesJpaController messengerMessagesDAO = new MessengerMessagesJpaController();
            return messengerMessagesDAO.getServerTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
