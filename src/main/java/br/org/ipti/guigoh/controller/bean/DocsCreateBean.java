/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.Doc;
import br.org.ipti.guigoh.model.entity.DocGuest;
import br.org.ipti.guigoh.model.entity.DocHistory;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.jpa.controller.DocGuestJpaController;
import br.org.ipti.guigoh.model.jpa.controller.DocHistoryJpaController;
import br.org.ipti.guigoh.model.jpa.controller.DocJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UtilJpaController;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.util.UploadService;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.Part;

/**
 *
 * @author iptipc008
 */
@Named
@ViewScoped
public class DocsCreateBean implements Serializable {

    private String text, title, userSearch, imageName, imageURL;
    private Boolean hasImage;
    private Integer docId, limit;
    private Character publicAccess;

    private Date date;

    private Part uploadedPhoto;
    private Integer[] cropCoordinates;

    private SocialProfile ownerSocialProfile, mySocialProfile;

    private List<DocGuest> guestList;
    private List<SocialProfile> socialProfileList, chosenSocialProfileList;
    private List<DocHistory> docHistoryList;

    private DocJpaController docJpaController;
    private DocHistoryJpaController docHistoryJpaController;
    private SocialProfileJpaController socialProfileJpaController;
    private UtilJpaController utilJpaController;
    private DocGuestJpaController docGuestJpaController;

