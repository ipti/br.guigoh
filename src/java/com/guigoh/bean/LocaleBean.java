/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bean;

import com.guigoh.bo.LanguageBO;
import com.guigoh.bo.SocialProfileBO;
import com.guigoh.bo.util.CookieService;
import com.guigoh.bo.util.translator.Translator;
import com.guigoh.entity.SocialProfile;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ipti004
 */
@ViewScoped
@ManagedBean(name = "localeBean")
public class LocaleBean implements Serializable {

    private Translator trans;
    private String locale;
    private String token;
    private SocialProfile socialProfile;
    private LanguageBO languageBO;
    private SocialProfileBO socialProfileBO;

    public LocaleBean() {
        trans = new Translator();
        languageBO = new LanguageBO();
        socialProfileBO = new SocialProfileBO();
        token = CookieService.getCookie("token");
        locale = CookieService.getCookie("locale") != null ? CookieService.getCookie("locale") : "ptBR";
        if (token != null) {
            socialProfile = socialProfileBO.findSocialProfile(token);
            locale = languageBO.findById(socialProfile.getLanguageId().getId()).getAcronym();
        }
        changeLocale("", locale);
    }

    public String getString(String string) {
        trans.setLocale(locale);
        return trans.getWord(string);
    }

    public final String changeLocale(String url, String locale) {
        CookieService.addCookie("locale", locale);
        if (token != null) {
            socialProfile.setLanguageId(languageBO.findByAcronym(locale));
            socialProfileBO.edit(socialProfile);
        }
        this.locale = locale;
        return url + "?faces-redirect=true&includeViewParams=true";
    }

    public void setAcronym(String acronym) {
        this.locale = acronym;
    }

    public String getAcronym() {
        return locale;
    }
}
