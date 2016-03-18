/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.Doc;
import br.org.ipti.guigoh.model.jpa.controller.DocJpaController;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author IPTIPC100
 */
@Named
@ViewScoped
public class DocsViewBean implements Serializable {

    private Integer docId;

    private Doc doc;

    public void init() throws IOException {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
        }
    }

    private void initGlobalVariables() throws IOException {
        if (docId != null) {
            DocJpaController docJpaController = new DocJpaController();
            doc = docJpaController.findDoc(docId);
            if (doc == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../home.xhtml");
            }
        } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../home.xhtml");
        }
    }

    public Doc getDoc() {
        return doc;
    }

    public void setDoc(Doc doc) {
        this.doc = doc;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }
}
