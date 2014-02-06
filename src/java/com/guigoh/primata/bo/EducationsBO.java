/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.EducationsDAO;
import com.guigoh.primata.entity.Educations;
import com.guigoh.primata.entity.EducationsName;
import com.guigoh.primata.entity.Users;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class EducationsBO implements Serializable {
    public List<Educations> findEducationsByTokenId(String token_id){
        EducationsDAO educationsDAO = new EducationsDAO();
        return educationsDAO.findEducationsByTokenId(token_id);
    }
    
    public List<Educations> getAll() {
        EducationsDAO educationsDAO = new EducationsDAO();
        return educationsDAO.findEducationsEntities();
    }
    
    public void create(Educations educations){
        try {
            EducationsDAO educationsDAO = new EducationsDAO();
            educationsDAO.create(educations);               
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void createInsert(Educations educations){
        try {
            EducationsDAO educationsDAO = new EducationsDAO();
            educationsDAO.createInsert(educations);               
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Educations findEducationsByName(Educations educations) {
        EducationsDAO educationsDAO = new EducationsDAO();
        return educationsDAO.findEducationsByName(educations);
    }
    
    public void removeEducation(Educations edu){
          try {
            EducationsDAO educationsDAO = new EducationsDAO();
            educationsDAO.destroy(edu.getEducationsPK());               
        } catch (Exception e) {
            e.printStackTrace();
        }
      }
}
