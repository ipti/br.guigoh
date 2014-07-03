/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.dao;

import com.guigoh.dao.exceptions.NonexistentEntityException;
import com.guigoh.dao.exceptions.PreexistingEntityException;
import com.guigoh.dao.exceptions.RollbackFailureException;
import com.guigoh.entity.EmailActivation;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author Joe
 */
public class EmailActivationDAO implements Serializable {

    private transient EntityManagerFactory emf = JPAUtil.getEMF();

    public EmailActivationDAO() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EmailActivation emailActivation) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(emailActivation);
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEmailActivation(emailActivation.getUsername()) != null) {
                throw new PreexistingEntityException("EmailActivation " + emailActivation + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EmailActivation emailActivation) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            emailActivation = em.merge(emailActivation);
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = emailActivation.getUsername();
                if (findEmailActivation(id) == null) {
                    throw new NonexistentEntityException("The emailActivation with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EmailActivation emailActivation;
            try {
                emailActivation = em.getReference(EmailActivation.class, id);
                emailActivation.getUsername();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The emailActivation with id " + id + " no longer exists.", enfe);
            }
            em.remove(emailActivation);
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

    public List<EmailActivation> findEmailActivationEntities() {
        return findEmailActivationEntities(true, -1, -1);
    }

    public List<EmailActivation> findEmailActivationEntities(int maxResults, int firstResult) {
        return findEmailActivationEntities(false, maxResults, firstResult);
    }

    private List<EmailActivation> findEmailActivationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EmailActivation.class));
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

    public EmailActivation findEmailActivation(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EmailActivation.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmailActivationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EmailActivation> rt = cq.from(EmailActivation.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public EmailActivation findEmailActivationByUsername(String username) {
        EntityManager em = getEntityManager();
        try {
            EmailActivation emailActivation = (EmailActivation) em.createNativeQuery("select * from email_activation "
                    + "where username = '" + username + "'", EmailActivation.class).getSingleResult();
            if (emailActivation == null) {
                return new EmailActivation();
            }
            return emailActivation;
        } catch (NoResultException e) {
            return new EmailActivation();
        } finally {
            em.close();
        }
    }
}
