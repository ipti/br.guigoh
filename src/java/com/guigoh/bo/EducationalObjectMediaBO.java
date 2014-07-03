/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.EducationalObjectMediaDAO;
import com.guigoh.entity.EducationalObjectMedia;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author ipti008
 */
public class EducationalObjectMediaBO implements Serializable{
    private EducationalObjectMediaDAO educationalObjectMediaDAO;

    public EducationalObjectMediaBO() {
        educationalObjectMediaDAO = new EducationalObjectMediaDAO();
    }
    
    public void create(EducationalObjectMedia educationalObjectMedia){
        try{
            educationalObjectMediaDAO.create(educationalObjectMedia);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void edit(EducationalObjectMedia educationalObjectMedia){
        try{
            educationalObjectMediaDAO.edit(educationalObjectMedia);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public List<EducationalObjectMedia> getMediasByEducationalObject(Integer educationalObjectID){
        try{
            return educationalObjectMediaDAO.getMediasByEducationalObject(educationalObjectID);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public String getMediaSize(Integer size){
        double convertedSizeMB = Math.ceil((double) size/Math.pow(1024, 2)*10)/10;
        double convertedSizeKB = Math.ceil(((double) size/1024)*10)/10;
        if (convertedSizeMB > 1){
            return convertedSizeMB + "mb";
        } else {
            return convertedSizeKB + "kb";
        }
    }
}
