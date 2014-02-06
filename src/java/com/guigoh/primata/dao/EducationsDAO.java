/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.dao;

import com.guigoh.primata.dao.exceptions.NonexistentEntityException;
import com.guigoh.primata.dao.exceptions.PreexistingEntityException;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.guigoh.primata.entity.State;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.EducationsLocation;
import com.guigoh.primata.entity.Country;
import com.guigoh.primata.entity.City;
import com.guigoh.primata.entity.Educations;
import com.guigoh.primata.entity.EducationsName;
import com.guigoh.primata.entity.EducationsPK;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Joe
 */
public class EducationsDAO implements Serializable {

    private EntityManagerFactory emf = JPAUtil.getEMF();

    public EducationsDAO() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Educations educations) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (educations.getEducationsPK() == null) {
            educations.setEducationsPK(new EducationsPK());
        }
        educations.getEducationsPK().setTokenId(educations.getSocialProfile().getTokenId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            State stateId = educations.getStateId();
            if (stateId != null) {
                stateId = em.getReference(stateId.getClass(), stateId.getId());
                educations.setStateId(stateId);
            }
            SocialProfile socialProfile = educations.getSocialProfile();
            if (socialProfile != null) {
                socialProfile = em.getReference(socialProfile.getClass(), socialProfile.getTokenId());
                educations.setSocialProfile(socialProfile);
            }
            EducationsLocation locationId = educations.getLocationId();
            if (locationId != null) {
                locationId = em.getReference(locationId.getClass(), locationId.getId());
                educations.setLocationId(locationId);
            }
            Country countryId = educations.getCountryId();
            if (countryId != null) {
                countryId = em.getReference(countryId.getClass(), countryId.getId());
                educations.setCountryId(countryId);
            }
            City cityId = educations.getCityId();
            if (cityId != null) {
                cityId = em.getReference(cityId.getClass(), cityId.getId());
                educations.setCityId(cityId);
            }
            EducationsName nameId = educations.getNameId();
            if (nameId != null) {
                nameId = em.getReference(nameId.getClass(), nameId.getId());
                educations.setNameId(nameId);
            }
            em.persist(educations);
            if (stateId != null) {
                stateId.getEducationsCollection().add(educations);
                stateId = em.merge(stateId);
            }
            if (socialProfile != null) {
                socialProfile.getEducationsCollection().add(educations);
                socialProfile = em.merge(socialProfile);
            }
            if (locationId != null) {
                locationId.getEducationsCollection().add(educations);
                locationId = em.merge(locationId);
            }
            if (countryId != null) {
                countryId.getEducationsCollection().add(educations);
                countryId = em.merge(countryId);
            }
            if (cityId != null) {
                cityId.getEducationsCollection().add(educations);
                cityId = em.merge(cityId);
            }
            if (nameId != null) {
                nameId.getEducationsCollection().add(educations);
                nameId = em.merge(nameId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEducations(educations.getEducationsPK()) != null) {
                throw new PreexistingEntityException("Educations " + educations + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Educations educations) throws NonexistentEntityException, RollbackFailureException, Exception {
        educations.getEducationsPK().setTokenId(educations.getSocialProfile().getTokenId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Educations persistentEducations = em.find(Educations.class, educations.getEducationsPK());
            State stateIdOld = persistentEducations.getStateId();
            State stateIdNew = educations.getStateId();
            SocialProfile socialProfileOld = persistentEducations.getSocialProfile();
            SocialProfile socialProfileNew = educations.getSocialProfile();
            EducationsLocation locationIdOld = persistentEducations.getLocationId();
            EducationsLocation locationIdNew = educations.getLocationId();
            Country countryIdOld = persistentEducations.getCountryId();
            Country countryIdNew = educations.getCountryId();
            City cityIdOld = persistentEducations.getCityId();
            City cityIdNew = educations.getCityId();
            EducationsName nameIdOld = persistentEducations.getNameId();
            EducationsName nameIdNew = educations.getNameId();
            if (stateIdNew != null) {
                stateIdNew = em.getReference(stateIdNew.getClass(), stateIdNew.getId());
                educations.setStateId(stateIdNew);
            }
            if (socialProfileNew != null) {
                socialProfileNew = em.getReference(socialProfileNew.getClass(), socialProfileNew.getTokenId());
                educations.setSocialProfile(socialProfileNew);
            }
            if (locationIdNew != null) {
                locationIdNew = em.getReference(locationIdNew.getClass(), locationIdNew.getId());
                educations.setLocationId(locationIdNew);
            }
            if (countryIdNew != null) {
                countryIdNew = em.getReference(countryIdNew.getClass(), countryIdNew.getId());
                educations.setCountryId(countryIdNew);
            }
            if (cityIdNew != null) {
                cityIdNew = em.getReference(cityIdNew.getClass(), cityIdNew.getId());
                educations.setCityId(cityIdNew);
            }
            if (nameIdNew != null) {
                nameIdNew = em.getReference(nameIdNew.getClass(), nameIdNew.getId());
                educations.setNameId(nameIdNew);
            }
            educations = em.merge(educations);
            if (stateIdOld != null && !stateIdOld.equals(stateIdNew)) {
                stateIdOld.getEducationsCollection().remove(educations);
                stateIdOld = em.merge(stateIdOld);
            }
            if (stateIdNew != null && !stateIdNew.equals(stateIdOld)) {
                stateIdNew.getEducationsCollection().add(educations);
                stateIdNew = em.merge(stateIdNew);
            }
            if (socialProfileOld != null && !socialProfileOld.equals(socialProfileNew)) {
                socialProfileOld.getEducationsCollection().remove(educations);
                socialProfileOld = em.merge(socialProfileOld);
            }
            if (socialProfileNew != null && !socialProfileNew.equals(socialProfileOld)) {
                socialProfileNew.getEducationsCollection().add(educations);
                socialProfileNew = em.merge(socialProfileNew);
            }
            if (locationIdOld != null && !locationIdOld.equals(locationIdNew)) {
                locationIdOld.getEducationsCollection().remove(educations);
                locationIdOld = em.merge(locationIdOld);
            }
            if (locationIdNew != null && !locationIdNew.equals(locationIdOld)) {
                locationIdNew.getEducationsCollection().add(educations);
                locationIdNew = em.merge(locationIdNew);
            }
            if (countryIdOld != null && !countryIdOld.equals(countryIdNew)) {
                countryIdOld.getEducationsCollection().remove(educations);
                countryIdOld = em.merge(countryIdOld);
            }
            if (countryIdNew != null && !countryIdNew.equals(countryIdOld)) {
                countryIdNew.getEducationsCollection().add(educations);
                countryIdNew = em.merge(countryIdNew);
            }
            if (cityIdOld != null && !cityIdOld.equals(cityIdNew)) {
                cityIdOld.getEducationsCollection().remove(educations);
                cityIdOld = em.merge(cityIdOld);
            }
            if (cityIdNew != null && !cityIdNew.equals(cityIdOld)) {
                cityIdNew.getEducationsCollection().add(educations);
                cityIdNew = em.merge(cityIdNew);
            }
            if (nameIdOld != null && !nameIdOld.equals(nameIdNew)) {
                nameIdOld.getEducationsCollection().remove(educations);
                nameIdOld = em.merge(nameIdOld);
            }
            if (nameIdNew != null && !nameIdNew.equals(nameIdOld)) {
                nameIdNew.getEducationsCollection().add(educations);
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
                EducationsPK id = educations.getEducationsPK();
                if (findEducations(id) == null) {
                    throw new NonexistentEntityException("The educations with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(EducationsPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Educations educations;
            try {
                educations = em.getReference(Educations.class, id);
                educations.getEducationsPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The educations with id " + id + " no longer exists.", enfe);
            }
            State stateId = educations.getStateId();
            if (stateId != null) {
                stateId.getEducationsCollection().remove(educations);
                stateId = em.merge(stateId);
            }
            SocialProfile socialProfile = educations.getSocialProfile();
            if (socialProfile != null) {
                socialProfile.getEducationsCollection().remove(educations);
                socialProfile = em.merge(socialProfile);
            }
            EducationsLocation locationId = educations.getLocationId();
            if (locationId != null) {
                locationId.getEducationsCollection().remove(educations);
                locationId = em.merge(locationId);
            }
            Country countryId = educations.getCountryId();
            if (countryId != null) {
                countryId.getEducationsCollection().remove(educations);
                countryId = em.merge(countryId);
            }
            City cityId = educations.getCityId();
            if (cityId != null) {
                cityId.getEducationsCollection().remove(educations);
                cityId = em.merge(cityId);
            }
            EducationsName nameId = educations.getNameId();
            if (nameId != null) {
                nameId.getEducationsCollection().remove(educations);
                nameId = em.merge(nameId);
            }
            em.remove(educations);
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

    public List<Educations> findEducationsEntities() {
        return findEducationsEntities(true, -1, -1);
    }

    public List<Educations> findEducationsEntities(int maxResults, int firstResult) {
        return findEducationsEntities(false, maxResults, firstResult);
    }

    private List<Educations> findEducationsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Educations.class));
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

    public Educations findEducations(EducationsPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Educations.class, id);
        } finally {
            em.close();
        }
    }

    public int getEducationsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Educations> rt = cq.from(Educations.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Educations> findEducationsByTokenId(String token_id) {
        EntityManager em = getEntityManager();
        try {
            List<Educations> educationsList = (List<Educations>) em.createNativeQuery("select * from primata_educations "
                    + "where token_id = '" + token_id + "'", Educations.class).getResultList();
            return educationsList;
        } finally {
            em.close();
        }
    }

    public void createInsert(Educations educations) throws RollbackFailureException, Exception {
        if (educations.getEducationsPK() == null) {
            educations.setEducationsPK(new EducationsPK());
        }
        educations.getEducationsPK().setTokenId(educations.getSocialProfile().getTokenId());
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            String sql = "INSERT INTO primata_educations (token_id, name_id, data_begin, data_end, location_id, country_id, state_id, city_id) "
                    + "VALUES(?1,?2,?3,?4,?5,?6,?7,null)";

            Query query = em.createNativeQuery(sql);
            query.setParameter(1, educations.getEducationsPK().getTokenId());
            query.setParameter(2, educations.getNameId().getId());
            query.setParameter(3, educations.getDataBegin());
            query.setParameter(4, educations.getDataEnd());
            query.setParameter(5, educations.getLocationId().getId());
            query.setParameter(6, educations.getCountryId().getId());
            query.setParameter(7, educations.getStateId().getId());
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

    public Educations findEducationsByName(Educations educations) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
