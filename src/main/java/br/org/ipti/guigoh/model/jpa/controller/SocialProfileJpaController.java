/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import br.org.ipti.guigoh.model.jpa.exceptions.IllegalOrphanException;
import br.org.ipti.guigoh.model.jpa.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.exceptions.PreexistingEntityException;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.org.ipti.guigoh.model.entity.SocialProfileVisibility;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.entity.Subnetwork;
import br.org.ipti.guigoh.model.entity.State;
import br.org.ipti.guigoh.model.entity.Scholarity;
import br.org.ipti.guigoh.model.entity.Occupations;
import br.org.ipti.guigoh.model.entity.Language;
import br.org.ipti.guigoh.model.entity.Country;
import br.org.ipti.guigoh.model.entity.City;
import br.org.ipti.guigoh.model.entity.Availability;
import br.org.ipti.guigoh.model.entity.Role;
import br.org.ipti.guigoh.model.entity.Interests;
import java.util.ArrayList;
import java.util.Collection;
import br.org.ipti.guigoh.model.entity.Experiences;
import br.org.ipti.guigoh.model.entity.Educations;
import br.org.ipti.guigoh.model.entity.DiscussionTopicWarnings;
import br.org.ipti.guigoh.model.entity.DiscussionTopic;
import br.org.ipti.guigoh.model.entity.DiscussionTopicMsg;
import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author ipti008
 */
public class SocialProfileJpaController implements Serializable {
    
    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();

