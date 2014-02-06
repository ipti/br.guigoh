/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.SecretQuestionDAO;
import com.guigoh.primata.entity.SecretQuestion;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class SecretQuestionBO implements Serializable {
    
    public List<SecretQuestion> getAll(){
        SecretQuestionDAO secretQuestionDAO = new SecretQuestionDAO();
        return secretQuestionDAO.findSecretQuestionEntities();
    }
    
}
