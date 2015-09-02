/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.model.entity.DiscussionTopicFiles;
import java.io.Serializable;
import java.util.ArrayList;
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
public class DiscussionTopicFilesJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();

    public DiscussionTopicFilesJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DiscussionTopicFiles discussionTopicFiles) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(discussionTopicFiles);
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

    public void edit(DiscussionTopicFiles discussionTopicFiles) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em.getTransaction().begin();
            em = getEntityManager();
            discussionTopicFiles = em.merge(discussionTopicFiles);
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = discussionTopicFiles.getId();
                if (findDiscussionTopicFiles(id) == null) {
                    throw new NonexistentEntityException("The discussionTopicFiles with id " + id + " no longer exists.");
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
            em.getTransaction().begin();
            em = getEntityManager();
            DiscussionTopicFiles discussionTopicFiles;
            try {
                discussionTopicFiles = em.getReference(DiscussionTopicFiles.class, id);
                discussionTopicFiles.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The discussionTopicFiles with id " + id + " no longer exists.", enfe);
            }
            em.remove(discussionTopicFiles);
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

    public List<DiscussionTopicFiles> findDiscussionTopicFilesEntities() {
        return findDiscussionTopicFilesEntities(true, -1, -1);
    }

    public List<DiscussionTopicFiles> findDiscussionTopicFilesEntities(int maxResults, int firstResult) {
        return findDiscussionTopicFilesEntities(false, maxResults, firstResult);
    }

    private List<DiscussionTopicFiles> findDiscussionTopicFilesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DiscussionTopicFiles.class));
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

    public DiscussionTopicFiles findDiscussionTopicFiles(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DiscussionTopicFiles.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiscussionTopicFilesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DiscussionTopicFiles> rt = cq.from(DiscussionTopicFiles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<DiscussionTopicFiles> getDiscussionTopicFilesByFK(Integer id, char type){
        EntityManager em = getEntityManager();
        try {
            List<DiscussionTopicFiles> discussionTopicFilesList = (List<DiscussionTopicFiles>) em.createNativeQuery("select * from discussion_topic_files "
                    + "where fk_id = " + id + " and fk_type = '"+type+"'", DiscussionTopicFiles.class).getResultList();
            if (discussionTopicFilesList == null) {
                return new ArrayList<>();
            }
            return discussionTopicFilesList;
        } finally {
            em.close();
        }
    }
}
