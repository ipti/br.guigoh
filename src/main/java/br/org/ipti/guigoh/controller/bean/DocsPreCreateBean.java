/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.Doc;
import br.org.ipti.guigoh.model.jpa.controller.DocJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.util.CookieService;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author IPTIPC100
 */
@Named
@ViewScoped
public class DocsPreCreateBean implements Serializable{
    
    private List<Doc> docList;
    
    private DocJpaController docJpaController;
    
    public void init() {
        initGlobalVariables();
    }
    
    public void changeDocStatus(int index) throws NonexistentEntityException, RollbackFailureException, Exception {
        docList.get(index).setPublicAccess((docList.get(index).getPublicAccess() == 'Y') ? 'N' : 'Y');
        docJpaController.edit(docList.get(index));
    }
    
    private void initGlobalVariables() {
        docJpaController = new DocJpaController();
        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
        
        docList = docJpaController.findBySocialProfileId(socialProfileJpaController.findSocialProfile(CookieService.getCookie("token")).getSocialProfileId(), false);
        Collections.reverse(docList);
    }

    public List<Doc> getDocList() {
        return docList;
    }

    public void setDocList(List<Doc> docList) {
        this.docList = docList;
    }
}
