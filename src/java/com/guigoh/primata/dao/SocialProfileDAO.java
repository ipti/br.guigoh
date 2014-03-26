/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.dao;

import com.guigoh.primata.bean.AuthBean;
import com.guigoh.primata.dao.exceptions.IllegalOrphanException;
import com.guigoh.primata.dao.exceptions.NonexistentEntityException;
import com.guigoh.primata.dao.exceptions.PreexistingEntityException;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import com.guigoh.primata.entity.Authorization;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.guigoh.primata.entity.Users;
import com.guigoh.primata.entity.State;
import com.guigoh.primata.entity.Occupations;
import com.guigoh.primata.entity.Country;
import com.guigoh.primata.entity.City;
import com.guigoh.primata.entity.Availability;
import com.guigoh.primata.entity.SocialProfileVisibility;
import com.guigoh.primata.entity.Subnetwork;
import com.guigoh.primata.entity.Language;
import com.guigoh.primata.entity.Scholarity;
import com.guigoh.primata.entity.Interests;
import java.util.ArrayList;
import java.util.Collection;
import com.guigoh.primata.entity.Experiences;
import com.guigoh.primata.entity.Educations;
import com.guigoh.primata.entity.OccupationsType;
import com.guigoh.primata.entity.SocialProfile;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityResult;
import javax.persistence.SqlResultSetMapping;
import javax.transaction.UserTransaction;

/**
 *
 * @author Joe
 */
public class SocialProfileDAO implements Serializable {

    private EntityManagerFactory emf = JPAUtil.getEMF();

