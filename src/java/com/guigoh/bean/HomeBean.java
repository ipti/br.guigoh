/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bean;

import com.guigoh.bo.EducationalObjectBO;
import com.guigoh.entity.EducationalObject;
import com.guigoh.bo.DiscussionTopicBO;
import com.guigoh.bo.InterestsBO;
import com.guigoh.bo.util.MD5Generator;
import com.guigoh.entity.Interests;
import com.guigoh.entity.NewActivity;
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

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            try {
                loadInterestThemes();
                loadEducationalObjects();
                loadLastTopicActivities();
                existsMore();
            } catch (Exception e) {
            }
        }
    }

    private void loadInterestThemes() {
        interestThemesList = InterestsBO.findInterestsByInterestsTypeName("Themes");
    }

    private void loadEducationalObjects() {
        educationalObjectList = EducationalObjectBO.getLatestFiveActiveEducationalObjects();
    }

    public void loadMoreEducationalObjects() {
        List<EducationalObject> outList = educationalObjectList;
        List<EducationalObject> moreObjects = EducationalObjectBO.loadMoreEducationalObjects(educationalObjectList.get(educationalObjectList.size() - 1).getDate());
        for (EducationalObject temp : moreObjects){
            outList.add(temp);
        }
        setEducationalObjectList(outList);
        existsMore();
    }
    
    public void existsMore(){
        if (EducationalObjectBO.loadMoreEducationalObjects(educationalObjectList.get(educationalObjectList.size() - 1).getDate()).isEmpty()){
            setExistsMoreObjects(false);
        } else {
            setExistsMoreObjects(true);
        }
    }

    private void loadLastTopicActivities() {
        newActivityList = DiscussionTopicBO.getLastActivities();
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

}
