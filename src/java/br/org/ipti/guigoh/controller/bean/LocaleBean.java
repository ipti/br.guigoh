/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.util.translator.Translator;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.jpa.controller.LanguageJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
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
    private final SocialProfileJpaController socialProfileJpaController;

    public LocaleBean() throws RollbackFailureException, Exception {
        trans = new Translator();
        token = CookieService.getCookie("token");
        languageJpaController = new LanguageJpaController();
        socialProfileJpaController = new SocialProfileJpaController();
        locale = CookieService.getCookie("locale") != null ? CookieService.getCookie("locale") : "ptBR";
        if (token != null) {
            socialProfile = socialProfileJpaController.findSocialProfile(token);
            locale = languageJpaController.findLanguage(socialProfile.getLanguageId().getId()).getAcronym();
        }
        changeLocale("", locale);
    }

    public String getString(String string) {
        trans.setLocale(locale);
        return trans.getWord(string);
    }

    public final String changeLocale(String url, String locale) throws NonexistentEntityException, RollbackFailureException, Exception {
        CookieService.addCookie("locale", locale);
        if (token != null) {
            socialProfile.setLanguageId(languageJpaController.findLanguageByAcronym(locale));
            socialProfileJpaController.edit(socialProfile);
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
