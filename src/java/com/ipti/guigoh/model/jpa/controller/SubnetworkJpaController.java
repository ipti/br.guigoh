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
import com.ipti.guigoh.model.entity.SocialProfile;
import com.ipti.guigoh.model.entity.Subnetwork;
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
public class SubnetworkJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();
    
    public SubnetworkJpaController() {

    }
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Subnetwork subnetwork) throws RollbackFailureException, Exception {
        if (subnetwork.getSocialProfileCollection() == null) {
            subnetwork.setSocialProfileCollection(new ArrayList<SocialProfile>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<SocialProfile> attachedSocialProfileCollection = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionSocialProfileToAttach : subnetwork.getSocialProfileCollection()) {
                socialProfileCollectionSocialProfileToAttach = em.getReference(socialProfileCollectionSocialProfileToAttach.getClass(), socialProfileCollectionSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollection.add(socialProfileCollectionSocialProfileToAttach);
            }
            subnetwork.setSocialProfileCollection(attachedSocialProfileCollection);
            em.persist(subnetwork);
            for (SocialProfile socialProfileCollectionSocialProfile : subnetwork.getSocialProfileCollection()) {
                Subnetwork oldSubnetworkIdOfSocialProfileCollectionSocialProfile = socialProfileCollectionSocialProfile.getSubnetworkId();
                socialProfileCollectionSocialProfile.setSubnetworkId(subnetwork);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
                if (oldSubnetworkIdOfSocialProfileCollectionSocialProfile != null) {
                    oldSubnetworkIdOfSocialProfileCollectionSocialProfile.getSocialProfileCollection().remove(socialProfileCollectionSocialProfile);
                    oldSubnetworkIdOfSocialProfileCollectionSocialProfile = em.merge(oldSubnetworkIdOfSocialProfileCollectionSocialProfile);
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

    public void edit(Subnetwork subnetwork) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Subnetwork persistentSubnetwork = em.find(Subnetwork.class, subnetwork.getId());
            Collection<SocialProfile> socialProfileCollectionOld = persistentSubnetwork.getSocialProfileCollection();
            Collection<SocialProfile> socialProfileCollectionNew = subnetwork.getSocialProfileCollection();
            Collection<SocialProfile> attachedSocialProfileCollectionNew = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionNewSocialProfileToAttach : socialProfileCollectionNew) {
                socialProfileCollectionNewSocialProfileToAttach = em.getReference(socialProfileCollectionNewSocialProfileToAttach.getClass(), socialProfileCollectionNewSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollectionNew.add(socialProfileCollectionNewSocialProfileToAttach);
            }
            socialProfileCollectionNew = attachedSocialProfileCollectionNew;
            subnetwork.setSocialProfileCollection(socialProfileCollectionNew);
            subnetwork = em.merge(subnetwork);
            for (SocialProfile socialProfileCollectionOldSocialProfile : socialProfileCollectionOld) {
                if (!socialProfileCollectionNew.contains(socialProfileCollectionOldSocialProfile)) {
                    socialProfileCollectionOldSocialProfile.setSubnetworkId(null);
                    socialProfileCollectionOldSocialProfile = em.merge(socialProfileCollectionOldSocialProfile);
                }
            }
            for (SocialProfile socialProfileCollectionNewSocialProfile : socialProfileCollectionNew) {
                if (!socialProfileCollectionOld.contains(socialProfileCollectionNewSocialProfile)) {
                    Subnetwork oldSubnetworkIdOfSocialProfileCollectionNewSocialProfile = socialProfileCollectionNewSocialProfile.getSubnetworkId();
                    socialProfileCollectionNewSocialProfile.setSubnetworkId(subnetwork);
                    socialProfileCollectionNewSocialProfile = em.merge(socialProfileCollectionNewSocialProfile);
                    if (oldSubnetworkIdOfSocialProfileCollectionNewSocialProfile != null && !oldSubnetworkIdOfSocialProfileCollectionNewSocialProfile.equals(subnetwork)) {
                        oldSubnetworkIdOfSocialProfileCollectionNewSocialProfile.getSocialProfileCollection().remove(socialProfileCollectionNewSocialProfile);
                        oldSubnetworkIdOfSocialProfileCollectionNewSocialProfile = em.merge(oldSubnetworkIdOfSocialProfileCollectionNewSocialProfile);
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
                Integer id = subnetwork.getId();
                if (findSubnetwork(id) == null) {
                    throw new NonexistentEntityException("The subnetwork with id " + id + " no longer exists.");
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
            Subnetwork subnetwork;
            try {
                subnetwork = em.getReference(Subnetwork.class, id);
                subnetwork.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subnetwork with id " + id + " no longer exists.", enfe);
            }
            Collection<SocialProfile> socialProfileCollection = subnetwork.getSocialProfileCollection();
            for (SocialProfile socialProfileCollectionSocialProfile : socialProfileCollection) {
                socialProfileCollectionSocialProfile.setSubnetworkId(null);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
            }
            em.remove(subnetwork);
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

    public List<Subnetwork> findSubnetworkEntities() {
        return findSubnetworkEntities(true, -1, -1);
    }

    public List<Subnetwork> findSubnetworkEntities(int maxResults, int firstResult) {
        return findSubnetworkEntities(false, maxResults, firstResult);
    }

    private List<Subnetwork> findSubnetworkEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Subnetwork.class));
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

    public Subnetwork findSubnetwork(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Subnetwork.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubnetworkCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Subnetwork> rt = cq.from(Subnetwork.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
