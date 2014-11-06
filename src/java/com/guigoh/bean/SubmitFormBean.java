/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bean;

import com.guigoh.bo.AuthorBO;
import com.guigoh.bo.EducationalObjectBO;
import com.guigoh.bo.EducationalObjectMediaBO;
import com.guigoh.entity.Author;
import com.guigoh.entity.EducationalObject;
import com.guigoh.entity.EducationalObjectMedia;
import com.guigoh.bo.InterestsBO;
import com.guigoh.bo.SocialProfileBO;
import com.guigoh.bo.TagsBO;
import com.guigoh.bo.util.CookieService;
import com.guigoh.bo.util.UploadService;
import com.guigoh.entity.Interests;
import com.guigoh.entity.SocialProfile;
import com.guigoh.entity.Tags;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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
    private transient Part imageFile;
    private transient Part mediaFile1;
    private transient Part mediaFile2;
    private transient Part mediaFile3;
    private boolean submitted;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            submitted = false;
            interestThemesList = new ArrayList<>();
            authorList = new ArrayList<>();
            educationalObject = new EducationalObject();
            author = new Author();
            loadInterestThemes();
        }
    }

    public void addAuthor() throws UnsupportedEncodingException {
        if (authorList.size() < 4) {
            boolean exists = false;
            for (Author author : authorList) {
                if (author.getName().equals(this.author.getName())
                        && author.getEmail().equals(this.author.getEmail())
                        && author.getPhone().equals(this.author.getPhone())
                        && author.getSite().equals(this.author.getSite())) {
                    exists = true;
                }
            }
            if (!exists) {
                author.setName(new String(author.getName().getBytes("ISO-8859-1"), "UTF-8"));
                authorList.add(author);
                author = new Author();
            }

        }
    }

    public void submitForm() throws IOException {
        EducationalObjectBO educationalObjectBO = new EducationalObjectBO();
        SocialProfileBO socialProfileBO = new SocialProfileBO();
        TagsBO tagBO = new TagsBO();
        AuthorBO authorBO = new AuthorBO();
        SocialProfile socialProfile = socialProfileBO.findSocialProfile(CookieService.getCookie("token"));
        educationalObject.setName(new String(educationalObject.getName().getBytes("ISO-8859-1"), "UTF-8"));
        educationalObject.setSocialProfileId(socialProfile);
        educationalObject.setStatus("PE");
        educationalObject.setDate(educationalObjectBO.getServerTime());
        educationalObjectBO.create(educationalObject);
        //String imagePath = File.separator + "home" + File.separator + "www" + File.separator + "com.guigoh.cdn" + File.separator + "guigoh" + File.separator + "educationalobjects" + File.separator + educationalObject.getId() + File.separator + "image" + File.separator;
        String imagePath = System.getProperty("user.home") + File.separator + "home" + File.separator + "www" + File.separator + "com.guigoh.cdn" + File.separator + "guigoh" + File.separator + "educationalobjects" + File.separator + educationalObject.getId() + File.separator + "image" + File.separator;
        UploadService.uploadFile(imageFile, imagePath);
        educationalObject.setImage("http://cdn.guigoh.com/guigoh/educationalobjects/" + educationalObject.getId() + "/image/" + imageFile.getSubmittedFileName());
        educationalObjectBO.edit(educationalObject);
        tags = new String(tags.getBytes("ISO-8859-1"), "UTF-8");
        String[] tagArray = tags.replace(" ", "").split(",");
        List<EducationalObject> educationalObjectList = new ArrayList<>();
        educationalObjectList.add(educationalObject);
        for (String tagValue : tagArray) {
            Tags tag = new Tags();
            tag.setEducationalObjectCollection(educationalObjectList);
            tag.setName(tagValue);
            tagBO.create(tag);
        }
        for (Author authorOE : authorList) {
            authorOE.setEducationalObjectCollection(educationalObjectList);
            authorBO.create(authorOE);
        }
        if (mediaFile1 != null){
            submitFile(mediaFile1);
        }
        if (mediaFile2 != null){
            submitFile(mediaFile2);
        }
        if (mediaFile3 != null){
            submitFile(mediaFile3);
        }
    }

    private void submitFile(Part part) throws IOException{
        EducationalObjectMediaBO educationalObjectMediaBO = new EducationalObjectMediaBO();
        //String mediaPath = File.separator + "home" + File.separator + "www" + File.separator + "com.guigoh.cdn" + File.separator + "guigoh" + File.separator + "educationalobjects" + File.separator + educationalObject.getId() + File.separator + "media" + File.separator;
        String mediaPath = System.getProperty("user.home") + File.separator + "home" + File.separator + "www" + File.separator + "com.guigoh.cdn" + File.separator + "guigoh" + File.separator + "educationalobjects" + File.separator + educationalObject.getId() + File.separator + "media" + File.separator;
        EducationalObjectMedia educationalObjectMedia = new EducationalObjectMedia();
        educationalObjectMedia.setEducationalObjectId(educationalObject);
        educationalObjectMedia.setSize(part.getSize());
        String[] fileSplit = part.getSubmittedFileName().split("\\.");
        educationalObjectMedia.setName(part.getSubmittedFileName().replace("." + fileSplit[fileSplit.length - 1], ""));
        educationalObjectMedia.setType(fileSplit[fileSplit.length - 1]);
        educationalObjectMedia.setMedia("http://cdn.guigoh.com/guigoh/educationalobjects/" + educationalObject.getId() + "/media/" + part.getSubmittedFileName());
        UploadService.uploadFile(part, mediaPath);
        educationalObjectMediaBO.create(educationalObjectMedia);
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

    public Part getMediaFile1() {
        return mediaFile1;
    }

    public void setMediaFile1(Part mediaFile1) {
        this.mediaFile1 = mediaFile1;
    }

    public Part getMediaFile2() {
        return mediaFile2;
    }

    public void setMediaFile2(Part mediaFile2) {
        this.mediaFile2 = mediaFile2;
    }

    public Part getMediaFile3() {
        return mediaFile3;
    }

    public void setMediaFile3(Part mediaFile3) {
        this.mediaFile3 = mediaFile3;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

}
