/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import br.org.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author iptipc008
 */
@ViewScoped
@Named
public class EducationalObjectLibraryBean implements Serializable {
    
    private Integer interestId;
    private Boolean existsMoreEducationalObjects;
    private String search;
    private Boolean hasEducationalObject;

    private Interests interest;

    private List<Interests> interestList;
    private List<EducationalObject> educationalObjectList, mostAcessedEducationalObjectList;

    private EducationalObjectJpaController educationalObjectJpaController;
    private InterestsJpaController interestsJpaController;

    public void init() throws IOException {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
            checkIfExistsMoreEducationalObjects();
        }
    }

    public void getMoreEducationalObjects() {
        List<EducationalObject> outList = educationalObjectList;
        List<EducationalObject> moreEducationalObjects = educationalObjectJpaController.findEducationalObjects(search, educationalObjectList.get(educationalObjectList.size() - 1).getDate(), interestId, 5);
        moreEducationalObjects.stream().forEach((temp) -> {
            outList.add(temp);
        });
        educationalObjectList = outList;
        checkIfExistsMoreEducationalObjects();
    }

    private void checkIfExistsMoreEducationalObjects() {
        if (!educationalObjectList.isEmpty()) {
            if (educationalObjectJpaController.findEducationalObjects(search, educationalObjectList.get(educationalObjectList.size() - 1).getDate(), interestId, null).isEmpty()) {
                existsMoreEducationalObjects = false;
            } else {
                existsMoreEducationalObjects = true;
            }
        } else {
            existsMoreEducationalObjects = false;
        }
    }

    public void searchEducationalObjectEvent() {
        if (search.length() >= 3) {
            educationalObjectList = educationalObjectJpaController.findEducationalObjects(search, null, interestId, 6);
        } else {
            educationalObjectList = educationalObjectJpaController.findEducationalObjects(null, null, interestId, 6);
        }
        checkIfExistsMoreEducationalObjects();
    }

    private void initGlobalVariables() throws IOException {
        educationalObjectJpaController = new EducationalObjectJpaController();
        interestsJpaController = new InterestsJpaController();

        if (interestId == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/home.xhtml");
        } else {
            interest = interestsJpaController.findInterests(interestId);
            educationalObjectList = educationalObjectJpaController.findEducationalObjects(search, null, interestId, 6);
            mostAcessedEducationalObjectList = educationalObjectJpaController.findMostAcessedEducationalObjects(interestId);
            interestList = interestsJpaController.findInterestsEntities();
            hasEducationalObject = !educationalObjectList.isEmpty();
        }
    }

    public Interests getInterest() {
        return interest;
    }

    public void setInterest(Interests interest) {
        this.interest = interest;
    }

    public List<EducationalObject> getEducationalObjectList() {
        return educationalObjectList;
    }

    public void setEducationalObjectList(List<EducationalObject> educationalObjectList) {
        this.educationalObjectList = educationalObjectList;
    }

    public List<Interests> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<Interests> interestList) {
        this.interestList = interestList;
    }

    public Integer getInterestId() {
        return interestId;
    }

    public void setInterestId(Integer interestId) {
        this.interestId = interestId;
    }

    public List<EducationalObject> getMostAcessedEducationalObjectList() {
        return mostAcessedEducationalObjectList;
    }

    public void setMostAcessedEducationalObjectList(List<EducationalObject> mostAcessedEducationalObjectList) {
        this.mostAcessedEducationalObjectList = mostAcessedEducationalObjectList;
    }

    public Boolean getExistsMoreEducationalObjects() {
        return existsMoreEducationalObjects;
    }

    public void setExistsMoreEducationalObjects(Boolean existsMoreEducationalObjects) {
        this.existsMoreEducationalObjects = existsMoreEducationalObjects;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Boolean getHasEducationalObject() {
        return hasEducationalObject;
    }

    public void setHasEducationalObject(Boolean hasEducationalObject) {
        this.hasEducationalObject = hasEducationalObject;
    }
}
