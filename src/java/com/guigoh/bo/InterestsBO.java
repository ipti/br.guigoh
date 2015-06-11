/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.InterestsDAO;
import com.guigoh.entity.Interests;
import com.guigoh.entity.SocialProfile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class InterestsBO implements Serializable {

    public static List<Interests> findInterests(Integer socialprofile_id) {
        InterestsDAO interestsDAO = new InterestsDAO();
        List<Interests> interestsList = interestsDAO.findInterestsBySocialProfileId(socialprofile_id);
        if (interestsList == null) {
            return new ArrayList<Interests>();
        }
        return interestsList;
    }

    public static List<Interests> findInterestsByInterestsTypeName(String interestsType) {
        InterestsDAO interestsDAO = new InterestsDAO();
        List<Interests> interestsList = interestsDAO.findInterestsByInterestsTypeName(interestsType);
        if (interestsList == null) {
            return new ArrayList<Interests>();
        }
        return interestsList;
    }
    
    public static List<Interests> findInterestsByInterestsTypeId(Integer interestsType) {
        InterestsDAO interestsDAO = new InterestsDAO();
        List<Interests> interestsList = interestsDAO.findInterestsByInterestsTypeId(interestsType);
        if (interestsList == null) {
            return new ArrayList<Interests>();
        }
        return interestsList;
    }

    public static Interests findInterestsByInterestsName(String interestsName) {
        InterestsDAO interestsDAO = new InterestsDAO();
        Interests interests = interestsDAO.findInterestsByInterestsName(interestsName);
        if (interests == null) {
            return new Interests();
        }
        return interests;
    }

    public static List<Interests> getAll() {
        InterestsDAO interestsDAO = new InterestsDAO();
        List<Interests> interestsList = interestsDAO.findInterestsEntities();
        if (interestsList == null) {
            return new ArrayList<Interests>();
        }
        return interestsList;
    }

    public static void destroyInterestsBySocialProfile(SocialProfile socialprofile) {
        try {
            InterestsDAO interestsDAO = new InterestsDAO();
            interestsDAO.destroyInterestsSocialProfile(socialprofile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void destroyInterestsBySocialProfileInterestsType(SocialProfile socialprofile, String interestsType) {
        try {
            InterestsDAO interestsDAO = new InterestsDAO();
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
            InterestsDAO interestsDAO = new InterestsDAO();
            interestsDAO.createInterestsBySocialProfileByInterest(interestsList, socialprofile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createInterestsBySocialProfileByIds(Integer[] interestsIds, SocialProfile socialprofile) {
        try {
            InterestsDAO interestsDAO = new InterestsDAO();
            interestsDAO.createInterestsBySocialProfileByIds(interestsIds, socialprofile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void create(Interests intetests) {
        try {
            InterestsDAO interestsDAO = new InterestsDAO();
            interestsDAO.create(intetests);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public static Interests findInterestsByID(Integer interestsID) {
        InterestsDAO interestsDAO = new InterestsDAO();
        Interests interests = interestsDAO.findInterestsByID(interestsID);
        if (interests == null) {
            return new Interests();
        }
        return interests;
    }
}
