/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import br.org.ipti.guigoh.model.jpa.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.exceptions.PreexistingEntityException;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.State;
import br.org.ipti.guigoh.model.entity.ExperiencesLocation;
import br.org.ipti.guigoh.model.entity.Country;
import br.org.ipti.guigoh.model.entity.City;
import br.org.ipti.guigoh.model.entity.Experiences;
import br.org.ipti.guigoh.model.entity.ExperiencesPK;
import br.org.ipti.guigoh.model.entity.Occupations;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Joe
 */
public class ExperiencesJpaController implements Serializable {

     private transient EntityManagerFactory emf = PersistenceUnit.getEMF();
    
    public ExperiencesJpaController() {

    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Experiences experiences) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (experiences.getExperiencesPK() == null) {
            experiences.setExperiencesPK(new ExperiencesPK());
        }
        experiences.getExperiencesPK().setTokenId(experiences.getSocialProfile().getTokenId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SocialProfile socialProfile = experiences.getSocialProfile();
            if (socialProfile != null) {
                socialProfile = em.getReference(socialProfile.getClass(), socialProfile.getTokenId());
                experiences.setSocialProfile(socialProfile);
            }
            State stateId = experiences.getStateId();
            if (stateId != null) {
                stateId = em.getReference(stateId.getClass(), stateId.getId());
                experiences.setStateId(stateId);
            }
            ExperiencesLocation locationId = experiences.getLocationId();
            if (locationId != null) {
                locationId = em.getReference(locationId.getClass(), locationId.getId());
                experiences.setLocationId(locationId);
            }
            Country countryId = experiences.getCountryId();
            if (countryId != null) {
                countryId = em.getReference(countryId.getClass(), countryId.getId());
                experiences.setCountryId(countryId);
            }
            City cityId = experiences.getCityId();
            if (cityId != null) {
                cityId = em.getReference(cityId.getClass(), cityId.getId());
                experiences.setCityId(cityId);
            }
            Occupations nameId = experiences.getNameId();
            if (nameId != null) {
                nameId = em.getReference(nameId.getClass(), nameId.getId());
                experiences.setNameId(nameId);
            }
            em.persist(experiences);
            if (socialProfile != null) {
                socialProfile.getExperiencesCollection().add(experiences);
                socialProfile = em.merge(socialProfile);
            }
            if (stateId != null) {
                stateId.getExperiencesCollection().add(experiences);
                stateId = em.merge(stateId);
            }
            if (locationId != null) {
                locationId.getExperiencesCollection().add(experiences);
                locationId = em.merge(locationId);
            }
            if (countryId != null) {
                countryId.getExperiencesCollection().add(experiences);
                countryId = em.merge(countryId);
            }
            if (cityId != null) {
                cityId.getExperiencesCollection().add(experiences);
                cityId = em.merge(cityId);
            }
            if (nameId != null) {
                nameId.getExperiencesCollection().add(experiences);
                nameId = em.merge(nameId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findExperiences(experiences.getExperiencesPK()) != null) {
                throw new PreexistingEntityException("Experiences " + experiences + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Experiences experiences) throws NonexistentEntityException, RollbackFailureException, Exception {
        experiences.getExperiencesPK().setTokenId(experiences.getSocialProfile().getTokenId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Experiences persistentExperiences = em.find(Experiences.class, experiences.getExperiencesPK());
            SocialProfile socialProfileOld = persistentExperiences.getSocialProfile();
            SocialProfile socialProfileNew = experiences.getSocialProfile();
            State stateIdOld = persistentExperiences.getStateId();
            State stateIdNew = experiences.getStateId();
            ExperiencesLocation locationIdOld = persistentExperiences.getLocationId();
            ExperiencesLocation locationIdNew = experiences.getLocationId();
            Country countryIdOld = persistentExperiences.getCountryId();
            Country countryIdNew = experiences.getCountryId();
            City cityIdOld = persistentExperiences.getCityId();
            City cityIdNew = experiences.getCityId();
            Occupations nameIdOld = persistentExperiences.getNameId();
            Occupations nameIdNew = experiences.getNameId();
            if (socialProfileNew != null) {
                socialProfileNew = em.getReference(socialProfileNew.getClass(), socialProfileNew.getTokenId());
                experiences.setSocialProfile(socialProfileNew);
            }
            if (stateIdNew != null) {
                stateIdNew = em.getReference(stateIdNew.getClass(), stateIdNew.getId());
                experiences.setStateId(stateIdNew);
            }
            if (locationIdNew != null) {
                locationIdNew = em.getReference(locationIdNew.getClass(), locationIdNew.getId());
                experiences.setLocationId(locationIdNew);
            }
            if (countryIdNew != null) {
                countryIdNew = em.getReference(countryIdNew.getClass(), countryIdNew.getId());
                experiences.setCountryId(countryIdNew);
            }
            if (cityIdNew != null) {
                cityIdNew = em.getReference(cityIdNew.getClass(), cityIdNew.getId());
                experiences.setCityId(cityIdNew);
            }
            if (nameIdNew != null) {
                nameIdNew = em.getReference(nameIdNew.getClass(), nameIdNew.getId());
                experiences.setNameId(nameIdNew);
            }
            experiences = em.merge(experiences);
            if (socialProfileOld != null && !socialProfileOld.equals(socialProfileNew)) {
                socialProfileOld.getExperiencesCollection().remove(experiences);
                socialProfileOld = em.merge(socialProfileOld);
            }
            if (socialProfileNew != null && !socialProfileNew.equals(socialProfileOld)) {
                socialProfileNew.getExperiencesCollection().add(experiences);
                socialProfileNew = em.merge(socialProfileNew);
            }
            if (stateIdOld != null && !stateIdOld.equals(stateIdNew)) {
                stateIdOld.getExperiencesCollection().remove(experiences);
                stateIdOld = em.merge(stateIdOld);
            }
            if (stateIdNew != null && !stateIdNew.equals(stateIdOld)) {
                stateIdNew.getExperiencesCollection().add(experiences);
                stateIdNew = em.merge(stateIdNew);
            }
            if (locationIdOld != null && !locationIdOld.equals(locationIdNew)) {
                locationIdOld.getExperiencesCollection().remove(experiences);
                locationIdOld = em.merge(locationIdOld);
            }
            if (locationIdNew != null && !locationIdNew.equals(locationIdOld)) {
                locationIdNew.getExperiencesCollection().add(experiences);
                locationIdNew = em.merge(locationIdNew);
            }
            if (countryIdOld != null && !countryIdOld.equals(countryIdNew)) {
                countryIdOld.getExperiencesCollection().remove(experiences);
                countryIdOld = em.merge(countryIdOld);
            }
            if (countryIdNew != null && !countryIdNew.equals(countryIdOld)) {
                countryIdNew.getExperiencesCollection().add(experiences);
                countryIdNew = em.merge(countryIdNew);
            }
            if (cityIdOld != null && !cityIdOld.equals(cityIdNew)) {
                cityIdOld.getExperiencesCollection().remove(experiences);
                cityIdOld = em.merge(cityIdOld);
            }
            if (cityIdNew != null && !cityIdNew.equals(cityIdOld)) {
                cityIdNew.getExperiencesCollection().add(experiences);
                cityIdNew = em.merge(cityIdNew);
            }
            if (nameIdOld != null && !nameIdOld.equals(nameIdNew)) {
                nameIdOld.getExperiencesCollection().remove(experiences);
                nameIdOld = em.merge(nameIdOld);
            }
            if (nameIdNew != null && !nameIdNew.equals(nameIdOld)) {
                nameIdNew.getExperiencesCollection().add(experiences);
                nameIdNew = em.merge(nameIdNew);
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
                ExperiencesPK id = experiences.getExperiencesPK();
                if (findExperiences(id) == null) {
                    throw new NonexistentEntityException("The experiences with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ExperiencesPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Experiences experiences;
            try {
                experiences = em.getReference(Experiences.class, id);
                experiences.getExperiencesPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The experiences with id " + id + " no longer exists.", enfe);
            }
            SocialProfile socialProfile = experiences.getSocialProfile();
            if (socialProfile != null) {
                socialProfile.getExperiencesCollection().remove(experiences);
                socialProfile = em.merge(socialProfile);
            }
            State stateId = experiences.getStateId();
            if (stateId != null) {
                stateId.getExperiencesCollection().remove(experiences);
                stateId = em.merge(stateId);
            }
            ExperiencesLocation locationId = experiences.getLocationId();
            if (locationId != null) {
                locationId.getExperiencesCollection().remove(experiences);
                locationId = em.merge(locationId);
            }
            Country countryId = experiences.getCountryId();
            if (countryId != null) {
                countryId.getExperiencesCollection().remove(experiences);
                countryId = em.merge(countryId);
            }
            City cityId = experiences.getCityId();
            if (cityId != null) {
                cityId.getExperiencesCollection().remove(experiences);
                cityId = em.merge(cityId);
            }
            Occupations nameId = experiences.getNameId();
            if (nameId != null) {
                nameId.getExperiencesCollection().remove(experiences);
                nameId = em.merge(nameId);
            }
            em.remove(experiences);
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

    public List<Experiences> findExperiencesEntities() {
        return findExperiencesEntities(true, -1, -1);
    }

    public List<Experiences> findExperiencesEntities(int maxResults, int firstResult) {
        return findExperiencesEntities(false, maxResults, firstResult);
    }

    private List<Experiences> findExperiencesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Experiences.class));
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

    public Experiences findExperiences(ExperiencesPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Experiences.class, id);
        } finally {
            em.close();
        }
    }

    public int getExperiencesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Experiences> rt = cq.from(Experiences.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Experiences> findExperiencesByTokenId(String token_id) {
        EntityManager em = getEntityManager();
        try {
            List<Experiences> formationsList = (List<Experiences>) em.createNativeQuery("select * from experiences "
                    + "where token_id = '" + token_id + "'", Experiences.class).getResultList();
            return formationsList;
        } finally {
            em.close();
        }
    }
    
    public void createInsert(Experiences experiences) throws RollbackFailureException, Exception {
        if (experiences.getExperiencesPK() == null) {
            experiences.setExperiencesPK(new ExperiencesPK());
        }
        experiences.getExperiencesPK().setTokenId(experiences.getSocialProfile().getTokenId());
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            String sql = "INSERT INTO experiences (token_id, name_id, data_begin, data_end, location_id, country_id, state_id, city_id) "
                    + "VALUES(?1,?2,?3,?4,?5,?6,?7,null)";

            Query query = em.createNativeQuery(sql);
            query.setParameter(1, experiences.getExperiencesPK().getTokenId());
            query.setParameter(2, experiences.getNameId().getId());
            query.setParameter(3, experiences.getDataBegin());
            query.setParameter(4, experiences.getDataEnd());
            query.setParameter(5, experiences.getLocationId().getId());
            query.setParameter(6, experiences.getCountryId().getId());
            query.setParameter(7, experiences.getStateId().getId());
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
