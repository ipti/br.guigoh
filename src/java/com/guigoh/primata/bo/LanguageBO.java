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
    
    public List<Language> getAll(){
        LanguageDAO languageDAO = new LanguageDAO();
        return languageDAO.findLanguageEntities();
    }
    
    public Language findById(Integer id){
        LanguageDAO languageDAO = new LanguageDAO();
        return languageDAO.findLanguage(id);
    }
    
    public Language findByAcronym(String acronym){
        LanguageDAO languageDAO = new LanguageDAO();
        return languageDAO.findLanguageByAcronym(acronym);
    }
}
