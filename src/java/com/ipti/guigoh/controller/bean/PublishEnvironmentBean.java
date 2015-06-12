/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipti.guigoh.controller.bean;

import com.guigoh.bo.EducationalObjectBO;
import com.ipti.guigoh.model.entity.EducationalObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ipti004
 */
@ViewScoped
@ManagedBean(name = "publishEnvironmentBean")
public class PublishEnvironmentBean implements Serializable{
    
    private List<EducationalObject> educationalObjectList;
    
    public void init(){
        if (!FacesContext.getCurrentInstance().isPostback()) {
            educationalObjectList = new ArrayList<>();
            loadEducationalObjectList();
        }
    }
    
    private void loadEducationalObjectList(){
        educationalObjectList = EducationalObjectBO.getLatestFourActiveEducationalObjects();
    }

    public List<EducationalObject> getEducationalObjectList() {
        return educationalObjectList;
    }

    public void setEducationalObjectList(List<EducationalObject> educationalObjectList) {
        this.educationalObjectList = educationalObjectList;
    }
    
    
}
