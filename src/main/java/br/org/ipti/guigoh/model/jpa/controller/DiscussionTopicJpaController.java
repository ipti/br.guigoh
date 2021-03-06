/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.IllegalOrphanException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.model.entity.DiscussionTopic;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.org.ipti.guigoh.model.entity.Tags;
import java.util.ArrayList;
import java.util.Collection;
import br.org.ipti.guigoh.model.entity.DiscussionTopicMsg;
import br.org.ipti.guigoh.model.entity.NewActivity;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Joe
 */
public class DiscussionTopicJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();

    public DiscussionTopicJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DiscussionTopic discussionTopic) throws RollbackFailureException, Exception {
        if (discussionTopic.getTagsCollection() == null) {
            discussionTopic.setTagsCollection(new ArrayList<Tags>());
        }
        if (discussionTopic.getDiscussionTopicMsgCollection() == null) {
            discussionTopic.setDiscussionTopicMsgCollection(new ArrayList<DiscussionTopicMsg>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Tags> attachedTagsCollection = new ArrayList<Tags>();
            for (Tags tagsCollectionTagsToAttach : discussionTopic.getTagsCollection()) {
                tagsCollectionTagsToAttach = em.getReference(tagsCollectionTagsToAttach.getClass(), tagsCollectionTagsToAttach.getId());
                attachedTagsCollection.add(tagsCollectionTagsToAttach);
            }
            discussionTopic.setTagsCollection(attachedTagsCollection);
            Collection<DiscussionTopicMsg> attachedDiscussionTopicMsgCollection = new ArrayList<DiscussionTopicMsg>();
            for (DiscussionTopicMsg discussionTopicMsgCollectionDiscussionTopicMsgToAttach : discussionTopic.getDiscussionTopicMsgCollection()) {
                discussionTopicMsgCollectionDiscussionTopicMsgToAttach = em.getReference(discussionTopicMsgCollectionDiscussionTopicMsgToAttach.getClass(), discussionTopicMsgCollectionDiscussionTopicMsgToAttach.getId());
                attachedDiscussionTopicMsgCollection.add(discussionTopicMsgCollectionDiscussionTopicMsgToAttach);
            }
            discussionTopic.setDiscussionTopicMsgCollection(attachedDiscussionTopicMsgCollection);
            em.persist(discussionTopic);
            for (Tags tagsCollectionTags : discussionTopic.getTagsCollection()) {
                tagsCollectionTags.getDiscussionTopicCollection().add(discussionTopic);
                tagsCollectionTags = em.merge(tagsCollectionTags);
            }
            for (DiscussionTopicMsg discussionTopicMsgCollectionDiscussionTopicMsg : discussionTopic.getDiscussionTopicMsgCollection()) {
                DiscussionTopic oldDiscussionTopicIdOfDiscussionTopicMsgCollectionDiscussionTopicMsg = discussionTopicMsgCollectionDiscussionTopicMsg.getDiscussionTopicId();
                discussionTopicMsgCollectionDiscussionTopicMsg.setDiscussionTopicId(discussionTopic);
                discussionTopicMsgCollectionDiscussionTopicMsg = em.merge(discussionTopicMsgCollectionDiscussionTopicMsg);
                if (oldDiscussionTopicIdOfDiscussionTopicMsgCollectionDiscussionTopicMsg != null) {
                    oldDiscussionTopicIdOfDiscussionTopicMsgCollectionDiscussionTopicMsg.getDiscussionTopicMsgCollection().remove(discussionTopicMsgCollectionDiscussionTopicMsg);
                    oldDiscussionTopicIdOfDiscussionTopicMsgCollectionDiscussionTopicMsg = em.merge(oldDiscussionTopicIdOfDiscussionTopicMsgCollectionDiscussionTopicMsg);
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

    public void edit(DiscussionTopic discussionTopic) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DiscussionTopic persistentDiscussionTopic = em.find(DiscussionTopic.class, discussionTopic.getId());
            Collection<Tags> tagsCollectionOld = persistentDiscussionTopic.getTagsCollection();
            Collection<Tags> tagsCollectionNew = discussionTopic.getTagsCollection();
            Collection<DiscussionTopicMsg> discussionTopicMsgCollectionOld = persistentDiscussionTopic.getDiscussionTopicMsgCollection();
            Collection<DiscussionTopicMsg> discussionTopicMsgCollectionNew = discussionTopic.getDiscussionTopicMsgCollection();
            List<String> illegalOrphanMessages = null;
            for (DiscussionTopicMsg discussionTopicMsgCollectionOldDiscussionTopicMsg : discussionTopicMsgCollectionOld) {
                if (!discussionTopicMsgCollectionNew.contains(discussionTopicMsgCollectionOldDiscussionTopicMsg)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DiscussionTopicMsg " + discussionTopicMsgCollectionOldDiscussionTopicMsg + " since its discussionTopicId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Tags> attachedTagsCollectionNew = new ArrayList<Tags>();
            for (Tags tagsCollectionNewTagsToAttach : tagsCollectionNew) {
                tagsCollectionNewTagsToAttach = em.getReference(tagsCollectionNewTagsToAttach.getClass(), tagsCollectionNewTagsToAttach.getId());
                attachedTagsCollectionNew.add(tagsCollectionNewTagsToAttach);
            }
            tagsCollectionNew = attachedTagsCollectionNew;
            discussionTopic.setTagsCollection(tagsCollectionNew);
            Collection<DiscussionTopicMsg> attachedDiscussionTopicMsgCollectionNew = new ArrayList<DiscussionTopicMsg>();
            for (DiscussionTopicMsg discussionTopicMsgCollectionNewDiscussionTopicMsgToAttach : discussionTopicMsgCollectionNew) {
                discussionTopicMsgCollectionNewDiscussionTopicMsgToAttach = em.getReference(discussionTopicMsgCollectionNewDiscussionTopicMsgToAttach.getClass(), discussionTopicMsgCollectionNewDiscussionTopicMsgToAttach.getId());
                attachedDiscussionTopicMsgCollectionNew.add(discussionTopicMsgCollectionNewDiscussionTopicMsgToAttach);
            }
            discussionTopicMsgCollectionNew = attachedDiscussionTopicMsgCollectionNew;
            discussionTopic.setDiscussionTopicMsgCollection(discussionTopicMsgCollectionNew);
            discussionTopic = em.merge(discussionTopic);
            for (Tags tagsCollectionOldTags : tagsCollectionOld) {
                if (!tagsCollectionNew.contains(tagsCollectionOldTags)) {
                    tagsCollectionOldTags.getDiscussionTopicCollection().remove(discussionTopic);
                    tagsCollectionOldTags = em.merge(tagsCollectionOldTags);
                }
            }
            for (Tags tagsCollectionNewTags : tagsCollectionNew) {
                if (!tagsCollectionOld.contains(tagsCollectionNewTags)) {
                    tagsCollectionNewTags.getDiscussionTopicCollection().add(discussionTopic);
                    tagsCollectionNewTags = em.merge(tagsCollectionNewTags);
                }
            }
            for (DiscussionTopicMsg discussionTopicMsgCollectionNewDiscussionTopicMsg : discussionTopicMsgCollectionNew) {
                if (!discussionTopicMsgCollectionOld.contains(discussionTopicMsgCollectionNewDiscussionTopicMsg)) {
                    DiscussionTopic oldDiscussionTopicIdOfDiscussionTopicMsgCollectionNewDiscussionTopicMsg = discussionTopicMsgCollectionNewDiscussionTopicMsg.getDiscussionTopicId();
                    discussionTopicMsgCollectionNewDiscussionTopicMsg.setDiscussionTopicId(discussionTopic);
                    discussionTopicMsgCollectionNewDiscussionTopicMsg = em.merge(discussionTopicMsgCollectionNewDiscussionTopicMsg);
                    if (oldDiscussionTopicIdOfDiscussionTopicMsgCollectionNewDiscussionTopicMsg != null && !oldDiscussionTopicIdOfDiscussionTopicMsgCollectionNewDiscussionTopicMsg.equals(discussionTopic)) {
                        oldDiscussionTopicIdOfDiscussionTopicMsgCollectionNewDiscussionTopicMsg.getDiscussionTopicMsgCollection().remove(discussionTopicMsgCollectionNewDiscussionTopicMsg);
                        oldDiscussionTopicIdOfDiscussionTopicMsgCollectionNewDiscussionTopicMsg = em.merge(oldDiscussionTopicIdOfDiscussionTopicMsgCollectionNewDiscussionTopicMsg);
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
                Integer id = discussionTopic.getId();
                if (findDiscussionTopic(id) == null) {
                    throw new NonexistentEntityException("The discussionTopic with id " + id + " no longer exists.");
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
            DiscussionTopic discussionTopic;
            try {
                discussionTopic = em.getReference(DiscussionTopic.class, id);
                discussionTopic.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The discussionTopic with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DiscussionTopicMsg> discussionTopicMsgCollectionOrphanCheck = discussionTopic.getDiscussionTopicMsgCollection();
            for (DiscussionTopicMsg discussionTopicMsgCollectionOrphanCheckDiscussionTopicMsg : discussionTopicMsgCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DiscussionTopic (" + discussionTopic + ") cannot be destroyed since the DiscussionTopicMsg " + discussionTopicMsgCollectionOrphanCheckDiscussionTopicMsg + " in its discussionTopicMsgCollection field has a non-nullable discussionTopicId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Tags> tagsCollection = discussionTopic.getTagsCollection();
            for (Tags tagsCollectionTags : tagsCollection) {
                tagsCollectionTags.getDiscussionTopicCollection().remove(discussionTopic);
                tagsCollectionTags = em.merge(tagsCollectionTags);
            }
            em.remove(discussionTopic);
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

    public List<DiscussionTopic> findDiscussionTopicEntities() {
        return findDiscussionTopicEntities(true, -1, -1);
    }

    public List<DiscussionTopic> findDiscussionTopicEntities(int maxResults, int firstResult) {
        return findDiscussionTopicEntities(false, maxResults, firstResult);
    }

    private List<DiscussionTopic> findDiscussionTopicEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DiscussionTopic.class));
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

    public DiscussionTopic findDiscussionTopic(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DiscussionTopic.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiscussionTopicCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DiscussionTopic> rt = cq.from(DiscussionTopic.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<NewActivity> getLastActivities(int quantity) {
        EntityManager em = getEntityManager();
        try {
            List<Object[]> objectList = em.createNativeQuery("select * from "
                    + "(select id as id, social_profile_id, title, data, 'DT' as type from discussion_topic "
                    + "where status = 'A' "
                    + "union "
                    + "select dt.id as id, dtm.social_profile_id, dt.title, dtm.data, 'DTM' as type from discussion_topic_msg dtm "
                    + "join discussion_topic dt on dtm.discussion_topic_id = dt.id "
                    + "where dtm.status = 'A' "
                    + "union "
                    + "select eo.id, eo.social_profile_id, eo.name, eo.date as data, 'EO' as type from educational_object eo "
                    + "where eo.status = 'AC' "
                    + "union "
                    + "select eo.id, eom.social_profile_fk, eo.name, eom.date as data, 'EOM' as type from educational_object_message eom "
                    + "join educational_object eo on eo.id = eom.educational_object_fk "
                    + "where eo.status = 'AC' "
                    + "union "
                    + "select eo.id, eol.social_profile_fk, eo.name, eol.date as data, 'EOL' as type from educational_object_like eol "
                    + "join educational_object eo on eo.id = eol.educational_object_fk "
                    + "where eo.status = 'AC' "
                    + "union "
                    + "select doc.id, doc.creator_social_profile_fk, doc.title, doc.date as data, 'DOC' as type from doc doc "
                    + "where doc.status = 'A' and doc.public_access = 'Y') "
                    + "as news order by data desc limit " + quantity).getResultList();
            List<NewActivity> newActivityList = new ArrayList<>();
            SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
            for (Object[] obj : objectList) {
                NewActivity newActivity = new NewActivity((Integer) obj[0], (SocialProfile) socialProfileJpaController.findSocialProfileBySocialProfileId((Integer) obj[1]), (String) obj[2], (Date) obj[3], (String) obj[4]);
                newActivityList.add(newActivity);
            }
            return newActivityList;
        } finally {
            em.close();
        }
    }

    public List<NewActivity> getMoreActivities(Date date) {
        EntityManager em = getEntityManager();
        try {
            List<Object[]> objectList = em.createNativeQuery("select * from "
                    + "(select id as id, social_profile_id, title, data, 'DT' as type from discussion_topic dt "
                    + "where status = 'A' and dt.data < '" + date + "'"
                    + "union "
                    + "select dt.id as id, dtm.social_profile_id, dt.title, dtm.data, 'DTM' as type from discussion_topic_msg dtm "
                    + "join discussion_topic dt on dtm.discussion_topic_id = dt.id "
                    + "where dtm.status = 'A' and dtm.data < '" + date + "' "
                    + "union "
                    + "select eo.id, eo.social_profile_id, eo.name, eo.date as data, 'EO' as type from educational_object eo "
                    + "where eo.status = 'AC' and eo.date < '" + date + "' "
                    + "union "
                    + "select eo.id, eom.social_profile_fk, eo.name, eom.date as data, 'EOM' as type from educational_object_message eom "
                    + "join educational_object eo on eo.id = eom.educational_object_fk "
                    + "where eo.status = 'AC' and eom.date < '" + date + "' "
                    + "union "
                    + "select eo.id, eol.social_profile_fk, eo.name, eol.date as data, 'EOL' as type from educational_object_like eol "
                    + "join educational_object eo on eo.id = eol.educational_object_fk "
                    + "where eo.status = 'AC' and eol.date < '" + date + "') "
                    + "as news order by data desc limit 4").getResultList();
            List<NewActivity> newActivityList = new ArrayList<>();
            SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
            for (Object[] obj : objectList) {
                NewActivity newActivity = new NewActivity((Integer) obj[0], (SocialProfile) socialProfileJpaController.findSocialProfileBySocialProfileId((Integer) obj[1]), (String) obj[2], (Date) obj[3], (String) obj[4]);
                newActivityList.add(newActivity);
            }
            return newActivityList;
        } finally {
            em.close();
        }
    }

    public List<DiscussionTopic> findMostAcessedTopics(Integer interestId) {
        EntityManager em = getEntityManager();
        try {
            List<DiscussionTopic> discussionTopicList = (List<DiscussionTopic>) em.createNativeQuery("select * from discussion_topic "
                    + "where status = 'A' and theme_id = '" + interestId + "' order by views desc limit 3 ", DiscussionTopic.class).getResultList();
            return discussionTopicList;

        } finally {
            em.close();
        }
    }

    public List<DiscussionTopic> findDiscussionTopics(String name, Date date, Integer interestId, Integer limit) {
        EntityManager em = getEntityManager();
        try {
            String partialQuery = "";
            if (name != null) {
                partialQuery += " and (upper(dt.title) like '%" + name.toUpperCase() + "%' or upper(t.name) like '%" + name.toUpperCase() + "%')";
            }
            if (interestId != null) {
                partialQuery += " and theme_id = '" + interestId + "'";
            }
            if (date != null) {
                partialQuery += " and data < '" + date + "'";
            }
            partialQuery += " order by data desc";
            if (limit != null) {
                partialQuery += " limit " + limit;
            }
            List<DiscussionTopic> discussionTopicList = (List<DiscussionTopic>) em.createNativeQuery("select distinct dt.* from discussion_topic dt "
                    + "left join topic_tags tt on tt.discussion_topic_id = dt.id "
                    + "left join tags t on tt.tags_id = t.id "
                    + "where dt.status = 'A' " + partialQuery, DiscussionTopic.class).getResultList();
            return discussionTopicList;

        } finally {
            em.close();
        }
    }

    public void increaseViews(Integer id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createNativeQuery("UPDATE discussion_topic SET views = views + 1 where id = " + id);
            query.executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
