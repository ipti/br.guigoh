/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.dao;


import java.io.Serializable;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Joerlan Lima
 */
    public class JPAUtil implements Serializable {
 
    private transient static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("GuigohPU");

    public static EntityManagerFactory getEMF() {
        return emf;
    }
}
