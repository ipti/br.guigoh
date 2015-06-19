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
            initGlobalVariables();
            loadEducationalObjectList();
        }
    }
    
    private void loadEducationalObjectList(){
        EducationalObjectJpaController educationalObjectJpaController = new EducationalObjectJpaController();
        educationalObjectList = educationalObjectJpaController.getLatestFourActiveEducationalObjects();
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
