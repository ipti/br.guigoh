/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.DiscussionTopicMsgDAO;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import com.guigoh.primata.entity.DiscussionTopicMsg;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IPTI
 */
public class DiscussionTopicMsgBO {
    
    public List<DiscussionTopicMsg> findDiscussionTopicMsgsByTopic(Integer id) {
        DiscussionTopicMsgDAO discussionTopicMsgDAO = new DiscussionTopicMsgDAO();
        List<DiscussionTopicMsg> discussionTopicMsgList = discussionTopicMsgDAO.findDiscussionTopicMsgsByTopic(id);
        if (discussionTopicMsgList == null) {
            return new ArrayList<DiscussionTopicMsg>();
        }
        return discussionTopicMsgList;
    }
    
    public void replyTopic(DiscussionTopicMsg discussionTopicMsg) throws RollbackFailureException, Exception{
        DiscussionTopicMsgDAO discussionTopicMsgDAO = new DiscussionTopicMsgDAO();
        discussionTopicMsgDAO.create(discussionTopicMsg);
    }
    
    public Timestamp getServerTime(){
        DiscussionTopicMsgDAO discussionTopicMsgDAO = new DiscussionTopicMsgDAO();
        return discussionTopicMsgDAO.getServerTime();
    }
}
