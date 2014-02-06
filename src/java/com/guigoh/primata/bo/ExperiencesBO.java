/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.ExperiencesDAO;
import com.guigoh.primata.dao.InterestsDAO;
import com.guigoh.primata.entity.Experiences;
import com.guigoh.primata.entity.ExperiencesPK;
import com.guigoh.primata.entity.Interests;
import com.guigoh.primata.entity.Users;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class ExperiencesBO implements Serializable {   
     public List<Experiences> findExperiencesByTokenId(String token_id) {
        ExperiencesDAO experiencesDAO = new ExperiencesDAO();
        return experiencesDAO.findExperiencesByTokenId(token_id);
    }
     
     public List<Experiences> getAll() {
        ExperiencesDAO experiencesDAO = new ExperiencesDAO();
        return experiencesDAO.findExperiencesEntities();
    }
     
      public void create(Experiences experiences){
        try {
            ExperiencesDAO experiencesDAO = new ExperiencesDAO();
            experiencesDAO.create(experiences);               
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
      
      public void createInsert(Experiences experiences){
        try {
            ExperiencesDAO experiencesDAO = new ExperiencesDAO();
            experiencesDAO.createInsert(experiences);               
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
      
      public void removeExperience(Experiences exp){
          try {
            ExperiencesDAO experiencesDAO = new ExperiencesDAO();
            experiencesDAO.destroy(exp.getExperiencesPK());
        } catch (Exception e) {
            e.printStackTrace();
        }
      }
}
