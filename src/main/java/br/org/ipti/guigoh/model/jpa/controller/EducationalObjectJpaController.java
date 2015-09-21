/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.jpa.controller.exceptions.IllegalOrphanException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.entity.Tags;
import java.util.ArrayList;
import java.util.Collection;
import br.org.ipti.guigoh.model.entity.Author;
import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.EducationalObjectMedia;
import br.org.ipti.guigoh.model.entity.EducationalObjectMessage;
import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import br.org.ipti.guigoh.util.CookieService;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ipti004
 */
public class EducationalObjectJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();

    public EducationalObjectJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EducationalObject educationalObject) throws RollbackFailureException, Exception {
        if (educationalObject.getTagsCollection() == null) {
            educationalObject.setTagsCollection(new ArrayList<Tags>());
        }
        if (educationalObject.getAuthorCollection() == null) {
            educationalObject.setAuthorCollection(new ArrayList<Author>());
        }
        if (educationalObject.getEducationalObjectMediaCollection() == null) {
            educationalObject.setEducationalObjectMediaCollection(new ArrayList<EducationalObjectMedia>());
        }
        if (educationalObject.getSocialProfileCollection() == null) {
            educationalObject.setSocialProfileCollection(new ArrayList<SocialProfile>());
        }
        if (educationalObject.getEducationalObjectMessageCollection() == null) {
            educationalObject.setEducationalObjectMessageCollection(new ArrayList<EducationalObjectMessage>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SocialProfile socialProfileId = educationalObject.getSocialProfileId();
            if (socialProfileId != null) {
                socialProfileId = em.getReference(socialProfileId.getClass(), socialProfileId.getTokenId());
                educationalObject.setSocialProfileId(socialProfileId);
            }
            Interests themeId = educationalObject.getThemeId();
            if (themeId != null) {
                themeId = em.getReference(themeId.getClass(), themeId.getId());
                educationalObject.setThemeId(themeId);
            }
            Collection<Tags> attachedTagsCollection = new ArrayList<Tags>();
            for (Tags tagsCollectionTagsToAttach : educationalObject.getTagsCollection()) {
                tagsCollectionTagsToAttach = em.getReference(tagsCollectionTagsToAttach.getClass(), tagsCollectionTagsToAttach.getId());
                attachedTagsCollection.add(tagsCollectionTagsToAttach);
            }
            educationalObject.setTagsCollection(attachedTagsCollection);
            Collection<Author> attachedAuthorCollection = new ArrayList<Author>();
            for (Author authorCollectionAuthorToAttach : educationalObject.getAuthorCollection()) {
                authorCollectionAuthorToAttach = em.getReference(authorCollectionAuthorToAttach.getClass(), authorCollectionAuthorToAttach.getId());
                attachedAuthorCollection.add(authorCollectionAuthorToAttach);
            }
            educationalObject.setAuthorCollection(attachedAuthorCollection);
            Collection<EducationalObjectMedia> attachedEducationalObjectMediaCollection = new ArrayList<EducationalObjectMedia>();
            for (EducationalObjectMedia educationalObjectMediaCollectionEducationalObjectMediaToAttach : educationalObject.getEducationalObjectMediaCollection()) {
                educationalObjectMediaCollectionEducationalObjectMediaToAttach = em.getReference(educationalObjectMediaCollectionEducationalObjectMediaToAttach.getClass(), educationalObjectMediaCollectionEducationalObjectMediaToAttach.getId());
                attachedEducationalObjectMediaCollection.add(educationalObjectMediaCollectionEducationalObjectMediaToAttach);
            }
            educationalObject.setEducationalObjectMediaCollection(attachedEducationalObjectMediaCollection);
            Collection<SocialProfile> attachedSocialProfileCollection = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionSocialProfileToAttach : educationalObject.getSocialProfileCollection()) {
                socialProfileCollectionSocialProfileToAttach = em.getReference(socialProfileCollectionSocialProfileToAttach.getClass(), socialProfileCollectionSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollection.add(socialProfileCollectionSocialProfileToAttach);
            }
            educationalObject.setSocialProfileCollection(attachedSocialProfileCollection);
            Collection<EducationalObjectMessage> attachedEducationalObjectMessageCollection = new ArrayList<EducationalObjectMessage>();
            for (EducationalObjectMessage educationalObjectMessageCollectionEducationalObjectMessageToAttach : educationalObject.getEducationalObjectMessageCollection()) {
                educationalObjectMessageCollectionEducationalObjectMessageToAttach = em.getReference(educationalObjectMessageCollectionEducationalObjectMessageToAttach.getClass(), educationalObjectMessageCollectionEducationalObjectMessageToAttach.getId());
                attachedEducationalObjectMessageCollection.add(educationalObjectMessageCollectionEducationalObjectMessageToAttach);
            }
            educationalObject.setEducationalObjectMessageCollection(attachedEducationalObjectMessageCollection);
            em.persist(educationalObject);
            if (socialProfileId != null) {
                socialProfileId.getEducationalObjectCollection().add(educationalObject);
                socialProfileId = em.merge(socialProfileId);
            }
            if (themeId != null) {
                themeId.getEducationalObjectCollection().add(educationalObject);
                themeId = em.merge(themeId);
            }
            for (Tags tagsCollectionTags : educationalObject.getTagsCollection()) {
                tagsCollectionTags.getEducationalObjectCollection().add(educationalObject);
                tagsCollectionTags = em.merge(tagsCollectionTags);
            }
            for (Author authorCollectionAuthor : educationalObject.getAuthorCollection()) {
                authorCollectionAuthor.getEducationalObjectCollection().add(educationalObject);
                authorCollectionAuthor = em.merge(authorCollectionAuthor);
            }
            for (EducationalObjectMedia educationalObjectMediaCollectionEducationalObjectMedia : educationalObject.getEducationalObjectMediaCollection()) {
                EducationalObject oldEducationalObjectIdOfEducationalObjectMediaCollectionEducationalObjectMedia = educationalObjectMediaCollectionEducationalObjectMedia.getEducationalObjectId();
                educationalObjectMediaCollectionEducationalObjectMedia.setEducationalObjectId(educationalObject);
                educationalObjectMediaCollectionEducationalObjectMedia = em.merge(educationalObjectMediaCollectionEducationalObjectMedia);
                if (oldEducationalObjectIdOfEducationalObjectMediaCollectionEducationalObjectMedia != null) {
                    oldEducationalObjectIdOfEducationalObjectMediaCollectionEducationalObjectMedia.getEducationalObjectMediaCollection().remove(educationalObjectMediaCollectionEducationalObjectMedia);
                    oldEducationalObjectIdOfEducationalObjectMediaCollectionEducationalObjectMedia = em.merge(oldEducationalObjectIdOfEducationalObjectMediaCollectionEducationalObjectMedia);
                }
            }
            for (SocialProfile socialProfileCollectionSocialProfile : educationalObject.getSocialProfileCollection()) {
                socialProfileCollectionSocialProfile.getEducationalObjectCollection().add(educationalObject);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
            }
            for (EducationalObjectMessage educationalObjectMessageCollectionEducationalObjectMessage : educationalObject.getEducationalObjectMessageCollection()) {
                EducationalObject oldEducationalObjectFkOfEducationalObjectMessageCollectionEducationalObjectMessage = educationalObjectMessageCollectionEducationalObjectMessage.getEducationalObjectFk();
                educationalObjectMessageCollectionEducationalObjectMessage.setEducationalObjectFk(educationalObject);
                educationalObjectMessageCollectionEducationalObjectMessage = em.merge(educationalObjectMessageCollectionEducationalObjectMessage);
                if (oldEducationalObjectFkOfEducationalObjectMessageCollectionEducationalObjectMessage != null) {
                    oldEducationalObjectFkOfEducationalObjectMessageCollectionEducationalObjectMessage.getEducationalObjectMessageCollection().remove(educationalObjectMessageCollectionEducationalObjectMessage);
                    oldEducationalObjectFkOfEducationalObjectMessageCollectionEducationalObjectMessage = em.merge(oldEducationalObjectFkOfEducationalObjectMessageCollectionEducationalObjectMessage);
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

    public void edit(EducationalObject educationalObject) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EducationalObject persistentEducationalObject = em.find(EducationalObject.class, educationalObject.getId());
            SocialProfile socialProfileIdOld = persistentEducationalObject.getSocialProfileId();
            SocialProfile socialProfileIdNew = educationalObject.getSocialProfileId();
            Interests themeIdOld = persistentEducationalObject.getThemeId();
            Interests themeIdNew = educationalObject.getThemeId();
            Collection<Tags> tagsCollectionOld = persistentEducationalObject.getTagsCollection();
            Collection<Tags> tagsCollectionNew = educationalObject.getTagsCollection();
            Collection<Author> authorCollectionOld = persistentEducationalObject.getAuthorCollection();
            Collection<Author> authorCollectionNew = educationalObject.getAuthorCollection();
            Collection<EducationalObjectMedia> educationalObjectMediaCollectionOld = persistentEducationalObject.getEducationalObjectMediaCollection();
            Collection<EducationalObjectMedia> educationalObjectMediaCollectionNew = educationalObject.getEducationalObjectMediaCollection();
            Collection<SocialProfile> socialProfileCollectionOld = persistentEducationalObject.getSocialProfileCollection();
            Collection<SocialProfile> socialProfileCollectionNew = educationalObject.getSocialProfileCollection();
            Collection<EducationalObjectMessage> educationalObjectMessageCollectionOld = persistentEducationalObject.getEducationalObjectMessageCollection();
            Collection<EducationalObjectMessage> educationalObjectMessageCollectionNew = educationalObject.getEducationalObjectMessageCollection();
            List<String> illegalOrphanMessages = null;
            for (EducationalObjectMedia educationalObjectMediaCollectionOldEducationalObjectMedia : educationalObjectMediaCollectionOld) {
                if (!educationalObjectMediaCollectionNew.contains(educationalObjectMediaCollectionOldEducationalObjectMedia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EducationalObjectMedia " + educationalObjectMediaCollectionOldEducationalObjectMedia + " since its educationalObjectId field is not nullable.");
                }
            }
            for (EducationalObjectMessage educationalObjectMessageCollectionOldEducationalObjectMessage : educationalObjectMessageCollectionOld) {
                if (!educationalObjectMessageCollectionNew.contains(educationalObjectMessageCollectionOldEducationalObjectMessage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EducationalObjectMessage " + educationalObjectMessageCollectionOldEducationalObjectMessage + " since its educationalObjectFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (socialProfileIdNew != null) {
                socialProfileIdNew = em.getReference(socialProfileIdNew.getClass(), socialProfileIdNew.getTokenId());
                educationalObject.setSocialProfileId(socialProfileIdNew);
            }
            if (themeIdNew != null) {
                themeIdNew = em.getReference(themeIdNew.getClass(), themeIdNew.getId());
                educationalObject.setThemeId(themeIdNew);
            }
            Collection<Tags> attachedTagsCollectionNew = new ArrayList<Tags>();
            for (Tags tagsCollectionNewTagsToAttach : tagsCollectionNew) {
                tagsCollectionNewTagsToAttach = em.getReference(tagsCollectionNewTagsToAttach.getClass(), tagsCollectionNewTagsToAttach.getId());
                attachedTagsCollectionNew.add(tagsCollectionNewTagsToAttach);
            }
            tagsCollectionNew = attachedTagsCollectionNew;
            educationalObject.setTagsCollection(tagsCollectionNew);
            Collection<Author> attachedAuthorCollectionNew = new ArrayList<Author>();
            for (Author authorCollectionNewAuthorToAttach : authorCollectionNew) {
                authorCollectionNewAuthorToAttach = em.getReference(authorCollectionNewAuthorToAttach.getClass(), authorCollectionNewAuthorToAttach.getId());
                attachedAuthorCollectionNew.add(authorCollectionNewAuthorToAttach);
            }
            authorCollectionNew = attachedAuthorCollectionNew;
            educationalObject.setAuthorCollection(authorCollectionNew);
            Collection<EducationalObjectMedia> attachedEducationalObjectMediaCollectionNew = new ArrayList<EducationalObjectMedia>();
            for (EducationalObjectMedia educationalObjectMediaCollectionNewEducationalObjectMediaToAttach : educationalObjectMediaCollectionNew) {
                educationalObjectMediaCollectionNewEducationalObjectMediaToAttach = em.getReference(educationalObjectMediaCollectionNewEducationalObjectMediaToAttach.getClass(), educationalObjectMediaCollectionNewEducationalObjectMediaToAttach.getId());
                attachedEducationalObjectMediaCollectionNew.add(educationalObjectMediaCollectionNewEducationalObjectMediaToAttach);
            }
            educationalObjectMediaCollectionNew = attachedEducationalObjectMediaCollectionNew;
            educationalObject.setEducationalObjectMediaCollection(educationalObjectMediaCollectionNew);
            Collection<SocialProfile> attachedSocialProfileCollectionNew = new ArrayList<SocialProfile>();
            for (SocialProfile socialProfileCollectionNewSocialProfileToAttach : socialProfileCollectionNew) {
                socialProfileCollectionNewSocialProfileToAttach = em.getReference(socialProfileCollectionNewSocialProfileToAttach.getClass(), socialProfileCollectionNewSocialProfileToAttach.getTokenId());
                attachedSocialProfileCollectionNew.add(socialProfileCollectionNewSocialProfileToAttach);
            }
            socialProfileCollectionNew = attachedSocialProfileCollectionNew;
            educationalObject.setSocialProfileCollection(socialProfileCollectionNew);
            Collection<EducationalObjectMessage> attachedEducationalObjectMessageCollectionNew = new ArrayList<EducationalObjectMessage>();
            for (EducationalObjectMessage educationalObjectMessageCollectionNewEducationalObjectMessageToAttach : educationalObjectMessageCollectionNew) {
                educationalObjectMessageCollectionNewEducationalObjectMessageToAttach = em.getReference(educationalObjectMessageCollectionNewEducationalObjectMessageToAttach.getClass(), educationalObjectMessageCollectionNewEducationalObjectMessageToAttach.getId());
                attachedEducationalObjectMessageCollectionNew.add(educationalObjectMessageCollectionNewEducationalObjectMessageToAttach);
            }
            educationalObjectMessageCollectionNew = attachedEducationalObjectMessageCollectionNew;
            educationalObject.setEducationalObjectMessageCollection(educationalObjectMessageCollectionNew);
            educationalObject = em.merge(educationalObject);
            if (socialProfileIdOld != null && !socialProfileIdOld.equals(socialProfileIdNew)) {
                socialProfileIdOld.getEducationalObjectCollection().remove(educationalObject);
                socialProfileIdOld = em.merge(socialProfileIdOld);
            }
            if (socialProfileIdNew != null && !socialProfileIdNew.equals(socialProfileIdOld)) {
                socialProfileIdNew.getEducationalObjectCollection().add(educationalObject);
                socialProfileIdNew = em.merge(socialProfileIdNew);
            }
            if (themeIdOld != null && !themeIdOld.equals(themeIdNew)) {
                themeIdOld.getEducationalObjectCollection().remove(educationalObject);
                themeIdOld = em.merge(themeIdOld);
            }
            if (themeIdNew != null && !themeIdNew.equals(themeIdOld)) {
                themeIdNew.getEducationalObjectCollection().add(educationalObject);
                themeIdNew = em.merge(themeIdNew);
            }
            for (Tags tagsCollectionOldTags : tagsCollectionOld) {
                if (!tagsCollectionNew.contains(tagsCollectionOldTags)) {
                    tagsCollectionOldTags.getEducationalObjectCollection().remove(educationalObject);
                    tagsCollectionOldTags = em.merge(tagsCollectionOldTags);
                }
            }
            for (Tags tagsCollectionNewTags : tagsCollectionNew) {
                if (!tagsCollectionOld.contains(tagsCollectionNewTags)) {
                    tagsCollectionNewTags.getEducationalObjectCollection().add(educationalObject);
                    tagsCollectionNewTags = em.merge(tagsCollectionNewTags);
                }
            }
            for (Author authorCollectionOldAuthor : authorCollectionOld) {
                if (!authorCollectionNew.contains(authorCollectionOldAuthor)) {
                    authorCollectionOldAuthor.getEducationalObjectCollection().remove(educationalObject);
                    authorCollectionOldAuthor = em.merge(authorCollectionOldAuthor);
                }
            }
            for (Author authorCollectionNewAuthor : authorCollectionNew) {
                if (!authorCollectionOld.contains(authorCollectionNewAuthor)) {
                    authorCollectionNewAuthor.getEducationalObjectCollection().add(educationalObject);
                    authorCollectionNewAuthor = em.merge(authorCollectionNewAuthor);
                }
            }
            for (EducationalObjectMedia educationalObjectMediaCollectionNewEducationalObjectMedia : educationalObjectMediaCollectionNew) {
                if (!educationalObjectMediaCollectionOld.contains(educationalObjectMediaCollectionNewEducationalObjectMedia)) {
                    EducationalObject oldEducationalObjectIdOfEducationalObjectMediaCollectionNewEducationalObjectMedia = educationalObjectMediaCollectionNewEducationalObjectMedia.getEducationalObjectId();
                    educationalObjectMediaCollectionNewEducationalObjectMedia.setEducationalObjectId(educationalObject);
                    educationalObjectMediaCollectionNewEducationalObjectMedia = em.merge(educationalObjectMediaCollectionNewEducationalObjectMedia);
                    if (oldEducationalObjectIdOfEducationalObjectMediaCollectionNewEducationalObjectMedia != null && !oldEducationalObjectIdOfEducationalObjectMediaCollectionNewEducationalObjectMedia.equals(educationalObject)) {
                        oldEducationalObjectIdOfEducationalObjectMediaCollectionNewEducationalObjectMedia.getEducationalObjectMediaCollection().remove(educationalObjectMediaCollectionNewEducationalObjectMedia);
                        oldEducationalObjectIdOfEducationalObjectMediaCollectionNewEducationalObjectMedia = em.merge(oldEducationalObjectIdOfEducationalObjectMediaCollectionNewEducationalObjectMedia);
                    }
                }
            }
            for (SocialProfile socialProfileCollectionOldSocialProfile : socialProfileCollectionOld) {
                if (!socialProfileCollectionNew.contains(socialProfileCollectionOldSocialProfile)) {
                    socialProfileCollectionOldSocialProfile.getEducationalObjectCollection().remove(educationalObject);
                    socialProfileCollectionOldSocialProfile = em.merge(socialProfileCollectionOldSocialProfile);
                }
            }
            for (SocialProfile socialProfileCollectionNewSocialProfile : socialProfileCollectionNew) {
                if (!socialProfileCollectionOld.contains(socialProfileCollectionNewSocialProfile)) {
                    socialProfileCollectionNewSocialProfile.getEducationalObjectCollection().add(educationalObject);
                    socialProfileCollectionNewSocialProfile = em.merge(socialProfileCollectionNewSocialProfile);
                }
            }
            for (EducationalObjectMessage educationalObjectMessageCollectionNewEducationalObjectMessage : educationalObjectMessageCollectionNew) {
                if (!educationalObjectMessageCollectionOld.contains(educationalObjectMessageCollectionNewEducationalObjectMessage)) {
                    EducationalObject oldEducationalObjectFkOfEducationalObjectMessageCollectionNewEducationalObjectMessage = educationalObjectMessageCollectionNewEducationalObjectMessage.getEducationalObjectFk();
                    educationalObjectMessageCollectionNewEducationalObjectMessage.setEducationalObjectFk(educationalObject);
                    educationalObjectMessageCollectionNewEducationalObjectMessage = em.merge(educationalObjectMessageCollectionNewEducationalObjectMessage);
                    if (oldEducationalObjectFkOfEducationalObjectMessageCollectionNewEducationalObjectMessage != null && !oldEducationalObjectFkOfEducationalObjectMessageCollectionNewEducationalObjectMessage.equals(educationalObject)) {
                        oldEducationalObjectFkOfEducationalObjectMessageCollectionNewEducationalObjectMessage.getEducationalObjectMessageCollection().remove(educationalObjectMessageCollectionNewEducationalObjectMessage);
                        oldEducationalObjectFkOfEducationalObjectMessageCollectionNewEducationalObjectMessage = em.merge(oldEducationalObjectFkOfEducationalObjectMessageCollectionNewEducationalObjectMessage);
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
                Integer id = educationalObject.getId();
                if (findEducationalObject(id) == null) {
                    throw new NonexistentEntityException("The educationalObject with id " + id + " no longer exists.");
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
            EducationalObject educationalObject;
            try {
                educationalObject = em.getReference(EducationalObject.class, id);
                educationalObject.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The educationalObject with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<EducationalObjectMedia> educationalObjectMediaCollectionOrphanCheck = educationalObject.getEducationalObjectMediaCollection();
            for (EducationalObjectMedia educationalObjectMediaCollectionOrphanCheckEducationalObjectMedia : educationalObjectMediaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EducationalObject (" + educationalObject + ") cannot be destroyed since the EducationalObjectMedia " + educationalObjectMediaCollectionOrphanCheckEducationalObjectMedia + " in its educationalObjectMediaCollection field has a non-nullable educationalObjectId field.");
            }
            Collection<EducationalObjectMessage> educationalObjectMessageCollectionOrphanCheck = educationalObject.getEducationalObjectMessageCollection();
            for (EducationalObjectMessage educationalObjectMessageCollectionOrphanCheckEducationalObjectMessage : educationalObjectMessageCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EducationalObject (" + educationalObject + ") cannot be destroyed since the EducationalObjectMessage " + educationalObjectMessageCollectionOrphanCheckEducationalObjectMessage + " in its educationalObjectMessageCollection field has a non-nullable educationalObjectFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            SocialProfile socialProfileId = educationalObject.getSocialProfileId();
            if (socialProfileId != null) {
                socialProfileId.getEducationalObjectCollection().remove(educationalObject);
                socialProfileId = em.merge(socialProfileId);
            }
            Interests themeId = educationalObject.getThemeId();
            if (themeId != null) {
                themeId.getEducationalObjectCollection().remove(educationalObject);
                themeId = em.merge(themeId);
            }
            Collection<Tags> tagsCollection = educationalObject.getTagsCollection();
            for (Tags tagsCollectionTags : tagsCollection) {
                tagsCollectionTags.getEducationalObjectCollection().remove(educationalObject);
                tagsCollectionTags = em.merge(tagsCollectionTags);
            }
            Collection<Author> authorCollection = educationalObject.getAuthorCollection();
            for (Author authorCollectionAuthor : authorCollection) {
                authorCollectionAuthor.getEducationalObjectCollection().remove(educationalObject);
                authorCollectionAuthor = em.merge(authorCollectionAuthor);
            }
            Collection<SocialProfile> socialProfileCollection = educationalObject.getSocialProfileCollection();
            for (SocialProfile socialProfileCollectionSocialProfile : socialProfileCollection) {
                socialProfileCollectionSocialProfile.getEducationalObjectCollection().remove(educationalObject);
                socialProfileCollectionSocialProfile = em.merge(socialProfileCollectionSocialProfile);
            }
            em.remove(educationalObject);
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

    public List<EducationalObject> findEducationalObjectEntities() {
        return findEducationalObjectEntities(true, -1, -1);
    }

    public List<EducationalObject> findEducationalObjectEntities(int maxResults, int firstResult) {
        return findEducationalObjectEntities(false, maxResults, firstResult);
    }

    private List<EducationalObject> findEducationalObjectEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EducationalObject.class));
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

    public EducationalObject findEducationalObject(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EducationalObject.class, id);
        } finally {
            em.close();
        }
    }

    public int getEducationalObjectCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EducationalObject> rt = cq.from(EducationalObject.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<EducationalObject> getPendingEducationalObjects() {
        EntityManager em = getEntityManager();
        try {
            List<EducationalObject> educationalObjectList = (List<EducationalObject>) em.createNativeQuery("select * from educational_object "
                    + "where status = 'PE' order by date desc", EducationalObject.class).getResultList();
            return educationalObjectList;
        } finally {
            em.close();
        }
    }
    
    public List<EducationalObject> getActiveEducationalObjects(){
        EntityManager em = getEntityManager();
        try {
            List<EducationalObject> educationalObjectList = (List<EducationalObject>) em.createNativeQuery("select * from educational_object "
                    + "where status = 'AC' order by date desc", EducationalObject.class).getResultList();
            return educationalObjectList;
        } finally {
            em.close();
        }
    }
    
    public List<EducationalObject> getInactiveEducationalObjects(){
        EntityManager em = getEntityManager();
        try {
            List<EducationalObject> educationalObjectList = (List<EducationalObject>) em.createNativeQuery("select * from educational_object "
                    + "where status = 'DE' order by date desc", EducationalObject.class).getResultList();
            return educationalObjectList;
        } finally {
            em.close();
        }
    }

    public List<EducationalObject> findEducationalObjectsBySocialProfileId(Integer id){
        EntityManager em = getEntityManager();
        try {
            List<EducationalObject> educationalObjectList = (List<EducationalObject>) 
                    em.createNativeQuery("select * from educational_object "
                            + "where status = 'AC' and social_profile_id = " + id, EducationalObject.class).getResultList();
            return educationalObjectList;
        } finally {
            em.close();
        }
    }
    
    public List<EducationalObject> findMostAcessedEducationalObjects(Integer interestId) {
        EntityManager em = getEntityManager();
        try {
            List<EducationalObject> educationalObjectList = (List<EducationalObject>) em.createNativeQuery("select * from educational_object "
                    + "where status = 'AC' and theme_id = '" + interestId + "' order by views desc limit 3 ", EducationalObject.class).getResultList();
            return educationalObjectList;

        } finally {
            em.close();
        }
    }
    
    public List<EducationalObject> findEducationalObjects(String name, Date date, Integer interestId, Integer limit) {
        EntityManager em = getEntityManager();
        try {
            String partialQuery = "";
            if (name != null) {
                partialQuery += " and (upper(eo.name) like '%" + name.toUpperCase() + "%' or upper(t.name) like '%" + name.toUpperCase() + "%')";
            }
            if (interestId != null) {
                partialQuery += " and theme_id = '" + interestId + "'";
            }
            if (date != null) {
                partialQuery += " and date < '" + date + "'";
            }
            partialQuery += " order by date desc";
            if (limit != null) {
                partialQuery += " limit " + limit;
            }
            List<EducationalObject> educationalObjectList = (List<EducationalObject>) em.createNativeQuery("select distinct eo.* from educational_object eo "
                            + "left join educational_object_tag eot on eot.educational_object_id = eo.id "
                            + "left join tags t on eot.tag_id = t.id "
                            + "where eo.status = 'AC' " + partialQuery, EducationalObject.class).getResultList();
            return educationalObjectList;

        } finally {
            em.close();
        }
    }
    
    public void increaseViews(Integer id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createNativeQuery("UPDATE educational_object SET views = views + 1 where id = " + id);
            query.executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
