/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.DiscussionTopicMsgDAO;
import com.guigoh.dao.exceptions.RollbackFailureException;
import com.guigoh.entity.DiscussionTopicMsg;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IPTI
 */
public class DiscussionTopicMsgBO implements Serializable{

    public static List<DiscussionTopicMsg> findDiscussionTopicMsgsByTopic(Integer id) {
        DiscussionTopicMsgDAO discussionTopicMsgDAO = new DiscussionTopicMsgDAO();
        List<DiscussionTopicMsg> discussionTopicMsgList = discussionTopicMsgDAO.findDiscussionTopicMsgsByTopic(id);
        if (discussionTopicMsgList == null) {
            return new ArrayList<DiscussionTopicMsg>();
        }
        return discussionTopicMsgList;
    }

    public static void replyTopic(DiscussionTopicMsg discussionTopicMsg) throws RollbackFailureException, Exception {
        DiscussionTopicMsgDAO discussionTopicMsgDAO = new DiscussionTopicMsgDAO();
        discussionTopicMsgDAO.create(discussionTopicMsg);
    }

    public static Timestamp getServerTime() {
        try {
            DiscussionTopicMsgDAO discussionTopicMsgDAO = new DiscussionTopicMsgDAO();
            return discussionTopicMsgDAO.getServerTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
