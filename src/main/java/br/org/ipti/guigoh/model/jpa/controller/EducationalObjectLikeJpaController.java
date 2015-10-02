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
import br.org.ipti.guigoh.model.entity.EducationalObjectLike;
import br.org.ipti.guigoh.model.entity.EducationalObjectLikePK;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.PreexistingEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author iptipc008
 */
public class EducationalObjectLikeJpaController implements Serializable {

    public EducationalObjectLikeJpaController() {
    }
    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EducationalObjectLike educationalObjectLike) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (educationalObjectLike.getEducationalObjectLikePK() == null) {
            educationalObjectLike.setEducationalObjectLikePK(new EducationalObjectLikePK());
        }
        educationalObjectLike.getEducationalObjectLikePK().setEducationalObjectFk(educationalObjectLike.getEducationalObject().getId());
        educationalObjectLike.getEducationalObjectLikePK().setSocialProfileFk(educationalObjectLike.getSocialProfile().getSocialProfileId());
        EntityManager em = null;
        try {
            em = getEntityManager();em.getTransaction().begin();
            EducationalObject educationalObject = educationalObjectLike.getEducationalObject();
            if (educationalObject != null) {
                educationalObject = em.getReference(educationalObject.getClass(), educationalObject.getId());
                educationalObjectLike.setEducationalObject(educationalObject);
            }
            SocialProfile socialProfile = educationalObjectLike.getSocialProfile();
            if (socialProfile != null) {
                socialProfile = em.getReference(socialProfile.getClass(), socialProfile.getTokenId());
                educationalObjectLike.setSocialProfile(socialProfile);
            }
            em.persist(educationalObjectLike);
            if (educationalObject != null) {
                educationalObject.getEducationalObjectLikeCollection().add(educationalObjectLike);
                educationalObject = em.merge(educationalObject);
            }
            if (socialProfile != null) {
                socialProfile.getEducationalObjectLikeCollection().add(educationalObjectLike);
                socialProfile = em.merge(socialProfile);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEducationalObjectLike(educationalObjectLike.getEducationalObjectLikePK()) != null) {
                throw new PreexistingEntityException("EducationalObjectLike " + educationalObjectLike + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EducationalObjectLike educationalObjectLike) throws NonexistentEntityException, RollbackFailureException, Exception {
        educationalObjectLike.getEducationalObjectLikePK().setEducationalObjectFk(educationalObjectLike.getEducationalObject().getId());
        educationalObjectLike.getEducationalObjectLikePK().setSocialProfileFk(educationalObjectLike.getSocialProfile().getSocialProfileId());
        EntityManager em = null;
        try {
            em = getEntityManager();em.getTransaction().begin();
            EducationalObjectLike persistentEducationalObjectLike = em.find(EducationalObjectLike.class, educationalObjectLike.getEducationalObjectLikePK());
            EducationalObject educationalObjectOld = persistentEducationalObjectLike.getEducationalObject();
            EducationalObject educationalObjectNew = educationalObjectLike.getEducationalObject();
            SocialProfile socialProfileOld = persistentEducationalObjectLike.getSocialProfile();
            SocialProfile socialProfileNew = educationalObjectLike.getSocialProfile();
            if (educationalObjectNew != null) {
                educationalObjectNew = em.getReference(educationalObjectNew.getClass(), educationalObjectNew.getId());
                educationalObjectLike.setEducationalObject(educationalObjectNew);
            }
            if (socialProfileNew != null) {
                socialProfileNew = em.getReference(socialProfileNew.getClass(), socialProfileNew.getTokenId());
                educationalObjectLike.setSocialProfile(socialProfileNew);
            }
            educationalObjectLike = em.merge(educationalObjectLike);
            if (educationalObjectOld != null && !educationalObjectOld.equals(educationalObjectNew)) {
                educationalObjectOld.getEducationalObjectLikeCollection().remove(educationalObjectLike);
                educationalObjectOld = em.merge(educationalObjectOld);
            }
            if (educationalObjectNew != null && !educationalObjectNew.equals(educationalObjectOld)) {
                educationalObjectNew.getEducationalObjectLikeCollection().add(educationalObjectLike);
                educationalObjectNew = em.merge(educationalObjectNew);
            }
            if (socialProfileOld != null && !socialProfileOld.equals(socialProfileNew)) {
                socialProfileOld.getEducationalObjectLikeCollection().remove(educationalObjectLike);
                socialProfileOld = em.merge(socialProfileOld);
            }
            if (socialProfileNew != null && !socialProfileNew.equals(socialProfileOld)) {
                socialProfileNew.getEducationalObjectLikeCollection().add(educationalObjectLike);
                socialProfileNew = em.merge(socialProfileNew);
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
                EducationalObjectLikePK id = educationalObjectLike.getEducationalObjectLikePK();
                if (findEducationalObjectLike(id) == null) {
                    throw new NonexistentEntityException("The educationalObjectLike with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(EducationalObjectLikePK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();em.getTransaction().begin();
            EducationalObjectLike educationalObjectLike;
            try {
                educationalObjectLike = em.getReference(EducationalObjectLike.class, id);
                educationalObjectLike.getEducationalObjectLikePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The educationalObjectLike with id " + id + " no longer exists.", enfe);
            }
            EducationalObject educationalObject = educationalObjectLike.getEducationalObject();
            if (educationalObject != null) {
                educationalObject.getEducationalObjectLikeCollection().remove(educationalObjectLike);
                educationalObject = em.merge(educationalObject);
            }
            SocialProfile socialProfile = educationalObjectLike.getSocialProfile();
            if (socialProfile != null) {
                socialProfile.getEducationalObjectLikeCollection().remove(educationalObjectLike);
                socialProfile = em.merge(socialProfile);
            }
            em.remove(educationalObjectLike);
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

    public List<EducationalObjectLike> findEducationalObjectLikeEntities() {
        return findEducationalObjectLikeEntities(true, -1, -1);
    }

    public List<EducationalObjectLike> findEducationalObjectLikeEntities(int maxResults, int firstResult) {
        return findEducationalObjectLikeEntities(false, maxResults, firstResult);
    }

    private List<EducationalObjectLike> findEducationalObjectLikeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EducationalObjectLike.class));
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

    public EducationalObjectLike findEducationalObjectLike(EducationalObjectLikePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EducationalObjectLike.class, id);
        } finally {
            em.close();
        }
    }

    public int getEducationalObjectLikeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EducationalObjectLike> rt = cq.from(EducationalObjectLike.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Long findEducationalObjectLikesQuantity(Integer educationalObjectId) {
        EntityManager em = getEntityManager();
        try {
            Long likes = (Long) em.createNativeQuery("select count(*) from educational_object_like "
                    + "where educational_object_fk = '" + educationalObjectId + "'").getSingleResult();
            return likes;
        } finally {
            em.close();
        }
    }
    
}
