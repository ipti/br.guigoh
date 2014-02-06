/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.dao;

import com.guigoh.primata.dao.exceptions.NonexistentEntityException;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import com.guigoh.primata.entity.Interests;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.guigoh.primata.entity.InterestsType;
import com.guigoh.primata.entity.SocialProfile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.transaction.UserTransaction;

/**
 *
 * @author Joe
 */
public class InterestsDAO implements Serializable {

    private EntityManagerFactory emf = JPAUtil.getEMF();

    public InterestsDAO() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Interests interests) throws RollbackFailureException, Exception {
        if (interests.getSocialProfileCollection() == null) {
            interests.setSocialProfileCollection(new ArrayList<SocialProfile>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InterestsType typeId = interests.getTypeId();
            if (typeId != null) {
                typeId = em.getReference(typeId.getClass(), typeId.getId());
                interests.setTypeId(typeId);
            }
            Collection<SocialProfile> attachedSocialProfileCollection = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionSocialProfileToAttach : interests.getSocialProfileCollection()) {
                socialProfileCollectionSocialProfileToAttach = em.getReference(socialProfileCollectionSocialProfileToAttach.getClass(), socialProfileCollectionSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollection.add(socialProfileCollectionSocialProfileToAttach);
            }
            interests.setSocialProfileCollection(attachedSocialProfileCollection);
            em.persist(interests);
            if (typeId != null) {
                typeId.getInterestsCollection().add(interests);
                typeId = em.merge(typeId);
            }
            for (SocialProfile socialProfileCollectionSocialProfile : interests.getSocialProfileCollection()) {
                socialProfileCollectionSocialProfile.getInterestsCollection().add(interests);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
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

    public void edit(Interests interests) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Interests persistentInterests = em.find(Interests.class, interests.getId());
            InterestsType typeIdOld = persistentInterests.getTypeId();
            InterestsType typeIdNew = interests.getTypeId();
            Collection<SocialProfile> socialProfileCollectionOld = persistentInterests.getSocialProfileCollection();
            Collection<SocialProfile> socialProfileCollectionNew = interests.getSocialProfileCollection();
            if (typeIdNew != null) {
                typeIdNew = em.getReference(typeIdNew.getClass(), typeIdNew.getId());
                interests.setTypeId(typeIdNew);
            }
            Collection<SocialProfile> attachedSocialProfileCollectionNew = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionNewSocialProfileToAttach : socialProfileCollectionNew) {
                socialProfileCollectionNewSocialProfileToAttach = em.getReference(socialProfileCollectionNewSocialProfileToAttach.getClass(), socialProfileCollectionNewSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollectionNew.add(socialProfileCollectionNewSocialProfileToAttach);
            }
            socialProfileCollectionNew = attachedSocialProfileCollectionNew;
            interests.setSocialProfileCollection(socialProfileCollectionNew);
            interests = em.merge(interests);
            if (typeIdOld != null && !typeIdOld.equals(typeIdNew)) {
                typeIdOld.getInterestsCollection().remove(interests);
                typeIdOld = em.merge(typeIdOld);
            }
            if (typeIdNew != null && !typeIdNew.equals(typeIdOld)) {
                typeIdNew.getInterestsCollection().add(interests);
                typeIdNew = em.merge(typeIdNew);
            }
            for (SocialProfile socialProfileCollectionOldSocialProfile : socialProfileCollectionOld) {
                if (!socialProfileCollectionNew.contains(socialProfileCollectionOldSocialProfile)) {
                    socialProfileCollectionOldSocialProfile.getInterestsCollection().remove(interests);
                    socialProfileCollectionOldSocialProfile = em.merge(socialProfileCollectionOldSocialProfile);
                }
            }
            for (SocialProfile socialProfileCollectionNewSocialProfile : socialProfileCollectionNew) {
                if (!socialProfileCollectionOld.contains(socialProfileCollectionNewSocialProfile)) {
                    socialProfileCollectionNewSocialProfile.getInterestsCollection().add(interests);
                    socialProfileCollectionNewSocialProfile = em.merge(socialProfileCollectionNewSocialProfile);
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
                Integer id = interests.getId();
                if (findInterests(id) == null) {
                    throw new NonexistentEntityException("The interests with id " + id + " no longer exists.");
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
            Interests interests;
            try {
                interests = em.getReference(Interests.class, id);
                interests.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The interests with id " + id + " no longer exists.", enfe);
            }
            InterestsType typeId = interests.getTypeId();
            if (typeId != null) {
                typeId.getInterestsCollection().remove(interests);
                typeId = em.merge(typeId);
            }
            Collection<SocialProfile> socialProfileCollection = interests.getSocialProfileCollection();
            for (SocialProfile socialProfileCollectionSocialProfile : socialProfileCollection) {
                socialProfileCollectionSocialProfile.getInterestsCollection().remove(interests);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
            }
            em.remove(interests);
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

    public List<Interests> findInterestsEntities() {
        return findInterestsEntities(true, -1, -1);
    }

    public List<Interests> findInterestsEntities(int maxResults, int firstResult) {
        return findInterestsEntities(false, maxResults, firstResult);
    }

    private List<Interests> findInterestsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Interests.class));
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

    public Interests findInterests(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Interests.class, id);
        } finally {
            em.close();
        }
    }

    public int getInterestsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Interests> rt = cq.from(Interests.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Interests> findInterestsBySocialProfileId(Integer socialprofile_id) {
        EntityManager em = getEntityManager();
        try {
            List<Interests> interestsList = (List<Interests>) em.createNativeQuery("select id, name, type_id from primata_interests it "
                    + "join primata_social_profile_interests si on it.id = si.interests_id "
                    + "where si.social_profile_id = " + socialprofile_id, Interests.class).getResultList();
            return interestsList;
        } finally {
            em.close();
        }
    }

    public List<Interests> findInterestsByInterestsTypeName(String interestsType) {
        EntityManager em = getEntityManager();
        try {
            List<Interests> interestsList = (List<Interests>) em.createNativeQuery("select it.id, it.name, it.type_id from primata_interests it "
                    + "join primata_interests_type ty on it.type_id = ty.id "
                    + "where ty.name = '" + interestsType + "'", Interests.class).getResultList();
            return interestsList;
        } finally {
            em.close();
        }
    }

    public List<Interests> findInterestsByInterestsTypeId(Integer interestsTypeId) {
        EntityManager em = getEntityManager();
        try {
            List<Interests> interestsList = (List<Interests>) em.createNativeQuery("select it.id, it.name, it.type_id from primata_interests it "
                    + "join primata_interests_type ty on it.type_id = ty.id "
                    + "where ty.id = '" + interestsTypeId + "'", Interests.class).getResultList();
            return interestsList;
        } finally {
            em.close();
        }
    }

    public Interests findInterestsByInterestsName(String interestsName) {
        EntityManager em = getEntityManager();
        try {
            Interests interests = (Interests) em.createNativeQuery("select * from primata_interests "
                    + "where name = '" + interestsName + "'", Interests.class).getSingleResult();
            if (interests == null) {
                return new Interests();
            }
            return interests;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public void destroyInterestsSocialProfile(SocialProfile socialprofile) throws RollbackFailureException, Exception {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.createNativeQuery("DELETE from primata_social_profile_interests where social_profile_id = "
                    + socialprofile.getSocialProfileId()).executeUpdate();
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

    public void createInterestsBySocialProfileByInterest(List<Interests> interestsList, SocialProfile socialprofile) throws RollbackFailureException, Exception {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            String sql = "INSERT INTO primata_social_profile_interests (interests_id, social_profile_id) "
                    + "VALUES(?1,?2)";

            Query query = em.createNativeQuery(sql);
            Boolean ok;
            List<Interests> InterestsListT = new ArrayList<Interests>();
            for (Interests interests : interestsList) {
                ok = true;
                if (interests != null) {
                    if (!(interests.getName().equals("") || interests.getName() == null)) {
                        for (Interests interestsT : InterestsListT) {
                            if (interestsT.getId() == interests.getId()) {
                                ok = false;
                            }
                        }
                        if (ok) {
                            query.setParameter(1, interests.getId());
                            query.setParameter(2, socialprofile.getSocialProfileId());

                            query.executeUpdate();
                        }
                    }
                }
                InterestsListT.add(interests);
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

    public void createInterestsBySocialProfileByIds(Integer[] interestsIds, SocialProfile socialprofile) throws RollbackFailureException, Exception {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            String sql = "INSERT INTO primata_social_profile_interests (interests_id, social_profile_id) "
                    + "VALUES(?1,?2)";

            Query query = em.createNativeQuery(sql);
            int cont = interestsIds.length;
            if (cont > 6) {
                cont = 6;
            }
            for (int i = 0; i < cont; i++) {
                if (interestsIds[i] != null) {
                    query.setParameter(1, interestsIds[i]);
                    query.setParameter(2, socialprofile.getSocialProfileId());

                    query.executeUpdate();
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

    public void destroyInterestsBySocialProfileInterestsType(SocialProfile socialprofile, Interests interests) throws RollbackFailureException, Exception {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.createNativeQuery("DELETE from primata_social_profile_interests where social_profile_id = "
                    + socialprofile.getSocialProfileId() + " and interests_id = " + interests.getId()).executeUpdate();
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
    
    public Interests findInterestsByID(Integer interestsID) {
        EntityManager em = getEntityManager();
        try {
            Interests interests = (Interests) em.createNativeQuery("select * from primata_interests "
                    + "where id = '" + interestsID + "'", Interests.class).getSingleResult();
            if (interests == null) {
                return new Interests();
            }
            return interests;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}