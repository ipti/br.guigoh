/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.dao;

import com.guigoh.dao.exceptions.IllegalOrphanException;
import com.guigoh.dao.exceptions.NonexistentEntityException;
import com.guigoh.dao.exceptions.PreexistingEntityException;
import com.guigoh.dao.exceptions.RollbackFailureException;
import com.guigoh.entity.UserAuthorization;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.guigoh.entity.Users;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Joe
 */
public class UserAuthorizationDAO implements Serializable {

     private transient EntityManagerFactory emf = JPAUtil.getEMF();
    
    public UserAuthorizationDAO() {

    }

   

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserAuthorization authorization) throws IllegalOrphanException, PreexistingEntityException, RollbackFailureException, Exception {
        List<String> illegalOrphanMessages = null;
        Users usersOrphanCheck = authorization.getUsers();
        if (usersOrphanCheck != null) {
            UserAuthorization oldAuthorizationOfUsers = usersOrphanCheck.getAuthorization();
            if (oldAuthorizationOfUsers != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Users " + usersOrphanCheck + " already has an item of type Authorization whose users column cannot be null. Please make another selection for the users field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users users = authorization.getUsers();
            if (users != null) {
                users = em.getReference(users.getClass(), users.getUsername());
                authorization.setUsers(users);
            }
            em.persist(authorization);
            if (users != null) {
                users.setAuthorization(authorization);
                users = em.merge(users);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAuthorization(authorization.getTokenId()) != null) {
                throw new PreexistingEntityException("Authorization " + authorization + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserAuthorization authorization) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserAuthorization persistentAuthorization = em.find(UserAuthorization.class, authorization.getTokenId());
            Users usersOld = persistentAuthorization.getUsers();
            Users usersNew = authorization.getUsers();
            List<String> illegalOrphanMessages = null;
            if (usersNew != null && !usersNew.equals(usersOld)) {
                UserAuthorization oldAuthorizationOfUsers = usersNew.getAuthorization();
                if (oldAuthorizationOfUsers != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Users " + usersNew + " already has an item of type Authorization whose users column cannot be null. Please make another selection for the users field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usersNew != null) {
                usersNew = em.getReference(usersNew.getClass(), usersNew.getUsername());
                authorization.setUsers(usersNew);
            }
            authorization = em.merge(authorization);
            if (usersOld != null && !usersOld.equals(usersNew)) {
                usersOld.setAuthorization(null);
                usersOld = em.merge(usersOld);
            }
            if (usersNew != null && !usersNew.equals(usersOld)) {
                usersNew.setAuthorization(authorization);
                usersNew = em.merge(usersNew);
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
                String id = authorization.getTokenId();
                if (findAuthorization(id) == null) {
                    throw new NonexistentEntityException("The authorization with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserAuthorization authorization;
            try {
                authorization = em.getReference(UserAuthorization.class, id);
                authorization.getTokenId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The authorization with id " + id + " no longer exists.", enfe);
            }
            Users users = authorization.getUsers();
            if (users != null) {
                users.setAuthorization(null);
                users = em.merge(users);
            }
            em.remove(authorization);
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

    public List<UserAuthorization> findAuthorizationEntities() {
        return findAuthorizationEntities(true, -1, -1);
    }

    public List<UserAuthorization> findAuthorizationEntities(int maxResults, int firstResult) {
        return findAuthorizationEntities(false, maxResults, firstResult);
    }

    private List<UserAuthorization> findAuthorizationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserAuthorization.class));
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

    public UserAuthorization findAuthorization(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserAuthorization.class, id);
        } finally {
            em.close();
        }
    }

    public int getAuthorizationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserAuthorization> rt = cq.from(UserAuthorization.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
     public List<UserAuthorization> findAuthorizationsBySubnetwork(Integer subnetwork) {
        EntityManager em = getEntityManager();
        try {
            List<UserAuthorization> authorizationList = (List<UserAuthorization>) em.createNativeQuery("select a.* from user_authorization a "
                    + "join social_profile s on a.token_id = s.token_id "
                    + "where a.status = 'PC' and s.subnetwork_id = '" + subnetwork + "'", UserAuthorization.class).getResultList();
            return authorizationList;
        } finally {
            em.close();
        }
    }
    
}
