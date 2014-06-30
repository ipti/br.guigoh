/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.mandril.bo.EducationalObjectBO;
import com.guigoh.mandril.entity.EducationalObject;
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
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author IPTI
 */
@SessionScoped
@ManagedBean(name = "themeBean")
public class ThemeBean implements Serializable{
    
    private Integer themeID;
    private Interests interest;
    private String generalSearch;
    private List<DiscussionTopic> discussionTopicList;
    private List<EducationalObject> educationalObjectList;
    private List<Tags> tagList;
    private String tagSelected;
    private DiscussionTopicBO dtBO;
    private EducationalObjectBO eoBO;
    
    public void init(){
        if(!FacesContext.getCurrentInstance().isPostback()){
            themeID = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            generalSearch = "";
            tagSelected = "";
            dtBO = new DiscussionTopicBO();
            eoBO = new EducationalObjectBO();
            loadInterest();
            discussionTopicList = new ArrayList<DiscussionTopic>();
            tagList = new ArrayList<Tags>();
            loadTags();
            loadDiscussionTopics();
            loadEducationalObjectsByTheme();
        }
    }
    
    private void loadInterest(){
        InterestsBO iBO = new InterestsBO();
        interest = iBO.findInterestsByID(themeID); 
    }
    
    private void loadTags(){
        TagsBO tBO = new TagsBO();
        tagList = tBO.getAllTags();
    }
    
    private void loadDiscussionTopics(){
        discussionTopicList = dtBO.findDiscussionTopicsByTheme(themeID);
    }
    
    private void loadEducationalObjectsByTheme(){
        educationalObjectList = eoBO.getActiveEducationalObjectsByTheme(themeID);
    }
    
    public void generalSearchEvent() {
        discussionTopicList = new ArrayList<DiscussionTopic>();
        educationalObjectList = new ArrayList<EducationalObject>();
        discussionTopicList = dtBO.loadDiscussionTopicsByExpression(generalSearch, tagSelected, themeID);
        educationalObjectList = eoBO.getEducationalObjectsByExpression(generalSearch, tagSelected, themeID);
    }

    public Integer getThemeID() {
        return themeID;
    }

    public void setThemeID(Integer themeID) {
        this.themeID = themeID;
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

    public List<EducationalObject> getEducationalObjectList() {
        return educationalObjectList;
    }

    public void setEducationalObjectList(List<EducationalObject> educationalObjectList) {
        this.educationalObjectList = educationalObjectList;
    }
    
}
