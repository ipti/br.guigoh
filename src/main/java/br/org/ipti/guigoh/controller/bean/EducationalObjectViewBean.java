/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.Author;
import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.EducationalObjectMedia;
import br.org.ipti.guigoh.model.entity.EducationalObjectMessage;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.AuthorJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectMediaJpaController;
import br.org.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UsersJpaController;
import br.org.ipti.guigoh.util.DownloadService;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author ipti008
 */
@ViewScoped
@Named
public class EducationalObjectViewBean implements Serializable {

    private Integer educationalObjectId;
    private String message;

    private EducationalObject educationalObject;

    private List<Interests> interestList;
    
    private EducationalObjectJpaController educationalObjectJpaController;
    private InterestsJpaController interestsJpaController;
    private UsersJpaController usersJpaController;

    public void init() throws IOException {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
        }
    }

    public Integer getIdByEmail(String email) {
        Users user = usersJpaController.findUsers(email);
        if (user != null) {
            return user.getSocialProfile().getSocialProfileId();
        } else {
            return null;
        }
    }

    private void initGlobalVariables() throws IOException {
        educationalObjectJpaController = new EducationalObjectJpaController();
        interestsJpaController = new InterestsJpaController();
        usersJpaController = new UsersJpaController();
        
        educationalObject = new EducationalObject();
        
        if (educationalObjectId == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/home.xhtml");
        } else {
            educationalObject = educationalObjectJpaController.findEducationalObject(educationalObjectId);
            Collections.sort((List<EducationalObjectMessage>) educationalObject.getEducationalObjectMessageCollection(), (c1, c2) -> c2.getDate().compareTo(c1.getDate()));
            interestList = interestsJpaController.findInterestsEntities();
        }
    }

    public EducationalObject getEducationalObject() {
        return educationalObject;
    }

    public void setEducationalObject(EducationalObject educationalObject) {
        this.educationalObject = educationalObject;
    }

    public Integer getEducationalObjectId() {
        return educationalObjectId;
    }

    public void setEducationalObjectId(Integer educationalObjectId) {
        this.educationalObjectId = educationalObjectId;
    }

    public List<Interests> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<Interests> interestList) {
        this.interestList = interestList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
