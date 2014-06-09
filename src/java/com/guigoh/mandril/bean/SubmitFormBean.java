/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.mandril.bean;

import com.guigoh.mandril.entity.Author;
import com.guigoh.mandril.entity.EducationalObject;
import com.guigoh.primata.bo.InterestsBO;
import com.guigoh.primata.entity.Interests;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ipti004
 */
@SessionScoped
@ManagedBean(name = "submitFormBean")
public class SubmitFormBean implements Serializable {

    private EducationalObject educationalObject;
    private List<Interests> interestThemesList;
    private List<Author> authorList;
    private Author author;
    private String tags;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            interestThemesList = new ArrayList<Interests>();
            authorList = new ArrayList<Author>();
            author = new Author();
            loadInterestThemes();
        }
    }

    public void addAuthor() {
        if (authorList.size() < 4) {
            if (author.getName().matches("[a-zA-Z ]{3,40}")
                    && author.getEmail().matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")
                    && author.getPhone().matches("\\(\\d{2}\\) \\d{4}-\\d{4}")
                    && author.getSite().matches("(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?")) {
                authorList.add(author);
                author = new Author();
            }
        }
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
}
