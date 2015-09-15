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
import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.EducationalObjectMessage;
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
public class EducationalObjectMessageJpaController implements Serializable {

    public EducationalObjectMessageJpaController() {
    }
    
    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EducationalObjectMessage educationalObjectMessage) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EducationalObject educationalObjectFk = educationalObjectMessage.getEducationalObjectFk();
            if (educationalObjectFk != null) {
                educationalObjectFk = em.getReference(educationalObjectFk.getClass(), educationalObjectFk.getId());
                educationalObjectMessage.setEducationalObjectFk(educationalObjectFk);
            }
            SocialProfile socialProfileFk = educationalObjectMessage.getSocialProfileFk();
            if (socialProfileFk != null) {
                socialProfileFk = em.getReference(socialProfileFk.getClass(), socialProfileFk.getTokenId());
                educationalObjectMessage.setSocialProfileFk(socialProfileFk);
            }
            em.persist(educationalObjectMessage);
            if (educationalObjectFk != null) {
                educationalObjectFk.getEducationalObjectMessageCollection().add(educationalObjectMessage);
                educationalObjectFk = em.merge(educationalObjectFk);
            }
            if (socialProfileFk != null) {
                socialProfileFk.getEducationalObjectMessageCollection().add(educationalObjectMessage);
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

    public void edit(EducationalObjectMessage educationalObjectMessage) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EducationalObjectMessage persistentEducationalObjectMessage = em.find(EducationalObjectMessage.class, educationalObjectMessage.getId());
            EducationalObject educationalObjectFkOld = persistentEducationalObjectMessage.getEducationalObjectFk();
            EducationalObject educationalObjectFkNew = educationalObjectMessage.getEducationalObjectFk();
            SocialProfile socialProfileFkOld = persistentEducationalObjectMessage.getSocialProfileFk();
            SocialProfile socialProfileFkNew = educationalObjectMessage.getSocialProfileFk();
            if (educationalObjectFkNew != null) {
                educationalObjectFkNew = em.getReference(educationalObjectFkNew.getClass(), educationalObjectFkNew.getId());
                educationalObjectMessage.setEducationalObjectFk(educationalObjectFkNew);
            }
            if (socialProfileFkNew != null) {
                socialProfileFkNew = em.getReference(socialProfileFkNew.getClass(), socialProfileFkNew.getTokenId());
                educationalObjectMessage.setSocialProfileFk(socialProfileFkNew);
            }
            educationalObjectMessage = em.merge(educationalObjectMessage);
            if (educationalObjectFkOld != null && !educationalObjectFkOld.equals(educationalObjectFkNew)) {
                educationalObjectFkOld.getEducationalObjectMessageCollection().remove(educationalObjectMessage);
                educationalObjectFkOld = em.merge(educationalObjectFkOld);
            }
            if (educationalObjectFkNew != null && !educationalObjectFkNew.equals(educationalObjectFkOld)) {
                educationalObjectFkNew.getEducationalObjectMessageCollection().add(educationalObjectMessage);
                educationalObjectFkNew = em.merge(educationalObjectFkNew);
            }
            if (socialProfileFkOld != null && !socialProfileFkOld.equals(socialProfileFkNew)) {
                socialProfileFkOld.getEducationalObjectMessageCollection().remove(educationalObjectMessage);
                socialProfileFkOld = em.merge(socialProfileFkOld);
            }
            if (socialProfileFkNew != null && !socialProfileFkNew.equals(socialProfileFkOld)) {
                socialProfileFkNew.getEducationalObjectMessageCollection().add(educationalObjectMessage);
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
                Integer id = educationalObjectMessage.getId();
                if (findEducationalObjectMessage(id) == null) {
                    throw new NonexistentEntityException("The educationalObjectMessage with id " + id + " no longer exists.");
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
            EducationalObjectMessage educationalObjectMessage;
            try {
                educationalObjectMessage = em.getReference(EducationalObjectMessage.class, id);
                educationalObjectMessage.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The educationalObjectMessage with id " + id + " no longer exists.", enfe);
            }
            EducationalObject educationalObjectFk = educationalObjectMessage.getEducationalObjectFk();
            if (educationalObjectFk != null) {
                educationalObjectFk.getEducationalObjectMessageCollection().remove(educationalObjectMessage);
                educationalObjectFk = em.merge(educationalObjectFk);
            }
            SocialProfile socialProfileFk = educationalObjectMessage.getSocialProfileFk();
            if (socialProfileFk != null) {
                socialProfileFk.getEducationalObjectMessageCollection().remove(educationalObjectMessage);
                socialProfileFk = em.merge(socialProfileFk);
            }
            em.remove(educationalObjectMessage);
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

    public List<EducationalObjectMessage> findEducationalObjectMessageEntities() {
        return findEducationalObjectMessageEntities(true, -1, -1);
    }

    public List<EducationalObjectMessage> findEducationalObjectMessageEntities(int maxResults, int firstResult) {
        return findEducationalObjectMessageEntities(false, maxResults, firstResult);
    }

    private List<EducationalObjectMessage> findEducationalObjectMessageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EducationalObjectMessage.class));
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

    public EducationalObjectMessage findEducationalObjectMessage(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EducationalObjectMessage.class, id);
        } finally {
            em.close();
        }
    }

    public int getEducationalObjectMessageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EducationalObjectMessage> rt = cq.from(EducationalObjectMessage.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
