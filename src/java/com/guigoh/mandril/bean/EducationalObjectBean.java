/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.mandril.bean;

import com.guigoh.mandril.bo.AuthorBO;
import com.guigoh.mandril.bo.EducationalObjectBO;
import com.guigoh.mandril.bo.EducationalObjectMediaBO;
import com.guigoh.mandril.entity.Author;
import com.guigoh.mandril.entity.EducationalObject;
import com.guigoh.mandril.entity.EducationalObjectMedia;
import com.guigoh.primata.bo.util.DownloadService;
import java.io.IOException;
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
    private EducationalObjectMediaBO educationalObjectMediaBO;
    private List<EducationalObjectMedia> educationalObjectMediaList;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            educationalObjectID = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            educationalObjectBO = new EducationalObjectBO();
            educationalObject = new EducationalObject();
            authorBO = new AuthorBO();
            authorList = new ArrayList<>();
            educationalObjectMediaBO = new EducationalObjectMediaBO();
            educationalObjectMediaList = new ArrayList<>();
            findEducationalObject(educationalObjectID);
            getAuthorsByEducationalObject(educationalObjectID);
            getMediasByEducationalObject(educationalObjectID);
        }
    }

    public String getMediaSize(Integer size){
        return educationalObjectMediaBO.getMediaSize(size);
    }
    
    public void downloadMedia(String path, String type) throws IOException{
        DownloadService.downloadFileFromURL(path, type);
    }
    
    private void findEducationalObject(Integer id) {
        educationalObject = educationalObjectBO.findEducationalObject(id);
    }

    private void getAuthorsByEducationalObject(Integer educationalObjectID){
        authorList = authorBO.getAuthorsByEducationalObject(educationalObjectID);
    }
    
    private void getMediasByEducationalObject(Integer educationalObjectID){
        educationalObjectMediaList = educationalObjectMediaBO.getMediasByEducationalObject(educationalObjectID);
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
