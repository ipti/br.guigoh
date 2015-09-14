/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.DiscussionTopic;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.jpa.controller.DiscussionTopicJpaController;
import br.org.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author iptipc008
 */
@ViewScoped
@Named
public class StudyGroupViewBean implements Serializable {

    private Integer interestId;
    private Boolean existsMoreDiscussionTopics;
    private String search;
    private Boolean hasTopic;

    private Interests interest;

    private List<Interests> interestList;
    private List<DiscussionTopic> discussionTopicList, mostAcessedDiscussionTopicList;

    private DiscussionTopicJpaController discussionTopicJpaController;
    private InterestsJpaController interestsJpaController;

    public void init() throws IOException {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
            checkIfExistsMoreDiscussionTopics();
        }
    }

    public void getMoreDiscussionTopics() {
        List<DiscussionTopic> outList = discussionTopicList;
        List<DiscussionTopic> moreTopics = discussionTopicJpaController.findDiscussionTopics(search, discussionTopicList.get(discussionTopicList.size() - 1).getData(), interestId, 5);
        moreTopics.stream().forEach((temp) -> {
            outList.add(temp);
        });
        setDiscussionTopicList(outList);
        checkIfExistsMoreDiscussionTopics();
    }

    private void checkIfExistsMoreDiscussionTopics() {
        if (!discussionTopicList.isEmpty()) {
            if (discussionTopicJpaController.findDiscussionTopics(search, discussionTopicList.get(discussionTopicList.size() - 1).getData(), interestId, null).isEmpty()) {
                setExistsMoreDiscussionTopics(false);
            } else {
                setExistsMoreDiscussionTopics(true);
            }
        } else {
            setExistsMoreDiscussionTopics(false);
        }
    }

    public void searchTopicEvent() {
        if (search.length() >= 3) {
            discussionTopicList = discussionTopicJpaController.findDiscussionTopics(search, null, interestId, 6);
        } else {
            discussionTopicList = discussionTopicJpaController.findDiscussionTopics(null, null, interestId, 6);
        }
        checkIfExistsMoreDiscussionTopics();
    }

    private void initGlobalVariables() throws IOException {
        discussionTopicJpaController = new DiscussionTopicJpaController();
        interestsJpaController = new InterestsJpaController();

        if (interestId == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/home.xhtml");
        } else {
            interest = interestsJpaController.findInterests(interestId);
            discussionTopicList = discussionTopicJpaController.findDiscussionTopics(search, null, interestId, 6);
            mostAcessedDiscussionTopicList = discussionTopicJpaController.findMostAcessedTopics(interestId);
            interestList = interestsJpaController.findInterestsEntities();
            hasTopic = !discussionTopicList.isEmpty();
        }
    }

    public Interests getInterest() {
        return interest;
    }

    public void setInterest(Interests interest) {
        this.interest = interest;
    }

    public List<DiscussionTopic> getDiscussionTopicList() {
        return discussionTopicList;
    }

    public void setDiscussionTopicList(List<DiscussionTopic> discussionTopicList) {
        this.discussionTopicList = discussionTopicList;
    }

    public List<Interests> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<Interests> interestList) {
        this.interestList = interestList;
    }

    public Integer getInterestId() {
        return interestId;
    }

    public void setInterestId(Integer interestId) {
        this.interestId = interestId;
    }

    public List<DiscussionTopic> getMostAcessedDiscussionTopicList() {
        return mostAcessedDiscussionTopicList;
    }

    public void setMostAcessedDiscussionTopicList(List<DiscussionTopic> mostAcessedDiscussionTopicList) {
        this.mostAcessedDiscussionTopicList = mostAcessedDiscussionTopicList;
    }

    public Boolean getExistsMoreDiscussionTopics() {
        return existsMoreDiscussionTopics;
    }

    public void setExistsMoreDiscussionTopics(Boolean existsMoreDiscussionTopics) {
        this.existsMoreDiscussionTopics = existsMoreDiscussionTopics;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Boolean getHasTopic() {
        return hasTopic;
    }

    public void setHasTopic(Boolean hasTopic) {
        this.hasTopic = hasTopic;
    }
}
