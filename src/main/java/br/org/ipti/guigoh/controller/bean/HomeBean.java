/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.util.MD5Generator;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.entity.NewActivity;
import br.org.ipti.guigoh.model.jpa.controller.DiscussionTopicJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import br.org.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author IPTI
 */
@ViewScoped
@ManagedBean(name = "homeBean")
public class HomeBean implements Serializable {

    private List<Interests> interestThemesList = new ArrayList<>();
    private List<EducationalObject> educationalObjectList = new ArrayList<>();
    private List<NewActivity> newActivityList = new ArrayList();

    private Boolean existsMoreObjects;

    private EducationalObjectJpaController educationalObjectJpaController = new EducationalObjectJpaController();

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
            loadInterestThemes();
            loadEducationalObjects();
            loadLastTopicActivities();
            existsMore();
        }
    }

    private void loadInterestThemes() {
        InterestsJpaController interestsJpaController = new InterestsJpaController();
        interestThemesList = interestsJpaController.findInterestsByInterestsTypeName("Themes");
    }

    private void loadEducationalObjects() {
        educationalObjectList = educationalObjectJpaController.getLatestFiveActiveEducationalObjects();
    }

    public void loadMoreEducationalObjects() {
        List<EducationalObject> outList = educationalObjectList;
        List<EducationalObject> moreObjects = educationalObjectJpaController.loadMoreEducationalObjects(educationalObjectList.get(educationalObjectList.size() - 1).getDate());
        moreObjects.stream().forEach((temp) -> {
            outList.add(temp);
        });
        setEducationalObjectList(outList);
        existsMore();
    }

    public void existsMore() {
        if (educationalObjectJpaController.loadMoreEducationalObjects(educationalObjectList.get(educationalObjectList.size() - 1).getDate()).isEmpty()) {
            setExistsMoreObjects(false);
        } else {
            setExistsMoreObjects(true);
        }
    }

    private void loadLastTopicActivities() {
        DiscussionTopicJpaController discussionTopicJpaController = new DiscussionTopicJpaController();
        newActivityList = discussionTopicJpaController.getLastActivities();
    }

    public String getMD5(String value) {
        return MD5Generator.generate(value);
    }

    public List<Interests> getInterestThemesList() {
        return interestThemesList;
    }

    public void setInterestThemesList(List<Interests> interestThemesList) {
        this.interestThemesList = interestThemesList;
    }

    public List<NewActivity> getNewActivityList() {
        return newActivityList;
    }

    public void setNewActivityList(List<NewActivity> newActivityList) {
        this.newActivityList = newActivityList;
    }

    public List<EducationalObject> getEducationalObjectList() {
        return educationalObjectList;
    }

    public void setEducationalObjectList(List<EducationalObject> educationalObjectList) {
        this.educationalObjectList = educationalObjectList;
    }

    public Boolean getExistsMoreObjects() {
        return existsMoreObjects;
    }

    public void setExistsMoreObjects(Boolean existsMoreObjects) {
        this.existsMoreObjects = existsMoreObjects;
    }

    private void initGlobalVariables() {
        educationalObjectJpaController = new EducationalObjectJpaController();
    }

}
