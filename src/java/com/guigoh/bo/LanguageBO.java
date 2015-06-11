/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.LanguageDAO;
import com.guigoh.entity.Language;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class LanguageBO implements Serializable {

    public static List<Language> getAll() {
        try {
            LanguageDAO languageDAO = new LanguageDAO();
            return languageDAO.findLanguageEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Language findById(Integer id) {
        try {
            LanguageDAO languageDAO = new LanguageDAO();
            return languageDAO.findLanguage(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Language findByAcronym(String acronym) {
        try {
            LanguageDAO languageDAO = new LanguageDAO();
            return languageDAO.findLanguageByAcronym(acronym);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
