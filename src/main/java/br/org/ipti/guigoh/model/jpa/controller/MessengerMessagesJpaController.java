/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.entity.MessengerMessages;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.jpa.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author IPTI
 */
public class MessengerMessagesJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();;
    
    public MessengerMessagesJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MessengerMessages messengerMessages) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(messengerMessages);
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

    public void edit(MessengerMessages messengerMessages) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            messengerMessages = em.merge(messengerMessages);
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = messengerMessages.getId();
                if (findMessengerMessages(id) == null) {
                    throw new NonexistentEntityException("The messengerMessages with id " + id + " no longer exists.");
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
            MessengerMessages messengerMessages;
            try {
                messengerMessages = em.getReference(MessengerMessages.class, id);
                messengerMessages.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The messengerMessages with id " + id + " no longer exists.", enfe);
            }
            em.remove(messengerMessages);
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

    public List<MessengerMessages> findMessengerMessagesEntities() {
        return findMessengerMessagesEntities(true, -1, -1);
    }

    public List<MessengerMessages> findMessengerMessagesEntities(int maxResults, int firstResult) {
        return findMessengerMessagesEntities(false, maxResults, firstResult);
    }

    private List<MessengerMessages> findMessengerMessagesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MessengerMessages.class));
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

    public MessengerMessages findMessengerMessages(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MessengerMessages.class, id);
        } finally {
            em.close();
        }
    }

    public int getMessengerMessagesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MessengerMessages> rt = cq.from(MessengerMessages.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<MessengerMessages> getAllNonReadMessages(Integer socialProfileId){
        EntityManager em = getEntityManager();
        try {
            List<MessengerMessages> messengerMessagesList = (List<MessengerMessages>) em.createNativeQuery("select * from messenger_messages "
                    + "where message_delivered = 'N' and social_profile_id_receiver = '" + socialProfileId + "'", MessengerMessages.class).getResultList();
            if (messengerMessagesList == null){
                messengerMessagesList = new ArrayList<>();
            }
            return messengerMessagesList;
        } finally {
            em.close();
        }
    }
    
    public List<MessengerMessages> getFriendNonReadMessages(Integer socialProfileId, Integer friendSocialProfileId){
        EntityManager em = getEntityManager();
        try {
            List<MessengerMessages> messengerMessagesList = (List<MessengerMessages>) em.createNativeQuery("select * from messenger_messages "
                    + "where message_delivered = 'N' and social_profile_id_receiver = '" + socialProfileId + "' and social_profile_id_sender = '" + friendSocialProfileId + "'", MessengerMessages.class).getResultList();
            if (messengerMessagesList == null){
                messengerMessagesList = new ArrayList<>();
            }
            return messengerMessagesList;
        } finally {
            em.close();
        }
    }
    
    public List<MessengerMessages> getLastTenMessages(Integer loggedSocialProfileId, Integer socialProfileId){
        EntityManager em = getEntityManager();
        try {
            List<MessengerMessages> messengerMessagesList = (List<MessengerMessages>) em.createNativeQuery("select * from messenger_messages "
                    + "where message_delivered = 'Y' and ((social_profile_id_receiver = '"+loggedSocialProfileId+"' and social_profile_id_sender = '"+socialProfileId+"') or (social_profile_id_receiver = '"+socialProfileId+"' and social_profile_id_sender = '"+loggedSocialProfileId+"')) order by message_date desc limit 10", MessengerMessages.class).getResultList();
            if (messengerMessagesList == null){
                messengerMessagesList = new ArrayList<>();
            }
            return messengerMessagesList;
        } finally {
            em.close();
        }
    }
    
    public List<MessengerMessages> getAllMessages(Integer loggedSocialProfileId, Integer socialProfileId){
        EntityManager em = getEntityManager();
        try {
            List<MessengerMessages> messengerMessagesList = (List<MessengerMessages>) em.createNativeQuery("select * from messenger_messages "
                    + "where ((social_profile_id_receiver = '"+loggedSocialProfileId+"' and social_profile_id_sender = '"+socialProfileId+"') or (social_profile_id_receiver = '"+socialProfileId+"' and social_profile_id_sender = '"+loggedSocialProfileId+"')) order by message_date", MessengerMessages.class).getResultList();
            if (messengerMessagesList == null){
                messengerMessagesList = new ArrayList<>();
            }
            return messengerMessagesList;
        } finally {
            em.close();
        }
    }
    
    public List<SocialProfile> getAllContacts(Integer loggedSocialProfileId){
        EntityManager em = getEntityManager();
        try {
            List<SocialProfile> contactsList = (List<SocialProfile>) em.createNativeQuery("select * from social_profile "
                    + "where social_profile_id in (select social_profile_id_receiver as spr from messenger_messages "
                    + "where social_profile_id_sender = '"+loggedSocialProfileId+"' and (message_delivered = 'Y' or message_delivered = 'N') "
                    + "UNION "
                    + "select social_profile_id_sender as spr from messenger_messages "
                    + "where social_profile_id_receiver = '"+loggedSocialProfileId+"' and (message_delivered = 'Y' or message_delivered = 'N')) ", SocialProfile.class).getResultList();
            if (contactsList == null){
                contactsList = new ArrayList<>();
            }
            return contactsList;
        } finally {
            em.close();
        }
    }
}
