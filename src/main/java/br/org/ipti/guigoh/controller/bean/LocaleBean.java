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
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@ViewScoped
@Named
public class LocaleBean implements Serializable {

    private Translator trans;
    private SocialProfile socialProfile;
    
    private String locale;
    private String token;
    
    private LanguageJpaController languageJpaController;
    private SocialProfileJpaController socialProfileJpaController;

    public LocaleBean() throws RollbackFailureException, Exception {
        initGlobalVariables();
    }

    public String getString(String string) {
        trans.setLocale(locale);
        return trans.getWord(string); 
    }

    public String changeLocale(String url, String locale) throws NonexistentEntityException, RollbackFailureException, Exception {
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

    private void initGlobalVariables() {
        languageJpaController = new LanguageJpaController();
        socialProfileJpaController = new SocialProfileJpaController();
        
        trans = new Translator();
        
        token = CookieService.getCookie("token");
        locale = CookieService.getCookie("locale") != null ? CookieService.getCookie("locale") : "ptBR";
        
        if (token != null) {
            socialProfile = socialProfileJpaController.findSocialProfile(token);
            locale = languageJpaController.findLanguage(socialProfile.getLanguageId().getId()).getAcronym();
        }
    }
}
