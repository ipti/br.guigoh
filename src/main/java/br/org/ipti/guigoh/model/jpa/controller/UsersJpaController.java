/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.entity.EmailActivation;
import br.org.ipti.guigoh.model.entity.Friends;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.IllegalOrphanException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.PreexistingEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.UserAuthorization;
import br.org.ipti.guigoh.model.entity.SecretQuestion;
import br.org.ipti.guigoh.model.entity.UserContactInfo;
import br.org.ipti.guigoh.model.entity.Users;
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
public class UsersJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();
    
    public UsersJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (users.getFriendsCollection() == null) {
            users.setFriendsCollection(new ArrayList<Friends>());
        }
        if (users.getFriendsCollection1() == null) {
            users.setFriendsCollection1(new ArrayList<Friends>());
        }
        if (users.getFriendsCollection2() == null) {
            users.setFriendsCollection2(new ArrayList<Friends>());
        }
        if (users.getUserContactInfoCollection() == null) {
            users.setUserContactInfoCollection(new ArrayList<UserContactInfo>());
        }
        if (users.getInterestsCollection() == null) {
            users.setInterestsCollection(new ArrayList<Interests>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();em.getTransaction().begin();
            SocialProfile socialProfile = users.getSocialProfile();
            if (socialProfile != null) {
                socialProfile = em.getReference(socialProfile.getClass(), socialProfile.getTokenId());
                users.setSocialProfile(socialProfile);
            }
            EmailActivation emailActivation = users.getEmailActivation();
            if (emailActivation != null) {
                emailActivation = em.getReference(emailActivation.getClass(), emailActivation.getUsername());
                users.setEmailActivation(emailActivation);
            }
            SecretQuestion secretQuestionId = users.getSecretQuestionId();
            if (secretQuestionId != null) {
                secretQuestionId = em.getReference(secretQuestionId.getClass(), secretQuestionId.getId());
                users.setSecretQuestionId(secretQuestionId);
            }
            UserAuthorization userAuthorization = users.getUserAuthorization();
            if (userAuthorization != null) {
                userAuthorization = em.getReference(userAuthorization.getClass(), userAuthorization.getTokenId());
                users.setUserAuthorization(userAuthorization);
            }
            Collection<Friends> attachedFriendsCollection = new ArrayList<Friends>();
            for (Friends friendsCollectionFriendsToAttach : users.getFriendsCollection()) {
                friendsCollectionFriendsToAttach = em.getReference(friendsCollectionFriendsToAttach.getClass(), friendsCollectionFriendsToAttach.getFriendsPK());
                attachedFriendsCollection.add(friendsCollectionFriendsToAttach);
            }
            users.setFriendsCollection(attachedFriendsCollection);
            Collection<Friends> attachedFriendsCollection1 = new ArrayList<Friends>();
            for (Friends friendsCollection1FriendsToAttach : users.getFriendsCollection1()) {
                friendsCollection1FriendsToAttach = em.getReference(friendsCollection1FriendsToAttach.getClass(), friendsCollection1FriendsToAttach.getFriendsPK());
                attachedFriendsCollection1.add(friendsCollection1FriendsToAttach);
            }
            users.setFriendsCollection1(attachedFriendsCollection1);
            Collection<Friends> attachedFriendsCollection2 = new ArrayList<Friends>();
            for (Friends friendsCollection2FriendsToAttach : users.getFriendsCollection2()) {
                friendsCollection2FriendsToAttach = em.getReference(friendsCollection2FriendsToAttach.getClass(), friendsCollection2FriendsToAttach.getFriendsPK());
                attachedFriendsCollection2.add(friendsCollection2FriendsToAttach);
            }
            users.setFriendsCollection2(attachedFriendsCollection2);
            Collection<UserContactInfo> attachedUserContactInfoCollection = new ArrayList<UserContactInfo>();
            for (UserContactInfo userContactInfoCollectionUserContactInfoToAttach : users.getUserContactInfoCollection()) {
                userContactInfoCollectionUserContactInfoToAttach = em.getReference(userContactInfoCollectionUserContactInfoToAttach.getClass(), userContactInfoCollectionUserContactInfoToAttach.getUserContactInfoPK());
                attachedUserContactInfoCollection.add(userContactInfoCollectionUserContactInfoToAttach);
            }
            users.setUserContactInfoCollection(attachedUserContactInfoCollection);
            Collection<Interests> attachedInterestsCollection = new ArrayList<Interests>();
            for (Interests interestsCollectionInterestsToAttach : users.getInterestsCollection()) {
                interestsCollectionInterestsToAttach = em.getReference(interestsCollectionInterestsToAttach.getClass(), interestsCollectionInterestsToAttach.getId());
                attachedInterestsCollection.add(interestsCollectionInterestsToAttach);
            }
            users.setInterestsCollection(attachedInterestsCollection);
            em.persist(users);
            if (socialProfile != null) {
                Users oldUsersOfSocialProfile = socialProfile.getUsers();
                if (oldUsersOfSocialProfile != null) {
                    oldUsersOfSocialProfile.setSocialProfile(null);
                    oldUsersOfSocialProfile = em.merge(oldUsersOfSocialProfile);
                }
                socialProfile.setUsers(users);
                socialProfile = em.merge(socialProfile);
            }
            if (emailActivation != null) {
                Users oldUsersOfEmailActivation = emailActivation.getUsers();
                if (oldUsersOfEmailActivation != null) {
                    oldUsersOfEmailActivation.setEmailActivation(null);
                    oldUsersOfEmailActivation = em.merge(oldUsersOfEmailActivation);
                }
                emailActivation.setUsers(users);
                emailActivation = em.merge(emailActivation);
            }
            if (secretQuestionId != null) {
                secretQuestionId.getUsersCollection().add(users);
                secretQuestionId = em.merge(secretQuestionId);
            }
            if (userAuthorization != null) {
                Users oldUsersOfUserAuthorization = userAuthorization.getUsers();
                if (oldUsersOfUserAuthorization != null) {
                    oldUsersOfUserAuthorization.setUserAuthorization(null);
                    oldUsersOfUserAuthorization = em.merge(oldUsersOfUserAuthorization);
                }
                userAuthorization.setUsers(users);
                userAuthorization = em.merge(userAuthorization);
            }
            for (Friends friendsCollectionFriends : users.getFriendsCollection()) {
                Users oldTokenFriend1OfFriendsCollectionFriends = friendsCollectionFriends.getTokenFriend1();
                friendsCollectionFriends.setTokenFriend1(users);
                friendsCollectionFriends = em.merge(friendsCollectionFriends);
                if (oldTokenFriend1OfFriendsCollectionFriends != null) {
                    oldTokenFriend1OfFriendsCollectionFriends.getFriendsCollection().remove(friendsCollectionFriends);
                    oldTokenFriend1OfFriendsCollectionFriends = em.merge(oldTokenFriend1OfFriendsCollectionFriends);
                }
            }
            for (Friends friendsCollection1Friends : users.getFriendsCollection1()) {
                Users oldTokenRecommenderOfFriendsCollection1Friends = friendsCollection1Friends.getTokenRecommender();
                friendsCollection1Friends.setTokenRecommender(users);
                friendsCollection1Friends = em.merge(friendsCollection1Friends);
                if (oldTokenRecommenderOfFriendsCollection1Friends != null) {
                    oldTokenRecommenderOfFriendsCollection1Friends.getFriendsCollection1().remove(friendsCollection1Friends);
                    oldTokenRecommenderOfFriendsCollection1Friends = em.merge(oldTokenRecommenderOfFriendsCollection1Friends);
                }
            }
            for (Friends friendsCollection2Friends : users.getFriendsCollection2()) {
                Users oldTokenFriend2OfFriendsCollection2Friends = friendsCollection2Friends.getTokenFriend2();
                friendsCollection2Friends.setTokenFriend2(users);
                friendsCollection2Friends = em.merge(friendsCollection2Friends);
                if (oldTokenFriend2OfFriendsCollection2Friends != null) {
                    oldTokenFriend2OfFriendsCollection2Friends.getFriendsCollection2().remove(friendsCollection2Friends);
                    oldTokenFriend2OfFriendsCollection2Friends = em.merge(oldTokenFriend2OfFriendsCollection2Friends);
                }
            }
            for (UserContactInfo userContactInfoCollectionUserContactInfo : users.getUserContactInfoCollection()) {
                Users oldUsersOfUserContactInfoCollectionUserContactInfo = userContactInfoCollectionUserContactInfo.getUsers();
                userContactInfoCollectionUserContactInfo.setUsers(users);
                userContactInfoCollectionUserContactInfo = em.merge(userContactInfoCollectionUserContactInfo);
                if (oldUsersOfUserContactInfoCollectionUserContactInfo != null) {
                    oldUsersOfUserContactInfoCollectionUserContactInfo.getUserContactInfoCollection().remove(userContactInfoCollectionUserContactInfo);
                    oldUsersOfUserContactInfoCollectionUserContactInfo = em.merge(oldUsersOfUserContactInfoCollectionUserContactInfo);
                }
            }
            for (Interests interestsCollectionInterests : users.getInterestsCollection()) {
                interestsCollectionInterests.getUsersCollection().add(users);
                interestsCollectionInterests = em.merge(interestsCollectionInterests);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUsers(users.getUsername()) != null) {
                throw new PreexistingEntityException("Users " + users + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getUsername());
            SocialProfile socialProfileOld = persistentUsers.getSocialProfile();
            SocialProfile socialProfileNew = users.getSocialProfile();
            EmailActivation emailActivationOld = persistentUsers.getEmailActivation();
            EmailActivation emailActivationNew = users.getEmailActivation();
            SecretQuestion secretQuestionIdOld = persistentUsers.getSecretQuestionId();
            SecretQuestion secretQuestionIdNew = users.getSecretQuestionId();
            UserAuthorization userAuthorizationOld = persistentUsers.getUserAuthorization();
            UserAuthorization userAuthorizationNew = users.getUserAuthorization();
            Collection<Friends> friendsCollectionOld = persistentUsers.getFriendsCollection();
            Collection<Friends> friendsCollectionNew = users.getFriendsCollection();
            Collection<Friends> friendsCollection1Old = persistentUsers.getFriendsCollection1();
            Collection<Friends> friendsCollection1New = users.getFriendsCollection1();
            Collection<Friends> friendsCollection2Old = persistentUsers.getFriendsCollection2();
            Collection<Friends> friendsCollection2New = users.getFriendsCollection2();
            Collection<UserContactInfo> userContactInfoCollectionOld = persistentUsers.getUserContactInfoCollection();
            Collection<UserContactInfo> userContactInfoCollectionNew = users.getUserContactInfoCollection();
            Collection<Interests> interestsCollectionOld = persistentUsers.getInterestsCollection();
            Collection<Interests> interestsCollectionNew = users.getInterestsCollection();
            List<String> illegalOrphanMessages = null;
            if (socialProfileOld != null && !socialProfileOld.equals(socialProfileNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain SocialProfile " + socialProfileOld + " since its users field is not nullable.");
            }
            if (emailActivationOld != null && !emailActivationOld.equals(emailActivationNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain EmailActivation " + emailActivationOld + " since its users field is not nullable.");
            }
            if (userAuthorizationOld != null && !userAuthorizationOld.equals(userAuthorizationNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain UserAuthorization " + userAuthorizationOld + " since its users field is not nullable.");
            }
            for (Friends friendsCollectionOldFriends : friendsCollectionOld) {
                if (!friendsCollectionNew.contains(friendsCollectionOldFriends)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Friends " + friendsCollectionOldFriends + " since its tokenFriend1 field is not nullable.");
                }
            }
            for (Friends friendsCollection2OldFriends : friendsCollection2Old) {
                if (!friendsCollection2New.contains(friendsCollection2OldFriends)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Friends " + friendsCollection2OldFriends + " since its tokenFriend2 field is not nullable.");
                }
            }
            for (UserContactInfo userContactInfoCollectionOldUserContactInfo : userContactInfoCollectionOld) {
                if (!userContactInfoCollectionNew.contains(userContactInfoCollectionOldUserContactInfo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserContactInfo " + userContactInfoCollectionOldUserContactInfo + " since its users field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (socialProfileNew != null) {
                socialProfileNew = em.getReference(socialProfileNew.getClass(), socialProfileNew.getTokenId());
                users.setSocialProfile(socialProfileNew);
            }
            if (emailActivationNew != null) {
                emailActivationNew = em.getReference(emailActivationNew.getClass(), emailActivationNew.getUsername());
                users.setEmailActivation(emailActivationNew);
            }
            if (secretQuestionIdNew != null) {
                secretQuestionIdNew = em.getReference(secretQuestionIdNew.getClass(), secretQuestionIdNew.getId());
                users.setSecretQuestionId(secretQuestionIdNew);
            }
            if (userAuthorizationNew != null) {
                userAuthorizationNew = em.getReference(userAuthorizationNew.getClass(), userAuthorizationNew.getTokenId());
                users.setUserAuthorization(userAuthorizationNew);
            }
            Collection<Friends> attachedFriendsCollectionNew = new ArrayList<Friends>();
            for (Friends friendsCollectionNewFriendsToAttach : friendsCollectionNew) {
                friendsCollectionNewFriendsToAttach = em.getReference(friendsCollectionNewFriendsToAttach.getClass(), friendsCollectionNewFriendsToAttach.getFriendsPK());
                attachedFriendsCollectionNew.add(friendsCollectionNewFriendsToAttach);
            }
            friendsCollectionNew = attachedFriendsCollectionNew;
            users.setFriendsCollection(friendsCollectionNew);
            Collection<Friends> attachedFriendsCollection1New = new ArrayList<Friends>();
            for (Friends friendsCollection1NewFriendsToAttach : friendsCollection1New) {
                friendsCollection1NewFriendsToAttach = em.getReference(friendsCollection1NewFriendsToAttach.getClass(), friendsCollection1NewFriendsToAttach.getFriendsPK());
                attachedFriendsCollection1New.add(friendsCollection1NewFriendsToAttach);
            }
            friendsCollection1New = attachedFriendsCollection1New;
            users.setFriendsCollection1(friendsCollection1New);
            Collection<Friends> attachedFriendsCollection2New = new ArrayList<Friends>();
            for (Friends friendsCollection2NewFriendsToAttach : friendsCollection2New) {
                friendsCollection2NewFriendsToAttach = em.getReference(friendsCollection2NewFriendsToAttach.getClass(), friendsCollection2NewFriendsToAttach.getFriendsPK());
                attachedFriendsCollection2New.add(friendsCollection2NewFriendsToAttach);
            }
            friendsCollection2New = attachedFriendsCollection2New;
            users.setFriendsCollection2(friendsCollection2New);
            Collection<UserContactInfo> attachedUserContactInfoCollectionNew = new ArrayList<UserContactInfo>();
            for (UserContactInfo userContactInfoCollectionNewUserContactInfoToAttach : userContactInfoCollectionNew) {
                userContactInfoCollectionNewUserContactInfoToAttach = em.getReference(userContactInfoCollectionNewUserContactInfoToAttach.getClass(), userContactInfoCollectionNewUserContactInfoToAttach.getUserContactInfoPK());
                attachedUserContactInfoCollectionNew.add(userContactInfoCollectionNewUserContactInfoToAttach);
            }
            userContactInfoCollectionNew = attachedUserContactInfoCollectionNew;
            users.setUserContactInfoCollection(userContactInfoCollectionNew);
            Collection<Interests> attachedInterestsCollectionNew = new ArrayList<Interests>();
            for (Interests interestsCollectionNewInterestsToAttach : interestsCollectionNew) {
                interestsCollectionNewInterestsToAttach = em.getReference(interestsCollectionNewInterestsToAttach.getClass(), interestsCollectionNewInterestsToAttach.getId());
                attachedInterestsCollectionNew.add(interestsCollectionNewInterestsToAttach);
            }
            interestsCollectionNew = attachedInterestsCollectionNew;
            users.setInterestsCollection(interestsCollectionNew);
            users = em.merge(users);
            if (socialProfileNew != null && !socialProfileNew.equals(socialProfileOld)) {
                Users oldUsersOfSocialProfile = socialProfileNew.getUsers();
                if (oldUsersOfSocialProfile != null) {
                    oldUsersOfSocialProfile.setSocialProfile(null);
                    oldUsersOfSocialProfile = em.merge(oldUsersOfSocialProfile);
                }
                socialProfileNew.setUsers(users);
                socialProfileNew = em.merge(socialProfileNew);
            }
            if (emailActivationNew != null && !emailActivationNew.equals(emailActivationOld)) {
                Users oldUsersOfEmailActivation = emailActivationNew.getUsers();
                if (oldUsersOfEmailActivation != null) {
                    oldUsersOfEmailActivation.setEmailActivation(null);
                    oldUsersOfEmailActivation = em.merge(oldUsersOfEmailActivation);
                }
                emailActivationNew.setUsers(users);
                emailActivationNew = em.merge(emailActivationNew);
            }
            if (secretQuestionIdOld != null && !secretQuestionIdOld.equals(secretQuestionIdNew)) {
                secretQuestionIdOld.getUsersCollection().remove(users);
                secretQuestionIdOld = em.merge(secretQuestionIdOld);
            }
            if (secretQuestionIdNew != null && !secretQuestionIdNew.equals(secretQuestionIdOld)) {
                secretQuestionIdNew.getUsersCollection().add(users);
                secretQuestionIdNew = em.merge(secretQuestionIdNew);
            }
            if (userAuthorizationNew != null && !userAuthorizationNew.equals(userAuthorizationOld)) {
                Users oldUsersOfUserAuthorization = userAuthorizationNew.getUsers();
                if (oldUsersOfUserAuthorization != null) {
                    oldUsersOfUserAuthorization.setUserAuthorization(null);
                    oldUsersOfUserAuthorization = em.merge(oldUsersOfUserAuthorization);
                }
                userAuthorizationNew.setUsers(users);
                userAuthorizationNew = em.merge(userAuthorizationNew);
            }
            for (Friends friendsCollectionNewFriends : friendsCollectionNew) {
                if (!friendsCollectionOld.contains(friendsCollectionNewFriends)) {
                    Users oldTokenFriend1OfFriendsCollectionNewFriends = friendsCollectionNewFriends.getTokenFriend1();
                    friendsCollectionNewFriends.setTokenFriend1(users);
                    friendsCollectionNewFriends = em.merge(friendsCollectionNewFriends);
                    if (oldTokenFriend1OfFriendsCollectionNewFriends != null && !oldTokenFriend1OfFriendsCollectionNewFriends.equals(users)) {
                        oldTokenFriend1OfFriendsCollectionNewFriends.getFriendsCollection().remove(friendsCollectionNewFriends);
                        oldTokenFriend1OfFriendsCollectionNewFriends = em.merge(oldTokenFriend1OfFriendsCollectionNewFriends);
                    }
                }
            }
            for (Friends friendsCollection1OldFriends : friendsCollection1Old) {
                if (!friendsCollection1New.contains(friendsCollection1OldFriends)) {
                    friendsCollection1OldFriends.setTokenRecommender(null);
                    friendsCollection1OldFriends = em.merge(friendsCollection1OldFriends);
                }
            }
            for (Friends friendsCollection1NewFriends : friendsCollection1New) {
                if (!friendsCollection1Old.contains(friendsCollection1NewFriends)) {
                    Users oldTokenRecommenderOfFriendsCollection1NewFriends = friendsCollection1NewFriends.getTokenRecommender();
                    friendsCollection1NewFriends.setTokenRecommender(users);
                    friendsCollection1NewFriends = em.merge(friendsCollection1NewFriends);
                    if (oldTokenRecommenderOfFriendsCollection1NewFriends != null && !oldTokenRecommenderOfFriendsCollection1NewFriends.equals(users)) {
                        oldTokenRecommenderOfFriendsCollection1NewFriends.getFriendsCollection1().remove(friendsCollection1NewFriends);
                        oldTokenRecommenderOfFriendsCollection1NewFriends = em.merge(oldTokenRecommenderOfFriendsCollection1NewFriends);
                    }
                }
            }
            for (Friends friendsCollection2NewFriends : friendsCollection2New) {
                if (!friendsCollection2Old.contains(friendsCollection2NewFriends)) {
                    Users oldTokenFriend2OfFriendsCollection2NewFriends = friendsCollection2NewFriends.getTokenFriend2();
                    friendsCollection2NewFriends.setTokenFriend2(users);
                    friendsCollection2NewFriends = em.merge(friendsCollection2NewFriends);
                    if (oldTokenFriend2OfFriendsCollection2NewFriends != null && !oldTokenFriend2OfFriendsCollection2NewFriends.equals(users)) {
                        oldTokenFriend2OfFriendsCollection2NewFriends.getFriendsCollection2().remove(friendsCollection2NewFriends);
                        oldTokenFriend2OfFriendsCollection2NewFriends = em.merge(oldTokenFriend2OfFriendsCollection2NewFriends);
                    }
                }
            }
            for (UserContactInfo userContactInfoCollectionNewUserContactInfo : userContactInfoCollectionNew) {
                if (!userContactInfoCollectionOld.contains(userContactInfoCollectionNewUserContactInfo)) {
                    Users oldUsersOfUserContactInfoCollectionNewUserContactInfo = userContactInfoCollectionNewUserContactInfo.getUsers();
                    userContactInfoCollectionNewUserContactInfo.setUsers(users);
                    userContactInfoCollectionNewUserContactInfo = em.merge(userContactInfoCollectionNewUserContactInfo);
                    if (oldUsersOfUserContactInfoCollectionNewUserContactInfo != null && !oldUsersOfUserContactInfoCollectionNewUserContactInfo.equals(users)) {
                        oldUsersOfUserContactInfoCollectionNewUserContactInfo.getUserContactInfoCollection().remove(userContactInfoCollectionNewUserContactInfo);
                        oldUsersOfUserContactInfoCollectionNewUserContactInfo = em.merge(oldUsersOfUserContactInfoCollectionNewUserContactInfo);
                    }
                }
            }
            for (Interests interestsCollectionOldInterests : interestsCollectionOld) {
                if (!interestsCollectionNew.contains(interestsCollectionOldInterests)) {
                    interestsCollectionOldInterests.getUsersCollection().remove(users);
                    interestsCollectionOldInterests = em.merge(interestsCollectionOldInterests);
                }
            }
            for (Interests interestsCollectionNewInterests : interestsCollectionNew) {
                if (!interestsCollectionOld.contains(interestsCollectionNewInterests)) {
                    interestsCollectionNewInterests.getUsersCollection().add(users);
                    interestsCollectionNewInterests = em.merge(interestsCollectionNewInterests);
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
                String id = users.getUsername();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
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
            em = getEntityManager();em.getTransaction().begin();
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getUsername();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            SocialProfile socialProfileOrphanCheck = users.getSocialProfile();
            if (socialProfileOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the SocialProfile " + socialProfileOrphanCheck + " in its socialProfile field has a non-nullable users field.");
            }
            EmailActivation emailActivationOrphanCheck = users.getEmailActivation();
            if (emailActivationOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the EmailActivation " + emailActivationOrphanCheck + " in its emailActivation field has a non-nullable users field.");
            }
            UserAuthorization userAuthorizationOrphanCheck = users.getUserAuthorization();
            if (userAuthorizationOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the UserAuthorization " + userAuthorizationOrphanCheck + " in its userAuthorization field has a non-nullable users field.");
            }
            Collection<Friends> friendsCollectionOrphanCheck = users.getFriendsCollection();
            for (Friends friendsCollectionOrphanCheckFriends : friendsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the Friends " + friendsCollectionOrphanCheckFriends + " in its friendsCollection field has a non-nullable tokenFriend1 field.");
            }
            Collection<Friends> friendsCollection2OrphanCheck = users.getFriendsCollection2();
            for (Friends friendsCollection2OrphanCheckFriends : friendsCollection2OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the Friends " + friendsCollection2OrphanCheckFriends + " in its friendsCollection2 field has a non-nullable tokenFriend2 field.");
            }
            Collection<UserContactInfo> userContactInfoCollectionOrphanCheck = users.getUserContactInfoCollection();
            for (UserContactInfo userContactInfoCollectionOrphanCheckUserContactInfo : userContactInfoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the UserContactInfo " + userContactInfoCollectionOrphanCheckUserContactInfo + " in its userContactInfoCollection field has a non-nullable users field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            SecretQuestion secretQuestionId = users.getSecretQuestionId();
            if (secretQuestionId != null) {
                secretQuestionId.getUsersCollection().remove(users);
                secretQuestionId = em.merge(secretQuestionId);
            }
            Collection<Friends> friendsCollection1 = users.getFriendsCollection1();
            for (Friends friendsCollection1Friends : friendsCollection1) {
                friendsCollection1Friends.setTokenRecommender(null);
                friendsCollection1Friends = em.merge(friendsCollection1Friends);
            }
            Collection<Interests> interestsCollection = users.getInterestsCollection();
            for (Interests interestsCollectionInterests : interestsCollection) {
                interestsCollectionInterests.getUsersCollection().remove(users);
                interestsCollectionInterests = em.merge(interestsCollectionInterests);
            }
            em.remove(users);
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

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public int getUsersQuantity() {
        EntityManager em = getEntityManager();
        try {
            Long usersQuantity = (Long) em.createNativeQuery("select count(*) from users u "
                    + "join user_authorization ua on u.token = ua.token_id "
                    + "where u.status = 'CA' and ua.status = 'AC'").getSingleResult();
            return usersQuantity.intValue();
        } finally {
            em.close();
        }
    }
}
