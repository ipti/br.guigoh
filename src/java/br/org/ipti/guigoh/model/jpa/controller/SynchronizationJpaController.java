/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import br.org.ipti.guigoh.model.jpa.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.model.entity.Synchronization;
import java.io.Serializable;
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
 * @author Joe
 */
public class SynchronizationJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();

    public SynchronizationJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Synchronization synchronization) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(synchronization);
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

    public void edit(Synchronization synchronization) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            synchronization = em.merge(synchronization);
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = synchronization.getId();
                if (findSynchronization(id) == null) {
                    throw new NonexistentEntityException("The synchronization with id " + id + " no longer exists.");
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
            Synchronization synchronization;
            try {
                synchronization = em.getReference(Synchronization.class, id);
                synchronization.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The synchronization with id " + id + " no longer exists.", enfe);
            }
            em.remove(synchronization);
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

    public List<Synchronization> findSynchronizationEntities() {
        return findSynchronizationEntities(true, -1, -1);
    }

    public List<Synchronization> findSynchronizationEntities(int maxResults, int firstResult) {
        return findSynchronizationEntities(false, maxResults, firstResult);
    }

    private List<Synchronization> findSynchronizationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Synchronization.class));
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

    public Synchronization findSynchronization(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Synchronization.class, id);
        } finally {
            em.close();
        }
    }

    public int getSynchronizationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Synchronization> rt = cq.from(Synchronization.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
