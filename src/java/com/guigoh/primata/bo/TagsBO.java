/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.TagsDAO;
import com.guigoh.primata.entity.DiscussionTopic;
import com.guigoh.primata.entity.Tags;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class TagsBO implements Serializable {

    public void create(Tags tags) {
        try {
            TagsDAO tagsDAO = new TagsDAO();
            tagsDAO.create(tags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     public void createTagsDiscussionTopic(Tags tags, DiscussionTopic discussionTopic) {
        try {
            TagsDAO tagsDAO = new TagsDAO();
            tagsDAO.createTagsDiscussionTopic(tags,discussionTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Tags findTagsByName(String tags) {
        try {
            TagsDAO tagsDAO = new TagsDAO();
            Tags tagsT = tagsDAO.findTagsByName(tags);
            if (tagsT == null) {
                return new Tags();
            }
            return tagsT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Tags();
    }
    
    public List<Tags> findTagsByText(String tags) {
        List<Tags> tagList = new ArrayList<Tags>();
        try {
            TagsDAO tagsDAO = new TagsDAO();
            tagList = tagsDAO.findTagsByText(tags);
            if (tagList == null) {
                return new ArrayList<Tags>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tagList;
    }
    
    public List<Tags> getAllTags(){
        List<Tags> tagList = new ArrayList<Tags>();
        try {
            TagsDAO tagsDAO = new TagsDAO();
            tagList = tagsDAO.findTagsEntities();
            if (tagList == null) {
                return new ArrayList<Tags>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tagList;
    }
}
