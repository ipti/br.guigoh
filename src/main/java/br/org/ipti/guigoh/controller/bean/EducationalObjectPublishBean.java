/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.Author;
import br.org.ipti.guigoh.model.entity.AuthorRole;
import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.EducationalObjectMedia;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.Tags;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.AuthorJpaController;
import br.org.ipti.guigoh.model.jpa.controller.AuthorRoleJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectMediaJpaController;
import br.org.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.TagsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UsersJpaController;
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
public class EducationalObjectPublishBean implements Serializable {

    private EducationalObject educationalObject;

    private List<Interests> interestList;
    private List<AuthorRole> authorRoleList;
    private List<Tags> tagList;
    private List<Author> authorList;

    private Author author;

    private String tag;

    private transient List<Part> fileList;
    
    private InterestsJpaController interestsJpaController;
    private AuthorRoleJpaController authorRoleJpaController;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
        }
    }
    
    public SocialProfile getSocialProfileByEmail(String email) {
        UsersJpaController usersJpaController = new UsersJpaController();
        Users user = usersJpaController.findUsers(email);
        if (user != null) {
            return user.getSocialProfile();
        } else {
            return null;
        }
    }
    
    public void addTag() {
        if (tagList.size() < 7) {
            boolean exists = false;
            for (Tags t : tagList) {
                if (t.getName().equals(tag)) {
                    exists = true;
                }
            }
            if (!exists) {
                Tags tag = new Tags();
                tag.setName(this.tag);
                tagList.add(tag);
                this.tag = "";
            }
        }
    }
    
    public void removeTag(int index) {
        tagList.remove(index);
    }
    
    public void addAuthor() throws UnsupportedEncodingException {
        if (authorList.size() < 10) {
            boolean exists = false;
            for (Author a : authorList) {
                if (a.getName().equals(this.author.getName())
                        && a.getEmail().equals(this.author.getEmail())) {
                    exists = true;
                }
            }
            if (!exists) {
                authorList.add(author);
                author = new Author();
            }
        }
    }
    
    public void removeAuthor(Author author) {
        authorList.remove(author);
    }

//    public void submitForm() throws IOException, Exception {
//        EducationalObjectJpaController educationalObjectJpaController = new EducationalObjectJpaController();
//        UtilJpaController utilJpaController = new UtilJpaController();
//        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
//        SocialProfile socialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));
//        educationalObject.setName(new String(educationalObject.getName().getBytes("ISO-8859-1"), "UTF-8"));
//        educationalObject.setSocialProfileId(socialProfile);
//        educationalObject.setStatus("PE");
//        educationalObject.setDate(utilJpaController.getTimestampServerTime());
//        educationalObjectJpaController.create(educationalObject);
//        String imagePath = File.separator + "home" + File.separator + "www" + File.separator + "com.guigoh.cdn"
//                + File.separator + "guigoh" + File.separator + "educationalobjects" + File.separator + educationalObject.getId()
//                + File.separator + "image" + File.separator;
//        UploadService.uploadFile(imageFile, imagePath, null);
//        educationalObject.setImage("http://cdn.guigoh.com/guigoh/educationalobjects/" + educationalObject.getId() + "/image/" + imageFile.getSubmittedFileName());
//        educationalObjectJpaController.edit(educationalObject);
//        tags = new String(tags.getBytes("ISO-8859-1"), "UTF-8");
//        String[] tagArray = tags.replace(" ", "").split(",");
//        List<EducationalObject> educationalObjectList = new ArrayList<>();
//        educationalObjectList.add(educationalObject);
//        TagsJpaController tagsJpaController = new TagsJpaController();
//        for (String tagValue : tagArray) {
//            Tags tagDB = tagsJpaController.findTagByName(tagValue);
//            if (tagDB == null) {
//                Tags tag = new Tags();
//                tag.setEducationalObjectCollection(educationalObjectList);
//                tag.setName(tagValue);
//                tagsJpaController.create(tag);
//            } else {
//                tagsJpaController.createTagsXEducationalObject(tagDB, educationalObject);
//            }
//        }
//        AuthorJpaController authorJpaController = new AuthorJpaController();
//        for (Author authorOE : authorList) {
//            authorOE.setEducationalObjectCollection(educationalObjectList);
//            authorJpaController.create(authorOE);
//        }
//        if (mediaFile1 != null) {
//            submitFile(mediaFile1);
//        }
//        if (mediaFile2 != null) {
//            submitFile(mediaFile2);
//        }
//        if (mediaFile3 != null) {
//            submitFile(mediaFile3);
//        }
//    }
//
//    private void submitFile(Part part) throws IOException, Exception {
//        String mediaPath = File.separator + "home" + File.separator + "www" + File.separator + "com.guigoh.cdn"
//                + File.separator + "guigoh" + File.separator + "educationalobjects" + File.separator + educationalObject.getId()
//                + File.separator + "media" + File.separator;
//        boolean success = UploadService.uploadFile(part, mediaPath, null);
//        if (success) {
//            EducationalObjectMedia educationalObjectMedia = new EducationalObjectMedia();
//            educationalObjectMedia.setEducationalObjectId(educationalObject);
//            educationalObjectMedia.setSize(part.getSize());
//            String[] fileSplit = part.getSubmittedFileName().split("\\.");
//            educationalObjectMedia.setName(part.getSubmittedFileName().replace("." + fileSplit[fileSplit.length - 1], ""));
//            educationalObjectMedia.setType(fileSplit[fileSplit.length - 1]);
//            educationalObjectMedia.setMedia("http://cdn.guigoh.com/guigoh/educationalobjects/" + educationalObject.getId() + "/media/" + part.getSubmittedFileName());
//            EducationalObjectMediaJpaController educationalObjectMediaJpaController = new EducationalObjectMediaJpaController();
//            educationalObjectMediaJpaController.create(educationalObjectMedia);
//        }
//    }

    private void initGlobalVariables() {
        interestsJpaController = new InterestsJpaController();
        authorRoleJpaController = new AuthorRoleJpaController();
        
        interestList = interestsJpaController.findInterestsEntities();
        authorRoleList = authorRoleJpaController.findAuthorRoleEntities();
        tagList = new ArrayList<>();
        fileList = new ArrayList<>();
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

    public List<Interests> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<Interests> interestList) {
        this.interestList = interestList;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Part> getFileList() {
        return fileList;
    }

    public void setFileList(List<Part> fileList) {
        this.fileList = fileList;
    }

    public List<Tags> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tags> tagList) {
        this.tagList = tagList;
    }

    public List<AuthorRole> getAuthorRoleList() {
        return authorRoleList;
    }

    public void setAuthorRoleList(List<AuthorRole> authorRoleList) {
        this.authorRoleList = authorRoleList;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }
}
