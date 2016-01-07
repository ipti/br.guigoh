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
import br.org.ipti.guigoh.model.entity.DocMessage;
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
public class DocMessageJpaController implements Serializable {

    public DocMessageJpaController() {
    }
    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DocMessage docMessage) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();em.getTransaction().begin();
            Doc docFk = docMessage.getDocFk();
            if (docFk != null) {
                docFk = em.getReference(docFk.getClass(), docFk.getId());
                docMessage.setDocFk(docFk);
            }
            SocialProfile socialProfileFk = docMessage.getSocialProfileFk();
            if (socialProfileFk != null) {
                socialProfileFk = em.getReference(socialProfileFk.getClass(), socialProfileFk.getTokenId());
                docMessage.setSocialProfileFk(socialProfileFk);
            }
            em.persist(docMessage);
            if (docFk != null) {
                docFk.getDocMessageCollection().add(docMessage);
                docFk = em.merge(docFk);
            }
            if (socialProfileFk != null) {
                socialProfileFk.getDocMessageCollection().add(docMessage);
                socialProfileFk = em.merge(socialProfileFk);
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

    public void edit(DocMessage docMessage) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();em.getTransaction().begin();
            DocMessage persistentDocMessage = em.find(DocMessage.class, docMessage.getId());
            Doc docFkOld = persistentDocMessage.getDocFk();
            Doc docFkNew = docMessage.getDocFk();
            SocialProfile socialProfileFkOld = persistentDocMessage.getSocialProfileFk();
            SocialProfile socialProfileFkNew = docMessage.getSocialProfileFk();
            if (docFkNew != null) {
                docFkNew = em.getReference(docFkNew.getClass(), docFkNew.getId());
                docMessage.setDocFk(docFkNew);
            }
            if (socialProfileFkNew != null) {
                socialProfileFkNew = em.getReference(socialProfileFkNew.getClass(), socialProfileFkNew.getTokenId());
                docMessage.setSocialProfileFk(socialProfileFkNew);
            }
            docMessage = em.merge(docMessage);
            if (docFkOld != null && !docFkOld.equals(docFkNew)) {
                docFkOld.getDocMessageCollection().remove(docMessage);
                docFkOld = em.merge(docFkOld);
            }
            if (docFkNew != null && !docFkNew.equals(docFkOld)) {
                docFkNew.getDocMessageCollection().add(docMessage);
                docFkNew = em.merge(docFkNew);
            }
            if (socialProfileFkOld != null && !socialProfileFkOld.equals(socialProfileFkNew)) {
                socialProfileFkOld.getDocMessageCollection().remove(docMessage);
                socialProfileFkOld = em.merge(socialProfileFkOld);
            }
            if (socialProfileFkNew != null && !socialProfileFkNew.equals(socialProfileFkOld)) {
                socialProfileFkNew.getDocMessageCollection().add(docMessage);
                socialProfileFkNew = em.merge(socialProfileFkNew);
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
                Integer id = docMessage.getId();
                if (findDocMessage(id) == null) {
                    throw new NonexistentEntityException("The docMessage with id " + id + " no longer exists.");
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
            DocMessage docMessage;
            try {
                docMessage = em.getReference(DocMessage.class, id);
                docMessage.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The docMessage with id " + id + " no longer exists.", enfe);
            }
            Doc docFk = docMessage.getDocFk();
            if (docFk != null) {
                docFk.getDocMessageCollection().remove(docMessage);
                docFk = em.merge(docFk);
            }
            SocialProfile socialProfileFk = docMessage.getSocialProfileFk();
            if (socialProfileFk != null) {
                socialProfileFk.getDocMessageCollection().remove(docMessage);
                socialProfileFk = em.merge(socialProfileFk);
            }
            em.remove(docMessage);
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

    public List<DocMessage> findDocMessageEntities() {
        return findDocMessageEntities(true, -1, -1);
    }

    public List<DocMessage> findDocMessageEntities(int maxResults, int firstResult) {
        return findDocMessageEntities(false, maxResults, firstResult);
    }

    private List<DocMessage> findDocMessageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DocMessage.class));
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

    public DocMessage findDocMessage(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DocMessage.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocMessageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DocMessage> rt = cq.from(DocMessage.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
