/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import br.org.ipti.guigoh.model.entity.DiscussionTopic;
import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.Interests;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author ipti008
 */
public class InterestsJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();

    public InterestsJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Interests interests) throws br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException, Exception {
        if (interests.getDiscussionTopicCollection() == null) {
            interests.setDiscussionTopicCollection(new ArrayList<DiscussionTopic>());
        }
        if (interests.getEducationalObjectCollection() == null) {
            interests.setEducationalObjectCollection(new ArrayList<EducationalObject>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<DiscussionTopic> attachedDiscussionTopicCollection = new ArrayList<DiscussionTopic>();
            for (DiscussionTopic discussionTopicCollectionDiscussionTopicToAttach : interests.getDiscussionTopicCollection()) {
                discussionTopicCollectionDiscussionTopicToAttach = em.getReference(discussionTopicCollectionDiscussionTopicToAttach.getClass(), discussionTopicCollectionDiscussionTopicToAttach.getId());
                attachedDiscussionTopicCollection.add(discussionTopicCollectionDiscussionTopicToAttach);
            }
            interests.setDiscussionTopicCollection(attachedDiscussionTopicCollection);
            Collection<EducationalObject> attachedEducationalObjectCollection = new ArrayList<EducationalObject>();
            for (EducationalObject educationalObjectCollectionEducationalObjectToAttach : interests.getEducationalObjectCollection()) {
                educationalObjectCollectionEducationalObjectToAttach = em.getReference(educationalObjectCollectionEducationalObjectToAttach.getClass(), educationalObjectCollectionEducationalObjectToAttach.getId());
                attachedEducationalObjectCollection.add(educationalObjectCollectionEducationalObjectToAttach);
            }
            interests.setEducationalObjectCollection(attachedEducationalObjectCollection);
            em.persist(interests);
            for (DiscussionTopic discussionTopicCollectionDiscussionTopic : interests.getDiscussionTopicCollection()) {
                Interests oldThemeIdOfDiscussionTopicCollectionDiscussionTopic = discussionTopicCollectionDiscussionTopic.getThemeId();
                discussionTopicCollectionDiscussionTopic.setThemeId(interests);
                discussionTopicCollectionDiscussionTopic = em.merge(discussionTopicCollectionDiscussionTopic);
                if (oldThemeIdOfDiscussionTopicCollectionDiscussionTopic != null) {
                    oldThemeIdOfDiscussionTopicCollectionDiscussionTopic.getDiscussionTopicCollection().remove(discussionTopicCollectionDiscussionTopic);
                    oldThemeIdOfDiscussionTopicCollectionDiscussionTopic = em.merge(oldThemeIdOfDiscussionTopicCollectionDiscussionTopic);
                }
            }
            for (EducationalObject educationalObjectCollectionEducationalObject : interests.getEducationalObjectCollection()) {
                Interests oldThemeIdOfEducationalObjectCollectionEducationalObject = educationalObjectCollectionEducationalObject.getThemeId();
                educationalObjectCollectionEducationalObject.setThemeId(interests);
                educationalObjectCollectionEducationalObject = em.merge(educationalObjectCollectionEducationalObject);
                if (oldThemeIdOfEducationalObjectCollectionEducationalObject != null) {
                    oldThemeIdOfEducationalObjectCollectionEducationalObject.getEducationalObjectCollection().remove(educationalObjectCollectionEducationalObject);
                    oldThemeIdOfEducationalObjectCollectionEducationalObject = em.merge(oldThemeIdOfEducationalObjectCollectionEducationalObject);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Interests interests) throws br.org.ipti.guigoh.model.jpa.controller.exceptions.IllegalOrphanException, br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException, br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Interests persistentInterests = em.find(Interests.class, interests.getId());
            Collection<DiscussionTopic> discussionTopicCollectionOld = persistentInterests.getDiscussionTopicCollection();
            Collection<DiscussionTopic> discussionTopicCollectionNew = interests.getDiscussionTopicCollection();
            Collection<EducationalObject> educationalObjectCollectionOld = persistentInterests.getEducationalObjectCollection();
            Collection<EducationalObject> educationalObjectCollectionNew = interests.getEducationalObjectCollection();
            List<String> illegalOrphanMessages = null;
            for (DiscussionTopic discussionTopicCollectionOldDiscussionTopic : discussionTopicCollectionOld) {
                if (!discussionTopicCollectionNew.contains(discussionTopicCollectionOldDiscussionTopic)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DiscussionTopic " + discussionTopicCollectionOldDiscussionTopic + " since its themeId field is not nullable.");
                }
            }
            for (EducationalObject educationalObjectCollectionOldEducationalObject : educationalObjectCollectionOld) {
                if (!educationalObjectCollectionNew.contains(educationalObjectCollectionOldEducationalObject)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EducationalObject " + educationalObjectCollectionOldEducationalObject + " since its themeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new br.org.ipti.guigoh.model.jpa.controller.exceptions.IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<DiscussionTopic> attachedDiscussionTopicCollectionNew = new ArrayList<DiscussionTopic>();
            for (DiscussionTopic discussionTopicCollectionNewDiscussionTopicToAttach : discussionTopicCollectionNew) {
                discussionTopicCollectionNewDiscussionTopicToAttach = em.getReference(discussionTopicCollectionNewDiscussionTopicToAttach.getClass(), discussionTopicCollectionNewDiscussionTopicToAttach.getId());
                attachedDiscussionTopicCollectionNew.add(discussionTopicCollectionNewDiscussionTopicToAttach);
            }
            discussionTopicCollectionNew = attachedDiscussionTopicCollectionNew;
            interests.setDiscussionTopicCollection(discussionTopicCollectionNew);
            Collection<EducationalObject> attachedEducationalObjectCollectionNew = new ArrayList<EducationalObject>();
            for (EducationalObject educationalObjectCollectionNewEducationalObjectToAttach : educationalObjectCollectionNew) {
                educationalObjectCollectionNewEducationalObjectToAttach = em.getReference(educationalObjectCollectionNewEducationalObjectToAttach.getClass(), educationalObjectCollectionNewEducationalObjectToAttach.getId());
                attachedEducationalObjectCollectionNew.add(educationalObjectCollectionNewEducationalObjectToAttach);
            }
            educationalObjectCollectionNew = attachedEducationalObjectCollectionNew;
            interests.setEducationalObjectCollection(educationalObjectCollectionNew);
            interests = em.merge(interests);
            for (DiscussionTopic discussionTopicCollectionNewDiscussionTopic : discussionTopicCollectionNew) {
                if (!discussionTopicCollectionOld.contains(discussionTopicCollectionNewDiscussionTopic)) {
                    Interests oldThemeIdOfDiscussionTopicCollectionNewDiscussionTopic = discussionTopicCollectionNewDiscussionTopic.getThemeId();
                    discussionTopicCollectionNewDiscussionTopic.setThemeId(interests);
                    discussionTopicCollectionNewDiscussionTopic = em.merge(discussionTopicCollectionNewDiscussionTopic);
                    if (oldThemeIdOfDiscussionTopicCollectionNewDiscussionTopic != null && !oldThemeIdOfDiscussionTopicCollectionNewDiscussionTopic.equals(interests)) {
                        oldThemeIdOfDiscussionTopicCollectionNewDiscussionTopic.getDiscussionTopicCollection().remove(discussionTopicCollectionNewDiscussionTopic);
                        oldThemeIdOfDiscussionTopicCollectionNewDiscussionTopic = em.merge(oldThemeIdOfDiscussionTopicCollectionNewDiscussionTopic);
                    }
                }
            }
            for (EducationalObject educationalObjectCollectionNewEducationalObject : educationalObjectCollectionNew) {
                if (!educationalObjectCollectionOld.contains(educationalObjectCollectionNewEducationalObject)) {
                    Interests oldThemeIdOfEducationalObjectCollectionNewEducationalObject = educationalObjectCollectionNewEducationalObject.getThemeId();
                    educationalObjectCollectionNewEducationalObject.setThemeId(interests);
                    educationalObjectCollectionNewEducationalObject = em.merge(educationalObjectCollectionNewEducationalObject);
                    if (oldThemeIdOfEducationalObjectCollectionNewEducationalObject != null && !oldThemeIdOfEducationalObjectCollectionNewEducationalObject.equals(interests)) {
                        oldThemeIdOfEducationalObjectCollectionNewEducationalObject.getEducationalObjectCollection().remove(educationalObjectCollectionNewEducationalObject);
                        oldThemeIdOfEducationalObjectCollectionNewEducationalObject = em.merge(oldThemeIdOfEducationalObjectCollectionNewEducationalObject);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = interests.getId();
                if (findInterests(id) == null) {
                    throw new br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException("The interests with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws br.org.ipti.guigoh.model.jpa.controller.exceptions.IllegalOrphanException, br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException, br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Interests interests;
            try {
                interests = em.getReference(Interests.class, id);
                interests.getId();
            } catch (EntityNotFoundException enfe) {
                throw new br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException("The interests with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DiscussionTopic> discussionTopicCollectionOrphanCheck = interests.getDiscussionTopicCollection();
            for (DiscussionTopic discussionTopicCollectionOrphanCheckDiscussionTopic : discussionTopicCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Interests (" + interests + ") cannot be destroyed since the DiscussionTopic " + discussionTopicCollectionOrphanCheckDiscussionTopic + " in its discussionTopicCollection field has a non-nullable themeId field.");
            }
            Collection<EducationalObject> educationalObjectCollectionOrphanCheck = interests.getEducationalObjectCollection();
            for (EducationalObject educationalObjectCollectionOrphanCheckEducationalObject : educationalObjectCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Interests (" + interests + ") cannot be destroyed since the EducationalObject " + educationalObjectCollectionOrphanCheckEducationalObject + " in its educationalObjectCollection field has a non-nullable themeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new br.org.ipti.guigoh.model.jpa.controller.exceptions.IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(interests);
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
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

    public Interests findInterestsByInterestsName(String interestsName) {
        EntityManager em = getEntityManager();
        try {
            Interests interests = (Interests) em.createNativeQuery("select * from interests "
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
}
