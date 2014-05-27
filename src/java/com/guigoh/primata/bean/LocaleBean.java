/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.LanguageBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.bo.util.CookieService;
import com.guigoh.primata.bo.util.translator.ConfigReader;
import com.guigoh.primata.bo.util.translator.Translator;
import com.guigoh.primata.entity.SocialProfile;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ipti004
 */
@SessionScoped
@ManagedBean(name = "localeBean")
public class LocaleBean implements Serializable {

    private Translator trans;
    private ConfigReader cr;
    private String acronym = "ptBR";
    private String token = "";
    private SocialProfile socialProfile;
    private LanguageBO languageBO;
    private SocialProfileBO socialProfileBO;

    public LocaleBean(){
        trans = new Translator();
        cr = new ConfigReader();
        languageBO = new LanguageBO();
        socialProfileBO = new SocialProfileBO();
        token = CookieService.getCookie("token");
        if (!token.equals("")) {
            socialProfile = socialProfileBO.findSocialProfile(token);
            acronym = languageBO.findById(socialProfile.getLanguageId().getId()).getAcronym();
        }
        changeLocale("", acronym);
    }

    public String getString(String string) {
        setLocale();
        return trans.getWord(string);
    }
    
    public final String changeLocale(String url, String locale){
        cr.editarTag("locale", locale);
        if (!token.equals("")) {
            socialProfile.setLanguageId(languageBO.findByAcronym(locale));
            socialProfileBO.edit(socialProfile);
        }
        acronym = locale;
        return url + "?faces-redirect=true&includeViewParams=true";
    }
    
    public void setLocale() {
        trans.setLocale(cr.getTag("locale"));
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getAcronym() {
        return acronym;
    }
}
