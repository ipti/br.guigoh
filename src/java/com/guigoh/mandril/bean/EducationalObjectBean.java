/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.mandril.bean;

import com.guigoh.mandril.bo.AuthorBO;
import com.guigoh.mandril.bo.EducationalObjectBO;
import com.guigoh.mandril.entity.Author;
import com.guigoh.mandril.entity.EducationalObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ipti008
 */
@SessionScoped
@ManagedBean(name = "educationalObjectBean")
public class EducationalObjectBean implements Serializable {

    private int educationalObjectID;
    private EducationalObject educationalObject;
    private EducationalObjectBO educationalObjectBO;
    private List<Author> authorList;
    private AuthorBO authorBO;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            educationalObjectBO = new EducationalObjectBO();
            educationalObject = new EducationalObject();
            authorBO = new AuthorBO();
            authorList = new ArrayList<Author>();
            findEducationalObject(educationalObjectID);
            getAuthorsByEducationalObject(educationalObjectID);
        }
    }

    private void findEducationalObject(Integer id) {
        educationalObject = educationalObjectBO.findEducationalObject(id);
    }

    private void getAuthorsByEducationalObject(Integer educationalObjectId){
        authorList = authorBO.getAuthorsByEducationalObject(educationalObjectId);
    }
    
    public EducationalObject getEducationalObject() {
        return educationalObject;
    }

    public void setEducationalObject(EducationalObject educationalObject) {
        this.educationalObject = educationalObject;
    }

    public int getEducationalObjectID() {
        return educationalObjectID;
    }

    public void setEducationalObjectID(int educationalObjectID) {
        this.educationalObjectID = educationalObjectID;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }
}
