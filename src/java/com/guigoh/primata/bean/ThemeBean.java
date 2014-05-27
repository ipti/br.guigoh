/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.DiscussionTopicBO;
import com.guigoh.primata.bo.InterestsBO;
import com.guigoh.primata.bo.TagsBO;
import com.guigoh.primata.entity.DiscussionTopic;
import com.guigoh.primata.entity.Interests;
import com.guigoh.primata.entity.Tags;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author IPTI
 */
@SessionScoped
@ManagedBean(name = "themeBean")
public class ThemeBean implements Serializable{
    
    private Integer id;
    private Interests interest;
    private String generalSearch = "";
    private List<DiscussionTopic> discussionTopicList;
    private List<Tags> tagList;
    private String tagSelected = "";
    
    public void init(){
        if(!FacesContext.getCurrentInstance().isPostback()){
            loadInterest();
            discussionTopicList = new ArrayList<DiscussionTopic>();
            tagList = new ArrayList<Tags>();
            loadTags();
            loadDiscussionTopics();
        }
    }
    
    private void loadInterest(){
        InterestsBO iBO = new InterestsBO();
        interest = iBO.findInterestsByID(id);
    }
    
    private void loadTags(){
        TagsBO tBO = new TagsBO();
        tagList = tBO.getAllTags();
    }
    
    private void loadDiscussionTopics(){
        DiscussionTopicBO dtBO = new DiscussionTopicBO();
        discussionTopicList = dtBO.findDiscussionTopicsByTheme(id);
    }
    
    public void generalSearchEvent() {
        DiscussionTopicBO dtBO = new DiscussionTopicBO();
        discussionTopicList = new ArrayList<DiscussionTopic>();
        discussionTopicList = dtBO.loadDiscussionTopicsByExpression(generalSearch, tagSelected, id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Interests getInterest() {
        return interest;
    }

    public void setInterest(Interests interest) {
        this.interest = interest;
    }

    public String getGeneralSearch() {
        return generalSearch;
    }

    public void setGeneralSearch(String generalSearch) {
        this.generalSearch = generalSearch;
    }

    public List<DiscussionTopic> getDiscussionTopicList() {
        return discussionTopicList;
    }

    public void setDiscussionTopicList(List<DiscussionTopic> discussionTopicList) {
        this.discussionTopicList = discussionTopicList;
    }

    public List<Tags> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tags> tagList) {
        this.tagList = tagList;
    }

    public String getTagSelected() {
        return tagSelected;
    }

    public void setTagSelected(String tagSelected) {
        this.tagSelected = tagSelected;
    }
    
}
