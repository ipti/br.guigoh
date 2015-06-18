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
import br.org.ipti.guigoh.model.entity.Scholarity;
import br.org.ipti.guigoh.model.entity.Educations;
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
public class EducationsNameJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();
    
    public EducationsNameJpaController() {

    }

    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EducationsName educationsName) throws RollbackFailureException, Exception {
        if (educationsName.getEducationsCollection() == null) {
            educationsName.setEducationsCollection(new ArrayList<Educations>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Scholarity scholarityId = educationsName.getScholarityId();
            if (scholarityId != null) {
                scholarityId = em.getReference(scholarityId.getClass(), scholarityId.getId());
                educationsName.setScholarityId(scholarityId);
            }
            Collection<Educations> attachedEducationsCollection = new ArrayList<Educations>();
            for (Educations educationsCollectionEducationsToAttach : educationsName.getEducationsCollection()) {
                educationsCollectionEducationsToAttach = em.getReference(educationsCollectionEducationsToAttach.getClass(), educationsCollectionEducationsToAttach.getEducationsPK());
                attachedEducationsCollection.add(educationsCollectionEducationsToAttach);
            }
            educationsName.setEducationsCollection(attachedEducationsCollection);
            em.persist(educationsName);
            if (scholarityId != null) {
                scholarityId.getEducationsNameCollection().add(educationsName);
                scholarityId = em.merge(scholarityId);
            }
            for (Educations educationsCollectionEducations : educationsName.getEducationsCollection()) {
                EducationsName oldNameIdOfEducationsCollectionEducations = educationsCollectionEducations.getNameId();
                educationsCollectionEducations.setNameId(educationsName);
                educationsCollectionEducations = em.merge(educationsCollectionEducations);
                if (oldNameIdOfEducationsCollectionEducations != null) {
                    oldNameIdOfEducationsCollectionEducations.getEducationsCollection().remove(educationsCollectionEducations);
                    oldNameIdOfEducationsCollectionEducations = em.merge(oldNameIdOfEducationsCollectionEducations);
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

    public void edit(EducationsName educationsName) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EducationsName persistentEducationsName = em.find(EducationsName.class, educationsName.getId());
            Scholarity scholarityIdOld = persistentEducationsName.getScholarityId();
            Scholarity scholarityIdNew = educationsName.getScholarityId();
            Collection<Educations> educationsCollectionOld = persistentEducationsName.getEducationsCollection();
            Collection<Educations> educationsCollectionNew = educationsName.getEducationsCollection();
            if (scholarityIdNew != null) {
                scholarityIdNew = em.getReference(scholarityIdNew.getClass(), scholarityIdNew.getId());
                educationsName.setScholarityId(scholarityIdNew);
            }
            Collection<Educations> attachedEducationsCollectionNew = new ArrayList<Educations>();
            for (Educations educationsCollectionNewEducationsToAttach : educationsCollectionNew) {
                educationsCollectionNewEducationsToAttach = em.getReference(educationsCollectionNewEducationsToAttach.getClass(), educationsCollectionNewEducationsToAttach.getEducationsPK());
                attachedEducationsCollectionNew.add(educationsCollectionNewEducationsToAttach);
            }
            educationsCollectionNew = attachedEducationsCollectionNew;
            educationsName.setEducationsCollection(educationsCollectionNew);
            educationsName = em.merge(educationsName);
            if (scholarityIdOld != null && !scholarityIdOld.equals(scholarityIdNew)) {
                scholarityIdOld.getEducationsNameCollection().remove(educationsName);
                scholarityIdOld = em.merge(scholarityIdOld);
            }
            if (scholarityIdNew != null && !scholarityIdNew.equals(scholarityIdOld)) {
                scholarityIdNew.getEducationsNameCollection().add(educationsName);
                scholarityIdNew = em.merge(scholarityIdNew);
            }
            for (Educations educationsCollectionOldEducations : educationsCollectionOld) {
                if (!educationsCollectionNew.contains(educationsCollectionOldEducations)) {
                    educationsCollectionOldEducations.setNameId(null);
                    educationsCollectionOldEducations = em.merge(educationsCollectionOldEducations);
                }
            }
            for (Educations educationsCollectionNewEducations : educationsCollectionNew) {
                if (!educationsCollectionOld.contains(educationsCollectionNewEducations)) {
                    EducationsName oldNameIdOfEducationsCollectionNewEducations = educationsCollectionNewEducations.getNameId();
                    educationsCollectionNewEducations.setNameId(educationsName);
                    educationsCollectionNewEducations = em.merge(educationsCollectionNewEducations);
                    if (oldNameIdOfEducationsCollectionNewEducations != null && !oldNameIdOfEducationsCollectionNewEducations.equals(educationsName)) {
                        oldNameIdOfEducationsCollectionNewEducations.getEducationsCollection().remove(educationsCollectionNewEducations);
                        oldNameIdOfEducationsCollectionNewEducations = em.merge(oldNameIdOfEducationsCollectionNewEducations);
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
                Integer id = educationsName.getId();
                if (findEducationsName(id) == null) {
                    throw new NonexistentEntityException("The educationsName with id " + id + " no longer exists.");
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
            EducationsName educationsName;
            try {
                educationsName = em.getReference(EducationsName.class, id);
                educationsName.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The educationsName with id " + id + " no longer exists.", enfe);
            }
            Scholarity scholarityId = educationsName.getScholarityId();
            if (scholarityId != null) {
                scholarityId.getEducationsNameCollection().remove(educationsName);
                scholarityId = em.merge(scholarityId);
            }
            Collection<Educations> educationsCollection = educationsName.getEducationsCollection();
            for (Educations educationsCollectionEducations : educationsCollection) {
                educationsCollectionEducations.setNameId(null);
                educationsCollectionEducations = em.merge(educationsCollectionEducations);
            }
            em.remove(educationsName);
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

    public List<EducationsName> findEducationsNameEntities() {
        return findEducationsNameEntities(true, -1, -1);
    }

    public List<EducationsName> findEducationsNameEntities(int maxResults, int firstResult) {
        return findEducationsNameEntities(false, maxResults, firstResult);
    }

    private List<EducationsName> findEducationsNameEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EducationsName.class));
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

    public EducationsName findEducationsName(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EducationsName.class, id);
        } finally {
            em.close();
        }
    }

    public int getEducationsNameCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EducationsName> rt = cq.from(EducationsName.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public EducationsName findEducationsNameByName(EducationsName nameId) {
        EntityManager em = getEntityManager();
        try {
            EducationsName educationsNametemp = (EducationsName) em.createNativeQuery("select * from educations_name "
                    + "where (UPPER(name) like '" + nameId.getName().toUpperCase() + "') ", EducationsName.class).getSingleResult();
            if (educationsNametemp == null) {
                return new EducationsName();
            }
            return educationsNametemp;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
}
