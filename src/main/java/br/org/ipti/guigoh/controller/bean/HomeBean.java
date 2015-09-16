/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.NewActivity;
import br.org.ipti.guigoh.model.jpa.controller.DiscussionTopicJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@ViewScoped
@Named
public class HomeBean implements Serializable {

    private List<EducationalObject> educationalObjectList = new ArrayList<>();
    private List<NewActivity> newActivityList = new ArrayList();

    private Boolean existsMoreEducationalObjects;
    private Boolean existsMoreActivities;

    private EducationalObjectJpaController educationalObjectJpaController;
    private DiscussionTopicJpaController discussionTopicJpaController;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
            getEducationalObjects();
            getActivities();
            checkIfExistsMoreEducationalObjects();
            checkIfExistsMoreActivities();
        }
    }

    private void getEducationalObjects() {
        educationalObjectList = educationalObjectJpaController.findEducationalObjects(null, null, null, 6);
    }

    private void getActivities() {
        newActivityList = discussionTopicJpaController.getLastActivities(6);
    }

    public void getMoreEducationalObjects() {
        List<EducationalObject> outList = educationalObjectList;
        List<EducationalObject> moreObjects = educationalObjectJpaController.findEducationalObjects(null, educationalObjectList.get(educationalObjectList.size() - 1).getDate(), null, 5);
        moreObjects.stream().forEach((temp) -> {
            outList.add(temp);
        });
        educationalObjectList = outList;
        checkIfExistsMoreEducationalObjects();
    }

    private void checkIfExistsMoreEducationalObjects() {
        if (!educationalObjectList.isEmpty()) {
            if (educationalObjectJpaController.findEducationalObjects(null, educationalObjectList.get(educationalObjectList.size() - 1).getDate(), null, null).isEmpty()) {
                existsMoreEducationalObjects = false;
            } else {
                existsMoreEducationalObjects = true;
            }
        } else {
            existsMoreEducationalObjects = false;
        }
    }

    public void getMoreActivities() {
        List<NewActivity> outList = newActivityList;
        List<NewActivity> moreActivities = discussionTopicJpaController.getMoreActivities(newActivityList.get(newActivityList.size() - 1).getData());
        moreActivities.stream().forEach((temp) -> {
            outList.add(temp);
        });
        newActivityList = outList;
        checkIfExistsMoreActivities();
    }

    private void checkIfExistsMoreActivities() {
        if (!newActivityList.isEmpty()) {
            if (discussionTopicJpaController.getMoreActivities(newActivityList.get(newActivityList.size() - 1).getData()).isEmpty()) {
                existsMoreActivities = false;
            } else {
                existsMoreActivities = true;
            }
        } else {
            existsMoreActivities = false;
        }
    }

    private void initGlobalVariables() {
        educationalObjectJpaController = new EducationalObjectJpaController();
        discussionTopicJpaController = new DiscussionTopicJpaController();
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

    public Boolean getExistsMoreActivities() {
        return existsMoreActivities;
    }

    public void setExistsMoreActivities(Boolean existsMoreActivities) {
        this.existsMoreActivities = existsMoreActivities;
    }

}
