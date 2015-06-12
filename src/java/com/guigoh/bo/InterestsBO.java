/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import com.ipti.guigoh.model.entity.Interests;
import com.ipti.guigoh.model.entity.SocialProfile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class InterestsBO implements Serializable {

    public static List<Interests> findInterests(Integer socialprofile_id) {
        InterestsJpaController interestsDAO = new InterestsJpaController();
        List<Interests> interestsList = interestsDAO.findInterestsBySocialProfileId(socialprofile_id);
        if (interestsList == null) {
            return new ArrayList<Interests>();
        }
        return interestsList;
    }

    public static List<Interests> findInterestsByInterestsTypeName(String interestsType) {
        InterestsJpaController interestsDAO = new InterestsJpaController();
        List<Interests> interestsList = interestsDAO.findInterestsByInterestsTypeName(interestsType);
        if (interestsList == null) {
            return new ArrayList<Interests>();
        }
        return interestsList;
    }
    
    public static List<Interests> findInterestsByInterestsTypeId(Integer interestsType) {
        InterestsJpaController interestsDAO = new InterestsJpaController();
        List<Interests> interestsList = interestsDAO.findInterestsByInterestsTypeId(interestsType);
        if (interestsList == null) {
            return new ArrayList<Interests>();
        }
        return interestsList;
    }

    public static Interests findInterestsByInterestsName(String interestsName) {
        InterestsJpaController interestsDAO = new InterestsJpaController();
        Interests interests = interestsDAO.findInterestsByInterestsName(interestsName);
        if (interests == null) {
            return new Interests();
        }
        return interests;
    }

    public static List<Interests> getAll() {
        InterestsJpaController interestsDAO = new InterestsJpaController();
        List<Interests> interestsList = interestsDAO.findInterestsEntities();
        if (interestsList == null) {
            return new ArrayList<Interests>();
        }
        return interestsList;
    }

    public static void destroyInterestsBySocialProfile(SocialProfile socialprofile) {
        try {
            InterestsJpaController interestsDAO = new InterestsJpaController();
            interestsDAO.destroyInterestsSocialProfile(socialprofile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void destroyInterestsBySocialProfileInterestsType(SocialProfile socialprofile, String interestsType) {
        try {
            InterestsJpaController interestsDAO = new InterestsJpaController();
            List<Interests> interestsListT = interestsDAO.findInterestsBySocialProfileId(socialprofile.getSocialProfileId());
            for (Interests interests : interestsListT) {
                if (interests.getTypeId().getName().equalsIgnoreCase(interestsType)) {
                    interestsDAO.destroyInterestsBySocialProfileInterestsType(socialprofile, interests);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createInterestsBySocialProfileByInterest(List<Interests> interestsList, SocialProfile socialprofile) {
        try {
            InterestsJpaController interestsDAO = new InterestsJpaController();
            interestsDAO.createInterestsBySocialProfileByInterest(interestsList, socialprofile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createInterestsBySocialProfileByIds(Integer[] interestsIds, SocialProfile socialprofile) {
        try {
            InterestsJpaController interestsDAO = new InterestsJpaController();
            interestsDAO.createInterestsBySocialProfileByIds(interestsIds, socialprofile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void create(Interests intetests) {
        try {
            InterestsJpaController interestsDAO = new InterestsJpaController();
            interestsDAO.create(intetests);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public static Interests findInterestsByID(Integer interestsID) {
        InterestsJpaController interestsDAO = new InterestsJpaController();
        Interests interests = interestsDAO.findInterestsByID(interestsID);
        if (interests == null) {
            return new Interests();
        }
        return interests;
    }
}
