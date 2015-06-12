/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.LanguageJpaController;
import com.ipti.guigoh.model.entity.Language;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class LanguageBO implements Serializable {

    public static List<Language> getAll() {
        try {
            LanguageJpaController languageDAO = new LanguageJpaController();
            return languageDAO.findLanguageEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Language findById(Integer id) {
        try {
            LanguageJpaController languageDAO = new LanguageJpaController();
            return languageDAO.findLanguage(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Language findByAcronym(String acronym) {
        try {
            LanguageJpaController languageDAO = new LanguageJpaController();
            return languageDAO.findLanguageByAcronym(acronym);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
