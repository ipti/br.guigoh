/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.DiscussionTopicDAO;
import com.guigoh.primata.dao.DiscussionTopicMsgDAO;
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

    public void create(DiscussionTopic discussionTopic) {
        try {
            DiscussionTopicDAO discussionTopicDAO = new DiscussionTopicDAO();
            discussionTopicDAO.create(discussionTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public List<NewActivity> getLastActivities(){
        DiscussionTopicDAO discussionTopicDAO = new DiscussionTopicDAO();
        List<NewActivity> newActivityList = discussionTopicDAO.getLastActivities();
        if (newActivityList == null) {
            return new ArrayList<NewActivity>();
        }
        return newActivityList;
    }

    public List<DiscussionTopic> findDiscussionTopicsByTheme(Integer id) {
        DiscussionTopicDAO discussionTopicDAO = new DiscussionTopicDAO();
        List<DiscussionTopic> discussionTopicList = discussionTopicDAO.findDiscussionTopicsByTheme(id);
        if (discussionTopicList == null) {
            return new ArrayList<DiscussionTopic>();
        }
        return discussionTopicList;
    }
    
    public List<DiscussionTopic> loadDiscussionTopicsByExpression(String expression, String tag, Integer id){
        DiscussionTopicDAO discussionTopicDAO = new DiscussionTopicDAO();
        List<DiscussionTopic> discussionTopicList = discussionTopicDAO.loadDiscussionTopicsByExpression(expression, tag, id);
        if (discussionTopicList == null) {
            return new ArrayList<DiscussionTopic>();
        }
        return discussionTopicList;
    }
    
    public DiscussionTopic findDiscussionTopicByID(int discussionTopicID){
        DiscussionTopicDAO discussionTopicDAO = new DiscussionTopicDAO();
        return discussionTopicDAO.findDiscussionTopic(discussionTopicID);
    }
    
    public Timestamp getServerTime(){
        DiscussionTopicDAO discussionTopicDAO = new DiscussionTopicDAO();
        return discussionTopicDAO.getServerTime();
    }
}