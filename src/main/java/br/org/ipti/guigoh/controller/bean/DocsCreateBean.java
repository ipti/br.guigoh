/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author iptipc008
 */
@Named
@ViewScoped
public class DocsCreateBean implements Serializable {

    private String text;
    
    public void init(){
        if (!FacesContext.getCurrentInstance().isPostback()) {
            
        }
    }
    
    public void save(){
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
}