    public SocialProfileJpaController() {
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
        if (socialProfile.getDiscussionTopicWarningsCollection() == null) {
            socialProfile.setDiscussionTopicWarningsCollection(new ArrayList<DiscussionTopicWarnings>());
        }
        if (socialProfile.getDiscussionTopicCollection() == null) {
            socialProfile.setDiscussionTopicCollection(new ArrayList<DiscussionTopic>());
        }
        if (socialProfile.getDiscussionTopicMsgCollection() == null) {
            socialProfile.setDiscussionTopicMsgCollection(new ArrayList<DiscussionTopicMsg>());
        }
        if (socialProfile.getEducationalObjectCollection() == null) {
            socialProfile.setEducationalObjectCollection(new ArrayList<EducationalObject>());
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
            SocialProfileVisibility socialProfileVisibility = socialProfile.getSocialProfileVisibility();
            if (socialProfileVisibility != null) {
                socialProfileVisibility = em.getReference(socialProfileVisibility.getClass(), socialProfileVisibility.getSocialProfileId());
                socialProfile.setSocialProfileVisibility(socialProfileVisibility);
            }
            Users users = socialProfile.getUsers();
            if (users != null) {
                users = em.getReference(users.getClass(), users.getUsername());
                socialProfile.setUsers(users);
            }
            Subnetwork subnetworkId = socialProfile.getSubnetworkId();
            if (subnetworkId != null) {
                subnetworkId = em.getReference(subnetworkId.getClass(), subnetworkId.getId());
                socialProfile.setSubnetworkId(subnetworkId);
            }
            State stateId = socialProfile.getStateId();
            if (stateId != null) {
                stateId = em.getReference(stateId.getClass(), stateId.getId());
                socialProfile.setStateId(stateId);
            }
            Scholarity scholarityId = socialProfile.getScholarityId();
            if (scholarityId != null) {
                scholarityId = em.getReference(scholarityId.getClass(), scholarityId.getId());
                socialProfile.setScholarityId(scholarityId);
            }
            Occupations occupationsId = socialProfile.getOccupationsId();
            if (occupationsId != null) {
                occupationsId = em.getReference(occupationsId.getClass(), occupationsId.getId());
                socialProfile.setOccupationsId(occupationsId);
            }
            Language languageId = socialProfile.getLanguageId();
            if (languageId != null) {
                languageId = em.getReference(languageId.getClass(), languageId.getId());
                socialProfile.setLanguageId(languageId);
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
            Role roleId = socialProfile.getRoleId();
            if (roleId != null) {
                roleId = em.getReference(roleId.getClass(), roleId.getId());
                socialProfile.setRoleId(roleId);
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
            Collection<DiscussionTopicWarnings> attachedDiscussionTopicWarningsCollection = new ArrayList<DiscussionTopicWarnings>();
            for (DiscussionTopicWarnings discussionTopicWarningsCollectionDiscussionTopicWarningsToAttach : socialProfile.getDiscussionTopicWarningsCollection()) {
                discussionTopicWarningsCollectionDiscussionTopicWarningsToAttach = em.getReference(discussionTopicWarningsCollectionDiscussionTopicWarningsToAttach.getClass(), discussionTopicWarningsCollectionDiscussionTopicWarningsToAttach.getId());
                attachedDiscussionTopicWarningsCollection.add(discussionTopicWarningsCollectionDiscussionTopicWarningsToAttach);
            }
            socialProfile.setDiscussionTopicWarningsCollection(attachedDiscussionTopicWarningsCollection);
            Collection<DiscussionTopic> attachedDiscussionTopicCollection = new ArrayList<DiscussionTopic>();
            for (DiscussionTopic discussionTopicCollectionDiscussionTopicToAttach : socialProfile.getDiscussionTopicCollection()) {
                discussionTopicCollectionDiscussionTopicToAttach = em.getReference(discussionTopicCollectionDiscussionTopicToAttach.getClass(), discussionTopicCollectionDiscussionTopicToAttach.getId());
                attachedDiscussionTopicCollection.add(discussionTopicCollectionDiscussionTopicToAttach);
            }
            socialProfile.setDiscussionTopicCollection(attachedDiscussionTopicCollection);
            Collection<DiscussionTopicMsg> attachedDiscussionTopicMsgCollection = new ArrayList<DiscussionTopicMsg>();
            for (DiscussionTopicMsg discussionTopicMsgCollectionDiscussionTopicMsgToAttach : socialProfile.getDiscussionTopicMsgCollection()) {
                discussionTopicMsgCollectionDiscussionTopicMsgToAttach = em.getReference(discussionTopicMsgCollectionDiscussionTopicMsgToAttach.getClass(), discussionTopicMsgCollectionDiscussionTopicMsgToAttach.getId());
                attachedDiscussionTopicMsgCollection.add(discussionTopicMsgCollectionDiscussionTopicMsgToAttach);
            }
            socialProfile.setDiscussionTopicMsgCollection(attachedDiscussionTopicMsgCollection);
            Collection<EducationalObject> attachedEducationalObjectCollection = new ArrayList<EducationalObject>();
            for (EducationalObject educationalObjectCollectionEducationalObjectToAttach : socialProfile.getEducationalObjectCollection()) {
                educationalObjectCollectionEducationalObjectToAttach = em.getReference(educationalObjectCollectionEducationalObjectToAttach.getClass(), educationalObjectCollectionEducationalObjectToAttach.getId());
                attachedEducationalObjectCollection.add(educationalObjectCollectionEducationalObjectToAttach);
            }
            socialProfile.setEducationalObjectCollection(attachedEducationalObjectCollection);
            em.persist(socialProfile);
            if (socialProfileVisibility != null) {
                SocialProfile oldSocialProfileOfSocialProfileVisibility = socialProfileVisibility.getSocialProfile();
                if (oldSocialProfileOfSocialProfileVisibility != null) {
                    oldSocialProfileOfSocialProfileVisibility.setSocialProfileVisibility(null);
                    oldSocialProfileOfSocialProfileVisibility = em.merge(oldSocialProfileOfSocialProfileVisibility);
                }
                socialProfileVisibility.setSocialProfile(socialProfile);
                socialProfileVisibility = em.merge(socialProfileVisibility);
            }
            if (users != null) {
                users.setSocialProfile(socialProfile);
                users = em.merge(users);
            }
            if (subnetworkId != null) {
                subnetworkId.getSocialProfileCollection().add(socialProfile);
                subnetworkId = em.merge(subnetworkId);
            }
            if (stateId != null) {
                stateId.getSocialProfileCollection().add(socialProfile);
                stateId = em.merge(stateId);
            }
            if (scholarityId != null) {
                scholarityId.getSocialProfileCollection().add(socialProfile);
                scholarityId = em.merge(scholarityId);
            }
            if (occupationsId != null) {
                occupationsId.getSocialProfileCollection().add(socialProfile);
                occupationsId = em.merge(occupationsId);
            }
            if (languageId != null) {
                languageId.getSocialProfileCollection().add(socialProfile);
                languageId = em.merge(languageId);
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
            if (roleId != null) {
                roleId.getSocialProfileCollection().add(socialProfile);
                roleId = em.merge(roleId);
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
            for (DiscussionTopicWarnings discussionTopicWarningsCollectionDiscussionTopicWarnings : socialProfile.getDiscussionTopicWarningsCollection()) {
                SocialProfile oldSocialProfileIdOfDiscussionTopicWarningsCollectionDiscussionTopicWarnings = discussionTopicWarningsCollectionDiscussionTopicWarnings.getSocialProfileId();
                discussionTopicWarningsCollectionDiscussionTopicWarnings.setSocialProfileId(socialProfile);
                discussionTopicWarningsCollectionDiscussionTopicWarnings = em.merge(discussionTopicWarningsCollectionDiscussionTopicWarnings);
                if (oldSocialProfileIdOfDiscussionTopicWarningsCollectionDiscussionTopicWarnings != null) {
                    oldSocialProfileIdOfDiscussionTopicWarningsCollectionDiscussionTopicWarnings.getDiscussionTopicWarningsCollection().remove(discussionTopicWarningsCollectionDiscussionTopicWarnings);
                    oldSocialProfileIdOfDiscussionTopicWarningsCollectionDiscussionTopicWarnings = em.merge(oldSocialProfileIdOfDiscussionTopicWarningsCollectionDiscussionTopicWarnings);
                }
            }
            for (DiscussionTopic discussionTopicCollectionDiscussionTopic : socialProfile.getDiscussionTopicCollection()) {
                SocialProfile oldSocialProfileIdOfDiscussionTopicCollectionDiscussionTopic = discussionTopicCollectionDiscussionTopic.getSocialProfileId();
                discussionTopicCollectionDiscussionTopic.setSocialProfileId(socialProfile);
                discussionTopicCollectionDiscussionTopic = em.merge(discussionTopicCollectionDiscussionTopic);
                if (oldSocialProfileIdOfDiscussionTopicCollectionDiscussionTopic != null) {
                    oldSocialProfileIdOfDiscussionTopicCollectionDiscussionTopic.getDiscussionTopicCollection().remove(discussionTopicCollectionDiscussionTopic);
                    oldSocialProfileIdOfDiscussionTopicCollectionDiscussionTopic = em.merge(oldSocialProfileIdOfDiscussionTopicCollectionDiscussionTopic);
                }
            }
            for (DiscussionTopicMsg discussionTopicMsgCollectionDiscussionTopicMsg : socialProfile.getDiscussionTopicMsgCollection()) {
                SocialProfile oldSocialProfileIdOfDiscussionTopicMsgCollectionDiscussionTopicMsg = discussionTopicMsgCollectionDiscussionTopicMsg.getSocialProfileId();
                discussionTopicMsgCollectionDiscussionTopicMsg.setSocialProfileId(socialProfile);
                discussionTopicMsgCollectionDiscussionTopicMsg = em.merge(discussionTopicMsgCollectionDiscussionTopicMsg);
                if (oldSocialProfileIdOfDiscussionTopicMsgCollectionDiscussionTopicMsg != null) {
                    oldSocialProfileIdOfDiscussionTopicMsgCollectionDiscussionTopicMsg.getDiscussionTopicMsgCollection().remove(discussionTopicMsgCollectionDiscussionTopicMsg);
                    oldSocialProfileIdOfDiscussionTopicMsgCollectionDiscussionTopicMsg = em.merge(oldSocialProfileIdOfDiscussionTopicMsgCollectionDiscussionTopicMsg);
                }
            }
            for (EducationalObject educationalObjectCollectionEducationalObject : socialProfile.getEducationalObjectCollection()) {
                SocialProfile oldSocialProfileIdOfEducationalObjectCollectionEducationalObject = educationalObjectCollectionEducationalObject.getSocialProfileId();
                educationalObjectCollectionEducationalObject.setSocialProfileId(socialProfile);
                educationalObjectCollectionEducationalObject = em.merge(educationalObjectCollectionEducationalObject);
                if (oldSocialProfileIdOfEducationalObjectCollectionEducationalObject != null) {
                    oldSocialProfileIdOfEducationalObjectCollectionEducationalObject.getEducationalObjectCollection().remove(educationalObjectCollectionEducationalObject);
                    oldSocialProfileIdOfEducationalObjectCollectionEducationalObject = em.merge(oldSocialProfileIdOfEducationalObjectCollectionEducationalObject);
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
            SocialProfileVisibility socialProfileVisibilityOld = persistentSocialProfile.getSocialProfileVisibility();
            SocialProfileVisibility socialProfileVisibilityNew = socialProfile.getSocialProfileVisibility();
            Users usersOld = persistentSocialProfile.getUsers();
            Users usersNew = socialProfile.getUsers();
            Subnetwork subnetworkIdOld = persistentSocialProfile.getSubnetworkId();
            Subnetwork subnetworkIdNew = socialProfile.getSubnetworkId();
            State stateIdOld = persistentSocialProfile.getStateId();
            State stateIdNew = socialProfile.getStateId();
            Scholarity scholarityIdOld = persistentSocialProfile.getScholarityId();
            Scholarity scholarityIdNew = socialProfile.getScholarityId();
            Occupations occupationsIdOld = persistentSocialProfile.getOccupationsId();
            Occupations occupationsIdNew = socialProfile.getOccupationsId();
            Language languageIdOld = persistentSocialProfile.getLanguageId();
            Language languageIdNew = socialProfile.getLanguageId();
            Country countryIdOld = persistentSocialProfile.getCountryId();
            Country countryIdNew = socialProfile.getCountryId();
            City cityIdOld = persistentSocialProfile.getCityId();
            City cityIdNew = socialProfile.getCityId();
            Availability availabilityIdOld = persistentSocialProfile.getAvailabilityId();
            Availability availabilityIdNew = socialProfile.getAvailabilityId();
            Role roleIdOld = persistentSocialProfile.getRoleId();
            Role roleIdNew = socialProfile.getRoleId();
            Collection<Interests> interestsCollectionOld = persistentSocialProfile.getInterestsCollection();
            Collection<Interests> interestsCollectionNew = socialProfile.getInterestsCollection();
            Collection<Experiences> experiencesCollectionOld = persistentSocialProfile.getExperiencesCollection();
            Collection<Experiences> experiencesCollectionNew = socialProfile.getExperiencesCollection();
            Collection<Educations> educationsCollectionOld = persistentSocialProfile.getEducationsCollection();
            Collection<Educations> educationsCollectionNew = socialProfile.getEducationsCollection();
            Collection<DiscussionTopicWarnings> discussionTopicWarningsCollectionOld = persistentSocialProfile.getDiscussionTopicWarningsCollection();
            Collection<DiscussionTopicWarnings> discussionTopicWarningsCollectionNew = socialProfile.getDiscussionTopicWarningsCollection();
            Collection<DiscussionTopic> discussionTopicCollectionOld = persistentSocialProfile.getDiscussionTopicCollection();
            Collection<DiscussionTopic> discussionTopicCollectionNew = socialProfile.getDiscussionTopicCollection();
            Collection<DiscussionTopicMsg> discussionTopicMsgCollectionOld = persistentSocialProfile.getDiscussionTopicMsgCollection();
            Collection<DiscussionTopicMsg> discussionTopicMsgCollectionNew = socialProfile.getDiscussionTopicMsgCollection();
            Collection<EducationalObject> educationalObjectCollectionOld = persistentSocialProfile.getEducationalObjectCollection();
            Collection<EducationalObject> educationalObjectCollectionNew = socialProfile.getEducationalObjectCollection();
            List<String> illegalOrphanMessages = null;
            if (socialProfileVisibilityOld != null && !socialProfileVisibilityOld.equals(socialProfileVisibilityNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain SocialProfileVisibility " + socialProfileVisibilityOld + " since its socialProfile field is not nullable.");
            }
            if (usersNew != null && !usersNew.equals(usersOld)) {
                SocialProfile oldSocialProfileOfUsers = usersNew.getSocialProfile();
                if (oldSocialProfileOfUsers != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Users " + usersNew + " already has an item of type SocialProfile whose users column cannot be null. Please make another selection for the users field.");
                }
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
            for (DiscussionTopicWarnings discussionTopicWarningsCollectionOldDiscussionTopicWarnings : discussionTopicWarningsCollectionOld) {
                if (!discussionTopicWarningsCollectionNew.contains(discussionTopicWarningsCollectionOldDiscussionTopicWarnings)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DiscussionTopicWarnings " + discussionTopicWarningsCollectionOldDiscussionTopicWarnings + " since its socialProfileId field is not nullable.");
                }
            }
            for (DiscussionTopic discussionTopicCollectionOldDiscussionTopic : discussionTopicCollectionOld) {
                if (!discussionTopicCollectionNew.contains(discussionTopicCollectionOldDiscussionTopic)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DiscussionTopic " + discussionTopicCollectionOldDiscussionTopic + " since its socialProfileId field is not nullable.");
                }
            }
            for (DiscussionTopicMsg discussionTopicMsgCollectionOldDiscussionTopicMsg : discussionTopicMsgCollectionOld) {
                if (!discussionTopicMsgCollectionNew.contains(discussionTopicMsgCollectionOldDiscussionTopicMsg)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DiscussionTopicMsg " + discussionTopicMsgCollectionOldDiscussionTopicMsg + " since its socialProfileId field is not nullable.");
                }
            }
            for (EducationalObject educationalObjectCollectionOldEducationalObject : educationalObjectCollectionOld) {
                if (!educationalObjectCollectionNew.contains(educationalObjectCollectionOldEducationalObject)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EducationalObject " + educationalObjectCollectionOldEducationalObject + " since its socialProfileId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (socialProfileVisibilityNew != null) {
                socialProfileVisibilityNew = em.getReference(socialProfileVisibilityNew.getClass(), socialProfileVisibilityNew.getSocialProfileId());
                socialProfile.setSocialProfileVisibility(socialProfileVisibilityNew);
            }
            if (usersNew != null) {
                usersNew = em.getReference(usersNew.getClass(), usersNew.getUsername());
                socialProfile.setUsers(usersNew);
            }
            if (subnetworkIdNew != null) {
                subnetworkIdNew = em.getReference(subnetworkIdNew.getClass(), subnetworkIdNew.getId());
                socialProfile.setSubnetworkId(subnetworkIdNew);
            }
            if (stateIdNew != null) {
                stateIdNew = em.getReference(stateIdNew.getClass(), stateIdNew.getId());
                socialProfile.setStateId(stateIdNew);
            }
            if (scholarityIdNew != null) {
                scholarityIdNew = em.getReference(scholarityIdNew.getClass(), scholarityIdNew.getId());
                socialProfile.setScholarityId(scholarityIdNew);
            }
            if (occupationsIdNew != null) {
                occupationsIdNew = em.getReference(occupationsIdNew.getClass(), occupationsIdNew.getId());
                socialProfile.setOccupationsId(occupationsIdNew);
            }
            if (languageIdNew != null) {
                languageIdNew = em.getReference(languageIdNew.getClass(), languageIdNew.getId());
                socialProfile.setLanguageId(languageIdNew);
            }
            if (countryIdNew != null) {
                countryIdNew = em.getReference(countryIdNew.getClass(), countryIdNew.getId());
                socialProfile.setCountryId(countryIdNew);
            }
            if (cityIdNew != null) {
                cityIdNew = em.getReference(cityIdNew.getClass(), cityIdNew.getId());
                socialProfile.setCityId(cityIdNew);
            }
            if (availabilityIdNew != null) {
                availabilityIdNew = em.getReference(availabilityIdNew.getClass(), availabilityIdNew.getId());
                socialProfile.setAvailabilityId(availabilityIdNew);
            }
            if (roleIdNew != null) {
                roleIdNew = em.getReference(roleIdNew.getClass(), roleIdNew.getId());
                socialProfile.setRoleId(roleIdNew);
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
            Collection<DiscussionTopicWarnings> attachedDiscussionTopicWarningsCollectionNew = new ArrayList<DiscussionTopicWarnings>();
            for (DiscussionTopicWarnings discussionTopicWarningsCollectionNewDiscussionTopicWarningsToAttach : discussionTopicWarningsCollectionNew) {
                discussionTopicWarningsCollectionNewDiscussionTopicWarningsToAttach = em.getReference(discussionTopicWarningsCollectionNewDiscussionTopicWarningsToAttach.getClass(), discussionTopicWarningsCollectionNewDiscussionTopicWarningsToAttach.getId());
                attachedDiscussionTopicWarningsCollectionNew.add(discussionTopicWarningsCollectionNewDiscussionTopicWarningsToAttach);
            }
            discussionTopicWarningsCollectionNew = attachedDiscussionTopicWarningsCollectionNew;
            socialProfile.setDiscussionTopicWarningsCollection(discussionTopicWarningsCollectionNew);
            Collection<DiscussionTopic> attachedDiscussionTopicCollectionNew = new ArrayList<DiscussionTopic>();
            for (DiscussionTopic discussionTopicCollectionNewDiscussionTopicToAttach : discussionTopicCollectionNew) {
                discussionTopicCollectionNewDiscussionTopicToAttach = em.getReference(discussionTopicCollectionNewDiscussionTopicToAttach.getClass(), discussionTopicCollectionNewDiscussionTopicToAttach.getId());
                attachedDiscussionTopicCollectionNew.add(discussionTopicCollectionNewDiscussionTopicToAttach);
            }
            discussionTopicCollectionNew = attachedDiscussionTopicCollectionNew;
            socialProfile.setDiscussionTopicCollection(discussionTopicCollectionNew);
            Collection<DiscussionTopicMsg> attachedDiscussionTopicMsgCollectionNew = new ArrayList<DiscussionTopicMsg>();
            for (DiscussionTopicMsg discussionTopicMsgCollectionNewDiscussionTopicMsgToAttach : discussionTopicMsgCollectionNew) {
                discussionTopicMsgCollectionNewDiscussionTopicMsgToAttach = em.getReference(discussionTopicMsgCollectionNewDiscussionTopicMsgToAttach.getClass(), discussionTopicMsgCollectionNewDiscussionTopicMsgToAttach.getId());
                attachedDiscussionTopicMsgCollectionNew.add(discussionTopicMsgCollectionNewDiscussionTopicMsgToAttach);
            }
            discussionTopicMsgCollectionNew = attachedDiscussionTopicMsgCollectionNew;
            socialProfile.setDiscussionTopicMsgCollection(discussionTopicMsgCollectionNew);
            Collection<EducationalObject> attachedEducationalObjectCollectionNew = new ArrayList<EducationalObject>();
            for (EducationalObject educationalObjectCollectionNewEducationalObjectToAttach : educationalObjectCollectionNew) {
                educationalObjectCollectionNewEducationalObjectToAttach = em.getReference(educationalObjectCollectionNewEducationalObjectToAttach.getClass(), educationalObjectCollectionNewEducationalObjectToAttach.getId());
                attachedEducationalObjectCollectionNew.add(educationalObjectCollectionNewEducationalObjectToAttach);
            }
            educationalObjectCollectionNew = attachedEducationalObjectCollectionNew;
            socialProfile.setEducationalObjectCollection(educationalObjectCollectionNew);
            socialProfile = em.merge(socialProfile);
            if (socialProfileVisibilityNew != null && !socialProfileVisibilityNew.equals(socialProfileVisibilityOld)) {
                SocialProfile oldSocialProfileOfSocialProfileVisibility = socialProfileVisibilityNew.getSocialProfile();
                if (oldSocialProfileOfSocialProfileVisibility != null) {
                    oldSocialProfileOfSocialProfileVisibility.setSocialProfileVisibility(null);
                    oldSocialProfileOfSocialProfileVisibility = em.merge(oldSocialProfileOfSocialProfileVisibility);
                }
                socialProfileVisibilityNew.setSocialProfile(socialProfile);
                socialProfileVisibilityNew = em.merge(socialProfileVisibilityNew);
            }
            if (usersOld != null && !usersOld.equals(usersNew)) {
                usersOld.setSocialProfile(null);
                usersOld = em.merge(usersOld);
            }
            if (usersNew != null && !usersNew.equals(usersOld)) {
                usersNew.setSocialProfile(socialProfile);
                usersNew = em.merge(usersNew);
            }
            if (subnetworkIdOld != null && !subnetworkIdOld.equals(subnetworkIdNew)) {
                subnetworkIdOld.getSocialProfileCollection().remove(socialProfile);
                subnetworkIdOld = em.merge(subnetworkIdOld);
            }
            if (subnetworkIdNew != null && !subnetworkIdNew.equals(subnetworkIdOld)) {
                subnetworkIdNew.getSocialProfileCollection().add(socialProfile);
                subnetworkIdNew = em.merge(subnetworkIdNew);
            }
            if (stateIdOld != null && !stateIdOld.equals(stateIdNew)) {
                stateIdOld.getSocialProfileCollection().remove(socialProfile);
                stateIdOld = em.merge(stateIdOld);
            }
            if (stateIdNew != null && !stateIdNew.equals(stateIdOld)) {
                stateIdNew.getSocialProfileCollection().add(socialProfile);
                stateIdNew = em.merge(stateIdNew);
            }
            if (scholarityIdOld != null && !scholarityIdOld.equals(scholarityIdNew)) {
                scholarityIdOld.getSocialProfileCollection().remove(socialProfile);
                scholarityIdOld = em.merge(scholarityIdOld);
            }
            if (scholarityIdNew != null && !scholarityIdNew.equals(scholarityIdOld)) {
                scholarityIdNew.getSocialProfileCollection().add(socialProfile);
                scholarityIdNew = em.merge(scholarityIdNew);
            }
            if (occupationsIdOld != null && !occupationsIdOld.equals(occupationsIdNew)) {
                occupationsIdOld.getSocialProfileCollection().remove(socialProfile);
                occupationsIdOld = em.merge(occupationsIdOld);
            }
            if (occupationsIdNew != null && !occupationsIdNew.equals(occupationsIdOld)) {
                occupationsIdNew.getSocialProfileCollection().add(socialProfile);
                occupationsIdNew = em.merge(occupationsIdNew);
            }
            if (languageIdOld != null && !languageIdOld.equals(languageIdNew)) {
                languageIdOld.getSocialProfileCollection().remove(socialProfile);
                languageIdOld = em.merge(languageIdOld);
            }
            if (languageIdNew != null && !languageIdNew.equals(languageIdOld)) {
                languageIdNew.getSocialProfileCollection().add(socialProfile);
                languageIdNew = em.merge(languageIdNew);
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
            if (roleIdOld != null && !roleIdOld.equals(roleIdNew)) {
                roleIdOld.getSocialProfileCollection().remove(socialProfile);
                roleIdOld = em.merge(roleIdOld);
            }
            if (roleIdNew != null && !roleIdNew.equals(roleIdOld)) {
                roleIdNew.getSocialProfileCollection().add(socialProfile);
                roleIdNew = em.merge(roleIdNew);
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
            for (DiscussionTopicWarnings discussionTopicWarningsCollectionNewDiscussionTopicWarnings : discussionTopicWarningsCollectionNew) {
                if (!discussionTopicWarningsCollectionOld.contains(discussionTopicWarningsCollectionNewDiscussionTopicWarnings)) {
                    SocialProfile oldSocialProfileIdOfDiscussionTopicWarningsCollectionNewDiscussionTopicWarnings = discussionTopicWarningsCollectionNewDiscussionTopicWarnings.getSocialProfileId();
                    discussionTopicWarningsCollectionNewDiscussionTopicWarnings.setSocialProfileId(socialProfile);
                    discussionTopicWarningsCollectionNewDiscussionTopicWarnings = em.merge(discussionTopicWarningsCollectionNewDiscussionTopicWarnings);
                    if (oldSocialProfileIdOfDiscussionTopicWarningsCollectionNewDiscussionTopicWarnings != null && !oldSocialProfileIdOfDiscussionTopicWarningsCollectionNewDiscussionTopicWarnings.equals(socialProfile)) {
                        oldSocialProfileIdOfDiscussionTopicWarningsCollectionNewDiscussionTopicWarnings.getDiscussionTopicWarningsCollection().remove(discussionTopicWarningsCollectionNewDiscussionTopicWarnings);
                        oldSocialProfileIdOfDiscussionTopicWarningsCollectionNewDiscussionTopicWarnings = em.merge(oldSocialProfileIdOfDiscussionTopicWarningsCollectionNewDiscussionTopicWarnings);
                    }
                }
            }
            for (DiscussionTopic discussionTopicCollectionNewDiscussionTopic : discussionTopicCollectionNew) {
                if (!discussionTopicCollectionOld.contains(discussionTopicCollectionNewDiscussionTopic)) {
                    SocialProfile oldSocialProfileIdOfDiscussionTopicCollectionNewDiscussionTopic = discussionTopicCollectionNewDiscussionTopic.getSocialProfileId();
                    discussionTopicCollectionNewDiscussionTopic.setSocialProfileId(socialProfile);
                    discussionTopicCollectionNewDiscussionTopic = em.merge(discussionTopicCollectionNewDiscussionTopic);
                    if (oldSocialProfileIdOfDiscussionTopicCollectionNewDiscussionTopic != null && !oldSocialProfileIdOfDiscussionTopicCollectionNewDiscussionTopic.equals(socialProfile)) {
                        oldSocialProfileIdOfDiscussionTopicCollectionNewDiscussionTopic.getDiscussionTopicCollection().remove(discussionTopicCollectionNewDiscussionTopic);
                        oldSocialProfileIdOfDiscussionTopicCollectionNewDiscussionTopic = em.merge(oldSocialProfileIdOfDiscussionTopicCollectionNewDiscussionTopic);
                    }
                }
            }
            for (DiscussionTopicMsg discussionTopicMsgCollectionNewDiscussionTopicMsg : discussionTopicMsgCollectionNew) {
                if (!discussionTopicMsgCollectionOld.contains(discussionTopicMsgCollectionNewDiscussionTopicMsg)) {
                    SocialProfile oldSocialProfileIdOfDiscussionTopicMsgCollectionNewDiscussionTopicMsg = discussionTopicMsgCollectionNewDiscussionTopicMsg.getSocialProfileId();
                    discussionTopicMsgCollectionNewDiscussionTopicMsg.setSocialProfileId(socialProfile);
                    discussionTopicMsgCollectionNewDiscussionTopicMsg = em.merge(discussionTopicMsgCollectionNewDiscussionTopicMsg);
                    if (oldSocialProfileIdOfDiscussionTopicMsgCollectionNewDiscussionTopicMsg != null && !oldSocialProfileIdOfDiscussionTopicMsgCollectionNewDiscussionTopicMsg.equals(socialProfile)) {
                        oldSocialProfileIdOfDiscussionTopicMsgCollectionNewDiscussionTopicMsg.getDiscussionTopicMsgCollection().remove(discussionTopicMsgCollectionNewDiscussionTopicMsg);
                        oldSocialProfileIdOfDiscussionTopicMsgCollectionNewDiscussionTopicMsg = em.merge(oldSocialProfileIdOfDiscussionTopicMsgCollectionNewDiscussionTopicMsg);
                    }
                }
            }
            for (EducationalObject educationalObjectCollectionNewEducationalObject : educationalObjectCollectionNew) {
                if (!educationalObjectCollectionOld.contains(educationalObjectCollectionNewEducationalObject)) {
                    SocialProfile oldSocialProfileIdOfEducationalObjectCollectionNewEducationalObject = educationalObjectCollectionNewEducationalObject.getSocialProfileId();
                    educationalObjectCollectionNewEducationalObject.setSocialProfileId(socialProfile);
                    educationalObjectCollectionNewEducationalObject = em.merge(educationalObjectCollectionNewEducationalObject);
                    if (oldSocialProfileIdOfEducationalObjectCollectionNewEducationalObject != null && !oldSocialProfileIdOfEducationalObjectCollectionNewEducationalObject.equals(socialProfile)) {
                        oldSocialProfileIdOfEducationalObjectCollectionNewEducationalObject.getEducationalObjectCollection().remove(educationalObjectCollectionNewEducationalObject);
                        oldSocialProfileIdOfEducationalObjectCollectionNewEducationalObject = em.merge(oldSocialProfileIdOfEducationalObjectCollectionNewEducationalObject);
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
            Collection<DiscussionTopicWarnings> discussionTopicWarningsCollectionOrphanCheck = socialProfile.getDiscussionTopicWarningsCollection();
            for (DiscussionTopicWarnings discussionTopicWarningsCollectionOrphanCheckDiscussionTopicWarnings : discussionTopicWarningsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SocialProfile (" + socialProfile + ") cannot be destroyed since the DiscussionTopicWarnings " + discussionTopicWarningsCollectionOrphanCheckDiscussionTopicWarnings + " in its discussionTopicWarningsCollection field has a non-nullable socialProfileId field.");
            }
            Collection<DiscussionTopic> discussionTopicCollectionOrphanCheck = socialProfile.getDiscussionTopicCollection();
            for (DiscussionTopic discussionTopicCollectionOrphanCheckDiscussionTopic : discussionTopicCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SocialProfile (" + socialProfile + ") cannot be destroyed since the DiscussionTopic " + discussionTopicCollectionOrphanCheckDiscussionTopic + " in its discussionTopicCollection field has a non-nullable socialProfileId field.");
            }
            Collection<DiscussionTopicMsg> discussionTopicMsgCollectionOrphanCheck = socialProfile.getDiscussionTopicMsgCollection();
            for (DiscussionTopicMsg discussionTopicMsgCollectionOrphanCheckDiscussionTopicMsg : discussionTopicMsgCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SocialProfile (" + socialProfile + ") cannot be destroyed since the DiscussionTopicMsg " + discussionTopicMsgCollectionOrphanCheckDiscussionTopicMsg + " in its discussionTopicMsgCollection field has a non-nullable socialProfileId field.");
            }
            Collection<EducationalObject> educationalObjectCollectionOrphanCheck = socialProfile.getEducationalObjectCollection();
            for (EducationalObject educationalObjectCollectionOrphanCheckEducationalObject : educationalObjectCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SocialProfile (" + socialProfile + ") cannot be destroyed since the EducationalObject " + educationalObjectCollectionOrphanCheckEducationalObject + " in its educationalObjectCollection field has a non-nullable socialProfileId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Users users = socialProfile.getUsers();
            if (users != null) {
                users.setSocialProfile(null);
                users = em.merge(users);
            }
            Subnetwork subnetworkId = socialProfile.getSubnetworkId();
            if (subnetworkId != null) {
                subnetworkId.getSocialProfileCollection().remove(socialProfile);
                subnetworkId = em.merge(subnetworkId);
            }
            State stateId = socialProfile.getStateId();
            if (stateId != null) {
                stateId.getSocialProfileCollection().remove(socialProfile);
                stateId = em.merge(stateId);
            }
            Scholarity scholarityId = socialProfile.getScholarityId();
            if (scholarityId != null) {
                scholarityId.getSocialProfileCollection().remove(socialProfile);
                scholarityId = em.merge(scholarityId);
            }
            Occupations occupationsId = socialProfile.getOccupationsId();
            if (occupationsId != null) {
                occupationsId.getSocialProfileCollection().remove(socialProfile);
                occupationsId = em.merge(occupationsId);
            }
            Language languageId = socialProfile.getLanguageId();
            if (languageId != null) {
                languageId.getSocialProfileCollection().remove(socialProfile);
                languageId = em.merge(languageId);
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
            Role roleId = socialProfile.getRoleId();
            if (roleId != null) {
                roleId.getSocialProfileCollection().remove(socialProfile);
                roleId = em.merge(roleId);
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
    
    public SocialProfile findSocialProfileBySocialProfileId(Integer id) {
        EntityManager em = getEntityManager();
        try {
            SocialProfile socialProfile = (SocialProfile) em.createNativeQuery("select * from social_profile "
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
            Query y = em.createNativeQuery("select s.*,a.* from social_profile s "
                    + "join authorization a on s.token_id = a.token_id "
                    + "where s.subnetwork_id = "+ subnetworkdId, "SocialProfileAuthorization");
            return y.getResultList();
        
        } finally {
            em.close();
        }
    }

    public List<SocialProfile> loadUserSearchList(SocialProfile socialProfile, Educations education, Integer experienceTime, Interests interest) {
        EntityManager em = getEntityManager();
        try {
            String sql = "select * from social_profile p ";

            if (socialProfile.getOccupationsId().getOccupationsTypeId().getId() != 0) {
                sql += "join occupations o on o.id = p.occupations_id ";
            }

            if (education.getNameId().getId() != 0 || education.getLocationId().getId() != 0) {
                sql += "join educations e on e.token_id = p.token_id ";
            }

            if (interest.getTypeId().getId() != 0) {
                sql += "join social_profile_interests pspi on pspi.social_profile_id = p.social_profile_id "
                        + "join interests pi on pi.id = pspi.interests_id ";
            }

            if (experienceTime != 0) {
                sql += "join experiences ex on ex.token_id = p.token_id ";
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
                sql += "and (select sum(exp.data_end - exp.data_begin) from experiences exp where exp.token_id = p.token_id) > '" + days + "' ";
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
            if (usersList == null) {
                return new ArrayList<>();
            }
            return usersList;
        } finally {
            em.close();
        }
    }
    
}
