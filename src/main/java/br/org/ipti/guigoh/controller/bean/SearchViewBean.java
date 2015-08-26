/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.jpa.controller.FriendsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author iptipc015
 */
@ViewScoped
@Named
public class SearchViewBean implements Serializable {

    private String generalSearch;

    private List<SocialProfile> socialProfileList;
    private List<EducationalObject> educationalObjectList;

    private SocialProfileJpaController socialProfileJpaController;
    private FriendsJpaController friendsJpaController;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
        }
    }

    public void renderSearchResult() {
        if (generalSearch.length() >= 3) {
            socialProfileList = socialProfileJpaController.findSocialProfilesByName(generalSearch, false);
        } else {
            socialProfileList.clear();
        }
    }
    
    public void isFriend(String friendTokenId){
    }

    private void initGlobalVariables() {
        generalSearch = "";

        socialProfileList = new ArrayList<>();
        educationalObjectList = new ArrayList<>();

        socialProfileJpaController = new SocialProfileJpaController();
        friendsJpaController = new FriendsJpaController();
    }

    public String getGeneralSearch() {
        return generalSearch;
    }

    public void setGeneralSearch(String generalSearch) {
        this.generalSearch = generalSearch;
    }

    public List<SocialProfile> getSocialProfileList() {
        return socialProfileList;
    }

    public void setSocialProfileList(List<SocialProfile> socialProfileList) {
        this.socialProfileList = socialProfileList;
    }

    public List<EducationalObject> getEducationalObjectList() {
        return educationalObjectList;
    }

    public void setEducationalObjectList(List<EducationalObject> educationalObjectList) {
        this.educationalObjectList = educationalObjectList;
    }

}