    public void init() throws IOException {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
            date = utilJpaController.getTimestampServerTime();
        }
    }

    public void addGuests() throws Exception {
        if (docId == null) {
            save();
        }
        for (SocialProfile socialProfile : chosenSocialProfileList) {
            DocGuest docGuest = new DocGuest();
            docGuest.setDocFk(docJpaController.findDoc(docId));
            docGuest.setSocialProfileFk(socialProfile);
            docGuest.setPermission(socialProfile.getDocPermission());
            docGuestJpaController.create(docGuest);
            guestList.add(docGuest);
        }
        resetModal();
    }

    public void uploadImage() throws IOException, NonexistentEntityException, RollbackFailureException, Exception {
        if (docId == null) {
            save();
        }
        String basePath = File.separator + "home" + File.separator + "www" + File.separator
                + "com.guigoh.cdn" + File.separator + "guigoh" + File.separator + "docs" + File.separator
                + docId + File.separator;
        String linkPath = "http://cdn.guigoh.com/guigoh/docs/" + docId + "/" + uploadedPhoto.getSubmittedFileName();
        UploadService.uploadFile(uploadedPhoto, basePath, cropCoordinates);
        Doc doc = docJpaController.findDoc(docId);
        doc.setImage(linkPath);
        docJpaController.edit(doc);
        hasImage = true;
        imageName = uploadedPhoto.getSubmittedFileName();
        imageURL = linkPath;
    }

    public void removeImage() throws NonexistentEntityException, RollbackFailureException, Exception {
        Doc doc = docJpaController.findDoc(docId);
        doc.setImage(null);
        docJpaController.edit(doc);
        hasImage = false;
        imageName = imageURL = "";
    }

    public void resetModal() {
        chosenSocialProfileList = new ArrayList<>();
        socialProfileList = new ArrayList<>();
        userSearch = "";
    }

    public void changeDocStatus() throws NonexistentEntityException, RollbackFailureException, Exception {
        Doc doc = docJpaController.findDoc(docId);
        publicAccess = (publicAccess == 'Y') ? 'N' : 'Y';
        doc.setPublicAccess(publicAccess);
        docJpaController.edit(doc);
    }

    public void loadDocHistory(Integer limit) {
        if (docId != null) {
            docHistoryList = docHistoryJpaController.findByDocId(docId);
            this.limit = limit;
        }
    }

    public void restoreDocHistory(DocHistory docHistory) throws NonexistentEntityException, RollbackFailureException, Exception {
        String docHistoryText = docHistory.getDoc();
        Doc doc = docJpaController.findDoc(docId);

        docHistory.setDate(doc.getDate());
        docHistory.setDoc(doc.getDoc());
        docHistory.setDocFk(doc);
        docHistory.setEditorSocialProfileFk(doc.getEditorSocialProfileFk());
        docHistoryJpaController.create(docHistory);

        doc.setDoc(docHistoryText);
        doc.setEditorSocialProfileFk(socialProfileJpaController.findSocialProfile(CookieService.getCookie("token")));
        doc.setDate(utilJpaController.getTimestampServerTime());
        docJpaController.edit(doc);

        text = doc.getDoc();
    }

    public void selectUser(SocialProfile socialProfile) {
        socialProfileList.remove(socialProfile);
        socialProfile.setDocPermission("RW");
        chosenSocialProfileList.add(socialProfile);
    }

    public void changePermission(int index, String permission, boolean modal) throws RollbackFailureException, Exception {
        if (modal) {
            chosenSocialProfileList.get(index).setDocPermission(permission);
        } else {
            guestList.get(index).setPermission(permission);
            docGuestJpaController.edit(guestList.get(index));
        }
    }

    public void removeChosenUser(SocialProfile socialProfile) {
        chosenSocialProfileList.remove(socialProfile);
        findUsers();
    }

    public void removeGuest(DocGuest guest) throws RollbackFailureException, Exception {
        guestList.remove(guest);
        docGuestJpaController.destroy(guest.getId());
    }

    public void findUsers() {
        if (!userSearch.equals("")) {
            List<Integer> excludedSocialProfileIdList = new ArrayList<>();
            chosenSocialProfileList.stream().forEach((socialProfile) -> {
                excludedSocialProfileIdList.add(socialProfile.getSocialProfileId());
            });
            guestList.stream().forEach((docGuest) -> {
                excludedSocialProfileIdList.add(docGuest.getSocialProfileFk().getSocialProfileId());
            });
            socialProfileList = socialProfileJpaController.findSocialProfiles(userSearch, mySocialProfile.getTokenId(), false, false, excludedSocialProfileIdList);
        } else {
            socialProfileList = new ArrayList<>();
        }
    }

    public void save() throws Exception {
        Doc doc;
        if (title.equals("")) {
            title = "Documento sem Título";
        }
        if (docId != null) {
            doc = docJpaController.findDoc(docId);
            if (doc != null && (!doc.getDoc().equals(text) || !doc.getTitle().equals(title))) {
                DocHistory docHistory = new DocHistory();
                docHistory.setDate(doc.getDate());
                docHistory.setDoc(doc.getDoc());
                docHistory.setDocFk(doc);
                docHistory.setEditorSocialProfileFk(doc.getEditorSocialProfileFk());
                docHistoryJpaController.create(docHistory);

                doc.setDoc(text);
                doc.setTitle(title);
                doc.setEditorSocialProfileFk(socialProfileJpaController.findSocialProfile(CookieService.getCookie("token")));
                doc.setDate(utilJpaController.getTimestampServerTime());
                docJpaController.edit(doc);
            }
        } else {
            doc = new Doc();
            doc.setCreatorSocialProfileFk(socialProfileJpaController.findSocialProfile(CookieService.getCookie("token")));
            doc.setDate(date);
            doc.setDoc(text);
            doc.setTitle(title);
            doc.setEditorSocialProfileFk(socialProfileJpaController.findSocialProfile(CookieService.getCookie("token")));
            doc.setStatus('A');
            doc.setPublicAccess('N');
            doc.setImage("/resources/common/images/doc.png");
            docJpaController.create(doc);

            docId = doc.getId();
            publicAccess = doc.getPublicAccess();
        }
    }

    private void initGlobalVariables() throws IOException {
        docJpaController = new DocJpaController();
        docGuestJpaController = new DocGuestJpaController();
        docHistoryJpaController = new DocHistoryJpaController();
        socialProfileJpaController = new SocialProfileJpaController();
        utilJpaController = new UtilJpaController();
        if (docId != null) {
            Doc doc = docJpaController.findDoc(docId);
            if (doc != null) {
                if (doc.getDocGuestCollection().contains(docGuestJpaController.findByUserTokenId(docId, CookieService.getCookie("token")))
                        || doc.getCreatorSocialProfileFk().getTokenId().equals(CookieService.getCookie("token"))) {
                    title = doc.getTitle();
                    text = doc.getDoc();
                    ownerSocialProfile = docJpaController.findDoc(docId).getCreatorSocialProfileFk();
                    guestList = docGuestJpaController.findByDocId(docId);
                    docHistoryList = docHistoryJpaController.findByDocId(docId);
                    limit = 10;
                    publicAccess = doc.getPublicAccess();
                    hasImage = doc.getImage() != null;
                    if (doc.getImage() != null) {
                        String[] urlChunks = doc.getImage().split("/");
                        imageName = urlChunks[urlChunks.length - 1];
                        imageURL = doc.getImage();
                    }
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/home.xhtml");
                }
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/home.xhtml");
            }
        } else {
            ownerSocialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));
            guestList = new ArrayList<>();
            hasImage = false;
        }
        mySocialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));

        chosenSocialProfileList = new ArrayList<>();

        cropCoordinates = new Integer[6];
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SocialProfile getOwnerSocialProfile() {
        return ownerSocialProfile;
    }

    public void setOwnerSocialProfile(SocialProfile ownerSocialProfile) {
        this.ownerSocialProfile = ownerSocialProfile;
    }

    public List<DocGuest> getGuestList() {
        return guestList;
    }

    public void setGuestList(List<DocGuest> guestList) {
        this.guestList = guestList;
    }

    public SocialProfile getMySocialProfile() {
        return mySocialProfile;
    }

    public void setMySocialProfile(SocialProfile mySocialProfile) {
        this.mySocialProfile = mySocialProfile;
    }

    public String getUserSearch() {
        return userSearch;
    }

    public void setUserSearch(String userSearch) {
        this.userSearch = userSearch;
    }

    public List<SocialProfile> getSocialProfileList() {
        return socialProfileList;
    }

    public void setSocialProfileList(List<SocialProfile> socialProfileList) {
        this.socialProfileList = socialProfileList;
    }

    public List<SocialProfile> getChosenSocialProfileList() {
        return chosenSocialProfileList;
    }

    public void setChosenSocialProfileList(List<SocialProfile> chosenSocialProfileList) {
        this.chosenSocialProfileList = chosenSocialProfileList;
    }

    public List<DocHistory> getDocHistoryList() {
        return docHistoryList;
    }

    public void setDocHistoryList(List<DocHistory> docHistoryList) {
        this.docHistoryList = docHistoryList;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Character getPublicAccess() {
        return publicAccess;
    }

    public void setPublicAccess(Character publicAccess) {
        this.publicAccess = publicAccess;
    }

    public Part getUploadedPhoto() {
        return uploadedPhoto;
    }

    public void setUploadedPhoto(Part uploadedPhoto) {
        this.uploadedPhoto = uploadedPhoto;
    }

    public Integer[] getCropCoordinates() {
        return cropCoordinates;
    }

    public void setCropCoordinates(Integer[] cropCoordinates) {
        this.cropCoordinates = cropCoordinates;
    }

    public Boolean getHasImage() {
        return hasImage;
    }

    public void setHasImage(Boolean hasImage) {
        this.hasImage = hasImage;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
