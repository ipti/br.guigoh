/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.dao;

import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.dao.exceptions.IllegalOrphanException;
import com.guigoh.primata.dao.exceptions.NonexistentEntityException;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import com.guigoh.primata.entity.DiscussionTopic;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.guigoh.primata.entity.Tags;
import java.util.ArrayList;
import java.util.Collection;
import com.guigoh.primata.entity.DiscussionTopicMsg;
import com.guigoh.primata.entity.NewActivity;
import com.guigoh.primata.entity.SocialProfile;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Joe
 */
public class DiscussionTopicDAO implements Serializable {

    private EntityManagerFactory emf = JPAUtil.getEMF();

    public DiscussionTopicDAO() {
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

    public List<DiscussionTopic> findDiscussionTopicsByTheme(Integer id) {
        EntityManager em = getEntityManager();
        try {
            List<DiscussionTopic> discussionTopicList = (List<DiscussionTopic>) em.createNativeQuery("select * from primata_discussion_topic "
                    + "where theme_id = " + id + " and status = 'A' order by data DESC", DiscussionTopic.class).getResultList();
            return discussionTopicList;
        } finally {
            em.close();
        }
    }

    public List<DiscussionTopic> loadDiscussionTopicsByExpression(String expression, String tag, Integer id) {
        EntityManager em = getEntityManager();
        try {


            String sql = "select distinct dt.* from primata_discussion_topic dt "
                    + "left join primata_discussion_topic_msg dtm on dt.id = dtm.discussion_topic_id ";
            if (tag != null) {
                if (!tag.equals("")) {
                    sql += "join primata_topic_tags tt on dt.id = tt.discussion_topic_id ";
                }
            }
            sql += "where dt.theme_id = " + id + " and dt.status = 'A' and "
                    + "((UPPER(dt.title) like '%" + expression.toUpperCase() + "%') "
                    + "or (UPPER(dt.body) like '%" + expression.toUpperCase() + "%') "
                    + "or (UPPER(dtm.reply) like '%" + expression.toUpperCase() + "%')) ";
            if (tag != null) {
                if (!tag.equals("")) {
                    sql += "and tt.tags_id = (select id from primata_tags where name = '" + tag + "') ";
                }
            }
            List<DiscussionTopic> discussionTopicList = (List<DiscussionTopic>) em.createNativeQuery(sql, DiscussionTopic.class).getResultList();
            return discussionTopicList;
        } finally {
            em.close();
        }
    }
    
    public List<NewActivity> getLastActivities(){
        EntityManager em = getEntityManager();
        try {
            List<Object[]> objectList = em.createNativeQuery("select * from "
                    + "(select id as id, social_profile_id, title, data, 'T' as type from primata_discussion_topic "
                    + "where status = 'A' "
                    + "union "
                    + "select dt.id as id, dtm.social_profile_id, dt.title, dtm.data, 'M' as type from primata_discussion_topic_msg dtm "
                    + "join primata_discussion_topic dt on dtm.discussion_topic_id = dt.id "
                    + "where dtm.status = 'A') "
                    + "as news order by data desc limit 5").getResultList();
            List<NewActivity> newActivityList = new ArrayList<NewActivity>();
            SocialProfileBO spBO = new SocialProfileBO();
            for(Object[] obj: objectList){
                NewActivity newActivity = new NewActivity((Integer)obj[0],(SocialProfile)spBO.findSocialProfileBySocialProfileId((Integer)obj[1]),(String)obj[2],(Date)obj[3],(String)obj[4]);
                newActivityList.add(newActivity);
            }
            return newActivityList;
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
