/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.MessengerMessagesDAO;
import com.guigoh.dao.exceptions.NonexistentEntityException;
import com.guigoh.dao.exceptions.RollbackFailureException;
import com.guigoh.entity.MessengerMessages;
import com.guigoh.entity.SocialProfile;
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
        MessengerMessagesDAO messengerMessagesDAO = new MessengerMessagesDAO();
        List<MessengerMessages> messagesList = messengerMessagesDAO.getNonReadMessages(socialProfileId);
        if (messagesList == null) {
            return new ArrayList<MessengerMessages>();
        }
        return messagesList;
    }

    public static List<MessengerMessages> getCurriculumMessages(Integer socialProfileId) {
        MessengerMessagesDAO messengerMessagesDAO = new MessengerMessagesDAO();
        List<MessengerMessages> messagesList = messengerMessagesDAO.getCurriculumMessages(socialProfileId);
        if (messagesList == null) {
            return new ArrayList<MessengerMessages>();
        }
        return messagesList;
    }

    public static List<MessengerMessages> getAllCurriculumMessages(Integer socialProfileId) {
        MessengerMessagesDAO messengerMessagesDAO = new MessengerMessagesDAO();
        List<MessengerMessages> messagesList = messengerMessagesDAO.getAllCurriculumMessages(socialProfileId);
        if (messagesList == null) {
            return new ArrayList<MessengerMessages>();
        }
        return messagesList;
    }

    public static List<MessengerMessages> getLastTenMessages(Integer loggedSocialProfileId, Integer socialProfileId) {
        MessengerMessagesDAO messengerMessagesDAO = new MessengerMessagesDAO();
        List<MessengerMessages> messagesList = messengerMessagesDAO.getLastTenMessages(loggedSocialProfileId, socialProfileId);
        if (messagesList == null) {
            return new ArrayList<MessengerMessages>();
        }
        return messagesList;
    }

    public static List<MessengerMessages> getAllMessages(Integer loggedSocialProfileId, Integer socialProfileId) {
        MessengerMessagesDAO messengerMessagesDAO = new MessengerMessagesDAO();
        List<MessengerMessages> messagesList = messengerMessagesDAO.getAllMessages(loggedSocialProfileId, socialProfileId);
        if (messagesList == null) {
            return new ArrayList<MessengerMessages>();
        }
        return messagesList;
    }

    public static List<SocialProfile> getAllContacts(Integer loggedSocialProfileId) {
        MessengerMessagesDAO messengerMessagesDAO = new MessengerMessagesDAO();
        List<SocialProfile> contactsList = messengerMessagesDAO.getAllContacts(loggedSocialProfileId);
        if (contactsList == null) {
            return new ArrayList<SocialProfile>();
        }
        return contactsList;
    }

    public static void editMessage(MessengerMessages mm) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            MessengerMessagesDAO messengerMessagesDAO = new MessengerMessagesDAO();
            messengerMessagesDAO.edit(mm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createMessage(MessengerMessages mm) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            MessengerMessagesDAO messengerMessagesDAO = new MessengerMessagesDAO();
            messengerMessagesDAO.create(mm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Timestamp getServerTime() {
        try {
            MessengerMessagesDAO messengerMessagesDAO = new MessengerMessagesDAO();
            return messengerMessagesDAO.getServerTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
