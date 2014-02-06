/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.dao;

import com.guigoh.primata.dao.exceptions.NonexistentEntityException;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.guigoh.primata.entity.Interests;
import com.guigoh.primata.entity.InterestsType;
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
public class InterestsTypeDAO implements Serializable {

    private EntityManagerFactory emf = JPAUtil.getEMF();
    
    public InterestsTypeDAO() {

    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(InterestsType interestsType) throws RollbackFailureException, Exception {
        if (interestsType.getInterestsCollection() == null) {
            interestsType.setInterestsCollection(new ArrayList<Interests>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Interests> attachedInterestsCollection = new ArrayList<Interests>();
            for (Interests interestsCollectionInterestsToAttach : interestsType.getInterestsCollection()) {
                interestsCollectionInterestsToAttach = em.getReference(interestsCollectionInterestsToAttach.getClass(), interestsCollectionInterestsToAttach.getId());
                attachedInterestsCollection.add(interestsCollectionInterestsToAttach);
            }
            interestsType.setInterestsCollection(attachedInterestsCollection);
            em.persist(interestsType);
            for (Interests interestsCollectionInterests : interestsType.getInterestsCollection()) {
                InterestsType oldTypeIdOfInterestsCollectionInterests = interestsCollectionInterests.getTypeId();
                interestsCollectionInterests.setTypeId(interestsType);
                interestsCollectionInterests = em.merge(interestsCollectionInterests);
                if (oldTypeIdOfInterestsCollectionInterests != null) {
                    oldTypeIdOfInterestsCollectionInterests.getInterestsCollection().remove(interestsCollectionInterests);
                    oldTypeIdOfInterestsCollectionInterests = em.merge(oldTypeIdOfInterestsCollectionInterests);
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

    public void edit(InterestsType interestsType) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InterestsType persistentInterestsType = em.find(InterestsType.class, interestsType.getId());
            Collection<Interests> interestsCollectionOld = persistentInterestsType.getInterestsCollection();
            Collection<Interests> interestsCollectionNew = interestsType.getInterestsCollection();
            Collection<Interests> attachedInterestsCollectionNew = new ArrayList<Interests>();
            for (Interests interestsCollectionNewInterestsToAttach : interestsCollectionNew) {
                interestsCollectionNewInterestsToAttach = em.getReference(interestsCollectionNewInterestsToAttach.getClass(), interestsCollectionNewInterestsToAttach.getId());
                attachedInterestsCollectionNew.add(interestsCollectionNewInterestsToAttach);
            }
            interestsCollectionNew = attachedInterestsCollectionNew;
            interestsType.setInterestsCollection(interestsCollectionNew);
            interestsType = em.merge(interestsType);
            for (Interests interestsCollectionOldInterests : interestsCollectionOld) {
                if (!interestsCollectionNew.contains(interestsCollectionOldInterests)) {
                    interestsCollectionOldInterests.setTypeId(null);
                    interestsCollectionOldInterests = em.merge(interestsCollectionOldInterests);
                }
            }
            for (Interests interestsCollectionNewInterests : interestsCollectionNew) {
                if (!interestsCollectionOld.contains(interestsCollectionNewInterests)) {
                    InterestsType oldTypeIdOfInterestsCollectionNewInterests = interestsCollectionNewInterests.getTypeId();
                    interestsCollectionNewInterests.setTypeId(interestsType);
                    interestsCollectionNewInterests = em.merge(interestsCollectionNewInterests);
                    if (oldTypeIdOfInterestsCollectionNewInterests != null && !oldTypeIdOfInterestsCollectionNewInterests.equals(interestsType)) {
                        oldTypeIdOfInterestsCollectionNewInterests.getInterestsCollection().remove(interestsCollectionNewInterests);
                        oldTypeIdOfInterestsCollectionNewInterests = em.merge(oldTypeIdOfInterestsCollectionNewInterests);
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
                Integer id = interestsType.getId();
                if (findInterestsType(id) == null) {
                    throw new NonexistentEntityException("The interestsType with id " + id + " no longer exists.");
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
            InterestsType interestsType;
            try {
                interestsType = em.getReference(InterestsType.class, id);
                interestsType.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The interestsType with id " + id + " no longer exists.", enfe);
            }
            Collection<Interests> interestsCollection = interestsType.getInterestsCollection();
            for (Interests interestsCollectionInterests : interestsCollection) {
                interestsCollectionInterests.setTypeId(null);
                interestsCollectionInterests = em.merge(interestsCollectionInterests);
            }
            em.remove(interestsType);
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

    public List<InterestsType> findInterestsTypeEntities() {
        return findInterestsTypeEntities(true, -1, -1);
    }

    public List<InterestsType> findInterestsTypeEntities(int maxResults, int firstResult) {
        return findInterestsTypeEntities(false, maxResults, firstResult);
    }

    private List<InterestsType> findInterestsTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InterestsType.class));
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

    public InterestsType findInterestsType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(InterestsType.class, id);
        } finally {
            em.close();
        }
    }

    public int getInterestsTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InterestsType> rt = cq.from(InterestsType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public InterestsType findInterestsTypeByName(String TypeName) {
        EntityManager em = getEntityManager();
        try {
            InterestsType interestsType = (InterestsType) em.createNativeQuery("select * from primata_interests_type "
                    + "where name = '" + TypeName + "'", InterestsType.class).getSingleResult();
            if (interestsType == null){
                return new InterestsType();
            }
            return interestsType;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
