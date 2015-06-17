/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import br.org.ipti.guigoh.model.jpa.controller.EmailActivationJpaController;
import br.org.ipti.guigoh.model.entity.EmailActivation;
import java.io.Serializable;

/**
 *
 * @author Joe
 */
public class EmailActivationBO implements Serializable {

    public static void create(EmailActivation emailactivation) {
        try {
            EmailActivationJpaController emailActivationDAO = new EmailActivationJpaController();
            emailActivationDAO.create(emailactivation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void destroy(EmailActivation emailactivation) {
        try {
            EmailActivationJpaController emailActivationDAO = new EmailActivationJpaController();
            emailActivationDAO.destroy(emailactivation.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static EmailActivation findEmailActivationByUsername(String username) {
        try {
            EmailActivationJpaController emailActivationDAO = new EmailActivationJpaController();
            return emailActivationDAO.findEmailActivationByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
