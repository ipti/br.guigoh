/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.SocialProfileDAO;
import com.guigoh.primata.dao.exceptions.PreexistingEntityException;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import com.guigoh.primata.entity.Educations;
import com.guigoh.primata.entity.Interests;
import com.guigoh.primata.entity.SocialProfile;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author Joe
 */
public class SocialProfileBO implements Serializable {

    private SocialProfileDAO socialProfileDAO;

    public SocialProfileBO() {
        socialProfileDAO = new SocialProfileDAO();
    }

    public void create(SocialProfile socialProfile) throws PreexistingEntityException, RollbackFailureException, Exception {
        try {
            socialProfileDAO.create(socialProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void edit(SocialProfile socialProfile) {
        try {
            socialProfileDAO.edit(socialProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SocialProfile findSocialProfile(String id) {
        try {
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

    public SocialProfile findSocialProfileBySocialProfileId(Integer id) {
        try {
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

    public List<SocialProfile> loadUserSearchList(SocialProfile socialProfile, Educations education, Integer experienceTime, Interests interest) {
        try {
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

    public List<SocialProfile> getAll() {
        try {
            return socialProfileDAO.findSocialProfileEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List getSocialProfileAuthorization(Integer subnetworkId) {
        try {
            return socialProfileDAO.loadAllSocialProfileAuthorization(subnetworkId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
