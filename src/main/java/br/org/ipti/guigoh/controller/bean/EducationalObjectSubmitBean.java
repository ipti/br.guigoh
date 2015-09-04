/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.Author;
import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.EducationalObjectMedia;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.Tags;
import br.org.ipti.guigoh.model.jpa.controller.AuthorJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectMediaJpaController;
import br.org.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.TagsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UtilJpaController;
import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.util.UploadService;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.Part;

/**
 *
 * @author ipti004
 */
@ViewScoped
@Named
public class EducationalObjectSubmitBean implements Serializable {

    private EducationalObject educationalObject;

    private List<Interests> interestThemesList;
    private List<Author> authorList;

    private Author author;

    private String tags;
    private boolean submitted;

    private transient Part imageFile, mediaFile1, mediaFile2, mediaFile3;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
            getInterestThemes();
        }
    }

    public void addAuthor() throws UnsupportedEncodingException {
        if (authorList.size() < 4) {
            boolean exists = false;
            for (Author a : authorList) {
                if (a.getName().equals(this.author.getName())
                        && a.getEmail().equals(this.author.getEmail())
                        && a.getPhone().equals(this.author.getPhone())
                        && a.getSite().equals(this.author.getSite())) {
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

    public void submitForm() throws IOException, Exception {
        EducationalObjectJpaController educationalObjectJpaController = new EducationalObjectJpaController();
        UtilJpaController utilJpaController = new UtilJpaController();
        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
        SocialProfile socialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));
        educationalObject.setName(new String(educationalObject.getName().getBytes("ISO-8859-1"), "UTF-8"));
        educationalObject.setSocialProfileId(socialProfile);
        educationalObject.setStatus("PE");
        educationalObject.setDate(utilJpaController.getTimestampServerTime());
        educationalObjectJpaController.create(educationalObject);
        String imagePath = File.separator + "home" + File.separator + "www" + File.separator + "com.guigoh.cdn" 
                + File.separator + "guigoh" + File.separator + "educationalobjects" + File.separator + educationalObject.getId() 
                + File.separator + "image" + File.separator;
        UploadService.uploadFile(imageFile, imagePath, null);
        educationalObject.setImage("http://cdn.guigoh.com/guigoh/educationalobjects/" + educationalObject.getId() + "/image/" + imageFile.getSubmittedFileName());
        educationalObjectJpaController.edit(educationalObject);
        tags = new String(tags.getBytes("ISO-8859-1"), "UTF-8");
        String[] tagArray = tags.replace(" ", "").split(",");
        List<EducationalObject> educationalObjectList = new ArrayList<>();
        educationalObjectList.add(educationalObject);
        TagsJpaController tagsJpaController = new TagsJpaController();
        for (String tagValue : tagArray) {
            Tags tagDB = tagsJpaController.findTagByName(tagValue);
            if (tagDB == null) {
                Tags tag = new Tags();
                tag.setEducationalObjectCollection(educationalObjectList);
                tag.setName(tagValue);
                tagsJpaController.create(tag);
            } else {
                tagsJpaController.createTagsXEducationalObject(tagDB, educationalObject);
            }
        }
        AuthorJpaController authorJpaController = new AuthorJpaController();
        for (Author authorOE : authorList) {
            authorOE.setEducationalObjectCollection(educationalObjectList);
            authorJpaController.create(authorOE);
        }
        if (mediaFile1 != null) {
            submitFile(mediaFile1);
        }
        if (mediaFile2 != null) {
            submitFile(mediaFile2);
        }
        if (mediaFile3 != null) {
            submitFile(mediaFile3);
        }
    }

    private void submitFile(Part part) throws IOException, Exception {
        String mediaPath = File.separator + "home" + File.separator + "www" + File.separator + "com.guigoh.cdn" 
                + File.separator + "guigoh" + File.separator + "educationalobjects" + File.separator + educationalObject.getId() 
                + File.separator + "media" + File.separator;
        EducationalObjectMedia educationalObjectMedia = new EducationalObjectMedia();
        educationalObjectMedia.setEducationalObjectId(educationalObject);
        educationalObjectMedia.setSize(part.getSize());
        String[] fileSplit = part.getSubmittedFileName().split("\\.");
        educationalObjectMedia.setName(part.getSubmittedFileName().replace("." + fileSplit[fileSplit.length - 1], ""));
        educationalObjectMedia.setType(fileSplit[fileSplit.length - 1]);
        educationalObjectMedia.setMedia("http://cdn.guigoh.com/guigoh/educationalobjects/" + educationalObject.getId() + "/media/" + part.getSubmittedFileName());
        UploadService.uploadFile(part, mediaPath, null);
        EducationalObjectMediaJpaController educationalObjectMediaJpaController = new EducationalObjectMediaJpaController();
        educationalObjectMediaJpaController.create(educationalObjectMedia);
    }

    private void getInterestThemes() {
        InterestsJpaController interestsJpaController = new InterestsJpaController();
        interestThemesList = interestsJpaController.findInterestsEntities();
    }

    private void initGlobalVariables() {
        submitted = false;

        interestThemesList = new ArrayList<>();
        authorList = new ArrayList<>();

        educationalObject = new EducationalObject();
        author = new Author();
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
