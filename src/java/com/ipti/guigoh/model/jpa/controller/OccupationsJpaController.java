/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipti.guigoh.model.jpa.controller;

import com.ipti.guigoh.model.jpa.util.PersistenceUnit;
import com.ipti.guigoh.model.jpa.exceptions.NonexistentEntityException;
import com.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.ipti.guigoh.model.entity.OccupationsType;
import com.ipti.guigoh.model.entity.SocialProfile;
import java.util.ArrayList;
import java.util.Collection;
import com.ipti.guigoh.model.entity.Experiences;
import com.ipti.guigoh.model.entity.Occupations;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.transaction.UserTransaction;

/**
 *
 * @author Joe
 */
public class OccupationsJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();

    public OccupationsJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Occupations occupations) throws RollbackFailureException, Exception {
        if (occupations.getSocialProfileCollection() == null) {
            occupations.setSocialProfileCollection(new ArrayList<SocialProfile>());
        }
        if (occupations.getExperiencesCollection() == null) {
            occupations.setExperiencesCollection(new ArrayList<Experiences>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OccupationsType occupationsTypeId = occupations.getOccupationsTypeId();
            if (occupationsTypeId != null) {
                occupationsTypeId = em.getReference(occupationsTypeId.getClass(), occupationsTypeId.getId());
                occupations.setOccupationsTypeId(occupationsTypeId);
            }
            Collection<SocialProfile> attachedSocialProfileCollection = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionSocialProfileToAttach : occupations.getSocialProfileCollection()) {
                socialProfileCollectionSocialProfileToAttach = em.getReference(socialProfileCollectionSocialProfileToAttach.getClass(), socialProfileCollectionSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollection.add(socialProfileCollectionSocialProfileToAttach);
            }
            occupations.setSocialProfileCollection(attachedSocialProfileCollection);
            Collection<Experiences> attachedExperiencesCollection = new ArrayList<Experiences>();
            for (Experiences experiencesCollectionExperiencesToAttach : occupations.getExperiencesCollection()) {
                experiencesCollectionExperiencesToAttach = em.getReference(experiencesCollectionExperiencesToAttach.getClass(), experiencesCollectionExperiencesToAttach.getExperiencesPK());
                attachedExperiencesCollection.add(experiencesCollectionExperiencesToAttach);
            }
            occupations.setExperiencesCollection(attachedExperiencesCollection);
            em.persist(occupations);
            if (occupationsTypeId != null) {
                occupationsTypeId.getOccupationsCollection().add(occupations);
                occupationsTypeId = em.merge(occupationsTypeId);
            }
            for (SocialProfile socialProfileCollectionSocialProfile : occupations.getSocialProfileCollection()) {
                Occupations oldOccupationsIdOfSocialProfileCollectionSocialProfile = socialProfileCollectionSocialProfile.getOccupationsId();
                socialProfileCollectionSocialProfile.setOccupationsId(occupations);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
                if (oldOccupationsIdOfSocialProfileCollectionSocialProfile != null) {
                    oldOccupationsIdOfSocialProfileCollectionSocialProfile.getSocialProfileCollection().remove(socialProfileCollectionSocialProfile);
                    oldOccupationsIdOfSocialProfileCollectionSocialProfile = em.merge(oldOccupationsIdOfSocialProfileCollectionSocialProfile);
                }
            }
            for (Experiences experiencesCollectionExperiences : occupations.getExperiencesCollection()) {
                Occupations oldNameIdOfExperiencesCollectionExperiences = experiencesCollectionExperiences.getNameId();
                experiencesCollectionExperiences.setNameId(occupations);
                experiencesCollectionExperiences = em.merge(experiencesCollectionExperiences);
                if (oldNameIdOfExperiencesCollectionExperiences != null) {
                    oldNameIdOfExperiencesCollectionExperiences.getExperiencesCollection().remove(experiencesCollectionExperiences);
                    oldNameIdOfExperiencesCollectionExperiences = em.merge(oldNameIdOfExperiencesCollectionExperiences);
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

    public void edit(Occupations occupations) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Occupations persistentOccupations = em.find(Occupations.class, occupations.getId());
            OccupationsType occupationsTypeIdOld = persistentOccupations.getOccupationsTypeId();
            OccupationsType occupationsTypeIdNew = occupations.getOccupationsTypeId();
            Collection<SocialProfile> socialProfileCollectionOld = persistentOccupations.getSocialProfileCollection();
            Collection<SocialProfile> socialProfileCollectionNew = occupations.getSocialProfileCollection();
            Collection<Experiences> experiencesCollectionOld = persistentOccupations.getExperiencesCollection();
            Collection<Experiences> experiencesCollectionNew = occupations.getExperiencesCollection();
            if (occupationsTypeIdNew != null) {
                occupationsTypeIdNew = em.getReference(occupationsTypeIdNew.getClass(), occupationsTypeIdNew.getId());
                occupations.setOccupationsTypeId(occupationsTypeIdNew);
            }
            Collection<SocialProfile> attachedSocialProfileCollectionNew = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionNewSocialProfileToAttach : socialProfileCollectionNew) {
                socialProfileCollectionNewSocialProfileToAttach = em.getReference(socialProfileCollectionNewSocialProfileToAttach.getClass(), socialProfileCollectionNewSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollectionNew.add(socialProfileCollectionNewSocialProfileToAttach);
            }
            socialProfileCollectionNew = attachedSocialProfileCollectionNew;
            occupations.setSocialProfileCollection(socialProfileCollectionNew);
            Collection<Experiences> attachedExperiencesCollectionNew = new ArrayList<Experiences>();
            for (Experiences experiencesCollectionNewExperiencesToAttach : experiencesCollectionNew) {
                experiencesCollectionNewExperiencesToAttach = em.getReference(experiencesCollectionNewExperiencesToAttach.getClass(), experiencesCollectionNewExperiencesToAttach.getExperiencesPK());
                attachedExperiencesCollectionNew.add(experiencesCollectionNewExperiencesToAttach);
            }
            experiencesCollectionNew = attachedExperiencesCollectionNew;
            occupations.setExperiencesCollection(experiencesCollectionNew);
            occupations = em.merge(occupations);
            if (occupationsTypeIdOld != null && !occupationsTypeIdOld.equals(occupationsTypeIdNew)) {
                occupationsTypeIdOld.getOccupationsCollection().remove(occupations);
                occupationsTypeIdOld = em.merge(occupationsTypeIdOld);
            }
            if (occupationsTypeIdNew != null && !occupationsTypeIdNew.equals(occupationsTypeIdOld)) {
                occupationsTypeIdNew.getOccupationsCollection().add(occupations);
                occupationsTypeIdNew = em.merge(occupationsTypeIdNew);
            }
            for (SocialProfile socialProfileCollectionOldSocialProfile : socialProfileCollectionOld) {
                if (!socialProfileCollectionNew.contains(socialProfileCollectionOldSocialProfile)) {
                    socialProfileCollectionOldSocialProfile.setOccupationsId(null);
                    socialProfileCollectionOldSocialProfile = em.merge(socialProfileCollectionOldSocialProfile);
                }
            }
            for (SocialProfile socialProfileCollectionNewSocialProfile : socialProfileCollectionNew) {
                if (!socialProfileCollectionOld.contains(socialProfileCollectionNewSocialProfile)) {
                    Occupations oldOccupationsIdOfSocialProfileCollectionNewSocialProfile = socialProfileCollectionNewSocialProfile.getOccupationsId();
                    socialProfileCollectionNewSocialProfile.setOccupationsId(occupations);
                    socialProfileCollectionNewSocialProfile = em.merge(socialProfileCollectionNewSocialProfile);
                    if (oldOccupationsIdOfSocialProfileCollectionNewSocialProfile != null && !oldOccupationsIdOfSocialProfileCollectionNewSocialProfile.equals(occupations)) {
                        oldOccupationsIdOfSocialProfileCollectionNewSocialProfile.getSocialProfileCollection().remove(socialProfileCollectionNewSocialProfile);
                        oldOccupationsIdOfSocialProfileCollectionNewSocialProfile = em.merge(oldOccupationsIdOfSocialProfileCollectionNewSocialProfile);
                    }
                }
            }
            for (Experiences experiencesCollectionOldExperiences : experiencesCollectionOld) {
                if (!experiencesCollectionNew.contains(experiencesCollectionOldExperiences)) {
                    experiencesCollectionOldExperiences.setNameId(null);
                    experiencesCollectionOldExperiences = em.merge(experiencesCollectionOldExperiences);
                }
            }
            for (Experiences experiencesCollectionNewExperiences : experiencesCollectionNew) {
                if (!experiencesCollectionOld.contains(experiencesCollectionNewExperiences)) {
                    Occupations oldNameIdOfExperiencesCollectionNewExperiences = experiencesCollectionNewExperiences.getNameId();
                    experiencesCollectionNewExperiences.setNameId(occupations);
                    experiencesCollectionNewExperiences = em.merge(experiencesCollectionNewExperiences);
                    if (oldNameIdOfExperiencesCollectionNewExperiences != null && !oldNameIdOfExperiencesCollectionNewExperiences.equals(occupations)) {
                        oldNameIdOfExperiencesCollectionNewExperiences.getExperiencesCollection().remove(experiencesCollectionNewExperiences);
                        oldNameIdOfExperiencesCollectionNewExperiences = em.merge(oldNameIdOfExperiencesCollectionNewExperiences);
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
                Integer id = occupations.getId();
                if (findOccupations(id) == null) {
                    throw new NonexistentEntityException("The occupations with id " + id + " no longer exists.");
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
            Occupations occupations;
            try {
                occupations = em.getReference(Occupations.class, id);
                occupations.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The occupations with id " + id + " no longer exists.", enfe);
            }
            OccupationsType occupationsTypeId = occupations.getOccupationsTypeId();
            if (occupationsTypeId != null) {
                occupationsTypeId.getOccupationsCollection().remove(occupations);
                occupationsTypeId = em.merge(occupationsTypeId);
            }
            Collection<SocialProfile> socialProfileCollection = occupations.getSocialProfileCollection();
            for (SocialProfile socialProfileCollectionSocialProfile : socialProfileCollection) {
                socialProfileCollectionSocialProfile.setOccupationsId(null);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
            }
            Collection<Experiences> experiencesCollection = occupations.getExperiencesCollection();
            for (Experiences experiencesCollectionExperiences : experiencesCollection) {
                experiencesCollectionExperiences.setNameId(null);
                experiencesCollectionExperiences = em.merge(experiencesCollectionExperiences);
            }
            em.remove(occupations);
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

    public List<Occupations> findOccupationsEntities() {
        return findOccupationsEntities(true, -1, -1);
    }

    public List<Occupations> findOccupationsEntities(int maxResults, int firstResult) {
        return findOccupationsEntities(false, maxResults, firstResult);
    }

    private List<Occupations> findOccupationsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Occupations.class));
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

    public Occupations findOccupations(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Occupations.class, id);
        } finally {
            em.close();
        }
    }

    public int getOccupationsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Occupations> rt = cq.from(Occupations.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Occupations> findOccupationsByType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            List<Occupations> cityList = (List<Occupations>) em.createNativeQuery("select * from occupations "
                    + "where  occupations_type_id = " + id, Occupations.class).getResultList();
            return cityList;
        } finally {
            em.close();
        }
    }

    public Occupations findOccupationsByName(Occupations occupations) {
        EntityManager em = getEntityManager();
        try {
            if (occupations.getName() == null){
                 return new Occupations();
            }
            Occupations occupationstemp = (Occupations) em.createNativeQuery("select * from occupations "
                    + "where (UPPER(name) like '" + occupations.getName().toUpperCase() + "') ", Occupations.class).getSingleResult();
            if (occupationstemp == null) {
                return new Occupations();
            }
            return occupationstemp;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
     public Occupations findOccupationsByNameByType(Occupations occupations) {
        EntityManager em = getEntityManager();
        try {
            if (occupations.getName() == null){
                 return new Occupations();
            }
            String otemp = occupations.getName().toUpperCase().replaceAll(" ", "");
            List<Occupations> occupationstemp = (List<Occupations>) em.createNativeQuery("select * from occupations "
                    + "where UPPER(regexp_replace(name,'\\s*', '', 'g')) like '" + otemp + "' and occupations_type_id = "+ occupations.getOccupationsTypeId().getId(), Occupations.class).getResultList();
            if (occupationstemp.isEmpty()) {
                return new Occupations();
            }
            return occupationstemp.get(0);
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
     
     public void createInsert(Occupations occupations) throws RollbackFailureException, Exception {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            String sql = "INSERT INTO occupations( name, occupations_type_id) "
                    + "VALUES( ?1, ?2)";

            Query query = em.createNativeQuery(sql);
            query.setParameter(1, occupations.getName());
            query.setParameter(2, occupations.getOccupationsTypeId().getId());
            query.executeUpdate();
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
}
