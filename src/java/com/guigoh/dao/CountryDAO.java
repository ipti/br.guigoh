/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.dao;

import com.guigoh.dao.exceptions.IllegalOrphanException;
import com.guigoh.dao.exceptions.NonexistentEntityException;
import com.guigoh.dao.exceptions.RollbackFailureException;
import com.guigoh.entity.Country;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.guigoh.entity.SocialProfile;
import java.util.ArrayList;
import java.util.Collection;
import com.guigoh.entity.State;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Joe
 */
public class CountryDAO implements Serializable {

    private transient EntityManagerFactory emf = JPAUtil.getEMF();

    public CountryDAO() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Country country) throws RollbackFailureException, Exception {
        if (country.getSocialProfileCollection() == null) {
            country.setSocialProfileCollection(new ArrayList<SocialProfile>());
        }
        if (country.getStateCollection() == null) {
            country.setStateCollection(new ArrayList<State>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<SocialProfile> attachedSocialProfileCollection = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionSocialProfileToAttach : country.getSocialProfileCollection()) {
                socialProfileCollectionSocialProfileToAttach = em.getReference(socialProfileCollectionSocialProfileToAttach.getClass(), socialProfileCollectionSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollection.add(socialProfileCollectionSocialProfileToAttach);
            }
            country.setSocialProfileCollection(attachedSocialProfileCollection);
            Collection<State> attachedStateCollection = new ArrayList<State>();
            for (State stateCollectionStateToAttach : country.getStateCollection()) {
                stateCollectionStateToAttach = em.getReference(stateCollectionStateToAttach.getClass(), stateCollectionStateToAttach.getId());
                attachedStateCollection.add(stateCollectionStateToAttach);
            }
            country.setStateCollection(attachedStateCollection);
            em.persist(country);
            for (SocialProfile socialProfileCollectionSocialProfile : country.getSocialProfileCollection()) {
                Country oldCountryIdOfSocialProfileCollectionSocialProfile = socialProfileCollectionSocialProfile.getCountryId();
                socialProfileCollectionSocialProfile.setCountryId(country);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
                if (oldCountryIdOfSocialProfileCollectionSocialProfile != null) {
                    oldCountryIdOfSocialProfileCollectionSocialProfile.getSocialProfileCollection().remove(socialProfileCollectionSocialProfile);
                    oldCountryIdOfSocialProfileCollectionSocialProfile = em.merge(oldCountryIdOfSocialProfileCollectionSocialProfile);
                }
            }
            for (State stateCollectionState : country.getStateCollection()) {
                Country oldCountryIdOfStateCollectionState = stateCollectionState.getCountryId();
                stateCollectionState.setCountryId(country);
                stateCollectionState = em.merge(stateCollectionState);
                if (oldCountryIdOfStateCollectionState != null) {
                    oldCountryIdOfStateCollectionState.getStateCollection().remove(stateCollectionState);
                    oldCountryIdOfStateCollectionState = em.merge(oldCountryIdOfStateCollectionState);
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

    public void edit(Country country) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Country persistentCountry = em.find(Country.class, country.getId());
            Collection<SocialProfile> socialProfileCollectionOld = persistentCountry.getSocialProfileCollection();
            Collection<SocialProfile> socialProfileCollectionNew = country.getSocialProfileCollection();
            Collection<State> stateCollectionOld = persistentCountry.getStateCollection();
            Collection<State> stateCollectionNew = country.getStateCollection();
            List<String> illegalOrphanMessages = null;
            for (State stateCollectionOldState : stateCollectionOld) {
                if (!stateCollectionNew.contains(stateCollectionOldState)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain State " + stateCollectionOldState + " since its countryId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<SocialProfile> attachedSocialProfileCollectionNew = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionNewSocialProfileToAttach : socialProfileCollectionNew) {
                socialProfileCollectionNewSocialProfileToAttach = em.getReference(socialProfileCollectionNewSocialProfileToAttach.getClass(), socialProfileCollectionNewSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollectionNew.add(socialProfileCollectionNewSocialProfileToAttach);
            }
            socialProfileCollectionNew = attachedSocialProfileCollectionNew;
            country.setSocialProfileCollection(socialProfileCollectionNew);
            Collection<State> attachedStateCollectionNew = new ArrayList<State>();
            for (State stateCollectionNewStateToAttach : stateCollectionNew) {
                stateCollectionNewStateToAttach = em.getReference(stateCollectionNewStateToAttach.getClass(), stateCollectionNewStateToAttach.getId());
                attachedStateCollectionNew.add(stateCollectionNewStateToAttach);
            }
            stateCollectionNew = attachedStateCollectionNew;
            country.setStateCollection(stateCollectionNew);
            country = em.merge(country);
            for (SocialProfile socialProfileCollectionOldSocialProfile : socialProfileCollectionOld) {
                if (!socialProfileCollectionNew.contains(socialProfileCollectionOldSocialProfile)) {
                    socialProfileCollectionOldSocialProfile.setCountryId(null);
                    socialProfileCollectionOldSocialProfile = em.merge(socialProfileCollectionOldSocialProfile);
                }
            }
            for (SocialProfile socialProfileCollectionNewSocialProfile : socialProfileCollectionNew) {
                if (!socialProfileCollectionOld.contains(socialProfileCollectionNewSocialProfile)) {
                    Country oldCountryIdOfSocialProfileCollectionNewSocialProfile = socialProfileCollectionNewSocialProfile.getCountryId();
                    socialProfileCollectionNewSocialProfile.setCountryId(country);
                    socialProfileCollectionNewSocialProfile = em.merge(socialProfileCollectionNewSocialProfile);
                    if (oldCountryIdOfSocialProfileCollectionNewSocialProfile != null && !oldCountryIdOfSocialProfileCollectionNewSocialProfile.equals(country)) {
                        oldCountryIdOfSocialProfileCollectionNewSocialProfile.getSocialProfileCollection().remove(socialProfileCollectionNewSocialProfile);
                        oldCountryIdOfSocialProfileCollectionNewSocialProfile = em.merge(oldCountryIdOfSocialProfileCollectionNewSocialProfile);
                    }
                }
            }
            for (State stateCollectionNewState : stateCollectionNew) {
                if (!stateCollectionOld.contains(stateCollectionNewState)) {
                    Country oldCountryIdOfStateCollectionNewState = stateCollectionNewState.getCountryId();
                    stateCollectionNewState.setCountryId(country);
                    stateCollectionNewState = em.merge(stateCollectionNewState);
                    if (oldCountryIdOfStateCollectionNewState != null && !oldCountryIdOfStateCollectionNewState.equals(country)) {
                        oldCountryIdOfStateCollectionNewState.getStateCollection().remove(stateCollectionNewState);
                        oldCountryIdOfStateCollectionNewState = em.merge(oldCountryIdOfStateCollectionNewState);
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
                Integer id = country.getId();
                if (findCountry(id) == null) {
                    throw new NonexistentEntityException("The country with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Country country;
            try {
                country = em.getReference(Country.class, id);
                country.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The country with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<State> stateCollectionOrphanCheck = country.getStateCollection();
            for (State stateCollectionOrphanCheckState : stateCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Country (" + country + ") cannot be destroyed since the State " + stateCollectionOrphanCheckState + " in its stateCollection field has a non-nullable countryId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<SocialProfile> socialProfileCollection = country.getSocialProfileCollection();
            for (SocialProfile socialProfileCollectionSocialProfile : socialProfileCollection) {
                socialProfileCollectionSocialProfile.setCountryId(null);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
            }
            em.remove(country);
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

    public List<Country> findCountryEntities() {
        return findCountryEntities(true, -1, -1);
    }

    public List<Country> findCountryEntities(int maxResults, int firstResult) {
        return findCountryEntities(false, maxResults, firstResult);
    }

    private List<Country> findCountryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Country.class));
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

    public Country findCountry(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Country.class, id);
        } finally {
            em.close();
        }
    }

    public int getCountryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Country> rt = cq.from(Country.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Country findCountryByName(String countryName) {
        EntityManager em = getEntityManager();
        try {
            Country country = (Country) em.createNativeQuery("select * from country "
                    + "where name = '" + countryName + "'", Country.class).getSingleResult();
            return country;
        } finally {
            em.close();
        }
    }
}
