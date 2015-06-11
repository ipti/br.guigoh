/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.EmailActivationDAO;
import com.guigoh.entity.EmailActivation;
import java.io.Serializable;

/**
 *
 * @author Joe
 */
public class EmailActivationBO implements Serializable {

    public static void create(EmailActivation emailactivation) {
        try {
            EmailActivationDAO emailActivationDAO = new EmailActivationDAO();
            emailActivationDAO.create(emailactivation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void destroy(EmailActivation emailactivation) {
        try {
            EmailActivationDAO emailActivationDAO = new EmailActivationDAO();
            emailActivationDAO.destroy(emailactivation.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static EmailActivation findEmailActivationByUsername(String username) {
        try {
            EmailActivationDAO emailActivationDAO = new EmailActivationDAO();
            return emailActivationDAO.findEmailActivationByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
