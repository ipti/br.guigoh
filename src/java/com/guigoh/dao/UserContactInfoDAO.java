/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.dao;

import com.guigoh.dao.exceptions.NonexistentEntityException;
import com.guigoh.dao.exceptions.PreexistingEntityException;
import com.guigoh.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.guigoh.entity.Users;
import com.guigoh.entity.ContactType;
import com.guigoh.entity.UserContactInfo;
import com.guigoh.entity.UserContactInfoPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Joe
 */
public class UserContactInfoDAO implements Serializable {

    public UserContactInfoDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserContactInfo userContactInfo) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (userContactInfo.getUserContactInfoPK() == null) {
            userContactInfo.setUserContactInfoPK(new UserContactInfoPK());
        }
        userContactInfo.getUserContactInfoPK().setTokenId(userContactInfo.getUsers().getToken());
        userContactInfo.getUserContactInfoPK().setContactTypeId(userContactInfo.getContactType().getId());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Users users = userContactInfo.getUsers();
            if (users != null) {
                users = em.getReference(users.getClass(), users.getUsername());
                userContactInfo.setUsers(users);
            }
            ContactType contactType = userContactInfo.getContactType();
            if (contactType != null) {
                contactType = em.getReference(contactType.getClass(), contactType.getId());
                userContactInfo.setContactType(contactType);
            }
            em.persist(userContactInfo);
            if (users != null) {
                users.getUserContactInfoCollection().add(userContactInfo);
                users = em.merge(users);
            }
            if (contactType != null) {
                contactType.getUserContactInfoCollection().add(userContactInfo);
                contactType = em.merge(contactType);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUserContactInfo(userContactInfo.getUserContactInfoPK()) != null) {
                throw new PreexistingEntityException("UserContactInfo " + userContactInfo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserContactInfo userContactInfo) throws NonexistentEntityException, RollbackFailureException, Exception {
        userContactInfo.getUserContactInfoPK().setTokenId(userContactInfo.getUsers().getToken());
        userContactInfo.getUserContactInfoPK().setContactTypeId(userContactInfo.getContactType().getId());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UserContactInfo persistentUserContactInfo = em.find(UserContactInfo.class, userContactInfo.getUserContactInfoPK());
            Users usersOld = persistentUserContactInfo.getUsers();
            Users usersNew = userContactInfo.getUsers();
            ContactType contactTypeOld = persistentUserContactInfo.getContactType();
            ContactType contactTypeNew = userContactInfo.getContactType();
            if (usersNew != null) {
                usersNew = em.getReference(usersNew.getClass(), usersNew.getUsername());
                userContactInfo.setUsers(usersNew);
            }
            if (contactTypeNew != null) {
                contactTypeNew = em.getReference(contactTypeNew.getClass(), contactTypeNew.getId());
                userContactInfo.setContactType(contactTypeNew);
            }
            userContactInfo = em.merge(userContactInfo);
            if (usersOld != null && !usersOld.equals(usersNew)) {
                usersOld.getUserContactInfoCollection().remove(userContactInfo);
                usersOld = em.merge(usersOld);
            }
            if (usersNew != null && !usersNew.equals(usersOld)) {
                usersNew.getUserContactInfoCollection().add(userContactInfo);
                usersNew = em.merge(usersNew);
            }
            if (contactTypeOld != null && !contactTypeOld.equals(contactTypeNew)) {
                contactTypeOld.getUserContactInfoCollection().remove(userContactInfo);
                contactTypeOld = em.merge(contactTypeOld);
            }
            if (contactTypeNew != null && !contactTypeNew.equals(contactTypeOld)) {
                contactTypeNew.getUserContactInfoCollection().add(userContactInfo);
                contactTypeNew = em.merge(contactTypeNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                UserContactInfoPK id = userContactInfo.getUserContactInfoPK();
                if (findUserContactInfo(id) == null) {
                    throw new NonexistentEntityException("The userContactInfo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(UserContactInfoPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UserContactInfo userContactInfo;
            try {
                userContactInfo = em.getReference(UserContactInfo.class, id);
                userContactInfo.getUserContactInfoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userContactInfo with id " + id + " no longer exists.", enfe);
            }
            Users users = userContactInfo.getUsers();
            if (users != null) {
                users.getUserContactInfoCollection().remove(userContactInfo);
                users = em.merge(users);
            }
            ContactType contactType = userContactInfo.getContactType();
            if (contactType != null) {
                contactType.getUserContactInfoCollection().remove(userContactInfo);
                contactType = em.merge(contactType);
            }
            em.remove(userContactInfo);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
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

    public List<UserContactInfo> findUserContactInfoEntities() {
        return findUserContactInfoEntities(true, -1, -1);
    }

    public List<UserContactInfo> findUserContactInfoEntities(int maxResults, int firstResult) {
        return findUserContactInfoEntities(false, maxResults, firstResult);
    }

    private List<UserContactInfo> findUserContactInfoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserContactInfo.class));
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

    public UserContactInfo findUserContactInfo(UserContactInfoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserContactInfo.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserContactInfoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserContactInfo> rt = cq.from(UserContactInfo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
