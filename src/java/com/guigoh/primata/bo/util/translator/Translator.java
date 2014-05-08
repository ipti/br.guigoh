/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo.util.translator;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author ipti
 */
public class Translator implements Serializable{

    private Locale locale;
 
    public void setLocalePTBR() {
        locale = new Locale("pt","BR");
    }
    
    public void setLocaleENUS() {
        locale = new Locale("en","US");
    }
    
    public void setLocaleFRFR() {
        locale = new Locale("fr","FR");
    }
    
    public void setLocale(String acronym){
        String acronymLang = acronym.substring(0,2);
        String acronymCountry = acronym.substring(2,4);
        locale = new Locale(acronymLang,acronymCountry);
    }

    public String getWord(String keyword) {
        return ResourceBundle.getBundle("com.guigoh.primata.bo.util.translator.Labels", locale).getString(keyword);
    }
}