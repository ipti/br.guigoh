/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.dao;

import com.guigoh.primata.dao.exceptions.NonexistentEntityException;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import com.guigoh.primata.entity.SecretQuestion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.guigoh.primata.entity.Users;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Joe
 */
public class SecretQuestionDAO implements Serializable {

    private EntityManagerFactory emf = JPAUtil.getEMF();
            
    public SecretQuestionDAO() {
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SecretQuestion secretQuestion) throws RollbackFailureException, Exception {
        if (secretQuestion.getUsersCollection() == null) {
            secretQuestion.setUsersCollection(new ArrayList<Users>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Users> attachedUsersCollection = new ArrayList<Users>();
            for (Users usersCollectionUsersToAttach : secretQuestion.getUsersCollection()) {
                usersCollectionUsersToAttach = em.getReference(usersCollectionUsersToAttach.getClass(), usersCollectionUsersToAttach.getUsername());
                attachedUsersCollection.add(usersCollectionUsersToAttach);
            }
            secretQuestion.setUsersCollection(attachedUsersCollection);
            em.persist(secretQuestion);
            for (Users usersCollectionUsers : secretQuestion.getUsersCollection()) {
                SecretQuestion oldSecretQuestionIdOfUsersCollectionUsers = usersCollectionUsers.getSecretQuestionId();
                usersCollectionUsers.setSecretQuestionId(secretQuestion);
                usersCollectionUsers = em.merge(usersCollectionUsers);
                if (oldSecretQuestionIdOfUsersCollectionUsers != null) {
                    oldSecretQuestionIdOfUsersCollectionUsers.getUsersCollection().remove(usersCollectionUsers);
                    oldSecretQuestionIdOfUsersCollectionUsers = em.merge(oldSecretQuestionIdOfUsersCollectionUsers);
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

    public void edit(SecretQuestion secretQuestion) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SecretQuestion persistentSecretQuestion = em.find(SecretQuestion.class, secretQuestion.getId());
            Collection<Users> usersCollectionOld = persistentSecretQuestion.getUsersCollection();
            Collection<Users> usersCollectionNew = secretQuestion.getUsersCollection();
            Collection<Users> attachedUsersCollectionNew = new ArrayList<Users>();
            for (Users usersCollectionNewUsersToAttach : usersCollectionNew) {
                usersCollectionNewUsersToAttach = em.getReference(usersCollectionNewUsersToAttach.getClass(), usersCollectionNewUsersToAttach.getUsername());
                attachedUsersCollectionNew.add(usersCollectionNewUsersToAttach);
            }
            usersCollectionNew = attachedUsersCollectionNew;
            secretQuestion.setUsersCollection(usersCollectionNew);
            secretQuestion = em.merge(secretQuestion);
            for (Users usersCollectionOldUsers : usersCollectionOld) {
                if (!usersCollectionNew.contains(usersCollectionOldUsers)) {
                    usersCollectionOldUsers.setSecretQuestionId(null);
                    usersCollectionOldUsers = em.merge(usersCollectionOldUsers);
                }
            }
            for (Users usersCollectionNewUsers : usersCollectionNew) {
                if (!usersCollectionOld.contains(usersCollectionNewUsers)) {
                    SecretQuestion oldSecretQuestionIdOfUsersCollectionNewUsers = usersCollectionNewUsers.getSecretQuestionId();
                    usersCollectionNewUsers.setSecretQuestionId(secretQuestion);
                    usersCollectionNewUsers = em.merge(usersCollectionNewUsers);
                    if (oldSecretQuestionIdOfUsersCollectionNewUsers != null && !oldSecretQuestionIdOfUsersCollectionNewUsers.equals(secretQuestion)) {
                        oldSecretQuestionIdOfUsersCollectionNewUsers.getUsersCollection().remove(usersCollectionNewUsers);
                        oldSecretQuestionIdOfUsersCollectionNewUsers = em.merge(oldSecretQuestionIdOfUsersCollectionNewUsers);
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
                Integer id = secretQuestion.getId();
                if (findSecretQuestion(id) == null) {
                    throw new NonexistentEntityException("The secretQuestion with id " + id + " no longer exists.");
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
            SecretQuestion secretQuestion;
            try {
                secretQuestion = em.getReference(SecretQuestion.class, id);
                secretQuestion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The secretQuestion with id " + id + " no longer exists.", enfe);
            }
            Collection<Users> usersCollection = secretQuestion.getUsersCollection();
            for (Users usersCollectionUsers : usersCollection) {
                usersCollectionUsers.setSecretQuestionId(null);
                usersCollectionUsers = em.merge(usersCollectionUsers);
            }
            em.remove(secretQuestion);
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

    public List<SecretQuestion> findSecretQuestionEntities() {
        return findSecretQuestionEntities(true, -1, -1);
    }

    public List<SecretQuestion> findSecretQuestionEntities(int maxResults, int firstResult) {
        return findSecretQuestionEntities(false, maxResults, firstResult);
    }

    private List<SecretQuestion> findSecretQuestionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SecretQuestion.class));
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

    public SecretQuestion findSecretQuestion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SecretQuestion.class, id);
        } finally {
            em.close();
        }
    }

    public int getSecretQuestionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SecretQuestion> rt = cq.from(SecretQuestion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
