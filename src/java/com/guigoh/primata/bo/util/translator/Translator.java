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

    private ResourceBundle translation;
    private Locale locale;
    
    public Translator (){
        
    }

    public void setLocalePTBR() {
        locale = new Locale("pt","BR");
        translation = ResourceBundle.getBundle("com.guigoh.primata.bo.util.translator.Labels", locale);
    }
    
    public void setLocaleENUS() {
        locale = new Locale("en","US");
        translation = ResourceBundle.getBundle("com.guigoh.primata.bo.util.translator.Labels", locale);
    }
    
    public void setLocaleFRFR() {
        locale = new Locale("fr","FR");
        translation = ResourceBundle.getBundle("com.guigoh.primata.bo.util.translator.Labels", locale);
    }

    public String getWord(String keyword) {
        return translation.getString(keyword);
    }
}