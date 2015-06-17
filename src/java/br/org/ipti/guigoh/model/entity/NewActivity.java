/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ipti004
 */

public class NewActivity implements Serializable{
    
    private int id;
    private SocialProfile socialProfile;
    private String title;
    private Date data;
    private String type;

    public NewActivity(int id, SocialProfile socialProfile, String title, Date data, String type) {
        this.id = id;
        this.socialProfile = socialProfile;
        this.title = title;
        this.data = data;
        this.type = type;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SocialProfile getSocialProfile() {
        return socialProfile;
    }

    public void setSocialProfile(SocialProfile socialProfile) {
        this.socialProfile = socialProfile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
