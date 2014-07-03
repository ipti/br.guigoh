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
import java.util.List;

/**
 *
 * @author ipti004
 */
public class EducationalObjectBO implements Serializable{
    
    private EducationalObjectDAO educationalObjectDAO;

    public EducationalObjectBO() {
        educationalObjectDAO = new EducationalObjectDAO();
    }

    public void create(EducationalObject educationalObject) {
        try {
            educationalObjectDAO.create(educationalObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void edit(EducationalObject educationalObject) {
        try {
            educationalObjectDAO.edit(educationalObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<EducationalObject> getAll() {
        try {
            return educationalObjectDAO.findEducationalObjectEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public EducationalObject getEducationalObject(Integer id){
        try {
            return educationalObjectDAO.findEducationalObject(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<EducationalObject> getLatestFourActiveEducationalObjects() {
        try {
            return educationalObjectDAO.getLatestFourActiveEducationalObjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<EducationalObject> getAllActiveEducationalObjects() {
        try {
            return educationalObjectDAO.getAllActiveEducationalObjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<EducationalObject> getPendingEducationalObjects() {
        try {
            return educationalObjectDAO.getPendingEducationalObjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<EducationalObject> getActiveEducationalObjects(){
        try {
            return educationalObjectDAO.getActiveEducationalObjects();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public List<EducationalObject> getInactiveEducationalObjects(){
        try {
            return educationalObjectDAO.getInactiveEducationalObjects();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public List<EducationalObject> getActiveEducationalObjectsByTheme(Integer theme_id) {
        try {
            return educationalObjectDAO.getActiveEducationalObjectsByTheme(theme_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<EducationalObject> getEducationalObjectsByExpression(String expression, String tag, Integer id) {
        List<EducationalObject> educationalObjectList = educationalObjectDAO.getEducationalObjectsByExpression(expression, tag, id);
        if (educationalObjectList == null) {
            return new ArrayList<EducationalObject>();
        }
        return educationalObjectList;
    }
    
    public EducationalObject findEducationalObject(Integer id){
        try{
            return educationalObjectDAO.findEducationalObject(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public Timestamp getServerTime(){
        try{
            return educationalObjectDAO.getServerTime();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
