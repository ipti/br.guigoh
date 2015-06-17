/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.jpa.exceptions.IllegalOrphanException;
import br.org.ipti.guigoh.model.jpa.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.model.entity.ContactType;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.org.ipti.guigoh.model.entity.UserContactInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Joe
 */
public class ContactTypeJpaController implements Serializable {

    public ContactTypeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ContactType contactType) throws RollbackFailureException, Exception {
        if (contactType.getUserContactInfoCollection() == null) {
            contactType.setUserContactInfoCollection(new ArrayList<UserContactInfo>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<UserContactInfo> attachedUserContactInfoCollection = new ArrayList<UserContactInfo>();
            for (UserContactInfo userContactInfoCollectionUserContactInfoToAttach : contactType.getUserContactInfoCollection()) {
                userContactInfoCollectionUserContactInfoToAttach = em.getReference(userContactInfoCollectionUserContactInfoToAttach.getClass(), userContactInfoCollectionUserContactInfoToAttach.getUserContactInfoPK());
                attachedUserContactInfoCollection.add(userContactInfoCollectionUserContactInfoToAttach);
            }
            contactType.setUserContactInfoCollection(attachedUserContactInfoCollection);
            em.persist(contactType);
            for (UserContactInfo userContactInfoCollectionUserContactInfo : contactType.getUserContactInfoCollection()) {
                ContactType oldContactTypeOfUserContactInfoCollectionUserContactInfo = userContactInfoCollectionUserContactInfo.getContactType();
                userContactInfoCollectionUserContactInfo.setContactType(contactType);
                userContactInfoCollectionUserContactInfo = em.merge(userContactInfoCollectionUserContactInfo);
                if (oldContactTypeOfUserContactInfoCollectionUserContactInfo != null) {
                    oldContactTypeOfUserContactInfoCollectionUserContactInfo.getUserContactInfoCollection().remove(userContactInfoCollectionUserContactInfo);
                    oldContactTypeOfUserContactInfoCollectionUserContactInfo = em.merge(oldContactTypeOfUserContactInfoCollectionUserContactInfo);
                }
            }
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

    public void edit(ContactType contactType) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ContactType persistentContactType = em.find(ContactType.class, contactType.getId());
            Collection<UserContactInfo> userContactInfoCollectionOld = persistentContactType.getUserContactInfoCollection();
            Collection<UserContactInfo> userContactInfoCollectionNew = contactType.getUserContactInfoCollection();
            List<String> illegalOrphanMessages = null;
            for (UserContactInfo userContactInfoCollectionOldUserContactInfo : userContactInfoCollectionOld) {
                if (!userContactInfoCollectionNew.contains(userContactInfoCollectionOldUserContactInfo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserContactInfo " + userContactInfoCollectionOldUserContactInfo + " since its contactType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<UserContactInfo> attachedUserContactInfoCollectionNew = new ArrayList<UserContactInfo>();
            for (UserContactInfo userContactInfoCollectionNewUserContactInfoToAttach : userContactInfoCollectionNew) {
                userContactInfoCollectionNewUserContactInfoToAttach = em.getReference(userContactInfoCollectionNewUserContactInfoToAttach.getClass(), userContactInfoCollectionNewUserContactInfoToAttach.getUserContactInfoPK());
                attachedUserContactInfoCollectionNew.add(userContactInfoCollectionNewUserContactInfoToAttach);
            }
            userContactInfoCollectionNew = attachedUserContactInfoCollectionNew;
            contactType.setUserContactInfoCollection(userContactInfoCollectionNew);
            contactType = em.merge(contactType);
            for (UserContactInfo userContactInfoCollectionNewUserContactInfo : userContactInfoCollectionNew) {
                if (!userContactInfoCollectionOld.contains(userContactInfoCollectionNewUserContactInfo)) {
                    ContactType oldContactTypeOfUserContactInfoCollectionNewUserContactInfo = userContactInfoCollectionNewUserContactInfo.getContactType();
                    userContactInfoCollectionNewUserContactInfo.setContactType(contactType);
                    userContactInfoCollectionNewUserContactInfo = em.merge(userContactInfoCollectionNewUserContactInfo);
                    if (oldContactTypeOfUserContactInfoCollectionNewUserContactInfo != null && !oldContactTypeOfUserContactInfoCollectionNewUserContactInfo.equals(contactType)) {
                        oldContactTypeOfUserContactInfoCollectionNewUserContactInfo.getUserContactInfoCollection().remove(userContactInfoCollectionNewUserContactInfo);
                        oldContactTypeOfUserContactInfoCollectionNewUserContactInfo = em.merge(oldContactTypeOfUserContactInfoCollectionNewUserContactInfo);
                    }
                }
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
                Integer id = contactType.getId();
                if (findContactType(id) == null) {
                    throw new NonexistentEntityException("The contactType with id " + id + " no longer exists.");
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
            utx.begin();
            em = getEntityManager();
            ContactType contactType;
            try {
                contactType = em.getReference(ContactType.class, id);
                contactType.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contactType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UserContactInfo> userContactInfoCollectionOrphanCheck = contactType.getUserContactInfoCollection();
            for (UserContactInfo userContactInfoCollectionOrphanCheckUserContactInfo : userContactInfoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ContactType (" + contactType + ") cannot be destroyed since the UserContactInfo " + userContactInfoCollectionOrphanCheckUserContactInfo + " in its userContactInfoCollection field has a non-nullable contactType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(contactType);
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

    public List<ContactType> findContactTypeEntities() {
        return findContactTypeEntities(true, -1, -1);
    }

    public List<ContactType> findContactTypeEntities(int maxResults, int firstResult) {
        return findContactTypeEntities(false, maxResults, firstResult);
    }

    private List<ContactType> findContactTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ContactType.class));
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

    public ContactType findContactType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ContactType.class, id);
        } finally {
            em.close();
        }
    }

    public int getContactTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ContactType> rt = cq.from(ContactType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
