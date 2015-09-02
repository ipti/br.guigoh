/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.DiscussionTopic;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.entity.Tags;
import br.org.ipti.guigoh.model.jpa.controller.DiscussionTopicJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import br.org.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.TagsJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author IPTI
 */
@ViewScoped
@Named
public class ThemeViewBean implements Serializable {

    private Integer themeID;
    private String generalSearch, tagSelected;

    private Interests interest;

    private List<DiscussionTopic> discussionTopicList;
    private List<EducationalObject> educationalObjectList;
    private List<Tags> tagList;

    private DiscussionTopicJpaController discussionTopicJpaController;
    private EducationalObjectJpaController educationalObjectJpaController;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
            getInterests();
            getTags();
            getDiscussionTopics();
            getEducationalObjects();
        }
    }

    private void getInterests() {
        InterestsJpaController interestsJpaController = new InterestsJpaController();
        interest = interestsJpaController.findInterests(themeID);
    }

    private void getTags() {
        TagsJpaController tagsJpaController = new TagsJpaController();
        tagList = tagsJpaController.findTagsEntities();
    }

    private void getDiscussionTopics() {
        discussionTopicList = discussionTopicJpaController.findDiscussionTopicsByTheme(themeID);
    }

    private void getEducationalObjects() {
        educationalObjectList = educationalObjectJpaController.getActiveEducationalObjectsByTheme(themeID);
    }

    public void generalSearchEvent() {
        discussionTopicList = new ArrayList<>();
        educationalObjectList = new ArrayList<>();
        discussionTopicList = discussionTopicJpaController.getDiscussionTopicsByExpression(generalSearch, tagSelected, themeID);
        educationalObjectList = educationalObjectJpaController.getEducationalObjectsByExpression(generalSearch, tagSelected, themeID);
    }

    private void initGlobalVariables() {
        discussionTopicJpaController = new DiscussionTopicJpaController();
        educationalObjectJpaController = new EducationalObjectJpaController();
        
        generalSearch = tagSelected = "";
        
        discussionTopicList = new ArrayList<>();
        tagList = new ArrayList<>();
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
