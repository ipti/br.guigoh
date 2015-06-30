/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author paulones
 */
public class UtilJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();
    
    public UtilJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public Timestamp getTimestampServerTime(){
        EntityManager em = getEntityManager();
        try {
            Timestamp serverTime = (Timestamp) em.createNativeQuery("SELECT date_trunc('seconds', now()::timestamp);").getSingleResult();
            return serverTime;
        } finally {
            em.close();
        }
    }
    
    public Double getEpochServerTime() {
        EntityManager em = getEntityManager();
        try {
            Double serverTime = (Double) em.createNativeQuery("select round( date_part( 'epoch', now() ) )").getSingleResult();
            return serverTime;
        } finally {
            em.close();
        }
    }
}
