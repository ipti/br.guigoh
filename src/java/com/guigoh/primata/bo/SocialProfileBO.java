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

    public void create(SocialProfile socialProfile) throws PreexistingEntityException, RollbackFailureException, Exception {
        SocialProfileDAO socialProfileDAO = new SocialProfileDAO();
        socialProfileDAO.create(socialProfile);
    }

    public void edit(SocialProfile socialProfile) {
        try {
            SocialProfileDAO socialProfileDAO = new SocialProfileDAO();
            socialProfileDAO.edit(socialProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SocialProfile findSocialProfile(String id) {
        try {
            SocialProfileDAO socialProfileDAO = new SocialProfileDAO();
            SocialProfile socialProfile = socialProfileDAO.findSocialProfile(id);
            if (socialProfile == null) {
                return new SocialProfile();
            }
            return socialProfile;
        } catch (Exception e) {
            e.printStackTrace();
            return new SocialProfile();
        }

    }

    public SocialProfile findSocialProfileBySocialProfileId(Integer id) {
        try {
            SocialProfileDAO socialProfileDAO = new SocialProfileDAO();
            SocialProfile socialProfile = socialProfileDAO.findSocialProfile(id);
            if (socialProfile == null) {
                return new SocialProfile();
            }
            return socialProfile;
        } catch (Exception e) {
            e.printStackTrace();
            return new SocialProfile();
        }

    }

    public List<SocialProfile> loadUserSearchList(SocialProfile socialProfile, Educations education, Integer experienceTime, Interests interest) {
        SocialProfileDAO socialProfileDAO = new SocialProfileDAO();
        List<SocialProfile> usersList = socialProfileDAO.loadUserSearchList(socialProfile, education, experienceTime, interest);
        if (usersList == null) {
            return new ArrayList<SocialProfile>();
        }
        return usersList;
    }

    public List<SocialProfile> getAll() {
        SocialProfileDAO socialProfileDAO = new SocialProfileDAO();
        return socialProfileDAO.findSocialProfileEntities();
    }

    public List getSocialProfileAuthorization(Integer subnetworkId) {
        SocialProfileDAO socialProfileDAO = new SocialProfileDAO();
        return socialProfileDAO.loadAllSocialProfileAuthorization(subnetworkId);
    }
}
