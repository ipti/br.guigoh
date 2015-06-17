/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import com.guigoh.bo.SocialProfileBO;
import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.util.translator.Translator;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.jpa.controller.LanguageJpaController;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author ipti004
 */
@ViewScoped
@ManagedBean(name = "localeBean")
public class LocaleBean implements Serializable {

    private final Translator trans;
    private String locale;
    private final String token;
    private SocialProfile socialProfile;
    private final LanguageJpaController languageJpaController;

    public LocaleBean() {
        trans = new Translator();
        token = CookieService.getCookie("token");
        languageJpaController = new LanguageJpaController();
        locale = CookieService.getCookie("locale") != null ? CookieService.getCookie("locale") : "ptBR";
        if (token != null) {
            socialProfile = SocialProfileBO.findSocialProfile(token);
            locale = languageJpaController.findLanguage(socialProfile.getLanguageId().getId()).getAcronym();
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
            socialProfile.setLanguageId(languageJpaController.findLanguageByAcronym(locale));
            SocialProfileBO.edit(socialProfile);
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
