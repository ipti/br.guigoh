/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bean;

import com.guigoh.bo.EducationalObjectBO;
import com.guigoh.entity.EducationalObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ipti004
 */
@SessionScoped
@ManagedBean(name = "publishEnvironmentBean")
public class PublishEnvironmentBean implements Serializable{
    
    private List<EducationalObject> educationalObjectList;
    private EducationalObjectBO educationalObjectBO; 
    
    public void init(){
        if (!FacesContext.getCurrentInstance().isPostback()) {
            educationalObjectBO = new EducationalObjectBO();
            educationalObjectList = new ArrayList<EducationalObject>();
            loadEducationalObjectList();
        }
    }
    
    private void loadEducationalObjectList(){
        educationalObjectList = educationalObjectBO.getLatestFourActiveEducationalObjects();
    }

    public List<EducationalObject> getEducationalObjectList() {
        return educationalObjectList;
    }

    public void setEducationalObjectList(List<EducationalObject> educationalObjectList) {
        this.educationalObjectList = educationalObjectList;
    }
    
    
}
