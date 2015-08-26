/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.Friends;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import br.org.ipti.guigoh.model.jpa.controller.FriendsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.util.CookieService;
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
    private EducationalObjectJpaController educationalObjectJpaController;
    private FriendsJpaController friendsJpaController;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
        }
    }

    public void renderSearchResult() {
        if (generalSearch.length() >= 3) {
            SocialProfile mySocialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));
            socialProfileList = socialProfileJpaController.findSocialProfilesByName(generalSearch, mySocialProfile.getTokenId(), false, 3);
            educationalObjectList = educationalObjectJpaController.findEducationalObjectsByName(generalSearch, 3);
        } else {
            socialProfileList.clear();
            educationalObjectList.clear();
        }
    }
    
    public Friends isFriend(String friendTokenId){
        return friendsJpaController.isFriend(friendTokenId, CookieService.getCookie("token"));
    }

    private void initGlobalVariables() {
        generalSearch = "";

        socialProfileList = new ArrayList<>();
        educationalObjectList = new ArrayList<>();

        socialProfileJpaController = new SocialProfileJpaController();
        friendsJpaController = new FriendsJpaController();
        educationalObjectJpaController = new EducationalObjectJpaController();
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
