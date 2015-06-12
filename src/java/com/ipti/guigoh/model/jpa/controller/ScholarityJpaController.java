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
import java.util.ArrayList;
import java.util.Collection;
import com.ipti.guigoh.model.entity.EducationsName;
import com.ipti.guigoh.model.entity.Scholarity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Joe
 */
public class ScholarityJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();

    public ScholarityJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Scholarity scholarity) throws RollbackFailureException, Exception {
        if (scholarity.getSocialProfileCollection() == null) {
            scholarity.setSocialProfileCollection(new ArrayList<SocialProfile>());
        }
        if (scholarity.getEducationsNameCollection() == null) {
            scholarity.setEducationsNameCollection(new ArrayList<EducationsName>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<SocialProfile> attachedSocialProfileCollection = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionSocialProfileToAttach : scholarity.getSocialProfileCollection()) {
                socialProfileCollectionSocialProfileToAttach = em.getReference(socialProfileCollectionSocialProfileToAttach.getClass(), socialProfileCollectionSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollection.add(socialProfileCollectionSocialProfileToAttach);
            }
            scholarity.setSocialProfileCollection(attachedSocialProfileCollection);
            Collection<EducationsName> attachedEducationsNameCollection = new ArrayList<EducationsName>();
            for (EducationsName educationsNameCollectionEducationsNameToAttach : scholarity.getEducationsNameCollection()) {
                educationsNameCollectionEducationsNameToAttach = em.getReference(educationsNameCollectionEducationsNameToAttach.getClass(), educationsNameCollectionEducationsNameToAttach.getId());
                attachedEducationsNameCollection.add(educationsNameCollectionEducationsNameToAttach);
            }
            scholarity.setEducationsNameCollection(attachedEducationsNameCollection);
            em.persist(scholarity);
            for (SocialProfile socialProfileCollectionSocialProfile : scholarity.getSocialProfileCollection()) {
                Scholarity oldScholarityIdOfSocialProfileCollectionSocialProfile = socialProfileCollectionSocialProfile.getScholarityId();
                socialProfileCollectionSocialProfile.setScholarityId(scholarity);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
                if (oldScholarityIdOfSocialProfileCollectionSocialProfile != null) {
                    oldScholarityIdOfSocialProfileCollectionSocialProfile.getSocialProfileCollection().remove(socialProfileCollectionSocialProfile);
                    oldScholarityIdOfSocialProfileCollectionSocialProfile = em.merge(oldScholarityIdOfSocialProfileCollectionSocialProfile);
                }
            }
            for (EducationsName educationsNameCollectionEducationsName : scholarity.getEducationsNameCollection()) {
                Scholarity oldScholarityIdOfEducationsNameCollectionEducationsName = educationsNameCollectionEducationsName.getScholarityId();
                educationsNameCollectionEducationsName.setScholarityId(scholarity);
                educationsNameCollectionEducationsName = em.merge(educationsNameCollectionEducationsName);
                if (oldScholarityIdOfEducationsNameCollectionEducationsName != null) {
                    oldScholarityIdOfEducationsNameCollectionEducationsName.getEducationsNameCollection().remove(educationsNameCollectionEducationsName);
                    oldScholarityIdOfEducationsNameCollectionEducationsName = em.merge(oldScholarityIdOfEducationsNameCollectionEducationsName);
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

    public void edit(Scholarity scholarity) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Scholarity persistentScholarity = em.find(Scholarity.class, scholarity.getId());
            Collection<SocialProfile> socialProfileCollectionOld = persistentScholarity.getSocialProfileCollection();
            Collection<SocialProfile> socialProfileCollectionNew = scholarity.getSocialProfileCollection();
            Collection<EducationsName> educationsNameCollectionOld = persistentScholarity.getEducationsNameCollection();
            Collection<EducationsName> educationsNameCollectionNew = scholarity.getEducationsNameCollection();
            Collection<SocialProfile> attachedSocialProfileCollectionNew = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionNewSocialProfileToAttach : socialProfileCollectionNew) {
                socialProfileCollectionNewSocialProfileToAttach = em.getReference(socialProfileCollectionNewSocialProfileToAttach.getClass(), socialProfileCollectionNewSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollectionNew.add(socialProfileCollectionNewSocialProfileToAttach);
            }
            socialProfileCollectionNew = attachedSocialProfileCollectionNew;
            scholarity.setSocialProfileCollection(socialProfileCollectionNew);
            Collection<EducationsName> attachedEducationsNameCollectionNew = new ArrayList<EducationsName>();
            for (EducationsName educationsNameCollectionNewEducationsNameToAttach : educationsNameCollectionNew) {
                educationsNameCollectionNewEducationsNameToAttach = em.getReference(educationsNameCollectionNewEducationsNameToAttach.getClass(), educationsNameCollectionNewEducationsNameToAttach.getId());
                attachedEducationsNameCollectionNew.add(educationsNameCollectionNewEducationsNameToAttach);
            }
            educationsNameCollectionNew = attachedEducationsNameCollectionNew;
            scholarity.setEducationsNameCollection(educationsNameCollectionNew);
            scholarity = em.merge(scholarity);
            for (SocialProfile socialProfileCollectionOldSocialProfile : socialProfileCollectionOld) {
                if (!socialProfileCollectionNew.contains(socialProfileCollectionOldSocialProfile)) {
                    socialProfileCollectionOldSocialProfile.setScholarityId(null);
                    socialProfileCollectionOldSocialProfile = em.merge(socialProfileCollectionOldSocialProfile);
                }
            }
            for (SocialProfile socialProfileCollectionNewSocialProfile : socialProfileCollectionNew) {
                if (!socialProfileCollectionOld.contains(socialProfileCollectionNewSocialProfile)) {
                    Scholarity oldScholarityIdOfSocialProfileCollectionNewSocialProfile = socialProfileCollectionNewSocialProfile.getScholarityId();
                    socialProfileCollectionNewSocialProfile.setScholarityId(scholarity);
                    socialProfileCollectionNewSocialProfile = em.merge(socialProfileCollectionNewSocialProfile);
                    if (oldScholarityIdOfSocialProfileCollectionNewSocialProfile != null && !oldScholarityIdOfSocialProfileCollectionNewSocialProfile.equals(scholarity)) {
                        oldScholarityIdOfSocialProfileCollectionNewSocialProfile.getSocialProfileCollection().remove(socialProfileCollectionNewSocialProfile);
                        oldScholarityIdOfSocialProfileCollectionNewSocialProfile = em.merge(oldScholarityIdOfSocialProfileCollectionNewSocialProfile);
                    }
                }
            }
            for (EducationsName educationsNameCollectionOldEducationsName : educationsNameCollectionOld) {
                if (!educationsNameCollectionNew.contains(educationsNameCollectionOldEducationsName)) {
                    educationsNameCollectionOldEducationsName.setScholarityId(null);
                    educationsNameCollectionOldEducationsName = em.merge(educationsNameCollectionOldEducationsName);
                }
            }
            for (EducationsName educationsNameCollectionNewEducationsName : educationsNameCollectionNew) {
                if (!educationsNameCollectionOld.contains(educationsNameCollectionNewEducationsName)) {
                    Scholarity oldScholarityIdOfEducationsNameCollectionNewEducationsName = educationsNameCollectionNewEducationsName.getScholarityId();
                    educationsNameCollectionNewEducationsName.setScholarityId(scholarity);
                    educationsNameCollectionNewEducationsName = em.merge(educationsNameCollectionNewEducationsName);
                    if (oldScholarityIdOfEducationsNameCollectionNewEducationsName != null && !oldScholarityIdOfEducationsNameCollectionNewEducationsName.equals(scholarity)) {
                        oldScholarityIdOfEducationsNameCollectionNewEducationsName.getEducationsNameCollection().remove(educationsNameCollectionNewEducationsName);
                        oldScholarityIdOfEducationsNameCollectionNewEducationsName = em.merge(oldScholarityIdOfEducationsNameCollectionNewEducationsName);
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
                Integer id = scholarity.getId();
                if (findScholarity(id) == null) {
                    throw new NonexistentEntityException("The scholarity with id " + id + " no longer exists.");
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
            Scholarity scholarity;
            try {
                scholarity = em.getReference(Scholarity.class, id);
                scholarity.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The scholarity with id " + id + " no longer exists.", enfe);
            }
            Collection<SocialProfile> socialProfileCollection = scholarity.getSocialProfileCollection();
            for (SocialProfile socialProfileCollectionSocialProfile : socialProfileCollection) {
                socialProfileCollectionSocialProfile.setScholarityId(null);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
            }
            Collection<EducationsName> educationsNameCollection = scholarity.getEducationsNameCollection();
            for (EducationsName educationsNameCollectionEducationsName : educationsNameCollection) {
                educationsNameCollectionEducationsName.setScholarityId(null);
                educationsNameCollectionEducationsName = em.merge(educationsNameCollectionEducationsName);
            }
            em.remove(scholarity);
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

    public List<Scholarity> findScholarityEntities() {
        return findScholarityEntities(true, -1, -1);
    }

    public List<Scholarity> findScholarityEntities(int maxResults, int firstResult) {
        return findScholarityEntities(false, maxResults, firstResult);
    }

    private List<Scholarity> findScholarityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Scholarity.class));
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

    public Scholarity findScholarity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Scholarity.class, id);
        } finally {
            em.close();
        }
    }

    public int getScholarityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Scholarity> rt = cq.from(Scholarity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
