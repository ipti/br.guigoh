/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.util.translator.ConfigReader;
import com.guigoh.primata.bo.util.translator.Translator;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
/**
 *
 * @author ipti004
 */
@RequestScoped
@ManagedBean(name = "localeBean")
public class LocaleBean {

    Translator trans = new Translator();
    ConfigReader cr = new ConfigReader();

    public LocaleBean() {
        
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