/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.mandril.bean;

import com.guigoh.mandril.bo.EducationalObjectBO;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ipti008
 */
@SessionScoped
@ManagedBean(name = "educationalObjectBean")
public class EducationalObjectBean implements Serializable{

    private EducationalObjectBO educationalObjectBO;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            educationalObjectBO = new EducationalObjectBO();
        }
    }

    public EducationalObjectBO getEducationalObjectBO() {
        return educationalObjectBO;
    }

    public void setEducationalObjectBO(EducationalObjectBO educationalObjectBO) {
        this.educationalObjectBO = educationalObjectBO;
    }
}
