/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.EmailActivationDAO;
import com.guigoh.primata.entity.EmailActivation;
import java.io.Serializable;

/**
 *
 * @author Joe
 */
public class EmailActivationBO implements Serializable {
    
    private EmailActivationDAO emailActivationDAO;
    
    public EmailActivationBO(){
        emailActivationDAO = new EmailActivationDAO();
    }

    public void create(EmailActivation emailactivation) {
        try {
            emailActivationDAO.create(emailactivation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy(EmailActivation emailactivation) {
        try {
            emailActivationDAO.destroy(emailactivation.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EmailActivation findEmailActivationByUsername(String username) {
        try {
            return emailActivationDAO.findEmailActivationByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
