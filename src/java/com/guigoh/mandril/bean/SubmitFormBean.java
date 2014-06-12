/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.mandril.bean;

import com.guigoh.mandril.entity.Author;
import com.guigoh.mandril.entity.EducationalObject;
import com.guigoh.primata.bo.InterestsBO;
import com.guigoh.primata.entity.Interests;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.Part;

/**
 *
 * @author ipti004
 */
@ViewScoped
@ManagedBean(name = "submitFormBean")
public class SubmitFormBean implements Serializable {

    private EducationalObject educationalObject;
    private List<Interests> interestThemesList;
    private List<Author> authorList;
    private Author author;
    private String tags;
    private Part imageFile;
    private Part mediaFile;
    private List<Part> mediaList;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            interestThemesList = new ArrayList<Interests>();
            authorList = new ArrayList<Author>();
            educationalObject = new EducationalObject();
            author = new Author();
            mediaList = new ArrayList<Part>();
            loadInterestThemes();
        }
    }

    public void addAuthor() {
        if (authorList.size() < 4) {
            if (author.getName().matches("[a-zA-Z ]{3,40}")
                    && (author.getEmail().matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b") || author.getEmail().equals(""))
                    && (author.getPhone().matches("\\(\\d{2}\\) \\d{4}-\\d{4}") || author.getPhone().equals(""))
                    && (author.getSite().matches("(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?") || author.getSite().equals(""))) {
                authorList.add(author);
                author = new Author();
            }
        }
    }

    public void addMedia() throws IOException {
        if (mediaList.size() < 3) {
            if(!mediaFile.getSubmittedFileName().equals("")){
                mediaList.add(mediaFile);
                mediaFile.delete();
            }
        }
    }

    
    public void submit() {
        System.out.println("addSubmit");
        System.out.println(imageFile);
    }

    private void loadInterestThemes() {
        InterestsBO interestsBO = new InterestsBO();
        interestThemesList = interestsBO.findInterestsByInterestsTypeName("Themes");
    }

    public EducationalObject getEducationalObject() {
        return educationalObject;
    }

    public void setEducationalObject(EducationalObject educationalObject) {
        this.educationalObject = educationalObject;
    }

    public List<Interests> getInterestThemesList() {
        return interestThemesList;
    }

    public void setInterestThemesList(List<Interests> interestThemesList) {
        this.interestThemesList = interestThemesList;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Part getImageFile() {
        return imageFile;
    }

    public void setImageFile(Part imageFile) {
        this.imageFile = imageFile;
    }

    public Part getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(Part mediaFile) {
        this.mediaFile = mediaFile;
    }

    public List<Part> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<Part> mediaList) {
        this.mediaList = mediaList;
    }

}
