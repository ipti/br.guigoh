/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.dao;

import com.guigoh.dao.exceptions.NonexistentEntityException;
import com.guigoh.dao.exceptions.RollbackFailureException;
import com.guigoh.entity.DiscussionTopicWarnings;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Joe
 */
public class DiscussionTopicWarningsDAO implements Serializable {

    private transient EntityManagerFactory emf = JPAUtil.getEMF();

    public DiscussionTopicWarningsDAO() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DiscussionTopicWarnings discussionTopicWarnings) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(discussionTopicWarnings);
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

    public void edit(DiscussionTopicWarnings discussionTopicWarnings) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            discussionTopicWarnings = em.merge(discussionTopicWarnings);
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = discussionTopicWarnings.getId();
                if (findDiscussionTopicWarnings(id) == null) {
                    throw new NonexistentEntityException("The discussionTopicWarnings with id " + id + " no longer exists.");
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
            DiscussionTopicWarnings discussionTopicWarnings;
            try {
                discussionTopicWarnings = em.getReference(DiscussionTopicWarnings.class, id);
                discussionTopicWarnings.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The discussionTopicWarnings with id " + id + " no longer exists.", enfe);
            }
            em.remove(discussionTopicWarnings);
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

    public List<DiscussionTopicWarnings> findDiscussionTopicWarningsEntities() {
        return findDiscussionTopicWarningsEntities(true, -1, -1);
    }

    public List<DiscussionTopicWarnings> findDiscussionTopicWarningsEntities(int maxResults, int firstResult) {
        return findDiscussionTopicWarningsEntities(false, maxResults, firstResult);
    }

    private List<DiscussionTopicWarnings> findDiscussionTopicWarningsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DiscussionTopicWarnings.class));
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

    public DiscussionTopicWarnings findDiscussionTopicWarnings(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DiscussionTopicWarnings.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiscussionTopicWarningsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DiscussionTopicWarnings> rt = cq.from(DiscussionTopicWarnings.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
