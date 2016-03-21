/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.model.entity.City;
import br.org.ipti.guigoh.model.entity.Educations;
import br.org.ipti.guigoh.model.entity.Experiences;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.org.ipti.guigoh.model.entity.State;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.Subnetwork;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Joe
 */
public class CityJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();
    
    public CityJpaController() {

    }
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(City city) throws RollbackFailureException, Exception {
        if (city.getSocialProfileCollection() == null) {
            city.setSocialProfileCollection(new ArrayList<SocialProfile>());
        }
        if (city.getExperiencesCollection() == null) {
            city.setExperiencesCollection(new ArrayList<Experiences>());
        }
        if (city.getEducationsCollection() == null) {
            city.setEducationsCollection(new ArrayList<Educations>());
        }
        if (city.getSubnetworkCollection() == null) {
            city.setSubnetworkCollection(new ArrayList<Subnetwork>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();em.getTransaction().begin();
            State stateId = city.getStateId();
            if (stateId != null) {
                stateId = em.getReference(stateId.getClass(), stateId.getId());
                city.setStateId(stateId);
            }
            Collection<SocialProfile> attachedSocialProfileCollection = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionSocialProfileToAttach : city.getSocialProfileCollection()) {
                socialProfileCollectionSocialProfileToAttach = em.getReference(socialProfileCollectionSocialProfileToAttach.getClass(), socialProfileCollectionSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollection.add(socialProfileCollectionSocialProfileToAttach);
            }
            city.setSocialProfileCollection(attachedSocialProfileCollection);
            Collection<Experiences> attachedExperiencesCollection = new ArrayList<Experiences>();
            for (Experiences experiencesCollectionExperiencesToAttach : city.getExperiencesCollection()) {
                experiencesCollectionExperiencesToAttach = em.getReference(experiencesCollectionExperiencesToAttach.getClass(), experiencesCollectionExperiencesToAttach.getExperiencesPK());
                attachedExperiencesCollection.add(experiencesCollectionExperiencesToAttach);
            }
            city.setExperiencesCollection(attachedExperiencesCollection);
            Collection<Educations> attachedEducationsCollection = new ArrayList<Educations>();
            for (Educations educationsCollectionEducationsToAttach : city.getEducationsCollection()) {
                educationsCollectionEducationsToAttach = em.getReference(educationsCollectionEducationsToAttach.getClass(), educationsCollectionEducationsToAttach.getEducationsPK());
                attachedEducationsCollection.add(educationsCollectionEducationsToAttach);
            }
            city.setEducationsCollection(attachedEducationsCollection);
            Collection<Subnetwork> attachedSubnetworkCollection = new ArrayList<Subnetwork>();
            for (Subnetwork subnetworkCollectionSubnetworkToAttach : city.getSubnetworkCollection()) {
                subnetworkCollectionSubnetworkToAttach = em.getReference(subnetworkCollectionSubnetworkToAttach.getClass(), subnetworkCollectionSubnetworkToAttach.getId());
                attachedSubnetworkCollection.add(subnetworkCollectionSubnetworkToAttach);
            }
            city.setSubnetworkCollection(attachedSubnetworkCollection);
            em.persist(city);
            if (stateId != null) {
                stateId.getCityCollection().add(city);
                stateId = em.merge(stateId);
            }
            for (SocialProfile socialProfileCollectionSocialProfile : city.getSocialProfileCollection()) {
                City oldCityIdOfSocialProfileCollectionSocialProfile = socialProfileCollectionSocialProfile.getCityId();
                socialProfileCollectionSocialProfile.setCityId(city);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
                if (oldCityIdOfSocialProfileCollectionSocialProfile != null) {
                    oldCityIdOfSocialProfileCollectionSocialProfile.getSocialProfileCollection().remove(socialProfileCollectionSocialProfile);
                    oldCityIdOfSocialProfileCollectionSocialProfile = em.merge(oldCityIdOfSocialProfileCollectionSocialProfile);
                }
            }
            for (Experiences experiencesCollectionExperiences : city.getExperiencesCollection()) {
                City oldCityIdOfExperiencesCollectionExperiences = experiencesCollectionExperiences.getCityId();
                experiencesCollectionExperiences.setCityId(city);
                experiencesCollectionExperiences = em.merge(experiencesCollectionExperiences);
                if (oldCityIdOfExperiencesCollectionExperiences != null) {
                    oldCityIdOfExperiencesCollectionExperiences.getExperiencesCollection().remove(experiencesCollectionExperiences);
                    oldCityIdOfExperiencesCollectionExperiences = em.merge(oldCityIdOfExperiencesCollectionExperiences);
                }
            }
            for (Educations educationsCollectionEducations : city.getEducationsCollection()) {
                City oldCityIdOfEducationsCollectionEducations = educationsCollectionEducations.getCityId();
                educationsCollectionEducations.setCityId(city);
                educationsCollectionEducations = em.merge(educationsCollectionEducations);
                if (oldCityIdOfEducationsCollectionEducations != null) {
                    oldCityIdOfEducationsCollectionEducations.getEducationsCollection().remove(educationsCollectionEducations);
                    oldCityIdOfEducationsCollectionEducations = em.merge(oldCityIdOfEducationsCollectionEducations);
                }
            }
            for (Subnetwork subnetworkCollectionSubnetwork : city.getSubnetworkCollection()) {
                City oldCityFkOfSubnetworkCollectionSubnetwork = subnetworkCollectionSubnetwork.getCityFk();
                subnetworkCollectionSubnetwork.setCityFk(city);
                subnetworkCollectionSubnetwork = em.merge(subnetworkCollectionSubnetwork);
                if (oldCityFkOfSubnetworkCollectionSubnetwork != null) {
                    oldCityFkOfSubnetworkCollectionSubnetwork.getSubnetworkCollection().remove(subnetworkCollectionSubnetwork);
                    oldCityFkOfSubnetworkCollectionSubnetwork = em.merge(oldCityFkOfSubnetworkCollectionSubnetwork);
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

    public void edit(City city) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();em.getTransaction().begin();
            City persistentCity = em.find(City.class, city.getId());
            State stateIdOld = persistentCity.getStateId();
            State stateIdNew = city.getStateId();
            Collection<SocialProfile> socialProfileCollectionOld = persistentCity.getSocialProfileCollection();
            Collection<SocialProfile> socialProfileCollectionNew = city.getSocialProfileCollection();
            Collection<Experiences> experiencesCollectionOld = persistentCity.getExperiencesCollection();
            Collection<Experiences> experiencesCollectionNew = city.getExperiencesCollection();
            Collection<Educations> educationsCollectionOld = persistentCity.getEducationsCollection();
            Collection<Educations> educationsCollectionNew = city.getEducationsCollection();
            Collection<Subnetwork> subnetworkCollectionOld = persistentCity.getSubnetworkCollection();
            Collection<Subnetwork> subnetworkCollectionNew = city.getSubnetworkCollection();
            if (stateIdNew != null) {
                stateIdNew = em.getReference(stateIdNew.getClass(), stateIdNew.getId());
                city.setStateId(stateIdNew);
            }
            Collection<SocialProfile> attachedSocialProfileCollectionNew = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionNewSocialProfileToAttach : socialProfileCollectionNew) {
                socialProfileCollectionNewSocialProfileToAttach = em.getReference(socialProfileCollectionNewSocialProfileToAttach.getClass(), socialProfileCollectionNewSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollectionNew.add(socialProfileCollectionNewSocialProfileToAttach);
            }
            socialProfileCollectionNew = attachedSocialProfileCollectionNew;
            city.setSocialProfileCollection(socialProfileCollectionNew);
            Collection<Experiences> attachedExperiencesCollectionNew = new ArrayList<Experiences>();
            for (Experiences experiencesCollectionNewExperiencesToAttach : experiencesCollectionNew) {
                experiencesCollectionNewExperiencesToAttach = em.getReference(experiencesCollectionNewExperiencesToAttach.getClass(), experiencesCollectionNewExperiencesToAttach.getExperiencesPK());
                attachedExperiencesCollectionNew.add(experiencesCollectionNewExperiencesToAttach);
            }
            experiencesCollectionNew = attachedExperiencesCollectionNew;
            city.setExperiencesCollection(experiencesCollectionNew);
            Collection<Educations> attachedEducationsCollectionNew = new ArrayList<Educations>();
            for (Educations educationsCollectionNewEducationsToAttach : educationsCollectionNew) {
                educationsCollectionNewEducationsToAttach = em.getReference(educationsCollectionNewEducationsToAttach.getClass(), educationsCollectionNewEducationsToAttach.getEducationsPK());
                attachedEducationsCollectionNew.add(educationsCollectionNewEducationsToAttach);
            }
            educationsCollectionNew = attachedEducationsCollectionNew;
            city.setEducationsCollection(educationsCollectionNew);
            Collection<Subnetwork> attachedSubnetworkCollectionNew = new ArrayList<Subnetwork>();
            for (Subnetwork subnetworkCollectionNewSubnetworkToAttach : subnetworkCollectionNew) {
                subnetworkCollectionNewSubnetworkToAttach = em.getReference(subnetworkCollectionNewSubnetworkToAttach.getClass(), subnetworkCollectionNewSubnetworkToAttach.getId());
                attachedSubnetworkCollectionNew.add(subnetworkCollectionNewSubnetworkToAttach);
            }
            subnetworkCollectionNew = attachedSubnetworkCollectionNew;
            city.setSubnetworkCollection(subnetworkCollectionNew);
            city = em.merge(city);
            if (stateIdOld != null && !stateIdOld.equals(stateIdNew)) {
                stateIdOld.getCityCollection().remove(city);
                stateIdOld = em.merge(stateIdOld);
            }
            if (stateIdNew != null && !stateIdNew.equals(stateIdOld)) {
                stateIdNew.getCityCollection().add(city);
                stateIdNew = em.merge(stateIdNew);
            }
            for (SocialProfile socialProfileCollectionOldSocialProfile : socialProfileCollectionOld) {
                if (!socialProfileCollectionNew.contains(socialProfileCollectionOldSocialProfile)) {
                    socialProfileCollectionOldSocialProfile.setCityId(null);
                    socialProfileCollectionOldSocialProfile = em.merge(socialProfileCollectionOldSocialProfile);
                }
            }
            for (SocialProfile socialProfileCollectionNewSocialProfile : socialProfileCollectionNew) {
                if (!socialProfileCollectionOld.contains(socialProfileCollectionNewSocialProfile)) {
                    City oldCityIdOfSocialProfileCollectionNewSocialProfile = socialProfileCollectionNewSocialProfile.getCityId();
                    socialProfileCollectionNewSocialProfile.setCityId(city);
                    socialProfileCollectionNewSocialProfile = em.merge(socialProfileCollectionNewSocialProfile);
                    if (oldCityIdOfSocialProfileCollectionNewSocialProfile != null && !oldCityIdOfSocialProfileCollectionNewSocialProfile.equals(city)) {
                        oldCityIdOfSocialProfileCollectionNewSocialProfile.getSocialProfileCollection().remove(socialProfileCollectionNewSocialProfile);
                        oldCityIdOfSocialProfileCollectionNewSocialProfile = em.merge(oldCityIdOfSocialProfileCollectionNewSocialProfile);
                    }
                }
            }
            for (Experiences experiencesCollectionOldExperiences : experiencesCollectionOld) {
                if (!experiencesCollectionNew.contains(experiencesCollectionOldExperiences)) {
                    experiencesCollectionOldExperiences.setCityId(null);
                    experiencesCollectionOldExperiences = em.merge(experiencesCollectionOldExperiences);
                }
            }
            for (Experiences experiencesCollectionNewExperiences : experiencesCollectionNew) {
                if (!experiencesCollectionOld.contains(experiencesCollectionNewExperiences)) {
                    City oldCityIdOfExperiencesCollectionNewExperiences = experiencesCollectionNewExperiences.getCityId();
                    experiencesCollectionNewExperiences.setCityId(city);
                    experiencesCollectionNewExperiences = em.merge(experiencesCollectionNewExperiences);
                    if (oldCityIdOfExperiencesCollectionNewExperiences != null && !oldCityIdOfExperiencesCollectionNewExperiences.equals(city)) {
                        oldCityIdOfExperiencesCollectionNewExperiences.getExperiencesCollection().remove(experiencesCollectionNewExperiences);
                        oldCityIdOfExperiencesCollectionNewExperiences = em.merge(oldCityIdOfExperiencesCollectionNewExperiences);
                    }
                }
            }
            for (Educations educationsCollectionOldEducations : educationsCollectionOld) {
                if (!educationsCollectionNew.contains(educationsCollectionOldEducations)) {
                    educationsCollectionOldEducations.setCityId(null);
                    educationsCollectionOldEducations = em.merge(educationsCollectionOldEducations);
                }
            }
            for (Educations educationsCollectionNewEducations : educationsCollectionNew) {
                if (!educationsCollectionOld.contains(educationsCollectionNewEducations)) {
                    City oldCityIdOfEducationsCollectionNewEducations = educationsCollectionNewEducations.getCityId();
                    educationsCollectionNewEducations.setCityId(city);
                    educationsCollectionNewEducations = em.merge(educationsCollectionNewEducations);
                    if (oldCityIdOfEducationsCollectionNewEducations != null && !oldCityIdOfEducationsCollectionNewEducations.equals(city)) {
                        oldCityIdOfEducationsCollectionNewEducations.getEducationsCollection().remove(educationsCollectionNewEducations);
                        oldCityIdOfEducationsCollectionNewEducations = em.merge(oldCityIdOfEducationsCollectionNewEducations);
                    }
                }
            }
            for (Subnetwork subnetworkCollectionOldSubnetwork : subnetworkCollectionOld) {
                if (!subnetworkCollectionNew.contains(subnetworkCollectionOldSubnetwork)) {
                    subnetworkCollectionOldSubnetwork.setCityFk(null);
                    subnetworkCollectionOldSubnetwork = em.merge(subnetworkCollectionOldSubnetwork);
                }
            }
            for (Subnetwork subnetworkCollectionNewSubnetwork : subnetworkCollectionNew) {
                if (!subnetworkCollectionOld.contains(subnetworkCollectionNewSubnetwork)) {
                    City oldCityFkOfSubnetworkCollectionNewSubnetwork = subnetworkCollectionNewSubnetwork.getCityFk();
                    subnetworkCollectionNewSubnetwork.setCityFk(city);
                    subnetworkCollectionNewSubnetwork = em.merge(subnetworkCollectionNewSubnetwork);
                    if (oldCityFkOfSubnetworkCollectionNewSubnetwork != null && !oldCityFkOfSubnetworkCollectionNewSubnetwork.equals(city)) {
                        oldCityFkOfSubnetworkCollectionNewSubnetwork.getSubnetworkCollection().remove(subnetworkCollectionNewSubnetwork);
                        oldCityFkOfSubnetworkCollectionNewSubnetwork = em.merge(oldCityFkOfSubnetworkCollectionNewSubnetwork);
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
                Integer id = city.getId();
                if (findCity(id) == null) {
                    throw new NonexistentEntityException("The city with id " + id + " no longer exists.");
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
            City city;
            try {
                city = em.getReference(City.class, id);
                city.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The city with id " + id + " no longer exists.", enfe);
            }
            State stateId = city.getStateId();
            if (stateId != null) {
                stateId.getCityCollection().remove(city);
                stateId = em.merge(stateId);
            }
            Collection<SocialProfile> socialProfileCollection = city.getSocialProfileCollection();
            for (SocialProfile socialProfileCollectionSocialProfile : socialProfileCollection) {
                socialProfileCollectionSocialProfile.setCityId(null);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
            }
            Collection<Experiences> experiencesCollection = city.getExperiencesCollection();
            for (Experiences experiencesCollectionExperiences : experiencesCollection) {
                experiencesCollectionExperiences.setCityId(null);
                experiencesCollectionExperiences = em.merge(experiencesCollectionExperiences);
            }
            Collection<Educations> educationsCollection = city.getEducationsCollection();
            for (Educations educationsCollectionEducations : educationsCollection) {
                educationsCollectionEducations.setCityId(null);
                educationsCollectionEducations = em.merge(educationsCollectionEducations);
            }
            Collection<Subnetwork> subnetworkCollection = city.getSubnetworkCollection();
            for (Subnetwork subnetworkCollectionSubnetwork : subnetworkCollection) {
                subnetworkCollectionSubnetwork.setCityFk(null);
                subnetworkCollectionSubnetwork = em.merge(subnetworkCollectionSubnetwork);
            }
            em.remove(city);
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

    public List<City> findCityEntities() {
        return findCityEntities(true, -1, -1);
    }

    public List<City> findCityEntities(int maxResults, int firstResult) {
        return findCityEntities(false, maxResults, firstResult);
    }

    private List<City> findCityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(City.class));
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

    public City findCity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(City.class, id);
        } finally {
            em.close();
        }
    }

    public int getCityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<City> rt = cq.from(City.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<City> findCitiesByStateId(Integer id) {
        EntityManager em = getEntityManager();
        try {
            List<City> cityList = (List<City>) em.createNativeQuery("select * from city "
                    + "where state_id = " + id + " order by name", City.class).getResultList();
            return cityList;
        } finally {
            em.close();
        }
    }
    
    public City findCityByName(String cityName) {
        EntityManager em = getEntityManager();
        try {
            City city = (City) em.createNativeQuery("select * from city "
                    + "where name = '" + cityName + "'", City.class).getSingleResult();
            return city;
        } finally {
            em.close();
        }
    }
}
