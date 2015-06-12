/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipti.guigoh.model.jpa.controller;

import com.ipti.guigoh.model.jpa.util.PersistenceUnit;
import com.ipti.guigoh.model.jpa.exceptions.NonexistentEntityException;
import com.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import com.ipti.guigoh.model.entity.Availability;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.ipti.guigoh.model.entity.SocialProfile;
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
public class AvailabilityJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();
    
    public AvailabilityJpaController() {

    }
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Availability availability) throws RollbackFailureException, Exception {
        if (availability.getSocialProfileCollection() == null) {
            availability.setSocialProfileCollection(new ArrayList<SocialProfile>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<SocialProfile> attachedSocialProfileCollection = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionSocialProfileToAttach : availability.getSocialProfileCollection()) {
                socialProfileCollectionSocialProfileToAttach = em.getReference(socialProfileCollectionSocialProfileToAttach.getClass(), socialProfileCollectionSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollection.add(socialProfileCollectionSocialProfileToAttach);
            }
            availability.setSocialProfileCollection(attachedSocialProfileCollection);
            em.persist(availability);
            for (SocialProfile socialProfileCollectionSocialProfile : availability.getSocialProfileCollection()) {
                Availability oldAvailabilityIdOfSocialProfileCollectionSocialProfile = socialProfileCollectionSocialProfile.getAvailabilityId();
                socialProfileCollectionSocialProfile.setAvailabilityId(availability);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
                if (oldAvailabilityIdOfSocialProfileCollectionSocialProfile != null) {
                    oldAvailabilityIdOfSocialProfileCollectionSocialProfile.getSocialProfileCollection().remove(socialProfileCollectionSocialProfile);
                    oldAvailabilityIdOfSocialProfileCollectionSocialProfile = em.merge(oldAvailabilityIdOfSocialProfileCollectionSocialProfile);
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

    public void edit(Availability availability) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Availability persistentAvailability = em.find(Availability.class, availability.getId());
            Collection<SocialProfile> socialProfileCollectionOld = persistentAvailability.getSocialProfileCollection();
            Collection<SocialProfile> socialProfileCollectionNew = availability.getSocialProfileCollection();
            Collection<SocialProfile> attachedSocialProfileCollectionNew = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionNewSocialProfileToAttach : socialProfileCollectionNew) {
                socialProfileCollectionNewSocialProfileToAttach = em.getReference(socialProfileCollectionNewSocialProfileToAttach.getClass(), socialProfileCollectionNewSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollectionNew.add(socialProfileCollectionNewSocialProfileToAttach);
            }
            socialProfileCollectionNew = attachedSocialProfileCollectionNew;
            availability.setSocialProfileCollection(socialProfileCollectionNew);
            availability = em.merge(availability);
            for (SocialProfile socialProfileCollectionOldSocialProfile : socialProfileCollectionOld) {
                if (!socialProfileCollectionNew.contains(socialProfileCollectionOldSocialProfile)) {
                    socialProfileCollectionOldSocialProfile.setAvailabilityId(null);
                    socialProfileCollectionOldSocialProfile = em.merge(socialProfileCollectionOldSocialProfile);
                }
            }
            for (SocialProfile socialProfileCollectionNewSocialProfile : socialProfileCollectionNew) {
                if (!socialProfileCollectionOld.contains(socialProfileCollectionNewSocialProfile)) {
                    Availability oldAvailabilityIdOfSocialProfileCollectionNewSocialProfile = socialProfileCollectionNewSocialProfile.getAvailabilityId();
                    socialProfileCollectionNewSocialProfile.setAvailabilityId(availability);
                    socialProfileCollectionNewSocialProfile = em.merge(socialProfileCollectionNewSocialProfile);
                    if (oldAvailabilityIdOfSocialProfileCollectionNewSocialProfile != null && !oldAvailabilityIdOfSocialProfileCollectionNewSocialProfile.equals(availability)) {
                        oldAvailabilityIdOfSocialProfileCollectionNewSocialProfile.getSocialProfileCollection().remove(socialProfileCollectionNewSocialProfile);
                        oldAvailabilityIdOfSocialProfileCollectionNewSocialProfile = em.merge(oldAvailabilityIdOfSocialProfileCollectionNewSocialProfile);
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
                Integer id = availability.getId();
                if (findAvailability(id) == null) {
                    throw new NonexistentEntityException("The availability with id " + id + " no longer exists.");
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
            Availability availability;
            try {
                availability = em.getReference(Availability.class, id);
                availability.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The availability with id " + id + " no longer exists.", enfe);
            }
            Collection<SocialProfile> socialProfileCollection = availability.getSocialProfileCollection();
            for (SocialProfile socialProfileCollectionSocialProfile : socialProfileCollection) {
                socialProfileCollectionSocialProfile.setAvailabilityId(null);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
            }
            em.remove(availability);
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

    public List<Availability> findAvailabilityEntities() {
        return findAvailabilityEntities(true, -1, -1);
    }

    public List<Availability> findAvailabilityEntities(int maxResults, int firstResult) {
        return findAvailabilityEntities(false, maxResults, firstResult);
    }

    private List<Availability> findAvailabilityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Availability.class));
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

    public Availability findAvailability(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Availability.class, id);
        } finally {
            em.close();
        }
    }

    public int getAvailabilityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Availability> rt = cq.from(Availability.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
