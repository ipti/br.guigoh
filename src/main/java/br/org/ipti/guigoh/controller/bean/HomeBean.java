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
        educationalObjectList = educationalObjectJpaController.getLatestActiveEducationalObjects(6);
    }
    
    private void getActivities() {
        newActivityList = discussionTopicJpaController.getLastActivities(6);
    }

    public void getMoreEducationalObjects() {
        List<EducationalObject> outList = educationalObjectList;
        List<EducationalObject> moreObjects = educationalObjectJpaController.getMoreEducationalObjects(educationalObjectList.get(educationalObjectList.size() - 1).getDate());
        moreObjects.stream().forEach((temp) -> {
            outList.add(temp);
        });
        setEducationalObjectList(outList);
        checkIfExistsMoreEducationalObjects();
    }

    private void checkIfExistsMoreEducationalObjects() {
        if (educationalObjectJpaController.getMoreEducationalObjects(educationalObjectList.get(educationalObjectList.size() - 1).getDate()).isEmpty()) {
            setExistsMoreEducationalObjects(false);
        } else {
            setExistsMoreEducationalObjects(true);
        }
    }
    
    public void getMoreActivities(){
        List<NewActivity> outList = newActivityList;
        List<NewActivity> moreActivities = discussionTopicJpaController.getMoreActivities(newActivityList.get(newActivityList.size() - 1).getData());
        moreActivities.stream().forEach((temp) -> {
            outList.add(temp);
        });
        setNewActivityList(outList);
        checkIfExistsMoreActivities();
    }
    
    private void checkIfExistsMoreActivities(){
        if (discussionTopicJpaController.getMoreActivities(newActivityList.get(newActivityList.size() - 1).getData()).isEmpty()) {
            setExistsMoreActivities(false);
        } else {
            setExistsMoreActivities(true);
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
