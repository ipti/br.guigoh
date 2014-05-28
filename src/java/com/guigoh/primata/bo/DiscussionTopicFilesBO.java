/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.DiscussionTopicFilesDAO;
import com.guigoh.primata.entity.DiscussionTopicFiles;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class DiscussionTopicFilesBO implements Serializable {

    private DiscussionTopicFilesDAO discussionTopicFilesDAO;
    
    public DiscussionTopicFilesBO(){
        discussionTopicFilesDAO = new DiscussionTopicFilesDAO();
    }
    
    public void create(DiscussionTopicFiles discussionTopicFiles) {
        try {
            discussionTopicFilesDAO.create(discussionTopicFiles);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<DiscussionTopicFiles> getDiscussionTopicFilesByFK(Integer id, char type) {
        List<DiscussionTopicFiles> discussionTopicFilesList = new ArrayList<DiscussionTopicFiles>();
        try {
            discussionTopicFilesList = discussionTopicFilesDAO.getDiscussionTopicFilesByFK(id, type);
            if (discussionTopicFilesList == null) {
                return new ArrayList<DiscussionTopicFiles>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return discussionTopicFilesList;
    }
}
