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
    
    public static void create(EducationalObjectMedia educationalObjectMedia){
        try{
            EducationalObjectMediaDAO educationalObjectMediaDAO = new EducationalObjectMediaDAO();
            educationalObjectMediaDAO.create(educationalObjectMedia);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void edit(EducationalObjectMedia educationalObjectMedia){
        try{
            EducationalObjectMediaDAO educationalObjectMediaDAO = new EducationalObjectMediaDAO();
            educationalObjectMediaDAO.edit(educationalObjectMedia);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static List<EducationalObjectMedia> getMediasByEducationalObject(Integer educationalObjectID){
        try{
            EducationalObjectMediaDAO educationalObjectMediaDAO = new EducationalObjectMediaDAO();
            return educationalObjectMediaDAO.getMediasByEducationalObject(educationalObjectID);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getMediaSize(Integer size){
        double convertedSizeMB = Math.ceil((double) size/Math.pow(1024, 2)*10)/10;
        double convertedSizeKB = Math.ceil(((double) size/1024)*10)/10;
        if (convertedSizeMB > 1){
            return convertedSizeMB + "mb";
        } else {
            return convertedSizeKB + "kb";
        }
    }
}
