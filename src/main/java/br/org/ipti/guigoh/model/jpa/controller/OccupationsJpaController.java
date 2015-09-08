/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import java.util.ArrayList;
import java.util.Collection;
import br.org.ipti.guigoh.model.entity.Experiences;
import br.org.ipti.guigoh.model.entity.Occupations;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

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
            Collection<SocialProfile> socialProfileCollectionOld = persistentOccupations.getSocialProfileCollection();
            Collection<SocialProfile> socialProfileCollectionNew = occupations.getSocialProfileCollection();
            Collection<Experiences> experiencesCollectionOld = persistentOccupations.getExperiencesCollection();
            Collection<Experiences> experiencesCollectionNew = occupations.getExperiencesCollection();
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

    public Occupations findOccupationByName(String name) {
        EntityManager em = getEntityManager();
        try {
            Occupations occupation = (Occupations) em.createNativeQuery("select * from occupations "
                    + "where (UPPER(name) like '" + name.toUpperCase() + "') ", Occupations.class).getSingleResult();
            return occupation;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
     
}
