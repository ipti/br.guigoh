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

@ViewScoped
@ManagedBean(name = "homeBean")
public class HomeBean implements Serializable {

    private List<Interests> interestThemesList = new ArrayList<>();
    private List<EducationalObject> educationalObjectList = new ArrayList<>();
    private List<NewActivity> newActivityList = new ArrayList();

    private Boolean existsMoreEducationalObjects;

    private EducationalObjectJpaController educationalObjectJpaController = new EducationalObjectJpaController();

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
            getInterestThemes();
            getEducationalObjects();
            getLastDiscussionTopicActivities();
            checkIfExistsMoreEducationalObjects();
        }
    }

    private void getInterestThemes() {
        InterestsJpaController interestsJpaController = new InterestsJpaController();
        interestThemesList = interestsJpaController.findInterestsByInterestsTypeName("Themes");
    }

    private void getEducationalObjects() {
        educationalObjectList = educationalObjectJpaController.getLatestFiveActiveEducationalObjects();
    }

    public void getMoreEducationalObjects() {
        List<EducationalObject> outList = educationalObjectList;
        List<EducationalObject> moreObjects = educationalObjectJpaController.loadMoreEducationalObjects(educationalObjectList.get(educationalObjectList.size() - 1).getDate());
        moreObjects.stream().forEach((temp) -> {
            outList.add(temp);
        });
        setEducationalObjectList(outList);
        checkIfExistsMoreEducationalObjects();
    }

    public void checkIfExistsMoreEducationalObjects() {
        if (educationalObjectJpaController.loadMoreEducationalObjects(educationalObjectList.get(educationalObjectList.size() - 1).getDate()).isEmpty()) {
            setExistsMoreEducationalObjects(false);
        } else {
            setExistsMoreEducationalObjects(true);
        }
    }

    private void getLastDiscussionTopicActivities() {
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

    public Boolean getExistsMoreEducationalObjects() {
        return existsMoreEducationalObjects;
    }

    public void setExistsMoreEducationalObjects(Boolean existsMoreEducationalObjects) {
        this.existsMoreEducationalObjects = existsMoreEducationalObjects;
    }

    private void initGlobalVariables() {
        educationalObjectJpaController = new EducationalObjectJpaController();
    }

}
