/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.mandril.bean;

import com.guigoh.mandril.entity.EducationalObject;
import com.guigoh.primata.bo.InterestsBO;
import com.guigoh.primata.entity.Interests;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author ipti004
 */
@SessionScoped
@ManagedBean(name = "submitFormBean")
public class SubmitFormBean implements Serializable{
    
    private EducationalObject educationalObject;
    private List<Interests> interestThemesList;
    private String tags;
    
    public void init(){
        interestThemesList = new ArrayList<Interests>();
        loadInterestThemes();
    }
    
    private void loadInterestThemes() {
        InterestsBO interestsBO = new InterestsBO();
        interestThemesList = interestsBO.findInterestsByInterestsTypeName("Themes");
    }

    public EducationalObject getEducationalObject() {
        return educationalObject;
    }

    public void setEducationalObject(EducationalObject educationalObject) {
        this.educationalObject = educationalObject;
    }

    public List<Interests> getInterestThemesList() {
        return interestThemesList;
    }

    public void setInterestThemesList(List<Interests> interestThemesList) {
        this.interestThemesList = interestThemesList;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
    
}
