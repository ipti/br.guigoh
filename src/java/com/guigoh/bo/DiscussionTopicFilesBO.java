/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.DiscussionTopicFilesJpaController;
import com.ipti.guigoh.model.entity.DiscussionTopicFiles;
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
            DiscussionTopicFilesJpaController discussionTopicFilesDAO = new DiscussionTopicFilesJpaController();
            discussionTopicFilesDAO.create(discussionTopicFiles);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<DiscussionTopicFiles> getDiscussionTopicFilesByFK(Integer id, char type) {
        List<DiscussionTopicFiles> discussionTopicFilesList = new ArrayList<DiscussionTopicFiles>();
        try {
            DiscussionTopicFilesJpaController discussionTopicFilesDAO = new DiscussionTopicFilesJpaController();
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
