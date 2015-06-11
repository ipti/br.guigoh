/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.DiscussionTopicFilesDAO;
import com.guigoh.entity.DiscussionTopicFiles;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class DiscussionTopicFilesBO implements Serializable {
    
    public static void create(DiscussionTopicFiles discussionTopicFiles) {
        try {
            DiscussionTopicFilesDAO discussionTopicFilesDAO = new DiscussionTopicFilesDAO();
            discussionTopicFilesDAO.create(discussionTopicFiles);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<DiscussionTopicFiles> getDiscussionTopicFilesByFK(Integer id, char type) {
        List<DiscussionTopicFiles> discussionTopicFilesList = new ArrayList<DiscussionTopicFiles>();
        try {
            DiscussionTopicFilesDAO discussionTopicFilesDAO = new DiscussionTopicFilesDAO();
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
