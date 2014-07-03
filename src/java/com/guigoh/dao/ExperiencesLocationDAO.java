/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.dao;

import com.guigoh.dao.exceptions.NonexistentEntityException;
import com.guigoh.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.guigoh.entity.Experiences;
import com.guigoh.entity.ExperiencesLocation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.transaction.UserTransaction;

/**
 *
 * @author Joe
 */
public class ExperiencesLocationDAO implements Serializable {

    private transient EntityManagerFactory emf = JPAUtil.getEMF();
    
    public ExperiencesLocationDAO() {

    }
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ExperiencesLocation experiencesLocation) throws RollbackFailureException, Exception {
        if (experiencesLocation.getExperiencesCollection() == null) {
            experiencesLocation.setExperiencesCollection(new ArrayList<Experiences>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Experiences> attachedExperiencesCollection = new ArrayList<Experiences>();
            for (Experiences experiencesCollectionExperiencesToAttach : experiencesLocation.getExperiencesCollection()) {
                experiencesCollectionExperiencesToAttach = em.getReference(experiencesCollectionExperiencesToAttach.getClass(), experiencesCollectionExperiencesToAttach.getExperiencesPK());
                attachedExperiencesCollection.add(experiencesCollectionExperiencesToAttach);
            }
            experiencesLocation.setExperiencesCollection(attachedExperiencesCollection);
            em.persist(experiencesLocation);
            for (Experiences experiencesCollectionExperiences : experiencesLocation.getExperiencesCollection()) {
                ExperiencesLocation oldLocationIdOfExperiencesCollectionExperiences = experiencesCollectionExperiences.getLocationId();
                experiencesCollectionExperiences.setLocationId(experiencesLocation);
                experiencesCollectionExperiences = em.merge(experiencesCollectionExperiences);
                if (oldLocationIdOfExperiencesCollectionExperiences != null) {
                    oldLocationIdOfExperiencesCollectionExperiences.getExperiencesCollection().remove(experiencesCollectionExperiences);
                    oldLocationIdOfExperiencesCollectionExperiences = em.merge(oldLocationIdOfExperiencesCollectionExperiences);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ExperiencesLocation experiencesLocation) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ExperiencesLocation persistentExperiencesLocation = em.find(ExperiencesLocation.class, experiencesLocation.getId());
            Collection<Experiences> experiencesCollectionOld = persistentExperiencesLocation.getExperiencesCollection();
            Collection<Experiences> experiencesCollectionNew = experiencesLocation.getExperiencesCollection();
            Collection<Experiences> attachedExperiencesCollectionNew = new ArrayList<Experiences>();
            for (Experiences experiencesCollectionNewExperiencesToAttach : experiencesCollectionNew) {
                experiencesCollectionNewExperiencesToAttach = em.getReference(experiencesCollectionNewExperiencesToAttach.getClass(), experiencesCollectionNewExperiencesToAttach.getExperiencesPK());
                attachedExperiencesCollectionNew.add(experiencesCollectionNewExperiencesToAttach);
            }
            experiencesCollectionNew = attachedExperiencesCollectionNew;
            experiencesLocation.setExperiencesCollection(experiencesCollectionNew);
            experiencesLocation = em.merge(experiencesLocation);
            for (Experiences experiencesCollectionOldExperiences : experiencesCollectionOld) {
                if (!experiencesCollectionNew.contains(experiencesCollectionOldExperiences)) {
                    experiencesCollectionOldExperiences.setLocationId(null);
                    experiencesCollectionOldExperiences = em.merge(experiencesCollectionOldExperiences);
                }
            }
            for (Experiences experiencesCollectionNewExperiences : experiencesCollectionNew) {
                if (!experiencesCollectionOld.contains(experiencesCollectionNewExperiences)) {
                    ExperiencesLocation oldLocationIdOfExperiencesCollectionNewExperiences = experiencesCollectionNewExperiences.getLocationId();
                    experiencesCollectionNewExperiences.setLocationId(experiencesLocation);
                    experiencesCollectionNewExperiences = em.merge(experiencesCollectionNewExperiences);
                    if (oldLocationIdOfExperiencesCollectionNewExperiences != null && !oldLocationIdOfExperiencesCollectionNewExperiences.equals(experiencesLocation)) {
                        oldLocationIdOfExperiencesCollectionNewExperiences.getExperiencesCollection().remove(experiencesCollectionNewExperiences);
                        oldLocationIdOfExperiencesCollectionNewExperiences = em.merge(oldLocationIdOfExperiencesCollectionNewExperiences);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = experiencesLocation.getId();
                if (findExperiencesLocation(id) == null) {
                    throw new NonexistentEntityException("The experiencesLocation with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ExperiencesLocation experiencesLocation;
            try {
                experiencesLocation = em.getReference(ExperiencesLocation.class, id);
                experiencesLocation.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The experiencesLocation with id " + id + " no longer exists.", enfe);
            }
            Collection<Experiences> experiencesCollection = experiencesLocation.getExperiencesCollection();
            for (Experiences experiencesCollectionExperiences : experiencesCollection) {
                experiencesCollectionExperiences.setLocationId(null);
                experiencesCollectionExperiences = em.merge(experiencesCollectionExperiences);
            }
            em.remove(experiencesLocation);
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ExperiencesLocation> findExperiencesLocationEntities() {
        return findExperiencesLocationEntities(true, -1, -1);
    }

    public List<ExperiencesLocation> findExperiencesLocationEntities(int maxResults, int firstResult) {
        return findExperiencesLocationEntities(false, maxResults, firstResult);
    }

    private List<ExperiencesLocation> findExperiencesLocationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ExperiencesLocation.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ExperiencesLocation findExperiencesLocation(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ExperiencesLocation.class, id);
        } finally {
            em.close();
        }
    }

    public int getExperiencesLocationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ExperiencesLocation> rt = cq.from(ExperiencesLocation.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public ExperiencesLocation findExperiencesLocationByName(ExperiencesLocation locationId) {
        EntityManager em = getEntityManager();
        try {
            ExperiencesLocation occupationstemp = (ExperiencesLocation) em.createNativeQuery("select * from experiences_location "
                    + "where (UPPER(name) like '" + locationId.getName().toUpperCase() + "') ", ExperiencesLocation.class).getSingleResult();
            if (occupationstemp == null) {
                return new ExperiencesLocation();
            }
            return occupationstemp;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
}
