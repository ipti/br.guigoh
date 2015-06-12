/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipti.guigoh.model.jpa.controller;

import com.ipti.guigoh.model.jpa.util.PersistenceUnit;
import com.ipti.guigoh.model.jpa.exceptions.NonexistentEntityException;
import com.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.ipti.guigoh.model.entity.Occupations;
import com.ipti.guigoh.model.entity.OccupationsType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Joe
 */
public class OccupationsTypeJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();
    
    public OccupationsTypeJpaController() {

    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(OccupationsType occupationsType) throws RollbackFailureException, Exception {
        if (occupationsType.getOccupationsCollection() == null) {
            occupationsType.setOccupationsCollection(new ArrayList<Occupations>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Occupations> attachedOccupationsCollection = new ArrayList<Occupations>();
            for (Occupations occupationsCollectionOccupationsToAttach : occupationsType.getOccupationsCollection()) {
                occupationsCollectionOccupationsToAttach = em.getReference(occupationsCollectionOccupationsToAttach.getClass(), occupationsCollectionOccupationsToAttach.getId());
                attachedOccupationsCollection.add(occupationsCollectionOccupationsToAttach);
            }
            occupationsType.setOccupationsCollection(attachedOccupationsCollection);
            em.persist(occupationsType);
            for (Occupations occupationsCollectionOccupations : occupationsType.getOccupationsCollection()) {
                OccupationsType oldOccupationsTypeIdOfOccupationsCollectionOccupations = occupationsCollectionOccupations.getOccupationsTypeId();
                occupationsCollectionOccupations.setOccupationsTypeId(occupationsType);
                occupationsCollectionOccupations = em.merge(occupationsCollectionOccupations);
                if (oldOccupationsTypeIdOfOccupationsCollectionOccupations != null) {
                    oldOccupationsTypeIdOfOccupationsCollectionOccupations.getOccupationsCollection().remove(occupationsCollectionOccupations);
                    oldOccupationsTypeIdOfOccupationsCollectionOccupations = em.merge(oldOccupationsTypeIdOfOccupationsCollectionOccupations);
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

    public void edit(OccupationsType occupationsType) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OccupationsType persistentOccupationsType = em.find(OccupationsType.class, occupationsType.getId());
            Collection<Occupations> occupationsCollectionOld = persistentOccupationsType.getOccupationsCollection();
            Collection<Occupations> occupationsCollectionNew = occupationsType.getOccupationsCollection();
            Collection<Occupations> attachedOccupationsCollectionNew = new ArrayList<Occupations>();
            for (Occupations occupationsCollectionNewOccupationsToAttach : occupationsCollectionNew) {
                occupationsCollectionNewOccupationsToAttach = em.getReference(occupationsCollectionNewOccupationsToAttach.getClass(), occupationsCollectionNewOccupationsToAttach.getId());
                attachedOccupationsCollectionNew.add(occupationsCollectionNewOccupationsToAttach);
            }
            occupationsCollectionNew = attachedOccupationsCollectionNew;
            occupationsType.setOccupationsCollection(occupationsCollectionNew);
            occupationsType = em.merge(occupationsType);
            for (Occupations occupationsCollectionOldOccupations : occupationsCollectionOld) {
                if (!occupationsCollectionNew.contains(occupationsCollectionOldOccupations)) {
                    occupationsCollectionOldOccupations.setOccupationsTypeId(null);
                    occupationsCollectionOldOccupations = em.merge(occupationsCollectionOldOccupations);
                }
            }
            for (Occupations occupationsCollectionNewOccupations : occupationsCollectionNew) {
                if (!occupationsCollectionOld.contains(occupationsCollectionNewOccupations)) {
                    OccupationsType oldOccupationsTypeIdOfOccupationsCollectionNewOccupations = occupationsCollectionNewOccupations.getOccupationsTypeId();
                    occupationsCollectionNewOccupations.setOccupationsTypeId(occupationsType);
                    occupationsCollectionNewOccupations = em.merge(occupationsCollectionNewOccupations);
                    if (oldOccupationsTypeIdOfOccupationsCollectionNewOccupations != null && !oldOccupationsTypeIdOfOccupationsCollectionNewOccupations.equals(occupationsType)) {
                        oldOccupationsTypeIdOfOccupationsCollectionNewOccupations.getOccupationsCollection().remove(occupationsCollectionNewOccupations);
                        oldOccupationsTypeIdOfOccupationsCollectionNewOccupations = em.merge(oldOccupationsTypeIdOfOccupationsCollectionNewOccupations);
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
                Integer id = occupationsType.getId();
                if (findOccupationsType(id) == null) {
                    throw new NonexistentEntityException("The occupationsType with id " + id + " no longer exists.");
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
            OccupationsType occupationsType;
            try {
                occupationsType = em.getReference(OccupationsType.class, id);
                occupationsType.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The occupationsType with id " + id + " no longer exists.", enfe);
            }
            Collection<Occupations> occupationsCollection = occupationsType.getOccupationsCollection();
            for (Occupations occupationsCollectionOccupations : occupationsCollection) {
                occupationsCollectionOccupations.setOccupationsTypeId(null);
                occupationsCollectionOccupations = em.merge(occupationsCollectionOccupations);
            }
            em.remove(occupationsType);
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

    public List<OccupationsType> findOccupationsTypeEntities() {
        return findOccupationsTypeEntities(true, -1, -1);
    }

    public List<OccupationsType> findOccupationsTypeEntities(int maxResults, int firstResult) {
        return findOccupationsTypeEntities(false, maxResults, firstResult);
    }

    private List<OccupationsType> findOccupationsTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(OccupationsType.class));
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

    public OccupationsType findOccupationsType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OccupationsType.class, id);
        } finally {
            em.close();
        }
    }

    public int getOccupationsTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<OccupationsType> rt = cq.from(OccupationsType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
