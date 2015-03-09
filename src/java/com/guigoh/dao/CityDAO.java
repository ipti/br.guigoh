/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.dao;

import com.guigoh.dao.exceptions.NonexistentEntityException;
import com.guigoh.dao.exceptions.RollbackFailureException;
import com.guigoh.entity.City;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.guigoh.entity.State;
import com.guigoh.entity.SocialProfile;
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
public class CityDAO implements Serializable {

    private transient EntityManagerFactory emf = JPAUtil.getEMF();
    
    public CityDAO() {

    }
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(City city) throws RollbackFailureException, Exception {
        if (city.getSocialProfileCollection() == null) {
            city.setSocialProfileCollection(new ArrayList<SocialProfile>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
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
            em = getEntityManager();
            em.getTransaction().begin();
            City persistentCity = em.find(City.class, city.getId());
            State stateIdOld = persistentCity.getStateId();
            State stateIdNew = city.getStateId();
            Collection<SocialProfile> socialProfileCollectionOld = persistentCity.getSocialProfileCollection();
            Collection<SocialProfile> socialProfileCollectionNew = city.getSocialProfileCollection();
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
            em = getEntityManager();
            em.getTransaction().begin();
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
    
    public List<City> findCitysByCountryId(Integer id) {
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
