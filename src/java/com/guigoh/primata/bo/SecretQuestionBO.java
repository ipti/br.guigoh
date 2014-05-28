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

    private SecretQuestionDAO secretQuestionDAO;

    public SecretQuestionBO() {
        secretQuestionDAO = new SecretQuestionDAO();
    }

    public List<SecretQuestion> getAll() {
        try {
            return secretQuestionDAO.findSecretQuestionEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
