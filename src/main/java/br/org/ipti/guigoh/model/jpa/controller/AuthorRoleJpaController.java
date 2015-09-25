/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.org.ipti.guigoh.model.entity.Author;
import br.org.ipti.guigoh.model.entity.AuthorRole;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author iptipc008
 */
public class AuthorRoleJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();
    
    public AuthorRoleJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AuthorRole authorRole) throws RollbackFailureException, Exception {
        if (authorRole.getAuthorCollection() == null) {
            authorRole.setAuthorCollection(new ArrayList<Author>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Author> attachedAuthorCollection = new ArrayList<Author>();
            for (Author authorCollectionAuthorToAttach : authorRole.getAuthorCollection()) {
                authorCollectionAuthorToAttach = em.getReference(authorCollectionAuthorToAttach.getClass(), authorCollectionAuthorToAttach.getId());
                attachedAuthorCollection.add(authorCollectionAuthorToAttach);
            }
            authorRole.setAuthorCollection(attachedAuthorCollection);
            em.persist(authorRole);
            for (Author authorCollectionAuthor : authorRole.getAuthorCollection()) {
                AuthorRole oldAuthorRoleFkOfAuthorCollectionAuthor = authorCollectionAuthor.getAuthorRoleFk();
                authorCollectionAuthor.setAuthorRoleFk(authorRole);
                authorCollectionAuthor = em.merge(authorCollectionAuthor);
                if (oldAuthorRoleFkOfAuthorCollectionAuthor != null) {
                    oldAuthorRoleFkOfAuthorCollectionAuthor.getAuthorCollection().remove(authorCollectionAuthor);
                    oldAuthorRoleFkOfAuthorCollectionAuthor = em.merge(oldAuthorRoleFkOfAuthorCollectionAuthor);
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

    public void edit(AuthorRole authorRole) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AuthorRole persistentAuthorRole = em.find(AuthorRole.class, authorRole.getId());
            Collection<Author> authorCollectionOld = persistentAuthorRole.getAuthorCollection();
            Collection<Author> authorCollectionNew = authorRole.getAuthorCollection();
            Collection<Author> attachedAuthorCollectionNew = new ArrayList<Author>();
            for (Author authorCollectionNewAuthorToAttach : authorCollectionNew) {
                authorCollectionNewAuthorToAttach = em.getReference(authorCollectionNewAuthorToAttach.getClass(), authorCollectionNewAuthorToAttach.getId());
                attachedAuthorCollectionNew.add(authorCollectionNewAuthorToAttach);
            }
            authorCollectionNew = attachedAuthorCollectionNew;
            authorRole.setAuthorCollection(authorCollectionNew);
            authorRole = em.merge(authorRole);
            for (Author authorCollectionOldAuthor : authorCollectionOld) {
                if (!authorCollectionNew.contains(authorCollectionOldAuthor)) {
                    authorCollectionOldAuthor.setAuthorRoleFk(null);
                    authorCollectionOldAuthor = em.merge(authorCollectionOldAuthor);
                }
            }
            for (Author authorCollectionNewAuthor : authorCollectionNew) {
                if (!authorCollectionOld.contains(authorCollectionNewAuthor)) {
                    AuthorRole oldAuthorRoleFkOfAuthorCollectionNewAuthor = authorCollectionNewAuthor.getAuthorRoleFk();
                    authorCollectionNewAuthor.setAuthorRoleFk(authorRole);
                    authorCollectionNewAuthor = em.merge(authorCollectionNewAuthor);
                    if (oldAuthorRoleFkOfAuthorCollectionNewAuthor != null && !oldAuthorRoleFkOfAuthorCollectionNewAuthor.equals(authorRole)) {
                        oldAuthorRoleFkOfAuthorCollectionNewAuthor.getAuthorCollection().remove(authorCollectionNewAuthor);
                        oldAuthorRoleFkOfAuthorCollectionNewAuthor = em.merge(oldAuthorRoleFkOfAuthorCollectionNewAuthor);
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
                Integer id = authorRole.getId();
                if (findAuthorRole(id) == null) {
                    throw new NonexistentEntityException("The authorRole with id " + id + " no longer exists.");
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
            AuthorRole authorRole;
            try {
                authorRole = em.getReference(AuthorRole.class, id);
                authorRole.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The authorRole with id " + id + " no longer exists.", enfe);
            }
            Collection<Author> authorCollection = authorRole.getAuthorCollection();
            for (Author authorCollectionAuthor : authorCollection) {
                authorCollectionAuthor.setAuthorRoleFk(null);
                authorCollectionAuthor = em.merge(authorCollectionAuthor);
            }
            em.remove(authorRole);
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

    public List<AuthorRole> findAuthorRoleEntities() {
        return findAuthorRoleEntities(true, -1, -1);
    }

    public List<AuthorRole> findAuthorRoleEntities(int maxResults, int firstResult) {
        return findAuthorRoleEntities(false, maxResults, firstResult);
    }

    private List<AuthorRole> findAuthorRoleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AuthorRole.class));
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

    public AuthorRole findAuthorRole(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AuthorRole.class, id);
        } finally {
            em.close();
        }
    }

    public int getAuthorRoleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AuthorRole> rt = cq.from(AuthorRole.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
