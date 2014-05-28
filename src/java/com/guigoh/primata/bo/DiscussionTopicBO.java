/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.DiscussionTopicDAO;
import com.guigoh.primata.entity.DiscussionTopic;
import com.guigoh.primata.entity.NewActivity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class DiscussionTopicBO implements Serializable {

    private DiscussionTopicDAO discussionTopicDAO;

    public DiscussionTopicBO() {
        discussionTopicDAO = new DiscussionTopicDAO();
    }

    public void create(DiscussionTopic discussionTopic) {
        try {
            discussionTopicDAO.create(discussionTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<NewActivity> getLastActivities() {
        List<NewActivity> newActivityList = discussionTopicDAO.getLastActivities();
        if (newActivityList == null) {
            return new ArrayList<NewActivity>();
        }
        return newActivityList;
    }

    public List<DiscussionTopic> findDiscussionTopicsByTheme(Integer id) {
        List<DiscussionTopic> discussionTopicList = discussionTopicDAO.findDiscussionTopicsByTheme(id);
        if (discussionTopicList == null) {
            return new ArrayList<DiscussionTopic>();
        }
        return discussionTopicList;
    }

    public List<DiscussionTopic> loadDiscussionTopicsByExpression(String expression, String tag, Integer id) {
        List<DiscussionTopic> discussionTopicList = discussionTopicDAO.loadDiscussionTopicsByExpression(expression, tag, id);
        if (discussionTopicList == null) {
            return new ArrayList<DiscussionTopic>();
        }
        return discussionTopicList;
    }

    public DiscussionTopic findDiscussionTopicByID(int discussionTopicID) {
        try {
            return discussionTopicDAO.findDiscussionTopic(discussionTopicID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Timestamp getServerTime() {
        try {
            return discussionTopicDAO.getServerTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}