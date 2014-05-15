/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.LanguageBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.bo.util.translator.ConfigReader;
import com.guigoh.primata.bo.util.translator.Translator;
import com.guigoh.primata.entity.Language;
import com.guigoh.primata.entity.SocialProfile;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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

    private Translator trans = new Translator();
    private ConfigReader cr = new ConfigReader();
    private String acronym = "ptBR";
    private String token = "";
    private SocialProfile socialProfile;
    private LanguageBO languageBO = new LanguageBO();
    private SocialProfileBO socialProfileBO = new SocialProfileBO();

    public LocaleBean() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().trim().equalsIgnoreCase("token")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
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