/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.LanguageDAO;
import com.guigoh.primata.entity.Language;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class LanguageBO implements Serializable {

    private LanguageDAO languageDAO;

    public LanguageBO() {
        languageDAO = new LanguageDAO();
    }

    public List<Language> getAll() {
        try {
            return languageDAO.findLanguageEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Language findById(Integer id) {
        try {
            return languageDAO.findLanguage(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Language findByAcronym(String acronym) {
        try {
            return languageDAO.findLanguageByAcronym(acronym);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
