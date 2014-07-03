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
import com.guigoh.entity.DiscussionTopic;
import com.guigoh.entity.DiscussionTopicMsg;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Joe
 */
public class DiscussionTopicMsgDAO implements Serializable {

    private transient EntityManagerFactory emf = JPAUtil.getEMF();

    public DiscussionTopicMsgDAO() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DiscussionTopicMsg discussionTopicMsg) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DiscussionTopic discussionTopicId = discussionTopicMsg.getDiscussionTopicId();
            if (discussionTopicId != null) {
                discussionTopicId = em.getReference(discussionTopicId.getClass(), discussionTopicId.getId());
                discussionTopicMsg.setDiscussionTopicId(discussionTopicId);
            }
            em.persist(discussionTopicMsg);
            if (discussionTopicId != null) {
                discussionTopicId.getDiscussionTopicMsgCollection().add(discussionTopicMsg);
                discussionTopicId = em.merge(discussionTopicId);
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

    public void edit(DiscussionTopicMsg discussionTopicMsg) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DiscussionTopicMsg persistentDiscussionTopicMsg = em.find(DiscussionTopicMsg.class, discussionTopicMsg.getId());
            DiscussionTopic discussionTopicIdOld = persistentDiscussionTopicMsg.getDiscussionTopicId();
            DiscussionTopic discussionTopicIdNew = discussionTopicMsg.getDiscussionTopicId();
            if (discussionTopicIdNew != null) {
                discussionTopicIdNew = em.getReference(discussionTopicIdNew.getClass(), discussionTopicIdNew.getId());
                discussionTopicMsg.setDiscussionTopicId(discussionTopicIdNew);
            }
            discussionTopicMsg = em.merge(discussionTopicMsg);
            if (discussionTopicIdOld != null && !discussionTopicIdOld.equals(discussionTopicIdNew)) {
                discussionTopicIdOld.getDiscussionTopicMsgCollection().remove(discussionTopicMsg);
                discussionTopicIdOld = em.merge(discussionTopicIdOld);
            }
            if (discussionTopicIdNew != null && !discussionTopicIdNew.equals(discussionTopicIdOld)) {
                discussionTopicIdNew.getDiscussionTopicMsgCollection().add(discussionTopicMsg);
                discussionTopicIdNew = em.merge(discussionTopicIdNew);
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
                Integer id = discussionTopicMsg.getId();
                if (findDiscussionTopicMsg(id) == null) {
                    throw new NonexistentEntityException("The discussionTopicMsg with id " + id + " no longer exists.");
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
            DiscussionTopicMsg discussionTopicMsg;
            try {
                discussionTopicMsg = em.getReference(DiscussionTopicMsg.class, id);
                discussionTopicMsg.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The discussionTopicMsg with id " + id + " no longer exists.", enfe);
            }
            DiscussionTopic discussionTopicId = discussionTopicMsg.getDiscussionTopicId();
            if (discussionTopicId != null) {
                discussionTopicId.getDiscussionTopicMsgCollection().remove(discussionTopicMsg);
                discussionTopicId = em.merge(discussionTopicId);
            }
            em.remove(discussionTopicMsg);
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

    public List<DiscussionTopicMsg> findDiscussionTopicMsgEntities() {
        return findDiscussionTopicMsgEntities(true, -1, -1);
    }

    public List<DiscussionTopicMsg> findDiscussionTopicMsgEntities(int maxResults, int firstResult) {
        return findDiscussionTopicMsgEntities(false, maxResults, firstResult);
    }

    private List<DiscussionTopicMsg> findDiscussionTopicMsgEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DiscussionTopicMsg.class));
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

    public DiscussionTopicMsg findDiscussionTopicMsg(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DiscussionTopicMsg.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiscussionTopicMsgCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DiscussionTopicMsg> rt = cq.from(DiscussionTopicMsg.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<DiscussionTopicMsg> findDiscussionTopicMsgsByTopic(Integer id){
        EntityManager em = getEntityManager();
        try {
            List<DiscussionTopicMsg> discussionTopicMsgList = (List<DiscussionTopicMsg>) em.createNativeQuery("select * from discussion_topic_msg "
                    + "where discussion_topic_id = " + id + " and status = 'A'", DiscussionTopicMsg.class).getResultList();
            return discussionTopicMsgList;
        } finally {
            em.close();
        }
    }
    
    public Timestamp getServerTime(){
        EntityManager em = getEntityManager();
        try {
            Timestamp serverTime = (Timestamp) em.createNativeQuery("SELECT date_trunc('seconds', now()::timestamp);").getSingleResult();
            return serverTime;
        } finally {
            em.close();
        }
    }
}
