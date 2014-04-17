/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.LanguageBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.bo.util.translator.ConfigReader;
import com.guigoh.primata.bo.util.translator.Translator;
import com.guigoh.primata.entity.SocialProfile;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
/**
 *
 * @author ipti004
 */
@RequestScoped
@ManagedBean(name = "localeBean")
public final class LocaleBean {

    Translator trans = new Translator();
    ConfigReader cr = new ConfigReader();

    public LocaleBean() {
        SocialProfile socialProfile = new SocialProfile();
        String token = "";
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
        SocialProfileBO spBO = new SocialProfileBO();
        LanguageBO lBO = new LanguageBO();
        String acronym = lBO.findById(spBO.findSocialProfile(token).getLanguageId().getId()).getAcronym();
        if(acronym.equals("ptBR")){
            changeToPTBR();
        }else if (acronym.equals("enUS")){
            changeToENUS();
        }else if(acronym.equals("frFR")){
            changeToFRFR();
        }
            
    }

    public String getString(String string) {
        getLocale();
        return trans.getWord(string);
    }
    
    public String changeToPTBR(){
        cr.editarTag("locale", "ptBR");
        return "";
    }
    
    public String changeToENUS(){
        
        cr.editarTag("locale", "enUS");
        return "";
    }
    
    public String changeToFRFR(){
        cr.editarTag("locale", "frFR");
        return "";
    }
    
    public void getLocale(){
        if (cr.getTag("locale").equals("enUS")) {
            trans.setLocaleENUS();
        } else if (cr.getTag("locale").equals("ptBR")){
            trans.setLocalePTBR();
        } else if (cr.getTag("locale").equals("frFR")){
            trans.setLocaleFRFR();
        }
    }
}