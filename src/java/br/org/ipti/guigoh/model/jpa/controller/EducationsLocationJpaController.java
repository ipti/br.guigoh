/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import br.org.ipti.guigoh.model.jpa.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.org.ipti.guigoh.model.entity.Educations;
import br.org.ipti.guigoh.model.entity.EducationsLocation;
import br.org.ipti.guigoh.model.entity.EducationsName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.transaction.UserTransaction;

/**
 *
 * @author Joe
 */
public class EducationsLocationJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();
    
    public EducationsLocationJpaController() {

    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EducationsLocation educationsLocation) throws RollbackFailureException, Exception {
        if (educationsLocation.getEducationsCollection() == null) {
            educationsLocation.setEducationsCollection(new ArrayList<Educations>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Educations> attachedEducationsCollection = new ArrayList<Educations>();
            for (Educations educationsCollectionEducationsToAttach : educationsLocation.getEducationsCollection()) {
                educationsCollectionEducationsToAttach = em.getReference(educationsCollectionEducationsToAttach.getClass(), educationsCollectionEducationsToAttach.getEducationsPK());
                attachedEducationsCollection.add(educationsCollectionEducationsToAttach);
            }
            educationsLocation.setEducationsCollection(attachedEducationsCollection);
            em.persist(educationsLocation);
            for (Educations educationsCollectionEducations : educationsLocation.getEducationsCollection()) {
                EducationsLocation oldLocationIdOfEducationsCollectionEducations = educationsCollectionEducations.getLocationId();
                educationsCollectionEducations.setLocationId(educationsLocation);
                educationsCollectionEducations = em.merge(educationsCollectionEducations);
                if (oldLocationIdOfEducationsCollectionEducations != null) {
                    oldLocationIdOfEducationsCollectionEducations.getEducationsCollection().remove(educationsCollectionEducations);
                    oldLocationIdOfEducationsCollectionEducations = em.merge(oldLocationIdOfEducationsCollectionEducations);
                }
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

    public void edit(EducationsLocation educationsLocation) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EducationsLocation persistentEducationsLocation = em.find(EducationsLocation.class, educationsLocation.getId());
            Collection<Educations> educationsCollectionOld = persistentEducationsLocation.getEducationsCollection();
            Collection<Educations> educationsCollectionNew = educationsLocation.getEducationsCollection();
            Collection<Educations> attachedEducationsCollectionNew = new ArrayList<Educations>();
            for (Educations educationsCollectionNewEducationsToAttach : educationsCollectionNew) {
                educationsCollectionNewEducationsToAttach = em.getReference(educationsCollectionNewEducationsToAttach.getClass(), educationsCollectionNewEducationsToAttach.getEducationsPK());
                attachedEducationsCollectionNew.add(educationsCollectionNewEducationsToAttach);
            }
            educationsCollectionNew = attachedEducationsCollectionNew;
            educationsLocation.setEducationsCollection(educationsCollectionNew);
            educationsLocation = em.merge(educationsLocation);
            for (Educations educationsCollectionOldEducations : educationsCollectionOld) {
                if (!educationsCollectionNew.contains(educationsCollectionOldEducations)) {
                    educationsCollectionOldEducations.setLocationId(null);
                    educationsCollectionOldEducations = em.merge(educationsCollectionOldEducations);
                }
            }
            for (Educations educationsCollectionNewEducations : educationsCollectionNew) {
                if (!educationsCollectionOld.contains(educationsCollectionNewEducations)) {
                    EducationsLocation oldLocationIdOfEducationsCollectionNewEducations = educationsCollectionNewEducations.getLocationId();
                    educationsCollectionNewEducations.setLocationId(educationsLocation);
                    educationsCollectionNewEducations = em.merge(educationsCollectionNewEducations);
                    if (oldLocationIdOfEducationsCollectionNewEducations != null && !oldLocationIdOfEducationsCollectionNewEducations.equals(educationsLocation)) {
                        oldLocationIdOfEducationsCollectionNewEducations.getEducationsCollection().remove(educationsCollectionNewEducations);
                        oldLocationIdOfEducationsCollectionNewEducations = em.merge(oldLocationIdOfEducationsCollectionNewEducations);
                    }
                }
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
                Integer id = educationsLocation.getId();
                if (findEducationsLocation(id) == null) {
                    throw new NonexistentEntityException("The educationsLocation with id " + id + " no longer exists.");
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
            EducationsLocation educationsLocation;
            try {
                educationsLocation = em.getReference(EducationsLocation.class, id);
                educationsLocation.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The educationsLocation with id " + id + " no longer exists.", enfe);
            }
            Collection<Educations> educationsCollection = educationsLocation.getEducationsCollection();
            for (Educations educationsCollectionEducations : educationsCollection) {
                educationsCollectionEducations.setLocationId(null);
                educationsCollectionEducations = em.merge(educationsCollectionEducations);
            }
            em.remove(educationsLocation);
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

    public List<EducationsLocation> findEducationsLocationEntities() {
        return findEducationsLocationEntities(true, -1, -1);
    }

    public List<EducationsLocation> findEducationsLocationEntities(int maxResults, int firstResult) {
        return findEducationsLocationEntities(false, maxResults, firstResult);
    }

    private List<EducationsLocation> findEducationsLocationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EducationsLocation.class));
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

    public EducationsLocation findEducationsLocation(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EducationsLocation.class, id);
        } finally {
            em.close();
        }
    }

    public int getEducationsLocationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EducationsLocation> rt = cq.from(EducationsLocation.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public EducationsLocation findEducationsByName(EducationsLocation locationId) {
        EntityManager em = getEntityManager();
        try {
            EducationsLocation educationsLocationtemp = (EducationsLocation) em.createNativeQuery("select * from educations_location "
                    + "where (UPPER(name) like '" + locationId.getName().toUpperCase() + "') ", EducationsLocation.class).getSingleResult();
            if (educationsLocationtemp == null) {
                return new EducationsLocation();
            }
            return educationsLocationtemp;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
}
