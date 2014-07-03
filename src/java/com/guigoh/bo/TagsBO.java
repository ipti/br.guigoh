/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.TagsDAO;
import com.guigoh.entity.DiscussionTopic;
import com.guigoh.entity.Tags;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class TagsBO implements Serializable {

    private TagsDAO tagsDAO;

    public TagsBO() {
        tagsDAO = new TagsDAO();
    }

    public void create(Tags tags) {
        try {
            tagsDAO.create(tags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTagsDiscussionTopic(Tags tags, DiscussionTopic discussionTopic) {
        try {
            tagsDAO.createTagsDiscussionTopic(tags, discussionTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Tags findTagsByName(String tags) {
        try {
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
            tagList = tagsDAO.findTagsByText(tags);
            if (tagList == null) {
                return new ArrayList<Tags>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tagList;
    }

    public List<Tags> getAllTags() {
        List<Tags> tagList = new ArrayList<Tags>();
        try {
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
