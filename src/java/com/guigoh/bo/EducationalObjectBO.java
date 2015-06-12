/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import com.ipti.guigoh.model.entity.EducationalObject;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ipti004
 */
public class EducationalObjectBO implements Serializable{

    public static void create(EducationalObject educationalObject) {
        try {
            EducationalObjectJpaController educationalObjectDAO = new EducationalObjectJpaController();
            educationalObjectDAO.create(educationalObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void edit(EducationalObject educationalObject) {
        try {
            EducationalObjectJpaController educationalObjectDAO = new EducationalObjectJpaController();
            educationalObjectDAO.edit(educationalObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static List<EducationalObject> getAll() {
        try {
            EducationalObjectJpaController educationalObjectDAO = new EducationalObjectJpaController();
            return educationalObjectDAO.findEducationalObjectEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static EducationalObject getEducationalObject(Integer id){
        try {
            EducationalObjectJpaController educationalObjectDAO = new EducationalObjectJpaController();
            return educationalObjectDAO.findEducationalObject(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> getLatestFourActiveEducationalObjects() {
        try {
            EducationalObjectJpaController educationalObjectDAO = new EducationalObjectJpaController();
            return educationalObjectDAO.getLatestFourActiveEducationalObjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> getLatestFiveActiveEducationalObjects() {
        try {
            EducationalObjectJpaController educationalObjectDAO = new EducationalObjectJpaController();
            return educationalObjectDAO.getLatestFiveActiveEducationalObjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> loadMoreEducationalObjects(Date date) {
        try {
            EducationalObjectJpaController educationalObjectDAO = new EducationalObjectJpaController();
            return educationalObjectDAO.loadMoreEducationalObjects(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> getAllActiveEducationalObjects() {
        try {
            EducationalObjectJpaController educationalObjectDAO = new EducationalObjectJpaController();
            return educationalObjectDAO.getAllActiveEducationalObjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> getPendingEducationalObjects() {
        try {
            EducationalObjectJpaController educationalObjectDAO = new EducationalObjectJpaController();
            return educationalObjectDAO.getPendingEducationalObjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> getActiveEducationalObjects(){
        try {
            EducationalObjectJpaController educationalObjectDAO = new EducationalObjectJpaController();
            return educationalObjectDAO.getActiveEducationalObjects();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> getInactiveEducationalObjects(){
        try {
            EducationalObjectJpaController educationalObjectDAO = new EducationalObjectJpaController();
            return educationalObjectDAO.getInactiveEducationalObjects();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> getActiveEducationalObjectsByTheme(Integer theme_id) {
        try {
            EducationalObjectJpaController educationalObjectDAO = new EducationalObjectJpaController();
            return educationalObjectDAO.getActiveEducationalObjectsByTheme(theme_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> getEducationalObjectsByExpression(String expression, String tag, Integer id) {
        EducationalObjectJpaController educationalObjectDAO = new EducationalObjectJpaController();
        List<EducationalObject> educationalObjectList = educationalObjectDAO.getEducationalObjectsByExpression(expression, tag, id);
        if (educationalObjectList == null) {
            return new ArrayList<EducationalObject>();
        }
        return educationalObjectList;
    }
    
    public static EducationalObject findEducationalObject(Integer id){
        try{
            EducationalObjectJpaController educationalObjectDAO = new EducationalObjectJpaController();
            return educationalObjectDAO.findEducationalObject(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static Timestamp getServerTime(){
        try{
            EducationalObjectJpaController educationalObjectDAO = new EducationalObjectJpaController();
            return educationalObjectDAO.getServerTime();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
