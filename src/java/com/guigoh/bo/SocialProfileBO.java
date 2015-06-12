/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import com.ipti.guigoh.model.jpa.exceptions.PreexistingEntityException;
import com.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import com.ipti.guigoh.model.entity.Educations;
import com.ipti.guigoh.model.entity.Interests;
import com.ipti.guigoh.model.entity.SocialProfile;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author Joe
 */
public class SocialProfileBO implements Serializable {

    public static void create(SocialProfile socialProfile) throws PreexistingEntityException, RollbackFailureException, Exception {
        try {
            SocialProfileJpaController socialProfileDAO = new SocialProfileJpaController();
            socialProfileDAO.create(socialProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void edit(SocialProfile socialProfile) {
        try {
            SocialProfileJpaController socialProfileDAO = new SocialProfileJpaController();
            socialProfileDAO.edit(socialProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SocialProfile findSocialProfile(String id) {
        try {
            SocialProfileJpaController socialProfileDAO = new SocialProfileJpaController();
            SocialProfile socialProfile = socialProfileDAO.findSocialProfile(id);
            if (socialProfile == null) {
                return new SocialProfile();
            } else {
                return socialProfile;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new SocialProfile();
        }
    }

    public static SocialProfile findSocialProfileBySocialProfileId(Integer id) {
        try {
            SocialProfileJpaController socialProfileDAO = new SocialProfileJpaController();
            SocialProfile socialProfile = socialProfileDAO.findSocialProfile(id);
            if (socialProfile == null) {
                return new SocialProfile();
            } else {
                return socialProfile;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new SocialProfile();
        }

    }

    public static List<SocialProfile> loadUserSearchList(SocialProfile socialProfile, Educations education, Integer experienceTime, Interests interest) {
        try {
            SocialProfileJpaController socialProfileDAO = new SocialProfileJpaController();
            List<SocialProfile> usersList = socialProfileDAO.loadUserSearchList(socialProfile, education, experienceTime, interest);
            if (usersList == null) {
                return new ArrayList<SocialProfile>();
            } else {
                return usersList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<SocialProfile> getAll() {
        try {
            SocialProfileJpaController socialProfileDAO = new SocialProfileJpaController();
            return socialProfileDAO.findSocialProfileEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List getSocialProfileAuthorization(Integer subnetworkId) {
        try {
            SocialProfileJpaController socialProfileDAO = new SocialProfileJpaController();
            return socialProfileDAO.loadAllSocialProfileAuthorization(subnetworkId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
