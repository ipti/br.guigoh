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
import br.org.ipti.guigoh.model.entity.DocGuest;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.transaction.UserTransaction;

/**
 *
 * @author iptipc008
 */
public class DocGuestJpaController implements Serializable {

    public DocGuestJpaController() {
    }
    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DocGuest docGuest) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Doc docFk = docGuest.getDocFk();
            if (docFk != null) {
                docFk = em.getReference(docFk.getClass(), docFk.getId());
                docGuest.setDocFk(docFk);
            }
            SocialProfile socialProfileFk = docGuest.getSocialProfileFk();
            if (socialProfileFk != null) {
                socialProfileFk = em.getReference(socialProfileFk.getClass(), socialProfileFk.getTokenId());
                docGuest.setSocialProfileFk(socialProfileFk);
            }
            em.persist(docGuest);
            if (docFk != null) {
                docFk.getDocGuestCollection().add(docGuest);
                docFk = em.merge(docFk);
            }
            if (socialProfileFk != null) {
                socialProfileFk.getDocGuestCollection().add(docGuest);
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

    public void edit(DocGuest docGuest) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DocGuest persistentDocGuest = em.find(DocGuest.class, docGuest.getId());
            Doc docFkOld = persistentDocGuest.getDocFk();
            Doc docFkNew = docGuest.getDocFk();
            SocialProfile socialProfileFkOld = persistentDocGuest.getSocialProfileFk();
            SocialProfile socialProfileFkNew = docGuest.getSocialProfileFk();
            if (docFkNew != null) {
                docFkNew = em.getReference(docFkNew.getClass(), docFkNew.getId());
                docGuest.setDocFk(docFkNew);
            }
            if (socialProfileFkNew != null) {
                socialProfileFkNew = em.getReference(socialProfileFkNew.getClass(), socialProfileFkNew.getTokenId());
                docGuest.setSocialProfileFk(socialProfileFkNew);
            }
            docGuest = em.merge(docGuest);
            if (docFkOld != null && !docFkOld.equals(docFkNew)) {
                docFkOld.getDocGuestCollection().remove(docGuest);
                docFkOld = em.merge(docFkOld);
            }
            if (docFkNew != null && !docFkNew.equals(docFkOld)) {
                docFkNew.getDocGuestCollection().add(docGuest);
                docFkNew = em.merge(docFkNew);
            }
            if (socialProfileFkOld != null && !socialProfileFkOld.equals(socialProfileFkNew)) {
                socialProfileFkOld.getDocGuestCollection().remove(docGuest);
                socialProfileFkOld = em.merge(socialProfileFkOld);
            }
            if (socialProfileFkNew != null && !socialProfileFkNew.equals(socialProfileFkOld)) {
                socialProfileFkNew.getDocGuestCollection().add(docGuest);
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
                Integer id = docGuest.getId();
                if (findDocGuest(id) == null) {
                    throw new NonexistentEntityException("The docGuest with id " + id + " no longer exists.");
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
            DocGuest docGuest;
            try {
                docGuest = em.getReference(DocGuest.class, id);
                docGuest.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The docGuest with id " + id + " no longer exists.", enfe);
            }
            Doc docFk = docGuest.getDocFk();
            if (docFk != null) {
                docFk.getDocGuestCollection().remove(docGuest);
                docFk = em.merge(docFk);
            }
            SocialProfile socialProfileFk = docGuest.getSocialProfileFk();
            if (socialProfileFk != null) {
                socialProfileFk.getDocGuestCollection().remove(docGuest);
                socialProfileFk = em.merge(socialProfileFk);
            }
            em.remove(docGuest);
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

    public List<DocGuest> findDocGuestEntities() {
        return findDocGuestEntities(true, -1, -1);
    }

    public List<DocGuest> findDocGuestEntities(int maxResults, int firstResult) {
        return findDocGuestEntities(false, maxResults, firstResult);
    }

    private List<DocGuest> findDocGuestEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DocGuest.class));
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

    public DocGuest findDocGuest(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DocGuest.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocGuestCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DocGuest> rt = cq.from(DocGuest.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<DocGuest> findByDocId(Integer docId) {
        EntityManager em = getEntityManager();
        try {
            List<DocGuest> docGuestList = (List<DocGuest>) em.createNativeQuery("select * from doc_guest dg "
                    + "where dg.doc_fk = '" + docId + "'", DocGuest.class).getResultList();
            if (docGuestList == null) {
                return new ArrayList<>();
            }
            return docGuestList;
        } finally {
            em.close();
        }
    }

    public DocGuest findByUserTokenId(Integer docId, String tokenId) {
        EntityManager em = getEntityManager();
        try {
            DocGuest docGuest = (DocGuest) em.createNativeQuery("select dg.* from doc_guest dg "
                    + "join social_profile sp on sp.social_profile_id = dg.social_profile_fk "
                    + "where dg.doc_fk = '" + docId + "' and sp.token_id = '" + tokenId + "'", DocGuest.class).getSingleResult();
            return docGuest;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
