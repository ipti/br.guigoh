/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.mandril.dao;

import com.guigoh.mandril.dao.exceptions.IllegalOrphanException;
import com.guigoh.mandril.dao.exceptions.NonexistentEntityException;
import com.guigoh.mandril.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.Interests;
import com.guigoh.primata.entity.Tags;
import java.util.ArrayList;
import java.util.Collection;
import com.guigoh.mandril.entity.Author;
import com.guigoh.mandril.entity.EducationalObject;
import com.guigoh.mandril.entity.EducationalObjectMedia;
import com.guigoh.primata.dao.JPAUtil;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ipti004
 */
public class EducationalObjectDAO implements Serializable {

    private transient EntityManagerFactory emf = JPAUtil.getEMF();

    public EducationalObjectDAO() {
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
            List<String> illegalOrphanMessages = null;
            for (EducationalObjectMedia educationalObjectMediaCollectionOldEducationalObjectMedia : educationalObjectMediaCollectionOld) {
                if (!educationalObjectMediaCollectionNew.contains(educationalObjectMediaCollectionOldEducationalObjectMedia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EducationalObjectMedia " + educationalObjectMediaCollectionOldEducationalObjectMedia + " since its educationalObjectId field is not nullable.");
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

    public List<EducationalObject> getActiveEducationalObjectsByTheme(Integer theme_id) {
        EntityManager em = getEntityManager();
        try {
            List<EducationalObject> educationalObjectList = (List<EducationalObject>) em.createNativeQuery("select * from mandril_educational_object "
                    + "where status = 'AC' and theme_id = " + theme_id, EducationalObject.class).getResultList();
            return educationalObjectList;
        } finally {
            em.close();
        }
    }

    public List<EducationalObject> getLatestFourActiveEducationalObjects() {
        EntityManager em = getEntityManager();
        try {
            List<EducationalObject> educationalObjectList = (List<EducationalObject>) em.createNativeQuery("select * from mandril_educational_object "
                    + "where status = 'AC' order by date desc limit 4", EducationalObject.class).getResultList();
            return educationalObjectList;
        } finally {
            em.close();
        }
    }

    public List<EducationalObject> getAllActiveEducationalObjects() {
        EntityManager em = getEntityManager();
        try {
            List<EducationalObject> educationalObjectList = (List<EducationalObject>) em.createNativeQuery("select * from mandril_educational_object "
                    + "where status = 'AC' order by date desc", EducationalObject.class).getResultList();
            return educationalObjectList;
        } finally {
            em.close();
        }
    }

    public List<EducationalObject> getPendingEducationalObjects() {
        EntityManager em = getEntityManager();
        try {
            List<EducationalObject> educationalObjectList = (List<EducationalObject>) em.createNativeQuery("select * from mandril_educational_object "
                    + "where status = 'PE' order by date desc", EducationalObject.class).getResultList();
            return educationalObjectList;
        } finally {
            em.close();
        }
    }

    public List<EducationalObject> getEducationalObjectsByExpression(String expression, String tag, Integer id) {
        EntityManager em = getEntityManager();
        try {

            String sql = "select distinct eo.* from mandril_educational_object eo ";
            if (tag != null) {
                if (!tag.equals("")) {
                    sql += "join mandril_educational_object_tag eot on eo.id = eot.educational_object_id ";
                }
            }
            sql += "where eo.theme_id = " + id + " and eo.status = 'AC' and "
                    + "UPPER(eo.name) like '%" + expression.toUpperCase() + "%' ";
            if (tag != null) {
                if (!tag.equals("")) {
                    sql += "and eot.tag_id = (select id from primata_tags where name = '" + tag + "') ";
                }
            }
            List<EducationalObject> discussionTopicList = (List<EducationalObject>) em.createNativeQuery(sql, EducationalObject.class).getResultList();
            return discussionTopicList;
        } finally {
            em.close();
        }
    }

    public Timestamp getServerTime() {
        EntityManager em = getEntityManager();
        try {
            Timestamp serverTime = (Timestamp) em.createNativeQuery("SELECT date_trunc('seconds', now()::timestamp);").getSingleResult();
            return serverTime;
        } finally {
            em.close();
        }
    }

}
