/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import br.org.ipti.guigoh.model.jpa.controller.DiscussionTopicJpaController;
import br.org.ipti.guigoh.model.entity.DiscussionTopic;
import br.org.ipti.guigoh.model.entity.NewActivity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class DiscussionTopicBO implements Serializable {

    public static void create(DiscussionTopic discussionTopic) {
        try {
            DiscussionTopicJpaController discussionTopicDAO = new DiscussionTopicJpaController();
            discussionTopicDAO.create(discussionTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<NewActivity> getLastActivities() {
        DiscussionTopicJpaController discussionTopicDAO = new DiscussionTopicJpaController();
        List<NewActivity> newActivityList = discussionTopicDAO.getLastActivities();
        if (newActivityList == null) {
            return new ArrayList<NewActivity>();
        }
        return newActivityList;
    }

    public static List<DiscussionTopic> findDiscussionTopicsByTheme(Integer id) {
        DiscussionTopicJpaController discussionTopicDAO = new DiscussionTopicJpaController();
        List<DiscussionTopic> discussionTopicList = discussionTopicDAO.findDiscussionTopicsByTheme(id);
        if (discussionTopicList == null) {
            return new ArrayList<DiscussionTopic>();
        }
        return discussionTopicList;
    }

    public static List<DiscussionTopic> loadDiscussionTopicsByExpression(String expression, String tag, Integer id) {
        DiscussionTopicJpaController discussionTopicDAO = new DiscussionTopicJpaController();
        List<DiscussionTopic> discussionTopicList = discussionTopicDAO.loadDiscussionTopicsByExpression(expression, tag, id);
        if (discussionTopicList == null) {
            return new ArrayList<DiscussionTopic>();
        }
        return discussionTopicList;
    }

    public static DiscussionTopic findDiscussionTopicByID(int discussionTopicID) {
        try {
            DiscussionTopicJpaController discussionTopicDAO = new DiscussionTopicJpaController();
            return discussionTopicDAO.findDiscussionTopic(discussionTopicID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Timestamp getServerTime() {
        try {
            DiscussionTopicJpaController discussionTopicDAO = new DiscussionTopicJpaController();
            return discussionTopicDAO.getServerTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}