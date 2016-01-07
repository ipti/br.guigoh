/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.org.ipti.guigoh.model.entity.Doc;
import br.org.ipti.guigoh.model.entity.DocHistory;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author iptipc008
 */
public class DocHistoryJpaController implements Serializable {

    public DocHistoryJpaController() {
    }
    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DocHistory docHistory) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();em.getTransaction().begin();
            Doc docFk = docHistory.getDocFk();
            if (docFk != null) {
                docFk = em.getReference(docFk.getClass(), docFk.getId());
                docHistory.setDocFk(docFk);
            }
            SocialProfile editorSocialProfileFk = docHistory.getEditorSocialProfileFk();
            if (editorSocialProfileFk != null) {
                editorSocialProfileFk = em.getReference(editorSocialProfileFk.getClass(), editorSocialProfileFk.getTokenId());
                docHistory.setEditorSocialProfileFk(editorSocialProfileFk);
            }
            em.persist(docHistory);
            if (docFk != null) {
                docFk.getDocHistoryCollection().add(docHistory);
                docFk = em.merge(docFk);
            }
            if (editorSocialProfileFk != null) {
                editorSocialProfileFk.getDocHistoryCollection().add(docHistory);
                editorSocialProfileFk = em.merge(editorSocialProfileFk);
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

    public void edit(DocHistory docHistory) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();em.getTransaction().begin();
            DocHistory persistentDocHistory = em.find(DocHistory.class, docHistory.getId());
            Doc docFkOld = persistentDocHistory.getDocFk();
            Doc docFkNew = docHistory.getDocFk();
            SocialProfile editorSocialProfileFkOld = persistentDocHistory.getEditorSocialProfileFk();
            SocialProfile editorSocialProfileFkNew = docHistory.getEditorSocialProfileFk();
            if (docFkNew != null) {
                docFkNew = em.getReference(docFkNew.getClass(), docFkNew.getId());
                docHistory.setDocFk(docFkNew);
            }
            if (editorSocialProfileFkNew != null) {
                editorSocialProfileFkNew = em.getReference(editorSocialProfileFkNew.getClass(), editorSocialProfileFkNew.getTokenId());
                docHistory.setEditorSocialProfileFk(editorSocialProfileFkNew);
            }
            docHistory = em.merge(docHistory);
            if (docFkOld != null && !docFkOld.equals(docFkNew)) {
                docFkOld.getDocHistoryCollection().remove(docHistory);
                docFkOld = em.merge(docFkOld);
            }
            if (docFkNew != null && !docFkNew.equals(docFkOld)) {
                docFkNew.getDocHistoryCollection().add(docHistory);
                docFkNew = em.merge(docFkNew);
            }
            if (editorSocialProfileFkOld != null && !editorSocialProfileFkOld.equals(editorSocialProfileFkNew)) {
                editorSocialProfileFkOld.getDocHistoryCollection().remove(docHistory);
                editorSocialProfileFkOld = em.merge(editorSocialProfileFkOld);
            }
            if (editorSocialProfileFkNew != null && !editorSocialProfileFkNew.equals(editorSocialProfileFkOld)) {
                editorSocialProfileFkNew.getDocHistoryCollection().add(docHistory);
                editorSocialProfileFkNew = em.merge(editorSocialProfileFkNew);
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
                Integer id = docHistory.getId();
                if (findDocHistory(id) == null) {
                    throw new NonexistentEntityException("The docHistory with id " + id + " no longer exists.");
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
            em = getEntityManager();em.getTransaction().begin();
            DocHistory docHistory;
            try {
                docHistory = em.getReference(DocHistory.class, id);
                docHistory.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The docHistory with id " + id + " no longer exists.", enfe);
            }
            Doc docFk = docHistory.getDocFk();
            if (docFk != null) {
                docFk.getDocHistoryCollection().remove(docHistory);
                docFk = em.merge(docFk);
            }
            SocialProfile editorSocialProfileFk = docHistory.getEditorSocialProfileFk();
            if (editorSocialProfileFk != null) {
                editorSocialProfileFk.getDocHistoryCollection().remove(docHistory);
                editorSocialProfileFk = em.merge(editorSocialProfileFk);
            }
            em.remove(docHistory);
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

    public List<DocHistory> findDocHistoryEntities() {
        return findDocHistoryEntities(true, -1, -1);
    }

    public List<DocHistory> findDocHistoryEntities(int maxResults, int firstResult) {
        return findDocHistoryEntities(false, maxResults, firstResult);
    }

    private List<DocHistory> findDocHistoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DocHistory.class));
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

    public DocHistory findDocHistory(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DocHistory.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocHistoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DocHistory> rt = cq.from(DocHistory.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
