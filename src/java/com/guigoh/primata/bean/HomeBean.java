/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.mandril.bo.EducationalObjectBO;
import com.guigoh.mandril.entity.EducationalObject;
import com.guigoh.primata.bo.DiscussionTopicBO;
import com.guigoh.primata.bo.InterestsBO;
import com.guigoh.primata.bo.util.MD5Generator;
import com.guigoh.primata.entity.Interests;
import com.guigoh.primata.entity.NewActivity;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author IPTI
 */
@SessionScoped
@ManagedBean(name = "homeBean")
public class HomeBean implements Serializable {

    List<Interests> interestThemesList = new ArrayList<Interests>();
    List<EducationalObject> educationalObjectList = new ArrayList<EducationalObject>();
    List<NewActivity> newActivityList = new ArrayList();

    public void init() {
        try {
            loadInterestThemes();
            loadEducationalObjects();
            loadLastTopicActivities();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadInterestThemes() {
        InterestsBO interestsBO = new InterestsBO();
        interestThemesList = interestsBO.findInterestsByInterestsTypeName("Themes");
    }
    
    private void loadEducationalObjects(){
        EducationalObjectBO educationalObjectBO = new EducationalObjectBO();
        educationalObjectList = educationalObjectBO.getAllActiveEducationalObjects();
        
    }

    private void loadLastTopicActivities() {
        DiscussionTopicBO dtBO = new DiscussionTopicBO();
        newActivityList = dtBO.getLastActivities();
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
    
}
