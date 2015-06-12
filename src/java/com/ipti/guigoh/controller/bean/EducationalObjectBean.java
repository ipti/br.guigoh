/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipti.guigoh.controller.bean;

import com.guigoh.bo.EducationalObjectBO;
import com.guigoh.bo.EducationalObjectMediaBO;
import com.ipti.guigoh.model.entity.Author;
import com.ipti.guigoh.model.entity.EducationalObject;
import com.ipti.guigoh.model.entity.EducationalObjectMedia;
import com.ipti.guigoh.model.jpa.controller.AuthorJpaController;
import com.ipti.guigoh.util.DownloadService;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ipti008
 */
@ViewScoped
@ManagedBean(name = "educationalObjectBean")
public class EducationalObjectBean implements Serializable {

    private int educationalObjectID;
    private EducationalObject educationalObject;
    private List<Author> authorList;
    private List<EducationalObjectMedia> educationalObjectMediaList;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            educationalObject = new EducationalObject();
            authorList = new ArrayList<>();
            educationalObjectMediaList = new ArrayList<>();
            findEducationalObject(educationalObjectID);
            getAuthorsByEducationalObject(educationalObjectID);
            getMediasByEducationalObject(educationalObjectID);
        }
    }

    public String getMediaSize(Integer size){
        return EducationalObjectMediaBO.getMediaSize(size);
    }
    
    public void downloadMedia(String path, String type) throws IOException{
        DownloadService.downloadFileFromURL(path, type);
    }
    
    private void findEducationalObject(Integer id) {
        educationalObject = EducationalObjectBO.findEducationalObject(id);
    }

    private void getAuthorsByEducationalObject(Integer educationalObjectID){
        AuthorJpaController authorJpaController = new AuthorJpaController();
        authorList = authorJpaController.getAuthorsByEducationalObject(educationalObjectID);
    }
    
    private void getMediasByEducationalObject(Integer educationalObjectID){
        educationalObjectMediaList = EducationalObjectMediaBO.getMediasByEducationalObject(educationalObjectID);
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

    public List<EducationalObjectMedia> getEducationalObjectMediaList() {
        return educationalObjectMediaList;
    }

    public void setEducationalObjectMediaList(List<EducationalObjectMedia> educationalObjectMediaList) {
        this.educationalObjectMediaList = educationalObjectMediaList;
    }
}
