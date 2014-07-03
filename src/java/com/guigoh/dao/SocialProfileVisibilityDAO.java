/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.dao;

import com.guigoh.dao.exceptions.IllegalOrphanException;
import com.guigoh.dao.exceptions.NonexistentEntityException;
import com.guigoh.dao.exceptions.PreexistingEntityException;
import com.guigoh.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.guigoh.entity.SocialProfile;
import com.guigoh.entity.SocialProfileVisibility;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Joe
 */
public class SocialProfileVisibilityDAO implements Serializable {

    public SocialProfileVisibilityDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SocialProfileVisibility socialProfileVisibility) throws IllegalOrphanException, PreexistingEntityException, RollbackFailureException, Exception {
        List<String> illegalOrphanMessages = null;
        SocialProfile socialProfileOrphanCheck = socialProfileVisibility.getSocialProfile();
        if (socialProfileOrphanCheck != null) {
            SocialProfileVisibility oldSocialProfileVisibilityOfSocialProfile = socialProfileOrphanCheck.getSocialProfileVisibility();
            if (oldSocialProfileVisibilityOfSocialProfile != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The SocialProfile " + socialProfileOrphanCheck + " already has an item of type SocialProfileVisibility whose socialProfile column cannot be null. Please make another selection for the socialProfile field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            SocialProfile socialProfile = socialProfileVisibility.getSocialProfile();
            if (socialProfile != null) {
                socialProfile = em.getReference(socialProfile.getClass(), socialProfile.getTokenId());
                socialProfileVisibility.setSocialProfile(socialProfile);
            }
            em.persist(socialProfileVisibility);
            if (socialProfile != null) {
                socialProfile.setSocialProfileVisibility(socialProfileVisibility);
                socialProfile = em.merge(socialProfile);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findSocialProfileVisibility(socialProfileVisibility.getSocialProfileId()) != null) {
                throw new PreexistingEntityException("SocialProfileVisibility " + socialProfileVisibility + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SocialProfileVisibility socialProfileVisibility) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            SocialProfileVisibility persistentSocialProfileVisibility = em.find(SocialProfileVisibility.class, socialProfileVisibility.getSocialProfileId());
            SocialProfile socialProfileOld = persistentSocialProfileVisibility.getSocialProfile();
            SocialProfile socialProfileNew = socialProfileVisibility.getSocialProfile();
            List<String> illegalOrphanMessages = null;
            if (socialProfileNew != null && !socialProfileNew.equals(socialProfileOld)) {
                SocialProfileVisibility oldSocialProfileVisibilityOfSocialProfile = socialProfileNew.getSocialProfileVisibility();
                if (oldSocialProfileVisibilityOfSocialProfile != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The SocialProfile " + socialProfileNew + " already has an item of type SocialProfileVisibility whose socialProfile column cannot be null. Please make another selection for the socialProfile field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (socialProfileNew != null) {
                socialProfileNew = em.getReference(socialProfileNew.getClass(), socialProfileNew.getTokenId());
                socialProfileVisibility.setSocialProfile(socialProfileNew);
            }
            socialProfileVisibility = em.merge(socialProfileVisibility);
            if (socialProfileOld != null && !socialProfileOld.equals(socialProfileNew)) {
                socialProfileOld.setSocialProfileVisibility(null);
                socialProfileOld = em.merge(socialProfileOld);
            }
            if (socialProfileNew != null && !socialProfileNew.equals(socialProfileOld)) {
                socialProfileNew.setSocialProfileVisibility(socialProfileVisibility);
                socialProfileNew = em.merge(socialProfileNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = socialProfileVisibility.getSocialProfileId();
                if (findSocialProfileVisibility(id) == null) {
                    throw new NonexistentEntityException("The socialProfileVisibility with id " + id + " no longer exists.");
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
            utx.begin();
            em = getEntityManager();
            SocialProfileVisibility socialProfileVisibility;
            try {
                socialProfileVisibility = em.getReference(SocialProfileVisibility.class, id);
                socialProfileVisibility.getSocialProfileId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The socialProfileVisibility with id " + id + " no longer exists.", enfe);
            }
            SocialProfile socialProfile = socialProfileVisibility.getSocialProfile();
            if (socialProfile != null) {
                socialProfile.setSocialProfileVisibility(null);
                socialProfile = em.merge(socialProfile);
            }
            em.remove(socialProfileVisibility);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
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

    public List<SocialProfileVisibility> findSocialProfileVisibilityEntities() {
        return findSocialProfileVisibilityEntities(true, -1, -1);
    }

    public List<SocialProfileVisibility> findSocialProfileVisibilityEntities(int maxResults, int firstResult) {
        return findSocialProfileVisibilityEntities(false, maxResults, firstResult);
    }

    private List<SocialProfileVisibility> findSocialProfileVisibilityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SocialProfileVisibility.class));
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

    public SocialProfileVisibility findSocialProfileVisibility(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SocialProfileVisibility.class, id);
        } finally {
            em.close();
        }
    }

    public int getSocialProfileVisibilityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SocialProfileVisibility> rt = cq.from(SocialProfileVisibility.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
