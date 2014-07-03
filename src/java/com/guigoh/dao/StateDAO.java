/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.dao;

import com.guigoh.dao.exceptions.IllegalOrphanException;
import com.guigoh.dao.exceptions.NonexistentEntityException;
import com.guigoh.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.guigoh.entity.Country;
import com.guigoh.entity.SocialProfile;
import java.util.ArrayList;
import java.util.Collection;
import com.guigoh.entity.City;
import com.guigoh.entity.State;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Joe
 */
public class StateDAO implements Serializable {

    private transient EntityManagerFactory emf = JPAUtil.getEMF();

    public StateDAO() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(State state) throws RollbackFailureException, Exception {
        if (state.getSocialProfileCollection() == null) {
            state.setSocialProfileCollection(new ArrayList<SocialProfile>());
        }
        if (state.getCityCollection() == null) {
            state.setCityCollection(new ArrayList<City>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Country countryId = state.getCountryId();
            if (countryId != null) {
                countryId = em.getReference(countryId.getClass(), countryId.getId());
                state.setCountryId(countryId);
            }
            Collection<SocialProfile> attachedSocialProfileCollection = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionSocialProfileToAttach : state.getSocialProfileCollection()) {
                socialProfileCollectionSocialProfileToAttach = em.getReference(socialProfileCollectionSocialProfileToAttach.getClass(), socialProfileCollectionSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollection.add(socialProfileCollectionSocialProfileToAttach);
            }
            state.setSocialProfileCollection(attachedSocialProfileCollection);
            Collection<City> attachedCityCollection = new ArrayList<City>();
            for (City cityCollectionCityToAttach : state.getCityCollection()) {
                cityCollectionCityToAttach = em.getReference(cityCollectionCityToAttach.getClass(), cityCollectionCityToAttach.getId());
                attachedCityCollection.add(cityCollectionCityToAttach);
            }
            state.setCityCollection(attachedCityCollection);
            em.persist(state);
            if (countryId != null) {
                countryId.getStateCollection().add(state);
                countryId = em.merge(countryId);
            }
            for (SocialProfile socialProfileCollectionSocialProfile : state.getSocialProfileCollection()) {
                State oldStateIdOfSocialProfileCollectionSocialProfile = socialProfileCollectionSocialProfile.getStateId();
                socialProfileCollectionSocialProfile.setStateId(state);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
                if (oldStateIdOfSocialProfileCollectionSocialProfile != null) {
                    oldStateIdOfSocialProfileCollectionSocialProfile.getSocialProfileCollection().remove(socialProfileCollectionSocialProfile);
                    oldStateIdOfSocialProfileCollectionSocialProfile = em.merge(oldStateIdOfSocialProfileCollectionSocialProfile);
                }
            }
            for (City cityCollectionCity : state.getCityCollection()) {
                State oldStateIdOfCityCollectionCity = cityCollectionCity.getStateId();
                cityCollectionCity.setStateId(state);
                cityCollectionCity = em.merge(cityCollectionCity);
                if (oldStateIdOfCityCollectionCity != null) {
                    oldStateIdOfCityCollectionCity.getCityCollection().remove(cityCollectionCity);
                    oldStateIdOfCityCollectionCity = em.merge(oldStateIdOfCityCollectionCity);
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

    public void edit(State state) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            State persistentState = em.find(State.class, state.getId());
            Country countryIdOld = persistentState.getCountryId();
            Country countryIdNew = state.getCountryId();
            Collection<SocialProfile> socialProfileCollectionOld = persistentState.getSocialProfileCollection();
            Collection<SocialProfile> socialProfileCollectionNew = state.getSocialProfileCollection();
            Collection<City> cityCollectionOld = persistentState.getCityCollection();
            Collection<City> cityCollectionNew = state.getCityCollection();
            List<String> illegalOrphanMessages = null;
            for (City cityCollectionOldCity : cityCollectionOld) {
                if (!cityCollectionNew.contains(cityCollectionOldCity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain City " + cityCollectionOldCity + " since its stateId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (countryIdNew != null) {
                countryIdNew = em.getReference(countryIdNew.getClass(), countryIdNew.getId());
                state.setCountryId(countryIdNew);
            }
            Collection<SocialProfile> attachedSocialProfileCollectionNew = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionNewSocialProfileToAttach : socialProfileCollectionNew) {
                socialProfileCollectionNewSocialProfileToAttach = em.getReference(socialProfileCollectionNewSocialProfileToAttach.getClass(), socialProfileCollectionNewSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollectionNew.add(socialProfileCollectionNewSocialProfileToAttach);
            }
            socialProfileCollectionNew = attachedSocialProfileCollectionNew;
            state.setSocialProfileCollection(socialProfileCollectionNew);
            Collection<City> attachedCityCollectionNew = new ArrayList<City>();
            for (City cityCollectionNewCityToAttach : cityCollectionNew) {
                cityCollectionNewCityToAttach = em.getReference(cityCollectionNewCityToAttach.getClass(), cityCollectionNewCityToAttach.getId());
                attachedCityCollectionNew.add(cityCollectionNewCityToAttach);
            }
            cityCollectionNew = attachedCityCollectionNew;
            state.setCityCollection(cityCollectionNew);
            state = em.merge(state);
            if (countryIdOld != null && !countryIdOld.equals(countryIdNew)) {
                countryIdOld.getStateCollection().remove(state);
                countryIdOld = em.merge(countryIdOld);
            }
            if (countryIdNew != null && !countryIdNew.equals(countryIdOld)) {
                countryIdNew.getStateCollection().add(state);
                countryIdNew = em.merge(countryIdNew);
            }
            for (SocialProfile socialProfileCollectionOldSocialProfile : socialProfileCollectionOld) {
                if (!socialProfileCollectionNew.contains(socialProfileCollectionOldSocialProfile)) {
                    socialProfileCollectionOldSocialProfile.setStateId(null);
                    socialProfileCollectionOldSocialProfile = em.merge(socialProfileCollectionOldSocialProfile);
                }
            }
            for (SocialProfile socialProfileCollectionNewSocialProfile : socialProfileCollectionNew) {
                if (!socialProfileCollectionOld.contains(socialProfileCollectionNewSocialProfile)) {
                    State oldStateIdOfSocialProfileCollectionNewSocialProfile = socialProfileCollectionNewSocialProfile.getStateId();
                    socialProfileCollectionNewSocialProfile.setStateId(state);
                    socialProfileCollectionNewSocialProfile = em.merge(socialProfileCollectionNewSocialProfile);
                    if (oldStateIdOfSocialProfileCollectionNewSocialProfile != null && !oldStateIdOfSocialProfileCollectionNewSocialProfile.equals(state)) {
                        oldStateIdOfSocialProfileCollectionNewSocialProfile.getSocialProfileCollection().remove(socialProfileCollectionNewSocialProfile);
                        oldStateIdOfSocialProfileCollectionNewSocialProfile = em.merge(oldStateIdOfSocialProfileCollectionNewSocialProfile);
                    }
                }
            }
            for (City cityCollectionNewCity : cityCollectionNew) {
                if (!cityCollectionOld.contains(cityCollectionNewCity)) {
                    State oldStateIdOfCityCollectionNewCity = cityCollectionNewCity.getStateId();
                    cityCollectionNewCity.setStateId(state);
                    cityCollectionNewCity = em.merge(cityCollectionNewCity);
                    if (oldStateIdOfCityCollectionNewCity != null && !oldStateIdOfCityCollectionNewCity.equals(state)) {
                        oldStateIdOfCityCollectionNewCity.getCityCollection().remove(cityCollectionNewCity);
                        oldStateIdOfCityCollectionNewCity = em.merge(oldStateIdOfCityCollectionNewCity);
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
                Integer id = state.getId();
                if (findState(id) == null) {
                    throw new NonexistentEntityException("The state with id " + id + " no longer exists.");
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
            State state;
            try {
                state = em.getReference(State.class, id);
                state.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The state with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<City> cityCollectionOrphanCheck = state.getCityCollection();
            for (City cityCollectionOrphanCheckCity : cityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This State (" + state + ") cannot be destroyed since the City " + cityCollectionOrphanCheckCity + " in its cityCollection field has a non-nullable stateId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Country countryId = state.getCountryId();
            if (countryId != null) {
                countryId.getStateCollection().remove(state);
                countryId = em.merge(countryId);
            }
            Collection<SocialProfile> socialProfileCollection = state.getSocialProfileCollection();
            for (SocialProfile socialProfileCollectionSocialProfile : socialProfileCollection) {
                socialProfileCollectionSocialProfile.setStateId(null);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
            }
            em.remove(state);
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

    public List<State> findStateEntities() {
        return findStateEntities(true, -1, -1);
    }

    public List<State> findStateEntities(int maxResults, int firstResult) {
        return findStateEntities(false, maxResults, firstResult);
    }

    private List<State> findStateEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(State.class));
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

    public State findState(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(State.class, id);
        } finally {
            em.close();
        }
    }

    public int getStateCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<State> rt = cq.from(State.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<State> findStatesByCountryId(Integer id) {
        EntityManager em = getEntityManager();
        try {
            List<State> stateList = (List<State>) em.createNativeQuery("select * from state "
                    + "where country_id = " + id, State.class).getResultList();
            return stateList;
        } finally {
            em.close();
        }
    }
    
    public State findStateByName(String stateName) {
        EntityManager em = getEntityManager();
        try {
            State state = (State) em.createNativeQuery("select * from state "
                    + "where name = '" + stateName + "'", State.class).getSingleResult();
            return state;
        } finally {
            em.close();
        }
    }
}
