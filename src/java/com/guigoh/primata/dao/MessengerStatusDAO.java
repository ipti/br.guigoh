/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.dao;

import com.guigoh.primata.dao.exceptions.NonexistentEntityException;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import com.guigoh.primata.entity.MessengerStatus;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author IPTI
 */
public class MessengerStatusDAO implements Serializable {

    private transient EntityManagerFactory emf = JPAUtil.getEMF();
    
    public MessengerStatusDAO() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MessengerStatus messengerStatus) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(messengerStatus);
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

    public void edit(MessengerStatus messengerStatus) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            messengerStatus = em.merge(messengerStatus);
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = messengerStatus.getSocialProfileId();
                if (findMessengerStatus(id) == null) {
                    throw new NonexistentEntityException("The messengerStatus with id " + id + " no longer exists.");
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
            MessengerStatus messengerStatus;
            try {
                messengerStatus = em.getReference(MessengerStatus.class, id);
                messengerStatus.getSocialProfileId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The messengerStatus with id " + id + " no longer exists.", enfe);
            }
            em.remove(messengerStatus);
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

    public List<MessengerStatus> findMessengerStatusEntities() {
        return findMessengerStatusEntities(true, -1, -1);
    }

    public List<MessengerStatus> findMessengerStatusEntities(int maxResults, int firstResult) {
        return findMessengerStatusEntities(false, maxResults, firstResult);
    }

    private List<MessengerStatus> findMessengerStatusEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MessengerStatus.class));
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

    public MessengerStatus findMessengerStatus(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MessengerStatus.class, id);
        } finally {
            em.close();
        }
    }

    public int getMessengerStatusCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MessengerStatus> rt = cq.from(MessengerStatus.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Double getServerTime() {
        EntityManager em = getEntityManager();
        try {
            Double serverTime = (Double) em.createNativeQuery("select round( date_part( 'epoch', now() ) )").getSingleResult();
            return serverTime;
        } finally {
            em.close();
        }
    }
    
    public Long getUsersOnline(){
        EntityManager em = getEntityManager();
        try {
            Long usersOnline = (Long) em.createNativeQuery("select count(*) from primata_messenger_status where (select round( date_part( 'epoch', now() ) )) - last_ping < 90").getSingleResult();
            return usersOnline;
        } finally {
            em.close();
        }
    }
    
}