    public SocialProfileDAO() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SocialProfile socialProfile) throws IllegalOrphanException, PreexistingEntityException, RollbackFailureException, Exception {
        if (socialProfile.getInterestsCollection() == null) {
            socialProfile.setInterestsCollection(new ArrayList<Interests>());
        }
        if (socialProfile.getExperiencesCollection() == null) {
            socialProfile.setExperiencesCollection(new ArrayList<Experiences>());
        }
        if (socialProfile.getEducationsCollection() == null) {
            socialProfile.setEducationsCollection(new ArrayList<Educations>());
        }
        List<String> illegalOrphanMessages = null;
        Users usersOrphanCheck = socialProfile.getUsers();
        if (usersOrphanCheck != null) {
            SocialProfile oldSocialProfileOfUsers = usersOrphanCheck.getSocialProfile();
            if (oldSocialProfileOfUsers != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Users " + usersOrphanCheck + " already has an item of type SocialProfile whose users column cannot be null. Please make another selection for the users field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users users = socialProfile.getUsers();
            if (users != null) {
                users = em.getReference(users.getClass(), users.getUsername());
                socialProfile.setUsers(users);
            }
            State stateId = socialProfile.getStateId();
            if (stateId != null) {
                stateId = em.getReference(stateId.getClass(), stateId.getId());
                socialProfile.setStateId(stateId);
            }
            Occupations occupationsId = socialProfile.getOccupationsId();
            if (occupationsId != null) {
                occupationsId = em.getReference(occupationsId.getClass(), occupationsId.getId());
                socialProfile.setOccupationsId(occupationsId);
            }
            Country countryId = socialProfile.getCountryId();
            if (countryId != null) {
                countryId = em.getReference(countryId.getClass(), countryId.getId());
                socialProfile.setCountryId(countryId);
            }
            City cityId = socialProfile.getCityId();
            if (cityId != null) {
                cityId = em.getReference(cityId.getClass(), cityId.getId());
                socialProfile.setCityId(cityId);
            }
            Availability availabilityId = socialProfile.getAvailabilityId();
            if (availabilityId != null) {
                availabilityId = em.getReference(availabilityId.getClass(), availabilityId.getId());
                socialProfile.setAvailabilityId(availabilityId);
            }
            SocialProfileVisibility socialProfileVisibility = socialProfile.getSocialProfileVisibility();
            if (socialProfileVisibility != null) {
                socialProfileVisibility = em.getReference(socialProfileVisibility.getClass(), socialProfileVisibility.getSocialProfileId());
                socialProfile.setSocialProfileVisibility(socialProfileVisibility);
            }
            Subnetwork subnetworkId = socialProfile.getSubnetworkId();
            if (subnetworkId != null) {
                subnetworkId = em.getReference(subnetworkId.getClass(), subnetworkId.getId());
                socialProfile.setSubnetworkId(subnetworkId);
            }
            Language languageId = socialProfile.getLanguageId();
            if (languageId != null) {
                languageId = em.getReference(languageId.getClass(), languageId.getId());
                socialProfile.setLanguageId(languageId);
            }
            Scholarity scholarityId = socialProfile.getScholarityId();
            if (scholarityId != null) {
                scholarityId = em.getReference(scholarityId.getClass(), scholarityId.getId());
                socialProfile.setScholarityId(scholarityId);
            }
            Collection<Interests> attachedInterestsCollection = new ArrayList<Interests>();
            for (Interests interestsCollectionInterestsToAttach : socialProfile.getInterestsCollection()) {
                interestsCollectionInterestsToAttach = em.getReference(interestsCollectionInterestsToAttach.getClass(), interestsCollectionInterestsToAttach.getId());
                attachedInterestsCollection.add(interestsCollectionInterestsToAttach);
            }
            socialProfile.setInterestsCollection(attachedInterestsCollection);
            Collection<Experiences> attachedExperiencesCollection = new ArrayList<Experiences>();
            for (Experiences experiencesCollectionExperiencesToAttach : socialProfile.getExperiencesCollection()) {
                experiencesCollectionExperiencesToAttach = em.getReference(experiencesCollectionExperiencesToAttach.getClass(), experiencesCollectionExperiencesToAttach.getExperiencesPK());
                attachedExperiencesCollection.add(experiencesCollectionExperiencesToAttach);
            }
            socialProfile.setExperiencesCollection(attachedExperiencesCollection);
            Collection<Educations> attachedEducationsCollection = new ArrayList<Educations>();
            for (Educations educationsCollectionEducationsToAttach : socialProfile.getEducationsCollection()) {
                educationsCollectionEducationsToAttach = em.getReference(educationsCollectionEducationsToAttach.getClass(), educationsCollectionEducationsToAttach.getEducationsPK());
                attachedEducationsCollection.add(educationsCollectionEducationsToAttach);
            }
            socialProfile.setEducationsCollection(attachedEducationsCollection);
            em.persist(socialProfile);
            if (users != null) {
                users.setSocialProfile(socialProfile);
                users = em.merge(users);
            }
            if (stateId != null) {
                stateId.getSocialProfileCollection().add(socialProfile);
                stateId = em.merge(stateId);
            }
            if (occupationsId != null) {
                occupationsId.getSocialProfileCollection().add(socialProfile);
                occupationsId = em.merge(occupationsId);
            }
            if (countryId != null) {
                countryId.getSocialProfileCollection().add(socialProfile);
                countryId = em.merge(countryId);
            }
            if (cityId != null) {
                cityId.getSocialProfileCollection().add(socialProfile);
                cityId = em.merge(cityId);
            }
            if (availabilityId != null) {
                availabilityId.getSocialProfileCollection().add(socialProfile);
                availabilityId = em.merge(availabilityId);
            }
            if (socialProfileVisibility != null) {
                SocialProfile oldSocialProfileOfSocialProfileVisibility = socialProfileVisibility.getSocialProfile();
                if (oldSocialProfileOfSocialProfileVisibility != null) {
                    oldSocialProfileOfSocialProfileVisibility.setSocialProfileVisibility(null);
                    oldSocialProfileOfSocialProfileVisibility = em.merge(oldSocialProfileOfSocialProfileVisibility);
                }
                socialProfileVisibility.setSocialProfile(socialProfile);
                socialProfileVisibility = em.merge(socialProfileVisibility);
            }
            if (subnetworkId != null) {
                subnetworkId.getSocialProfileCollection().add(socialProfile);
                subnetworkId = em.merge(subnetworkId);
            }
            if (languageId != null) {
                languageId.getSocialProfileCollection().add(socialProfile);
                languageId = em.merge(languageId);
            }
            if (scholarityId != null) {
                scholarityId.getSocialProfileCollection().add(socialProfile);
                scholarityId = em.merge(scholarityId);
            }
            for (Interests interestsCollectionInterests : socialProfile.getInterestsCollection()) {
                interestsCollectionInterests.getSocialProfileCollection().add(socialProfile);
                interestsCollectionInterests = em.merge(interestsCollectionInterests);
            }
            for (Experiences experiencesCollectionExperiences : socialProfile.getExperiencesCollection()) {
                SocialProfile oldSocialProfileOfExperiencesCollectionExperiences = experiencesCollectionExperiences.getSocialProfile();
                experiencesCollectionExperiences.setSocialProfile(socialProfile);
                experiencesCollectionExperiences = em.merge(experiencesCollectionExperiences);
                if (oldSocialProfileOfExperiencesCollectionExperiences != null) {
                    oldSocialProfileOfExperiencesCollectionExperiences.getExperiencesCollection().remove(experiencesCollectionExperiences);
                    oldSocialProfileOfExperiencesCollectionExperiences = em.merge(oldSocialProfileOfExperiencesCollectionExperiences);
                }
            }
            for (Educations educationsCollectionEducations : socialProfile.getEducationsCollection()) {
                SocialProfile oldSocialProfileOfEducationsCollectionEducations = educationsCollectionEducations.getSocialProfile();
                educationsCollectionEducations.setSocialProfile(socialProfile);
                educationsCollectionEducations = em.merge(educationsCollectionEducations);
                if (oldSocialProfileOfEducationsCollectionEducations != null) {
                    oldSocialProfileOfEducationsCollectionEducations.getEducationsCollection().remove(educationsCollectionEducations);
                    oldSocialProfileOfEducationsCollectionEducations = em.merge(oldSocialProfileOfEducationsCollectionEducations);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findSocialProfile(socialProfile.getTokenId()) != null) {
                throw new PreexistingEntityException("SocialProfile " + socialProfile + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SocialProfile socialProfile) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SocialProfile persistentSocialProfile = em.find(SocialProfile.class, socialProfile.getTokenId());
            Users usersOld = persistentSocialProfile.getUsers();
            Users usersNew = socialProfile.getUsers();
            State stateIdOld = persistentSocialProfile.getStateId();
            State stateIdNew = socialProfile.getStateId();
            Occupations occupationsIdOld = persistentSocialProfile.getOccupationsId();
            Occupations occupationsIdNew = socialProfile.getOccupationsId();
            Country countryIdOld = persistentSocialProfile.getCountryId();
            Country countryIdNew = socialProfile.getCountryId();
            City cityIdOld = persistentSocialProfile.getCityId();
            City cityIdNew = socialProfile.getCityId();
            Availability availabilityIdOld = persistentSocialProfile.getAvailabilityId();
            Availability availabilityIdNew = socialProfile.getAvailabilityId();
            SocialProfileVisibility socialProfileVisibilityOld = persistentSocialProfile.getSocialProfileVisibility();
            SocialProfileVisibility socialProfileVisibilityNew = socialProfile.getSocialProfileVisibility();
            Subnetwork subnetworkIdOld = persistentSocialProfile.getSubnetworkId();
            Subnetwork subnetworkIdNew = socialProfile.getSubnetworkId();
            Language languageIdOld = persistentSocialProfile.getLanguageId();
            Language languageIdNew = socialProfile.getLanguageId();
            Scholarity scholarityIdOld = persistentSocialProfile.getScholarityId();
            Scholarity scholarityIdNew = socialProfile.getScholarityId();
            Collection<Interests> interestsCollectionOld = persistentSocialProfile.getInterestsCollection();
            Collection<Interests> interestsCollectionNew = socialProfile.getInterestsCollection();
            Collection<Experiences> experiencesCollectionOld = persistentSocialProfile.getExperiencesCollection();
            Collection<Experiences> experiencesCollectionNew = socialProfile.getExperiencesCollection();
            Collection<Educations> educationsCollectionOld = persistentSocialProfile.getEducationsCollection();
            Collection<Educations> educationsCollectionNew = socialProfile.getEducationsCollection();
            List<String> illegalOrphanMessages = null;
            if (usersNew != null && !usersNew.equals(usersOld)) {
                SocialProfile oldSocialProfileOfUsers = usersNew.getSocialProfile();
                if (oldSocialProfileOfUsers != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Users " + usersNew + " already has an item of type SocialProfile whose users column cannot be null. Please make another selection for the users field.");
                }
            }
            if (socialProfileVisibilityOld != null && !socialProfileVisibilityOld.equals(socialProfileVisibilityNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain SocialProfileVisibility " + socialProfileVisibilityOld + " since its socialProfile field is not nullable.");
            }
            for (Experiences experiencesCollectionOldExperiences : experiencesCollectionOld) {
                if (!experiencesCollectionNew.contains(experiencesCollectionOldExperiences)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Experiences " + experiencesCollectionOldExperiences + " since its socialProfile field is not nullable.");
                }
            }
            for (Educations educationsCollectionOldEducations : educationsCollectionOld) {
                if (!educationsCollectionNew.contains(educationsCollectionOldEducations)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Educations " + educationsCollectionOldEducations + " since its socialProfile field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usersNew != null) {
                usersNew = em.getReference(usersNew.getClass(), usersNew.getUsername());
                socialProfile.setUsers(usersNew);
            }
            if (stateIdNew != null) {
                if (stateIdNew.getId() == 0){
                    socialProfile.setStateId(null);
                }else{
                    stateIdNew = em.getReference(stateIdNew.getClass(), stateIdNew.getId());
                    socialProfile.setStateId(stateIdNew);
                }
            }
            if (occupationsIdNew != null) {
                if (occupationsIdNew.getId() == 0){
                    socialProfile.setOccupationsId(null);
                }else{
                    occupationsIdNew = em.getReference(occupationsIdNew.getClass(), occupationsIdNew.getId());
                    socialProfile.setOccupationsId(occupationsIdNew);
                }
            }
            if (countryIdNew != null) {
                if (countryIdNew.getId() == 0){
                    socialProfile.setCountryId(null);
                }else{
                    countryIdNew = em.getReference(countryIdNew.getClass(), countryIdNew.getId());
                    socialProfile.setCountryId(countryIdNew);
                }
            }
            if (cityIdNew != null) {
                if (cityIdNew.getId() == 0){
                    socialProfile.setCityId(null);
                }else{
                    cityIdNew = em.getReference(cityIdNew.getClass(), cityIdNew.getId());
                    socialProfile.setCityId(cityIdNew);
                }
            }
            if (availabilityIdNew != null) {
                if (availabilityIdNew.getId() == 0){
                    socialProfile.setAvailabilityId(null);
                }else{
                    availabilityIdNew = em.getReference(availabilityIdNew.getClass(), availabilityIdNew.getId());
                    socialProfile.setAvailabilityId(availabilityIdNew);
                }
            }
            if (socialProfileVisibilityNew != null) {
                if (socialProfileVisibilityNew.getSocialProfileId() == 0){
                    socialProfile.setSocialProfileVisibility(null);
                }else{
                    socialProfileVisibilityNew = em.getReference(socialProfileVisibilityNew.getClass(), socialProfileVisibilityNew.getSocialProfileId());
                    socialProfile.setSocialProfileVisibility(socialProfileVisibilityNew);
                }
            }
            if (subnetworkIdNew != null) {
                if (subnetworkIdNew.getId() == 0){
                    socialProfile.setSubnetworkId(null);
                }else{
                    subnetworkIdNew = em.getReference(subnetworkIdNew.getClass(), subnetworkIdNew.getId());
                    socialProfile.setSubnetworkId(subnetworkIdNew);
                }
            }
            if (languageIdNew != null) {
                if (languageIdNew.getId() == 0){
                    socialProfile.setLanguageId(null);
                }else{
                    languageIdNew = em.getReference(languageIdNew.getClass(), languageIdNew.getId());
                    socialProfile.setLanguageId(languageIdNew);
                }
            }
            if (scholarityIdNew != null) {
                if (scholarityIdNew.getId() == 0){
                    socialProfile.setScholarityId(null);
                }else{
                    scholarityIdNew = em.getReference(scholarityIdNew.getClass(), scholarityIdNew.getId());
                    socialProfile.setScholarityId(scholarityIdNew);
                }
            }
            Collection<Interests> attachedInterestsCollectionNew = new ArrayList<Interests>();
            for (Interests interestsCollectionNewInterestsToAttach : interestsCollectionNew) {
                interestsCollectionNewInterestsToAttach = em.getReference(interestsCollectionNewInterestsToAttach.getClass(), interestsCollectionNewInterestsToAttach.getId());
                attachedInterestsCollectionNew.add(interestsCollectionNewInterestsToAttach);
            }
            interestsCollectionNew = attachedInterestsCollectionNew;
            socialProfile.setInterestsCollection(interestsCollectionNew);
            Collection<Experiences> attachedExperiencesCollectionNew = new ArrayList<Experiences>();
            for (Experiences experiencesCollectionNewExperiencesToAttach : experiencesCollectionNew) {
                experiencesCollectionNewExperiencesToAttach = em.getReference(experiencesCollectionNewExperiencesToAttach.getClass(), experiencesCollectionNewExperiencesToAttach.getExperiencesPK());
                attachedExperiencesCollectionNew.add(experiencesCollectionNewExperiencesToAttach);
            }
            experiencesCollectionNew = attachedExperiencesCollectionNew;
            socialProfile.setExperiencesCollection(experiencesCollectionNew);
            Collection<Educations> attachedEducationsCollectionNew = new ArrayList<Educations>();
            for (Educations educationsCollectionNewEducationsToAttach : educationsCollectionNew) {
                educationsCollectionNewEducationsToAttach = em.getReference(educationsCollectionNewEducationsToAttach.getClass(), educationsCollectionNewEducationsToAttach.getEducationsPK());
                attachedEducationsCollectionNew.add(educationsCollectionNewEducationsToAttach);
            }
            educationsCollectionNew = attachedEducationsCollectionNew;
            socialProfile.setEducationsCollection(educationsCollectionNew);
            socialProfile = em.merge(socialProfile);
            if (usersOld != null && !usersOld.equals(usersNew)) {
                usersOld.setSocialProfile(null);
                usersOld = em.merge(usersOld);
            }
            if (usersNew != null && !usersNew.equals(usersOld)) {
                usersNew.setSocialProfile(socialProfile);
                usersNew = em.merge(usersNew);
            }
            if (stateIdOld != null && !stateIdOld.equals(stateIdNew)) {
                stateIdOld.getSocialProfileCollection().remove(socialProfile);
                stateIdOld = em.merge(stateIdOld);
            }
            if (stateIdNew != null && !stateIdNew.equals(stateIdOld)) {
                stateIdNew.getSocialProfileCollection().add(socialProfile);
                stateIdNew = em.merge(stateIdNew);
            }
            if (occupationsIdOld != null && !occupationsIdOld.equals(occupationsIdNew)) {
                occupationsIdOld.getSocialProfileCollection().remove(socialProfile);
                occupationsIdOld = em.merge(occupationsIdOld);
            }
            if (occupationsIdNew != null && !occupationsIdNew.equals(occupationsIdOld)) {
                occupationsIdNew.getSocialProfileCollection().add(socialProfile);
                occupationsIdNew = em.merge(occupationsIdNew);
            }
            if (countryIdOld != null && !countryIdOld.equals(countryIdNew)) {
                countryIdOld.getSocialProfileCollection().remove(socialProfile);
                countryIdOld = em.merge(countryIdOld);
            }
            if (countryIdNew != null && !countryIdNew.equals(countryIdOld)) {
                countryIdNew.getSocialProfileCollection().add(socialProfile);
                countryIdNew = em.merge(countryIdNew);
            }
            if (cityIdOld != null && !cityIdOld.equals(cityIdNew)) {
                cityIdOld.getSocialProfileCollection().remove(socialProfile);
                cityIdOld = em.merge(cityIdOld);
            }
            if (cityIdNew != null && !cityIdNew.equals(cityIdOld)) {
                cityIdNew.getSocialProfileCollection().add(socialProfile);
                cityIdNew = em.merge(cityIdNew);
            }
            if (availabilityIdOld != null && !availabilityIdOld.equals(availabilityIdNew)) {
                availabilityIdOld.getSocialProfileCollection().remove(socialProfile);
                availabilityIdOld = em.merge(availabilityIdOld);
            }
            if (availabilityIdNew != null && !availabilityIdNew.equals(availabilityIdOld)) {
                availabilityIdNew.getSocialProfileCollection().add(socialProfile);
                availabilityIdNew = em.merge(availabilityIdNew);
            }
            if (socialProfileVisibilityNew != null && !socialProfileVisibilityNew.equals(socialProfileVisibilityOld)) {
                SocialProfile oldSocialProfileOfSocialProfileVisibility = socialProfileVisibilityNew.getSocialProfile();
                if (oldSocialProfileOfSocialProfileVisibility != null) {
                    oldSocialProfileOfSocialProfileVisibility.setSocialProfileVisibility(null);
                    oldSocialProfileOfSocialProfileVisibility = em.merge(oldSocialProfileOfSocialProfileVisibility);
                }
                socialProfileVisibilityNew.setSocialProfile(socialProfile);
                socialProfileVisibilityNew = em.merge(socialProfileVisibilityNew);
            }
            if (subnetworkIdOld != null && !subnetworkIdOld.equals(subnetworkIdNew)) {
                subnetworkIdOld.getSocialProfileCollection().remove(socialProfile);
                subnetworkIdOld = em.merge(subnetworkIdOld);
            }
            if (subnetworkIdNew != null && !subnetworkIdNew.equals(subnetworkIdOld)) {
                subnetworkIdNew.getSocialProfileCollection().add(socialProfile);
                subnetworkIdNew = em.merge(subnetworkIdNew);
            }
            if (languageIdOld != null && !languageIdOld.equals(languageIdNew)) {
                languageIdOld.getSocialProfileCollection().remove(socialProfile);
                languageIdOld = em.merge(languageIdOld);
            }
            if (languageIdNew != null && !languageIdNew.equals(languageIdOld)) {
                languageIdNew.getSocialProfileCollection().add(socialProfile);
                languageIdNew = em.merge(languageIdNew);
            }
            if (scholarityIdOld != null && !scholarityIdOld.equals(scholarityIdNew)) {
                scholarityIdOld.getSocialProfileCollection().remove(socialProfile);
                scholarityIdOld = em.merge(scholarityIdOld);
            }
            if (scholarityIdNew != null && !scholarityIdNew.equals(scholarityIdOld)) {
                scholarityIdNew.getSocialProfileCollection().add(socialProfile);
                scholarityIdNew = em.merge(scholarityIdNew);
            }
            for (Interests interestsCollectionOldInterests : interestsCollectionOld) {
                if (!interestsCollectionNew.contains(interestsCollectionOldInterests)) {
                    interestsCollectionOldInterests.getSocialProfileCollection().remove(socialProfile);
                    interestsCollectionOldInterests = em.merge(interestsCollectionOldInterests);
                }
            }
            for (Interests interestsCollectionNewInterests : interestsCollectionNew) {
                if (!interestsCollectionOld.contains(interestsCollectionNewInterests)) {
                    interestsCollectionNewInterests.getSocialProfileCollection().add(socialProfile);
                    interestsCollectionNewInterests = em.merge(interestsCollectionNewInterests);
                }
            }
            for (Experiences experiencesCollectionNewExperiences : experiencesCollectionNew) {
                if (!experiencesCollectionOld.contains(experiencesCollectionNewExperiences)) {
                    SocialProfile oldSocialProfileOfExperiencesCollectionNewExperiences = experiencesCollectionNewExperiences.getSocialProfile();
                    experiencesCollectionNewExperiences.setSocialProfile(socialProfile);
                    experiencesCollectionNewExperiences = em.merge(experiencesCollectionNewExperiences);
                    if (oldSocialProfileOfExperiencesCollectionNewExperiences != null && !oldSocialProfileOfExperiencesCollectionNewExperiences.equals(socialProfile)) {
                        oldSocialProfileOfExperiencesCollectionNewExperiences.getExperiencesCollection().remove(experiencesCollectionNewExperiences);
                        oldSocialProfileOfExperiencesCollectionNewExperiences = em.merge(oldSocialProfileOfExperiencesCollectionNewExperiences);
                    }
                }
            }
            for (Educations educationsCollectionNewEducations : educationsCollectionNew) {
                if (!educationsCollectionOld.contains(educationsCollectionNewEducations)) {
                    SocialProfile oldSocialProfileOfEducationsCollectionNewEducations = educationsCollectionNewEducations.getSocialProfile();
                    educationsCollectionNewEducations.setSocialProfile(socialProfile);
                    educationsCollectionNewEducations = em.merge(educationsCollectionNewEducations);
                    if (oldSocialProfileOfEducationsCollectionNewEducations != null && !oldSocialProfileOfEducationsCollectionNewEducations.equals(socialProfile)) {
                        oldSocialProfileOfEducationsCollectionNewEducations.getEducationsCollection().remove(educationsCollectionNewEducations);
                        oldSocialProfileOfEducationsCollectionNewEducations = em.merge(oldSocialProfileOfEducationsCollectionNewEducations);
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
                String id = socialProfile.getTokenId();
                if (findSocialProfile(id) == null) {
                    throw new NonexistentEntityException("The socialProfile with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SocialProfile socialProfile;
            try {
                socialProfile = em.getReference(SocialProfile.class, id);
                socialProfile.getTokenId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The socialProfile with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            SocialProfileVisibility socialProfileVisibilityOrphanCheck = socialProfile.getSocialProfileVisibility();
            if (socialProfileVisibilityOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SocialProfile (" + socialProfile + ") cannot be destroyed since the SocialProfileVisibility " + socialProfileVisibilityOrphanCheck + " in its socialProfileVisibility field has a non-nullable socialProfile field.");
            }
            Collection<Experiences> experiencesCollectionOrphanCheck = socialProfile.getExperiencesCollection();
            for (Experiences experiencesCollectionOrphanCheckExperiences : experiencesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SocialProfile (" + socialProfile + ") cannot be destroyed since the Experiences " + experiencesCollectionOrphanCheckExperiences + " in its experiencesCollection field has a non-nullable socialProfile field.");
            }
            Collection<Educations> educationsCollectionOrphanCheck = socialProfile.getEducationsCollection();
            for (Educations educationsCollectionOrphanCheckEducations : educationsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SocialProfile (" + socialProfile + ") cannot be destroyed since the Educations " + educationsCollectionOrphanCheckEducations + " in its educationsCollection field has a non-nullable socialProfile field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Users users = socialProfile.getUsers();
            if (users != null) {
                users.setSocialProfile(null);
                users = em.merge(users);
            }
            State stateId = socialProfile.getStateId();
            if (stateId != null) {
                stateId.getSocialProfileCollection().remove(socialProfile);
                stateId = em.merge(stateId);
            }
            Occupations occupationsId = socialProfile.getOccupationsId();
            if (occupationsId != null) {
                occupationsId.getSocialProfileCollection().remove(socialProfile);
                occupationsId = em.merge(occupationsId);
            }
            Country countryId = socialProfile.getCountryId();
            if (countryId != null) {
                countryId.getSocialProfileCollection().remove(socialProfile);
                countryId = em.merge(countryId);
            }
            City cityId = socialProfile.getCityId();
            if (cityId != null) {
                cityId.getSocialProfileCollection().remove(socialProfile);
                cityId = em.merge(cityId);
            }
            Availability availabilityId = socialProfile.getAvailabilityId();
            if (availabilityId != null) {
                availabilityId.getSocialProfileCollection().remove(socialProfile);
                availabilityId = em.merge(availabilityId);
            }
            Subnetwork subnetworkId = socialProfile.getSubnetworkId();
            if (subnetworkId != null) {
                subnetworkId.getSocialProfileCollection().remove(socialProfile);
                subnetworkId = em.merge(subnetworkId);
            }
            Language languageId = socialProfile.getLanguageId();
            if (languageId != null) {
                languageId.getSocialProfileCollection().remove(socialProfile);
                languageId = em.merge(languageId);
            }
            Scholarity scholarityId = socialProfile.getScholarityId();
            if (scholarityId != null) {
                scholarityId.getSocialProfileCollection().remove(socialProfile);
                scholarityId = em.merge(scholarityId);
            }
            Collection<Interests> interestsCollection = socialProfile.getInterestsCollection();
            for (Interests interestsCollectionInterests : interestsCollection) {
                interestsCollectionInterests.getSocialProfileCollection().remove(socialProfile);
                interestsCollectionInterests = em.merge(interestsCollectionInterests);
            }
            em.remove(socialProfile);
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

    public List<SocialProfile> findSocialProfileEntities() {
        return findSocialProfileEntities(true, -1, -1);
    }

    public List<SocialProfile> findSocialProfileEntities(int maxResults, int firstResult) {
        return findSocialProfileEntities(false, maxResults, firstResult);
    }

    private List<SocialProfile> findSocialProfileEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SocialProfile.class));
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

    public SocialProfile findSocialProfile(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SocialProfile.class, id);
        } finally {
            em.close();
        }
    }

    public int getSocialProfileCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SocialProfile> rt = cq.from(SocialProfile.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public SocialProfile findSocialProfile(Integer id) {
        EntityManager em = getEntityManager();
        try {
            SocialProfile socialProfile = (SocialProfile) em.createNativeQuery("select * from primata_social_profile "
                    + "where social_profile_id = " + id, SocialProfile.class).getSingleResult();
            return socialProfile;
        } finally {
            em.close();
        }
    }
    /**
     *
     * @return
     */
    public List loadAllSocialProfileAuthorization(Integer subnetworkdId) {
        EntityManager em = getEntityManager();
        
        try {
            Query y = em.createNativeQuery("select s.*,a.* from primata_social_profile s "
                    + "join primata_authorization a on s.token_id = a.token_id "
                    + "where s.subnetwork_id = "+ subnetworkdId, "SocialProfileAuthorization");
            return y.getResultList();
        
        } finally {
            em.close();
        }
    }

    public List<SocialProfile> loadUserSearchList(SocialProfile socialProfile, Educations education, Integer experienceTime, Interests interest) {
        EntityManager em = getEntityManager();
        try {
            String sql = "select * from primata_social_profile p ";

            if (socialProfile.getOccupationsId().getOccupationsTypeId().getId() != 0) {
                sql += "join primata_occupations o on o.id = p.occupations_id ";
            }

            if (education.getNameId().getId() != 0 || education.getLocationId().getId() != 0) {
                sql += "join primata_educations e on e.token_id = p.token_id ";
            }

            if (interest.getTypeId().getId() != 0) {
                sql += "join primata_social_profile_interests pspi on pspi.social_profile_id = p.social_profile_id "
                        + "join primata_interests pi on pi.id = pspi.interests_id ";
            }

            if (experienceTime != 0) {
                sql += "join primata_experiences ex on ex.token_id = p.token_id ";
            }

            sql += "where (UPPER(p.name) like '" + socialProfile.getName().toUpperCase() + "%') ";

            if (socialProfile.getOccupationsId().getOccupationsTypeId().getId() != 0) {
                sql += "and o.occupations_type_id = '" + socialProfile.getOccupationsId().getOccupationsTypeId().getId() + "' ";
            }

            if (experienceTime != 0) {
                Integer days;
                if (experienceTime == 6) {
                    days = 365 / 2;
                } else {
                    days = 365 * experienceTime;
                }
                sql += "and (select sum(exp.data_end - exp.data_begin) from primata_experiences exp where exp.token_id = p.token_id) > '" + days + "' ";
            }

            if (education.getNameId().getId() != 0) {
                sql += "and e.name_id = '" + education.getNameId().getId() + "' ";
            }

            if (education.getLocationId().getId() != 0) {
                sql += "and e.location_id = '" + education.getLocationId().getId() + "' ";
            }

            if (interest.getTypeId().getId() != 0) {
                sql += "and pi.type_id = '" + interest.getTypeId().getId() + "' ";
            }

            if (interest.getId() != 0) {
                sql += "and pspi.interests_id = '" + interest.getId() + "' ";
            }

            if (socialProfile.getGender() != null) {
                sql += "and p.gender = '" + socialProfile.getGender() + "' ";
            }
            if (socialProfile.getCountryId().getId() != 0) {
                sql += "and p.country_id = '" + socialProfile.getCountryId().getId() + "' ";
            }
            if (socialProfile.getStateId().getId() != 0) {
                sql += "and p.state_id = '" + socialProfile.getStateId().getId() + "' ";
            }
            if (socialProfile.getCityId().getId() != 0) {
                sql += "and p.city_id = '" + socialProfile.getCityId().getId() + "' ";
            }
            if (socialProfile.getOccupationsId().getId() != 0) {
                sql += "and p.occupations_id = '" + socialProfile.getOccupationsId().getId() + "' ";
            }
            if (socialProfile.getScholarityId().getId() != 0) {
                sql += "and p.scholarity_id = '" + socialProfile.getScholarityId().getId() + "' ";
            }
            List<SocialProfile> usersList = (List<SocialProfile>) em.createNativeQuery(sql, SocialProfile.class).getResultList();
            return usersList;
        } finally {
            em.close();
        }
    }
}
