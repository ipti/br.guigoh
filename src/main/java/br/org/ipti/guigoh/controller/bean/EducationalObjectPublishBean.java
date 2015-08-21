/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author ipti004
 */
@ViewScoped
@Named
public class EducationalObjectPublishBean implements Serializable{
    
    private List<EducationalObject> educationalObjectList;
    
    public void init(){
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
            getEducationalObjects();
        }
    }
    
    private void getEducationalObjects(){
        EducationalObjectJpaController educationalObjectJpaController = new EducationalObjectJpaController();
        educationalObjectList = educationalObjectJpaController.getLatestActiveEducationalObjects(4);
    }
    
    private void initGlobalVariables() {
        educationalObjectList = new ArrayList<>();
    }

    public List<EducationalObject> getEducationalObjectList() {
        return educationalObjectList;
    }

    public void setEducationalObjectList(List<EducationalObject> educationalObjectList) {
        this.educationalObjectList = educationalObjectList;
    }
}