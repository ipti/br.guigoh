/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.EducationalObjectDAO;
import com.guigoh.entity.EducationalObject;
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
            EducationalObjectDAO educationalObjectDAO = new EducationalObjectDAO();
            educationalObjectDAO.create(educationalObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void edit(EducationalObject educationalObject) {
        try {
            EducationalObjectDAO educationalObjectDAO = new EducationalObjectDAO();
            educationalObjectDAO.edit(educationalObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static List<EducationalObject> getAll() {
        try {
            EducationalObjectDAO educationalObjectDAO = new EducationalObjectDAO();
            return educationalObjectDAO.findEducationalObjectEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static EducationalObject getEducationalObject(Integer id){
        try {
            EducationalObjectDAO educationalObjectDAO = new EducationalObjectDAO();
            return educationalObjectDAO.findEducationalObject(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> getLatestFourActiveEducationalObjects() {
        try {
            EducationalObjectDAO educationalObjectDAO = new EducationalObjectDAO();
            return educationalObjectDAO.getLatestFourActiveEducationalObjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> getLatestFiveActiveEducationalObjects() {
        try {
            EducationalObjectDAO educationalObjectDAO = new EducationalObjectDAO();
            return educationalObjectDAO.getLatestFiveActiveEducationalObjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> loadMoreEducationalObjects(Date date) {
        try {
            EducationalObjectDAO educationalObjectDAO = new EducationalObjectDAO();
            return educationalObjectDAO.loadMoreEducationalObjects(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> getAllActiveEducationalObjects() {
        try {
            EducationalObjectDAO educationalObjectDAO = new EducationalObjectDAO();
            return educationalObjectDAO.getAllActiveEducationalObjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> getPendingEducationalObjects() {
        try {
            EducationalObjectDAO educationalObjectDAO = new EducationalObjectDAO();
            return educationalObjectDAO.getPendingEducationalObjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> getActiveEducationalObjects(){
        try {
            EducationalObjectDAO educationalObjectDAO = new EducationalObjectDAO();
            return educationalObjectDAO.getActiveEducationalObjects();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> getInactiveEducationalObjects(){
        try {
            EducationalObjectDAO educationalObjectDAO = new EducationalObjectDAO();
            return educationalObjectDAO.getInactiveEducationalObjects();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> getActiveEducationalObjectsByTheme(Integer theme_id) {
        try {
            EducationalObjectDAO educationalObjectDAO = new EducationalObjectDAO();
            return educationalObjectDAO.getActiveEducationalObjectsByTheme(theme_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<EducationalObject> getEducationalObjectsByExpression(String expression, String tag, Integer id) {
        EducationalObjectDAO educationalObjectDAO = new EducationalObjectDAO();
        List<EducationalObject> educationalObjectList = educationalObjectDAO.getEducationalObjectsByExpression(expression, tag, id);
        if (educationalObjectList == null) {
            return new ArrayList<EducationalObject>();
        }
        return educationalObjectList;
    }
    
    public static EducationalObject findEducationalObject(Integer id){
        try{
            EducationalObjectDAO educationalObjectDAO = new EducationalObjectDAO();
            return educationalObjectDAO.findEducationalObject(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static Timestamp getServerTime(){
        try{
            EducationalObjectDAO educationalObjectDAO = new EducationalObjectDAO();
            return educationalObjectDAO.getServerTime();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
