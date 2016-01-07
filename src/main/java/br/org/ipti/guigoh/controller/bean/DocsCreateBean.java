/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.Doc;
import br.org.ipti.guigoh.model.entity.DocHistory;
import br.org.ipti.guigoh.model.jpa.controller.DocHistoryJpaController;
import br.org.ipti.guigoh.model.jpa.controller.DocJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UtilJpaController;
import br.org.ipti.guigoh.util.CookieService;
import java.io.Serializable;
import java.util.Date;
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

    private String text;
    private String title;
    private Integer docId;

    private Date date;

    private DocJpaController docJpaController;
    private DocHistoryJpaController docHistoryJpaController;
    private SocialProfileJpaController socialProfileJpaController;
    private UtilJpaController utilJpaController;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
            date = utilJpaController.getTimestampServerTime();
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

    private void initGlobalVariables() {
        docJpaController = new DocJpaController();
        docHistoryJpaController = new DocHistoryJpaController();
        socialProfileJpaController = new SocialProfileJpaController();
        utilJpaController = new UtilJpaController();
        
        if (docId != null) {
            Doc doc = docJpaController.findDoc(docId);
            title = doc.getTitle();
            text = doc.getDoc();
        }
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
}
