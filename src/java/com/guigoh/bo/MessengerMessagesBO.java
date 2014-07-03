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

    private MessengerMessagesDAO messengerMessagesDAO;

    public MessengerMessagesBO() {
        messengerMessagesDAO = new MessengerMessagesDAO();
    }

    public List<MessengerMessages> getNonReadMessages(Integer socialProfileId) {
        List<MessengerMessages> messagesList = messengerMessagesDAO.getNonReadMessages(socialProfileId);
        if (messagesList == null) {
            return new ArrayList<MessengerMessages>();
        }
        return messagesList;
    }

    public List<MessengerMessages> getCurriculumMessages(Integer socialProfileId) {
        List<MessengerMessages> messagesList = messengerMessagesDAO.getCurriculumMessages(socialProfileId);
        if (messagesList == null) {
            return new ArrayList<MessengerMessages>();
        }
        return messagesList;
    }

    public List<MessengerMessages> getAllCurriculumMessages(Integer socialProfileId) {
        List<MessengerMessages> messagesList = messengerMessagesDAO.getAllCurriculumMessages(socialProfileId);
        if (messagesList == null) {
            return new ArrayList<MessengerMessages>();
        }
        return messagesList;
    }

    public List<MessengerMessages> getLastTenMessages(Integer loggedSocialProfileId, Integer socialProfileId) {
        List<MessengerMessages> messagesList = messengerMessagesDAO.getLastTenMessages(loggedSocialProfileId, socialProfileId);
        if (messagesList == null) {
            return new ArrayList<MessengerMessages>();
        }
        return messagesList;
    }

    public List<MessengerMessages> getAllMessages(Integer loggedSocialProfileId, Integer socialProfileId) {
        List<MessengerMessages> messagesList = messengerMessagesDAO.getAllMessages(loggedSocialProfileId, socialProfileId);
        if (messagesList == null) {
            return new ArrayList<MessengerMessages>();
        }
        return messagesList;
    }

    public List<SocialProfile> getAllContacts(Integer loggedSocialProfileId) {
        List<SocialProfile> contactsList = messengerMessagesDAO.getAllContacts(loggedSocialProfileId);
        if (contactsList == null) {
            return new ArrayList<SocialProfile>();
        }
        return contactsList;
    }

    public void editMessage(MessengerMessages mm) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            messengerMessagesDAO.edit(mm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createMessage(MessengerMessages mm) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            messengerMessagesDAO.create(mm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Timestamp getServerTime() {
        try {
            return messengerMessagesDAO.getServerTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
