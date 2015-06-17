/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.jpa.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.EducationalObjectMedia;
import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author ipti008
 */
public class EducationalObjectMediaJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();
    
    public EducationalObjectMediaJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EducationalObjectMedia educationalObjectMedia) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EducationalObject educationalObjectId = educationalObjectMedia.getEducationalObjectId();
            if (educationalObjectId != null) {
                educationalObjectId = em.getReference(educationalObjectId.getClass(), educationalObjectId.getId());
                educationalObjectMedia.setEducationalObjectId(educationalObjectId);
            }
            em.persist(educationalObjectMedia);
            if (educationalObjectId != null) {
                educationalObjectId.getEducationalObjectMediaCollection().add(educationalObjectMedia);
                educationalObjectId = em.merge(educationalObjectId);
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

    public void edit(EducationalObjectMedia educationalObjectMedia) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EducationalObjectMedia persistentEducationalObjectMedia = em.find(EducationalObjectMedia.class, educationalObjectMedia.getId());
            EducationalObject educationalObjectIdOld = persistentEducationalObjectMedia.getEducationalObjectId();
            EducationalObject educationalObjectIdNew = educationalObjectMedia.getEducationalObjectId();
            if (educationalObjectIdNew != null) {
                educationalObjectIdNew = em.getReference(educationalObjectIdNew.getClass(), educationalObjectIdNew.getId());
                educationalObjectMedia.setEducationalObjectId(educationalObjectIdNew);
            }
            educationalObjectMedia = em.merge(educationalObjectMedia);
            if (educationalObjectIdOld != null && !educationalObjectIdOld.equals(educationalObjectIdNew)) {
                educationalObjectIdOld.getEducationalObjectMediaCollection().remove(educationalObjectMedia);
                educationalObjectIdOld = em.merge(educationalObjectIdOld);
            }
            if (educationalObjectIdNew != null && !educationalObjectIdNew.equals(educationalObjectIdOld)) {
                educationalObjectIdNew.getEducationalObjectMediaCollection().add(educationalObjectMedia);
                educationalObjectIdNew = em.merge(educationalObjectIdNew);
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
                Integer id = educationalObjectMedia.getId();
                if (findEducationalObjectMedia(id) == null) {
                    throw new NonexistentEntityException("The educationalObjectMedia with id " + id + " no longer exists.");
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
            EducationalObjectMedia educationalObjectMedia;
            try {
                educationalObjectMedia = em.getReference(EducationalObjectMedia.class, id);
                educationalObjectMedia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The educationalObjectMedia with id " + id + " no longer exists.", enfe);
            }
            EducationalObject educationalObjectId = educationalObjectMedia.getEducationalObjectId();
            if (educationalObjectId != null) {
                educationalObjectId.getEducationalObjectMediaCollection().remove(educationalObjectMedia);
                educationalObjectId = em.merge(educationalObjectId);
            }
            em.remove(educationalObjectMedia);
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

    public List<EducationalObjectMedia> findEducationalObjectMediaEntities() {
        return findEducationalObjectMediaEntities(true, -1, -1);
    }

    public List<EducationalObjectMedia> findEducationalObjectMediaEntities(int maxResults, int firstResult) {
        return findEducationalObjectMediaEntities(false, maxResults, firstResult);
    }

    private List<EducationalObjectMedia> findEducationalObjectMediaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EducationalObjectMedia.class));
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

    public EducationalObjectMedia findEducationalObjectMedia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EducationalObjectMedia.class, id);
        } finally {
            em.close();
        }
    }

    public int getEducationalObjectMediaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EducationalObjectMedia> rt = cq.from(EducationalObjectMedia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<EducationalObjectMedia> getMediasByEducationalObject(Integer educationalObjectID){
        EntityManager em = getEntityManager();
        try{
            List<EducationalObjectMedia> educationalObjectMediaList = (List<EducationalObjectMedia>)em.createNativeQuery("select * from educational_object_media eom"
                    + " where educational_object_id = " + educationalObjectID, EducationalObjectMedia.class).getResultList();
            return educationalObjectMediaList;
        } finally{
            em.close();
        }
    }
}
