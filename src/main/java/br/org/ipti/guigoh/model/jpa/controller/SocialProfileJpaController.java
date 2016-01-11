/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

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
import br.org.ipti.guigoh.model.entity.Experiences;
import java.util.ArrayList;
import java.util.Collection;
import br.org.ipti.guigoh.model.entity.Educations;
import br.org.ipti.guigoh.model.entity.DiscussionTopicWarnings;
import br.org.ipti.guigoh.model.entity.DiscussionTopic;
import br.org.ipti.guigoh.model.entity.DiscussionTopicMsg;
import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.EducationalObjectMessage;
import br.org.ipti.guigoh.model.entity.EducationalObjectLike;
import br.org.ipti.guigoh.model.entity.DocGuest;
import br.org.ipti.guigoh.model.entity.Doc;
import br.org.ipti.guigoh.model.entity.DocHistory;
import br.org.ipti.guigoh.model.entity.DocMessage;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.IllegalOrphanException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.PreexistingEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author iptipc008
 */
public class SocialProfileJpaController implements Serializable {

    public SocialProfileJpaController() {
    }

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SocialProfile socialProfile) throws IllegalOrphanException, PreexistingEntityException, RollbackFailureException, Exception {
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
        if (socialProfile.getEducationalObjectMessageCollection() == null) {
            socialProfile.setEducationalObjectMessageCollection(new ArrayList<EducationalObjectMessage>());
        }
        if (socialProfile.getEducationalObjectLikeCollection() == null) {
            socialProfile.setEducationalObjectLikeCollection(new ArrayList<EducationalObjectLike>());
        }
        if (socialProfile.getDocGuestCollection() == null) {
            socialProfile.setDocGuestCollection(new ArrayList<DocGuest>());
        }
        if (socialProfile.getDocCollection() == null) {
            socialProfile.setDocCollection(new ArrayList<Doc>());
        }
        if (socialProfile.getDocCollection1() == null) {
            socialProfile.setDocCollection1(new ArrayList<Doc>());
        }
        if (socialProfile.getDocHistoryCollection() == null) {
            socialProfile.setDocHistoryCollection(new ArrayList<DocHistory>());
        }
        if (socialProfile.getDocMessageCollection() == null) {
            socialProfile.setDocMessageCollection(new ArrayList<DocMessage>());
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
            Collection<EducationalObjectMessage> attachedEducationalObjectMessageCollection = new ArrayList<EducationalObjectMessage>();
            for (EducationalObjectMessage educationalObjectMessageCollectionEducationalObjectMessageToAttach : socialProfile.getEducationalObjectMessageCollection()) {
                educationalObjectMessageCollectionEducationalObjectMessageToAttach = em.getReference(educationalObjectMessageCollectionEducationalObjectMessageToAttach.getClass(), educationalObjectMessageCollectionEducationalObjectMessageToAttach.getId());
                attachedEducationalObjectMessageCollection.add(educationalObjectMessageCollectionEducationalObjectMessageToAttach);
            }
            socialProfile.setEducationalObjectMessageCollection(attachedEducationalObjectMessageCollection);
            Collection<EducationalObjectLike> attachedEducationalObjectLikeCollection = new ArrayList<EducationalObjectLike>();
            for (EducationalObjectLike educationalObjectLikeCollectionEducationalObjectLikeToAttach : socialProfile.getEducationalObjectLikeCollection()) {
                educationalObjectLikeCollectionEducationalObjectLikeToAttach = em.getReference(educationalObjectLikeCollectionEducationalObjectLikeToAttach.getClass(), educationalObjectLikeCollectionEducationalObjectLikeToAttach.getEducationalObjectLikePK());
                attachedEducationalObjectLikeCollection.add(educationalObjectLikeCollectionEducationalObjectLikeToAttach);
            }
            socialProfile.setEducationalObjectLikeCollection(attachedEducationalObjectLikeCollection);
            Collection<DocGuest> attachedDocGuestCollection = new ArrayList<DocGuest>();
            for (DocGuest docGuestCollectionDocGuestToAttach : socialProfile.getDocGuestCollection()) {
                docGuestCollectionDocGuestToAttach = em.getReference(docGuestCollectionDocGuestToAttach.getClass(), docGuestCollectionDocGuestToAttach.getId());
                attachedDocGuestCollection.add(docGuestCollectionDocGuestToAttach);
            }
            socialProfile.setDocGuestCollection(attachedDocGuestCollection);
            Collection<Doc> attachedDocCollection = new ArrayList<Doc>();
            for (Doc docCollectionDocToAttach : socialProfile.getDocCollection()) {
                docCollectionDocToAttach = em.getReference(docCollectionDocToAttach.getClass(), docCollectionDocToAttach.getId());
                attachedDocCollection.add(docCollectionDocToAttach);
            }
            socialProfile.setDocCollection(attachedDocCollection);
            Collection<Doc> attachedDocCollection1 = new ArrayList<Doc>();
            for (Doc docCollection1DocToAttach : socialProfile.getDocCollection1()) {
                docCollection1DocToAttach = em.getReference(docCollection1DocToAttach.getClass(), docCollection1DocToAttach.getId());
                attachedDocCollection1.add(docCollection1DocToAttach);
            }
            socialProfile.setDocCollection1(attachedDocCollection1);
            Collection<DocHistory> attachedDocHistoryCollection = new ArrayList<DocHistory>();
            for (DocHistory docHistoryCollectionDocHistoryToAttach : socialProfile.getDocHistoryCollection()) {
                docHistoryCollectionDocHistoryToAttach = em.getReference(docHistoryCollectionDocHistoryToAttach.getClass(), docHistoryCollectionDocHistoryToAttach.getId());
                attachedDocHistoryCollection.add(docHistoryCollectionDocHistoryToAttach);
            }
            socialProfile.setDocHistoryCollection(attachedDocHistoryCollection);
            Collection<DocMessage> attachedDocMessageCollection = new ArrayList<DocMessage>();
            for (DocMessage docMessageCollectionDocMessageToAttach : socialProfile.getDocMessageCollection()) {
                docMessageCollectionDocMessageToAttach = em.getReference(docMessageCollectionDocMessageToAttach.getClass(), docMessageCollectionDocMessageToAttach.getId());
                attachedDocMessageCollection.add(docMessageCollectionDocMessageToAttach);
            }
            socialProfile.setDocMessageCollection(attachedDocMessageCollection);
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
            for (EducationalObjectMessage educationalObjectMessageCollectionEducationalObjectMessage : socialProfile.getEducationalObjectMessageCollection()) {
                SocialProfile oldSocialProfileFkOfEducationalObjectMessageCollectionEducationalObjectMessage = educationalObjectMessageCollectionEducationalObjectMessage.getSocialProfileFk();
                educationalObjectMessageCollectionEducationalObjectMessage.setSocialProfileFk(socialProfile);
                educationalObjectMessageCollectionEducationalObjectMessage = em.merge(educationalObjectMessageCollectionEducationalObjectMessage);
                if (oldSocialProfileFkOfEducationalObjectMessageCollectionEducationalObjectMessage != null) {
                    oldSocialProfileFkOfEducationalObjectMessageCollectionEducationalObjectMessage.getEducationalObjectMessageCollection().remove(educationalObjectMessageCollectionEducationalObjectMessage);
                    oldSocialProfileFkOfEducationalObjectMessageCollectionEducationalObjectMessage = em.merge(oldSocialProfileFkOfEducationalObjectMessageCollectionEducationalObjectMessage);
                }
            }
            for (EducationalObjectLike educationalObjectLikeCollectionEducationalObjectLike : socialProfile.getEducationalObjectLikeCollection()) {
                SocialProfile oldSocialProfileOfEducationalObjectLikeCollectionEducationalObjectLike = educationalObjectLikeCollectionEducationalObjectLike.getSocialProfile();
                educationalObjectLikeCollectionEducationalObjectLike.setSocialProfile(socialProfile);
                educationalObjectLikeCollectionEducationalObjectLike = em.merge(educationalObjectLikeCollectionEducationalObjectLike);
                if (oldSocialProfileOfEducationalObjectLikeCollectionEducationalObjectLike != null) {
                    oldSocialProfileOfEducationalObjectLikeCollectionEducationalObjectLike.getEducationalObjectLikeCollection().remove(educationalObjectLikeCollectionEducationalObjectLike);
                    oldSocialProfileOfEducationalObjectLikeCollectionEducationalObjectLike = em.merge(oldSocialProfileOfEducationalObjectLikeCollectionEducationalObjectLike);
                }
            }
            for (DocGuest docGuestCollectionDocGuest : socialProfile.getDocGuestCollection()) {
                SocialProfile oldSocialProfileFkOfDocGuestCollectionDocGuest = docGuestCollectionDocGuest.getSocialProfileFk();
                docGuestCollectionDocGuest.setSocialProfileFk(socialProfile);
                docGuestCollectionDocGuest = em.merge(docGuestCollectionDocGuest);
                if (oldSocialProfileFkOfDocGuestCollectionDocGuest != null) {
                    oldSocialProfileFkOfDocGuestCollectionDocGuest.getDocGuestCollection().remove(docGuestCollectionDocGuest);
                    oldSocialProfileFkOfDocGuestCollectionDocGuest = em.merge(oldSocialProfileFkOfDocGuestCollectionDocGuest);
                }
            }
            for (Doc docCollectionDoc : socialProfile.getDocCollection()) {
                SocialProfile oldCreatorSocialProfileFkOfDocCollectionDoc = docCollectionDoc.getCreatorSocialProfileFk();
                docCollectionDoc.setCreatorSocialProfileFk(socialProfile);
                docCollectionDoc = em.merge(docCollectionDoc);
                if (oldCreatorSocialProfileFkOfDocCollectionDoc != null) {
                    oldCreatorSocialProfileFkOfDocCollectionDoc.getDocCollection().remove(docCollectionDoc);
                    oldCreatorSocialProfileFkOfDocCollectionDoc = em.merge(oldCreatorSocialProfileFkOfDocCollectionDoc);
                }
            }
            for (Doc docCollection1Doc : socialProfile.getDocCollection1()) {
                SocialProfile oldEditorSocialProfileFkOfDocCollection1Doc = docCollection1Doc.getEditorSocialProfileFk();
                docCollection1Doc.setEditorSocialProfileFk(socialProfile);
                docCollection1Doc = em.merge(docCollection1Doc);
                if (oldEditorSocialProfileFkOfDocCollection1Doc != null) {
                    oldEditorSocialProfileFkOfDocCollection1Doc.getDocCollection1().remove(docCollection1Doc);
                    oldEditorSocialProfileFkOfDocCollection1Doc = em.merge(oldEditorSocialProfileFkOfDocCollection1Doc);
                }
            }
            for (DocHistory docHistoryCollectionDocHistory : socialProfile.getDocHistoryCollection()) {
                SocialProfile oldEditorSocialProfileFkOfDocHistoryCollectionDocHistory = docHistoryCollectionDocHistory.getEditorSocialProfileFk();
                docHistoryCollectionDocHistory.setEditorSocialProfileFk(socialProfile);
                docHistoryCollectionDocHistory = em.merge(docHistoryCollectionDocHistory);
                if (oldEditorSocialProfileFkOfDocHistoryCollectionDocHistory != null) {
                    oldEditorSocialProfileFkOfDocHistoryCollectionDocHistory.getDocHistoryCollection().remove(docHistoryCollectionDocHistory);
                    oldEditorSocialProfileFkOfDocHistoryCollectionDocHistory = em.merge(oldEditorSocialProfileFkOfDocHistoryCollectionDocHistory);
                }
            }
            for (DocMessage docMessageCollectionDocMessage : socialProfile.getDocMessageCollection()) {
                SocialProfile oldSocialProfileFkOfDocMessageCollectionDocMessage = docMessageCollectionDocMessage.getSocialProfileFk();
                docMessageCollectionDocMessage.setSocialProfileFk(socialProfile);
                docMessageCollectionDocMessage = em.merge(docMessageCollectionDocMessage);
                if (oldSocialProfileFkOfDocMessageCollectionDocMessage != null) {
                    oldSocialProfileFkOfDocMessageCollectionDocMessage.getDocMessageCollection().remove(docMessageCollectionDocMessage);
                    oldSocialProfileFkOfDocMessageCollectionDocMessage = em.merge(oldSocialProfileFkOfDocMessageCollectionDocMessage);
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
            Collection<EducationalObjectMessage> educationalObjectMessageCollectionOld = persistentSocialProfile.getEducationalObjectMessageCollection();
            Collection<EducationalObjectMessage> educationalObjectMessageCollectionNew = socialProfile.getEducationalObjectMessageCollection();
            Collection<EducationalObjectLike> educationalObjectLikeCollectionOld = persistentSocialProfile.getEducationalObjectLikeCollection();
            Collection<EducationalObjectLike> educationalObjectLikeCollectionNew = socialProfile.getEducationalObjectLikeCollection();
            Collection<DocGuest> docGuestCollectionOld = persistentSocialProfile.getDocGuestCollection();
            Collection<DocGuest> docGuestCollectionNew = socialProfile.getDocGuestCollection();
            Collection<Doc> docCollectionOld = persistentSocialProfile.getDocCollection();
            Collection<Doc> docCollectionNew = socialProfile.getDocCollection();
            Collection<Doc> docCollection1Old = persistentSocialProfile.getDocCollection1();
            Collection<Doc> docCollection1New = socialProfile.getDocCollection1();
            Collection<DocHistory> docHistoryCollectionOld = persistentSocialProfile.getDocHistoryCollection();
            Collection<DocHistory> docHistoryCollectionNew = socialProfile.getDocHistoryCollection();
            Collection<DocMessage> docMessageCollectionOld = persistentSocialProfile.getDocMessageCollection();
            Collection<DocMessage> docMessageCollectionNew = socialProfile.getDocMessageCollection();
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
            for (EducationalObjectMessage educationalObjectMessageCollectionOldEducationalObjectMessage : educationalObjectMessageCollectionOld) {
                if (!educationalObjectMessageCollectionNew.contains(educationalObjectMessageCollectionOldEducationalObjectMessage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EducationalObjectMessage " + educationalObjectMessageCollectionOldEducationalObjectMessage + " since its socialProfileFk field is not nullable.");
                }
            }
            for (EducationalObjectLike educationalObjectLikeCollectionOldEducationalObjectLike : educationalObjectLikeCollectionOld) {
                if (!educationalObjectLikeCollectionNew.contains(educationalObjectLikeCollectionOldEducationalObjectLike)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EducationalObjectLike " + educationalObjectLikeCollectionOldEducationalObjectLike + " since its socialProfile field is not nullable.");
                }
            }
            for (DocGuest docGuestCollectionOldDocGuest : docGuestCollectionOld) {
                if (!docGuestCollectionNew.contains(docGuestCollectionOldDocGuest)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DocGuest " + docGuestCollectionOldDocGuest + " since its socialProfileFk field is not nullable.");
                }
            }
            for (Doc docCollectionOldDoc : docCollectionOld) {
                if (!docCollectionNew.contains(docCollectionOldDoc)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Doc " + docCollectionOldDoc + " since its creatorSocialProfileFk field is not nullable.");
                }
            }
            for (Doc docCollection1OldDoc : docCollection1Old) {
                if (!docCollection1New.contains(docCollection1OldDoc)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Doc " + docCollection1OldDoc + " since its editorSocialProfileFk field is not nullable.");
                }
            }
            for (DocHistory docHistoryCollectionOldDocHistory : docHistoryCollectionOld) {
                if (!docHistoryCollectionNew.contains(docHistoryCollectionOldDocHistory)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DocHistory " + docHistoryCollectionOldDocHistory + " since its editorSocialProfileFk field is not nullable.");
                }
            }
            for (DocMessage docMessageCollectionOldDocMessage : docMessageCollectionOld) {
                if (!docMessageCollectionNew.contains(docMessageCollectionOldDocMessage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DocMessage " + docMessageCollectionOldDocMessage + " since its socialProfileFk field is not nullable.");
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
            Collection<EducationalObjectMessage> attachedEducationalObjectMessageCollectionNew = new ArrayList<EducationalObjectMessage>();
            for (EducationalObjectMessage educationalObjectMessageCollectionNewEducationalObjectMessageToAttach : educationalObjectMessageCollectionNew) {
                educationalObjectMessageCollectionNewEducationalObjectMessageToAttach = em.getReference(educationalObjectMessageCollectionNewEducationalObjectMessageToAttach.getClass(), educationalObjectMessageCollectionNewEducationalObjectMessageToAttach.getId());
                attachedEducationalObjectMessageCollectionNew.add(educationalObjectMessageCollectionNewEducationalObjectMessageToAttach);
            }
            educationalObjectMessageCollectionNew = attachedEducationalObjectMessageCollectionNew;
            socialProfile.setEducationalObjectMessageCollection(educationalObjectMessageCollectionNew);
            Collection<EducationalObjectLike> attachedEducationalObjectLikeCollectionNew = new ArrayList<EducationalObjectLike>();
            for (EducationalObjectLike educationalObjectLikeCollectionNewEducationalObjectLikeToAttach : educationalObjectLikeCollectionNew) {
                educationalObjectLikeCollectionNewEducationalObjectLikeToAttach = em.getReference(educationalObjectLikeCollectionNewEducationalObjectLikeToAttach.getClass(), educationalObjectLikeCollectionNewEducationalObjectLikeToAttach.getEducationalObjectLikePK());
                attachedEducationalObjectLikeCollectionNew.add(educationalObjectLikeCollectionNewEducationalObjectLikeToAttach);
            }
            educationalObjectLikeCollectionNew = attachedEducationalObjectLikeCollectionNew;
            socialProfile.setEducationalObjectLikeCollection(educationalObjectLikeCollectionNew);
            Collection<DocGuest> attachedDocGuestCollectionNew = new ArrayList<DocGuest>();
            for (DocGuest docGuestCollectionNewDocGuestToAttach : docGuestCollectionNew) {
                docGuestCollectionNewDocGuestToAttach = em.getReference(docGuestCollectionNewDocGuestToAttach.getClass(), docGuestCollectionNewDocGuestToAttach.getId());
                attachedDocGuestCollectionNew.add(docGuestCollectionNewDocGuestToAttach);
            }
            docGuestCollectionNew = attachedDocGuestCollectionNew;
            socialProfile.setDocGuestCollection(docGuestCollectionNew);
            Collection<Doc> attachedDocCollectionNew = new ArrayList<Doc>();
            for (Doc docCollectionNewDocToAttach : docCollectionNew) {
                docCollectionNewDocToAttach = em.getReference(docCollectionNewDocToAttach.getClass(), docCollectionNewDocToAttach.getId());
                attachedDocCollectionNew.add(docCollectionNewDocToAttach);
            }
            docCollectionNew = attachedDocCollectionNew;
            socialProfile.setDocCollection(docCollectionNew);
            Collection<Doc> attachedDocCollection1New = new ArrayList<Doc>();
            for (Doc docCollection1NewDocToAttach : docCollection1New) {
                docCollection1NewDocToAttach = em.getReference(docCollection1NewDocToAttach.getClass(), docCollection1NewDocToAttach.getId());
                attachedDocCollection1New.add(docCollection1NewDocToAttach);
            }
            docCollection1New = attachedDocCollection1New;
            socialProfile.setDocCollection1(docCollection1New);
            Collection<DocHistory> attachedDocHistoryCollectionNew = new ArrayList<DocHistory>();
            for (DocHistory docHistoryCollectionNewDocHistoryToAttach : docHistoryCollectionNew) {
                docHistoryCollectionNewDocHistoryToAttach = em.getReference(docHistoryCollectionNewDocHistoryToAttach.getClass(), docHistoryCollectionNewDocHistoryToAttach.getId());
                attachedDocHistoryCollectionNew.add(docHistoryCollectionNewDocHistoryToAttach);
            }
            docHistoryCollectionNew = attachedDocHistoryCollectionNew;
            socialProfile.setDocHistoryCollection(docHistoryCollectionNew);
            Collection<DocMessage> attachedDocMessageCollectionNew = new ArrayList<DocMessage>();
            for (DocMessage docMessageCollectionNewDocMessageToAttach : docMessageCollectionNew) {
                docMessageCollectionNewDocMessageToAttach = em.getReference(docMessageCollectionNewDocMessageToAttach.getClass(), docMessageCollectionNewDocMessageToAttach.getId());
                attachedDocMessageCollectionNew.add(docMessageCollectionNewDocMessageToAttach);
            }
            docMessageCollectionNew = attachedDocMessageCollectionNew;
            socialProfile.setDocMessageCollection(docMessageCollectionNew);
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
            for (EducationalObjectMessage educationalObjectMessageCollectionNewEducationalObjectMessage : educationalObjectMessageCollectionNew) {
                if (!educationalObjectMessageCollectionOld.contains(educationalObjectMessageCollectionNewEducationalObjectMessage)) {
                    SocialProfile oldSocialProfileFkOfEducationalObjectMessageCollectionNewEducationalObjectMessage = educationalObjectMessageCollectionNewEducationalObjectMessage.getSocialProfileFk();
                    educationalObjectMessageCollectionNewEducationalObjectMessage.setSocialProfileFk(socialProfile);
                    educationalObjectMessageCollectionNewEducationalObjectMessage = em.merge(educationalObjectMessageCollectionNewEducationalObjectMessage);
                    if (oldSocialProfileFkOfEducationalObjectMessageCollectionNewEducationalObjectMessage != null && !oldSocialProfileFkOfEducationalObjectMessageCollectionNewEducationalObjectMessage.equals(socialProfile)) {
                        oldSocialProfileFkOfEducationalObjectMessageCollectionNewEducationalObjectMessage.getEducationalObjectMessageCollection().remove(educationalObjectMessageCollectionNewEducationalObjectMessage);
                        oldSocialProfileFkOfEducationalObjectMessageCollectionNewEducationalObjectMessage = em.merge(oldSocialProfileFkOfEducationalObjectMessageCollectionNewEducationalObjectMessage);
                    }
                }
            }
            for (EducationalObjectLike educationalObjectLikeCollectionNewEducationalObjectLike : educationalObjectLikeCollectionNew) {
                if (!educationalObjectLikeCollectionOld.contains(educationalObjectLikeCollectionNewEducationalObjectLike)) {
                    SocialProfile oldSocialProfileOfEducationalObjectLikeCollectionNewEducationalObjectLike = educationalObjectLikeCollectionNewEducationalObjectLike.getSocialProfile();
                    educationalObjectLikeCollectionNewEducationalObjectLike.setSocialProfile(socialProfile);
                    educationalObjectLikeCollectionNewEducationalObjectLike = em.merge(educationalObjectLikeCollectionNewEducationalObjectLike);
                    if (oldSocialProfileOfEducationalObjectLikeCollectionNewEducationalObjectLike != null && !oldSocialProfileOfEducationalObjectLikeCollectionNewEducationalObjectLike.equals(socialProfile)) {
                        oldSocialProfileOfEducationalObjectLikeCollectionNewEducationalObjectLike.getEducationalObjectLikeCollection().remove(educationalObjectLikeCollectionNewEducationalObjectLike);
                        oldSocialProfileOfEducationalObjectLikeCollectionNewEducationalObjectLike = em.merge(oldSocialProfileOfEducationalObjectLikeCollectionNewEducationalObjectLike);
                    }
                }
            }
            for (DocGuest docGuestCollectionNewDocGuest : docGuestCollectionNew) {
                if (!docGuestCollectionOld.contains(docGuestCollectionNewDocGuest)) {
                    SocialProfile oldSocialProfileFkOfDocGuestCollectionNewDocGuest = docGuestCollectionNewDocGuest.getSocialProfileFk();
                    docGuestCollectionNewDocGuest.setSocialProfileFk(socialProfile);
                    docGuestCollectionNewDocGuest = em.merge(docGuestCollectionNewDocGuest);
                    if (oldSocialProfileFkOfDocGuestCollectionNewDocGuest != null && !oldSocialProfileFkOfDocGuestCollectionNewDocGuest.equals(socialProfile)) {
                        oldSocialProfileFkOfDocGuestCollectionNewDocGuest.getDocGuestCollection().remove(docGuestCollectionNewDocGuest);
                        oldSocialProfileFkOfDocGuestCollectionNewDocGuest = em.merge(oldSocialProfileFkOfDocGuestCollectionNewDocGuest);
                    }
                }
            }
            for (Doc docCollectionNewDoc : docCollectionNew) {
                if (!docCollectionOld.contains(docCollectionNewDoc)) {
                    SocialProfile oldCreatorSocialProfileFkOfDocCollectionNewDoc = docCollectionNewDoc.getCreatorSocialProfileFk();
                    docCollectionNewDoc.setCreatorSocialProfileFk(socialProfile);
                    docCollectionNewDoc = em.merge(docCollectionNewDoc);
                    if (oldCreatorSocialProfileFkOfDocCollectionNewDoc != null && !oldCreatorSocialProfileFkOfDocCollectionNewDoc.equals(socialProfile)) {
                        oldCreatorSocialProfileFkOfDocCollectionNewDoc.getDocCollection().remove(docCollectionNewDoc);
                        oldCreatorSocialProfileFkOfDocCollectionNewDoc = em.merge(oldCreatorSocialProfileFkOfDocCollectionNewDoc);
                    }
                }
            }
            for (Doc docCollection1NewDoc : docCollection1New) {
                if (!docCollection1Old.contains(docCollection1NewDoc)) {
                    SocialProfile oldEditorSocialProfileFkOfDocCollection1NewDoc = docCollection1NewDoc.getEditorSocialProfileFk();
                    docCollection1NewDoc.setEditorSocialProfileFk(socialProfile);
                    docCollection1NewDoc = em.merge(docCollection1NewDoc);
                    if (oldEditorSocialProfileFkOfDocCollection1NewDoc != null && !oldEditorSocialProfileFkOfDocCollection1NewDoc.equals(socialProfile)) {
                        oldEditorSocialProfileFkOfDocCollection1NewDoc.getDocCollection1().remove(docCollection1NewDoc);
                        oldEditorSocialProfileFkOfDocCollection1NewDoc = em.merge(oldEditorSocialProfileFkOfDocCollection1NewDoc);
                    }
                }
            }
            for (DocHistory docHistoryCollectionNewDocHistory : docHistoryCollectionNew) {
                if (!docHistoryCollectionOld.contains(docHistoryCollectionNewDocHistory)) {
                    SocialProfile oldEditorSocialProfileFkOfDocHistoryCollectionNewDocHistory = docHistoryCollectionNewDocHistory.getEditorSocialProfileFk();
                    docHistoryCollectionNewDocHistory.setEditorSocialProfileFk(socialProfile);
                    docHistoryCollectionNewDocHistory = em.merge(docHistoryCollectionNewDocHistory);
                    if (oldEditorSocialProfileFkOfDocHistoryCollectionNewDocHistory != null && !oldEditorSocialProfileFkOfDocHistoryCollectionNewDocHistory.equals(socialProfile)) {
                        oldEditorSocialProfileFkOfDocHistoryCollectionNewDocHistory.getDocHistoryCollection().remove(docHistoryCollectionNewDocHistory);
                        oldEditorSocialProfileFkOfDocHistoryCollectionNewDocHistory = em.merge(oldEditorSocialProfileFkOfDocHistoryCollectionNewDocHistory);
                    }
                }
            }
            for (DocMessage docMessageCollectionNewDocMessage : docMessageCollectionNew) {
                if (!docMessageCollectionOld.contains(docMessageCollectionNewDocMessage)) {
                    SocialProfile oldSocialProfileFkOfDocMessageCollectionNewDocMessage = docMessageCollectionNewDocMessage.getSocialProfileFk();
                    docMessageCollectionNewDocMessage.setSocialProfileFk(socialProfile);
                    docMessageCollectionNewDocMessage = em.merge(docMessageCollectionNewDocMessage);
                    if (oldSocialProfileFkOfDocMessageCollectionNewDocMessage != null && !oldSocialProfileFkOfDocMessageCollectionNewDocMessage.equals(socialProfile)) {
                        oldSocialProfileFkOfDocMessageCollectionNewDocMessage.getDocMessageCollection().remove(docMessageCollectionNewDocMessage);
                        oldSocialProfileFkOfDocMessageCollectionNewDocMessage = em.merge(oldSocialProfileFkOfDocMessageCollectionNewDocMessage);
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
            Collection<EducationalObjectMessage> educationalObjectMessageCollectionOrphanCheck = socialProfile.getEducationalObjectMessageCollection();
            for (EducationalObjectMessage educationalObjectMessageCollectionOrphanCheckEducationalObjectMessage : educationalObjectMessageCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SocialProfile (" + socialProfile + ") cannot be destroyed since the EducationalObjectMessage " + educationalObjectMessageCollectionOrphanCheckEducationalObjectMessage + " in its educationalObjectMessageCollection field has a non-nullable socialProfileFk field.");
            }
            Collection<EducationalObjectLike> educationalObjectLikeCollectionOrphanCheck = socialProfile.getEducationalObjectLikeCollection();
            for (EducationalObjectLike educationalObjectLikeCollectionOrphanCheckEducationalObjectLike : educationalObjectLikeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SocialProfile (" + socialProfile + ") cannot be destroyed since the EducationalObjectLike " + educationalObjectLikeCollectionOrphanCheckEducationalObjectLike + " in its educationalObjectLikeCollection field has a non-nullable socialProfile field.");
            }
            Collection<DocGuest> docGuestCollectionOrphanCheck = socialProfile.getDocGuestCollection();
            for (DocGuest docGuestCollectionOrphanCheckDocGuest : docGuestCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SocialProfile (" + socialProfile + ") cannot be destroyed since the DocGuest " + docGuestCollectionOrphanCheckDocGuest + " in its docGuestCollection field has a non-nullable socialProfileFk field.");
            }
            Collection<Doc> docCollectionOrphanCheck = socialProfile.getDocCollection();
            for (Doc docCollectionOrphanCheckDoc : docCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SocialProfile (" + socialProfile + ") cannot be destroyed since the Doc " + docCollectionOrphanCheckDoc + " in its docCollection field has a non-nullable creatorSocialProfileFk field.");
            }
            Collection<Doc> docCollection1OrphanCheck = socialProfile.getDocCollection1();
            for (Doc docCollection1OrphanCheckDoc : docCollection1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SocialProfile (" + socialProfile + ") cannot be destroyed since the Doc " + docCollection1OrphanCheckDoc + " in its docCollection1 field has a non-nullable editorSocialProfileFk field.");
            }
            Collection<DocHistory> docHistoryCollectionOrphanCheck = socialProfile.getDocHistoryCollection();
            for (DocHistory docHistoryCollectionOrphanCheckDocHistory : docHistoryCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SocialProfile (" + socialProfile + ") cannot be destroyed since the DocHistory " + docHistoryCollectionOrphanCheckDocHistory + " in its docHistoryCollection field has a non-nullable editorSocialProfileFk field.");
            }
            Collection<DocMessage> docMessageCollectionOrphanCheck = socialProfile.getDocMessageCollection();
            for (DocMessage docMessageCollectionOrphanCheckDocMessage : docMessageCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SocialProfile (" + socialProfile + ") cannot be destroyed since the DocMessage " + docMessageCollectionOrphanCheckDocMessage + " in its docMessageCollection field has a non-nullable socialProfileFk field.");
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

    public List findAllSocialProfileAuthorization(Integer subnetworkdId) {
        EntityManager em = getEntityManager();
        try {
            Query y = em.createNativeQuery("select s.*,a.* from social_profile s "
                    + "join authorization a on s.token_id = a.token_id "
                    + "where s.subnetwork_id = " + subnetworkdId, "SocialProfileAuthorization");
            return y.getResultList();

        } finally {
            em.close();
        }
    }

    public List<SocialProfile> findSocialProfiles(String name, String token, boolean includeSessionUser, boolean includeVisitors, List<Integer> excludedSocialProfileIdList) {
        EntityManager em = getEntityManager();
        try {
            String partialQuery = "";
            String partialJoin = "";
            if (!includeSessionUser) {
                partialQuery += "and token_id <> '" + token + "' ";
            }
            if (!includeVisitors) {
                partialQuery += "and (role_id is null or r.name <> 'Visitante') ";
            }
            if (excludedSocialProfileIdList != null && !excludedSocialProfileIdList.isEmpty()) {
                for (Integer socialProfileId : excludedSocialProfileIdList) {
                    partialQuery += "and social_profile_id <> '" + socialProfileId + "' ";
                }
            }
            List<SocialProfile> socialProfileList = (List<SocialProfile>) em.createNativeQuery("select distinct sp.* from social_profile sp "
                    + "left join city c on c.id = sp.city_id "
                    + "left join subnetwork sn on sn.id = sp.subnetwork_id "
                    + "left join role r on r.id = sp.role_id "
                    + "where (upper(sp.name) like '%" + name.toUpperCase() + "%' "
                    + "or upper(c.name) like '%" + name.toUpperCase() + "%' "
                    + "or upper(sn.description) like '%" + name.toUpperCase() + "%') " + partialQuery, SocialProfile.class).getResultList();
            return socialProfileList;
        } finally {
            em.close();
        }
    }
}
