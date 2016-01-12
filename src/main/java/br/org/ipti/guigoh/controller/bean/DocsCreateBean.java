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
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.util.CookieService;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author iptipc008
 */
@Named
@ViewScoped
public class DocsCreateBean implements Serializable {

    private String text, title, userSearch;
    private Integer docId;

    private Date date;

    private SocialProfile ownerSocialProfile, mySocialProfile;

    private List<DocGuest> guestList;
    private List<SocialProfile> socialProfileList, chosenSocialProfileList;

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

    public void addGuest() throws Exception {
        if (docId == null) {
            save();
        }
        for (SocialProfile socialProfile : chosenSocialProfileList) {
            DocGuest docGuest = new DocGuest();
            docGuest.setDocFk(docJpaController.findDoc(docId));
            docGuest.setSocialProfileFk(socialProfile);
            docGuest.setPermission("RW");
            docGuestJpaController.create(docGuest);
            guestList.add(docGuest);
        }
        chosenSocialProfileList = new ArrayList<>();
        socialProfileList = new ArrayList<>();
        userSearch = "";

    }

    public void selectUser(SocialProfile socialProfile) {
        socialProfileList.remove(socialProfile);
        chosenSocialProfileList.add(socialProfile);
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
                docHistory.setTitle(doc.getTitle());
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
            docJpaController.create(doc);
            
            docId = doc.getId();
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
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/home.xhtml");
                }
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/home.xhtml");
            }
        } else {
            ownerSocialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));
            guestList = new ArrayList<>();
        }
        mySocialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));

        chosenSocialProfileList = new ArrayList<>();
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
}
