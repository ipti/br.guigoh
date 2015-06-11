/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.SecretQuestionDAO;
import com.guigoh.entity.SecretQuestion;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class SecretQuestionBO implements Serializable {

    public static List<SecretQuestion> getAll() {
        try {
            SecretQuestionDAO secretQuestionDAO = new SecretQuestionDAO();
            return secretQuestionDAO.findSecretQuestionEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
