/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.util.translator;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author ipti
 */
public class Translator implements Serializable {

    private Locale locale;

    public void setLocale(String acronym) {
        if (acronym != null) {
            String acronymLang = acronym.substring(0, 2);
            String acronymCountry = acronym.substring(2, 4);
            locale = new Locale(acronymLang, acronymCountry);
        }
    }

    public String getWord(String keyword) {
        return ResourceBundle.getBundle("i18n.Labels", locale).getString(keyword);
    }
}