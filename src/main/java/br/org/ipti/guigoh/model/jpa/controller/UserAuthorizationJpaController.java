/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.IllegalOrphanException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.PreexistingEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.model.entity.UserAuthorization;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.org.ipti.guigoh.model.entity.Users;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Joe
 */
public class UserAuthorizationJpaController implements Serializable {

     private transient EntityManagerFactory emf = PersistenceUnit.getEMF();
    
    public UserAuthorizationJpaController() {

    }

   

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserAuthorization userAuthorization) throws IllegalOrphanException, PreexistingEntityException, RollbackFailureException, Exception {
        List<String> illegalOrphanMessages = null;
        Users usersOrphanCheck = userAuthorization.getUsers();
        if (usersOrphanCheck != null) {
            UserAuthorization oldUserAuthorizationOfUsers = usersOrphanCheck.getUserAuthorization();
            if (oldUserAuthorizationOfUsers != null) {
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
            Users users = userAuthorization.getUsers();
            if (users != null) {
                users = em.getReference(users.getClass(), users.getUsername());
                userAuthorization.setUsers(users);
            }
            em.persist(userAuthorization);
            if (users != null) {
                users.setUserAuthorization(userAuthorization);
                users = em.merge(users);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUserAuthorization(userAuthorization.getTokenId()) != null) {
                throw new PreexistingEntityException("Authorization " + userAuthorization + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserAuthorization userAuthorization) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserAuthorization persistentUserAuthorization = em.find(UserAuthorization.class, userAuthorization.getTokenId());
            Users usersOld = persistentUserAuthorization.getUsers();
            Users usersNew = userAuthorization.getUsers();
            List<String> illegalOrphanMessages = null;
            if (usersNew != null && !usersNew.equals(usersOld)) {
                UserAuthorization oldAuthorizationOfUsers = usersNew.getUserAuthorization();
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
                userAuthorization.setUsers(usersNew);
            }
            userAuthorization = em.merge(userAuthorization);
            if (usersOld != null && !usersOld.equals(usersNew)) {
                usersOld.setUserAuthorization(null);
                usersOld = em.merge(usersOld);
            }
            if (usersNew != null && !usersNew.equals(usersOld)) {
                usersNew.setUserAuthorization(userAuthorization);
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
                String id = userAuthorization.getTokenId();
                if (findUserAuthorization(id) == null) {
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
            UserAuthorization userAuthorization;
            try {
                userAuthorization = em.getReference(UserAuthorization.class, id);
                userAuthorization.getTokenId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The authorization with id " + id + " no longer exists.", enfe);
            }
            Users users = userAuthorization.getUsers();
            if (users != null) {
                users.setUserAuthorization(null);
                users = em.merge(users);
            }
            em.remove(userAuthorization);
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

    public List<UserAuthorization> getPendingUsers(){
        EntityManager em = getEntityManager();
        try {
            List<UserAuthorization> pendingUserList = (List<UserAuthorization>) em.createNativeQuery("select * from social_profile where token_id in "
                    + "(select token_id from user_authorization where status = 'PC') "
                    + "order by social_profile_id desc", 
                    UserAuthorization.class).getResultList();
            return pendingUserList;
        } finally {
            em.close();
        }
    }
    
    public List<UserAuthorization> getActiveUsers(){
        EntityManager em = getEntityManager();
        try {
            List<UserAuthorization> activeUserList = (List<UserAuthorization>) em.createNativeQuery("select * from social_profile where token_id in "
                    + "(select token_id from user_authorization where status = 'AC') "
                    + "order by social_profile_id desc", 
                    UserAuthorization.class).getResultList();
            return activeUserList;
        } finally {
            em.close();
        }
    }
    
    public List<UserAuthorization> getInactiveUsers(){
        EntityManager em = getEntityManager();
        try {
            List<UserAuthorization> inactiveUserList = (List<UserAuthorization>) em.createNativeQuery("select * from social_profile where token_id in "
                    + "(select token_id from user_authorization where status = 'IC') "
                    + "order by social_profile_id desc", 
                    UserAuthorization.class).getResultList();
            return inactiveUserList;
        } finally {
            em.close();
        }
    }
    
    public List<UserAuthorization> findUserAuthorizationsByRole(String role){
        EntityManager em = getEntityManager();
        try{
            List<UserAuthorization> authorizationList = (List<UserAuthorization>) em.createNativeQuery("select * from user_authorization where roles = '" + role + "'",
                    UserAuthorization.class).getResultList();
            return authorizationList;
        } finally {
            em.close();
        }
    }
    
    public List<UserAuthorization> findUserAuthorizationEntities() {
        return findUserAuthorizationEntities(true, -1, -1);
    }

    public List<UserAuthorization> findUserAuthorizationEntities(int maxResults, int firstResult) {
        return findUserAuthorizationEntities(false, maxResults, firstResult);
    }

    private List<UserAuthorization> findUserAuthorizationEntities(boolean all, int maxResults, int firstResult) {
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

    public UserAuthorization findUserAuthorization(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserAuthorization.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserAuthorizationCount() {
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
    
     public List<UserAuthorization> findUserAuthorizationsBySubnetwork(Integer subnetwork) {
        EntityManager em = getEntityManager();
        try {
            List<UserAuthorization> userAuthorizationList = (List<UserAuthorization>) em.createNativeQuery("select a.* from user_authorization a "
                    + "join social_profile s on a.token_id = s.token_id "
                    + "where a.status = 'PC' and s.subnetwork_id = '" + subnetwork + "'", UserAuthorization.class).getResultList();
            if (userAuthorizationList == null) {
                return new ArrayList<>();
            }
            return userAuthorizationList;
        } finally {
            em.close();
        }
    }
    
}
