/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.mandril.bean;

import com.guigoh.mandril.bo.EducationalObjectBO;
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
public class PublishEnvironmentBean {
    
    private List<EducationalObjectBO> educationalObjectList;
    private EducationalObjectBO educationalObjectBO; 
    
    public void init(){
        if (!FacesContext.getCurrentInstance().isPostback()) {
            educationalObjectBO = new EducationalObjectBO();
            educationalObjectList = new ArrayList<EducationalObjectBO>();
        }
    }
}
