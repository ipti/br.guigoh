/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.DiscussionTopicDAO;
import com.guigoh.entity.DiscussionTopic;
import com.guigoh.entity.NewActivity;
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
            DiscussionTopicDAO discussionTopicDAO = new DiscussionTopicDAO();
            discussionTopicDAO.create(discussionTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<NewActivity> getLastActivities() {
        DiscussionTopicDAO discussionTopicDAO = new DiscussionTopicDAO();
        List<NewActivity> newActivityList = discussionTopicDAO.getLastActivities();
        if (newActivityList == null) {
            return new ArrayList<NewActivity>();
        }
        return newActivityList;
    }

    public static List<DiscussionTopic> findDiscussionTopicsByTheme(Integer id) {
        DiscussionTopicDAO discussionTopicDAO = new DiscussionTopicDAO();
        List<DiscussionTopic> discussionTopicList = discussionTopicDAO.findDiscussionTopicsByTheme(id);
        if (discussionTopicList == null) {
            return new ArrayList<DiscussionTopic>();
        }
        return discussionTopicList;
    }

    public static List<DiscussionTopic> loadDiscussionTopicsByExpression(String expression, String tag, Integer id) {
        DiscussionTopicDAO discussionTopicDAO = new DiscussionTopicDAO();
        List<DiscussionTopic> discussionTopicList = discussionTopicDAO.loadDiscussionTopicsByExpression(expression, tag, id);
        if (discussionTopicList == null) {
            return new ArrayList<DiscussionTopic>();
        }
        return discussionTopicList;
    }

    public static DiscussionTopic findDiscussionTopicByID(int discussionTopicID) {
        try {
            DiscussionTopicDAO discussionTopicDAO = new DiscussionTopicDAO();
            return discussionTopicDAO.findDiscussionTopic(discussionTopicID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Timestamp getServerTime() {
        try {
            DiscussionTopicDAO discussionTopicDAO = new DiscussionTopicDAO();
            return discussionTopicDAO.getServerTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}