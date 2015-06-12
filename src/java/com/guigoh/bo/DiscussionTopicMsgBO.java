/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.DiscussionTopicMsgJpaController;
import com.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import com.ipti.guigoh.model.entity.DiscussionTopicMsg;
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
        DiscussionTopicMsgJpaController discussionTopicMsgDAO = new DiscussionTopicMsgJpaController();
        List<DiscussionTopicMsg> discussionTopicMsgList = discussionTopicMsgDAO.findDiscussionTopicMsgsByTopic(id);
        if (discussionTopicMsgList == null) {
            return new ArrayList<DiscussionTopicMsg>();
        }
        return discussionTopicMsgList;
    }

    public static void replyTopic(DiscussionTopicMsg discussionTopicMsg) throws RollbackFailureException, Exception {
        DiscussionTopicMsgJpaController discussionTopicMsgDAO = new DiscussionTopicMsgJpaController();
        discussionTopicMsgDAO.create(discussionTopicMsg);
    }

    public static Timestamp getServerTime() {
        try {
            DiscussionTopicMsgJpaController discussionTopicMsgDAO = new DiscussionTopicMsgJpaController();
            return discussionTopicMsgDAO.getServerTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
