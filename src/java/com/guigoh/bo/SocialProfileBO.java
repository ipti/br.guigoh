/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.SocialProfileDAO;
import com.guigoh.dao.exceptions.PreexistingEntityException;
import com.guigoh.dao.exceptions.RollbackFailureException;
import com.guigoh.entity.Educations;
import com.guigoh.entity.Interests;
import com.guigoh.entity.SocialProfile;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author Joe
 */
public class SocialProfileBO implements Serializable {

    public static void create(SocialProfile socialProfile) throws PreexistingEntityException, RollbackFailureException, Exception {
        try {
            SocialProfileDAO socialProfileDAO = new SocialProfileDAO();
            socialProfileDAO.create(socialProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void edit(SocialProfile socialProfile) {
        try {
            SocialProfileDAO socialProfileDAO = new SocialProfileDAO();
            socialProfileDAO.edit(socialProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SocialProfile findSocialProfile(String id) {
        try {
            SocialProfileDAO socialProfileDAO = new SocialProfileDAO();
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
            SocialProfileDAO socialProfileDAO = new SocialProfileDAO();
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
            SocialProfileDAO socialProfileDAO = new SocialProfileDAO();
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
            SocialProfileDAO socialProfileDAO = new SocialProfileDAO();
            return socialProfileDAO.findSocialProfileEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List getSocialProfileAuthorization(Integer subnetworkId) {
        try {
            SocialProfileDAO socialProfileDAO = new SocialProfileDAO();
            return socialProfileDAO.loadAllSocialProfileAuthorization(subnetworkId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
