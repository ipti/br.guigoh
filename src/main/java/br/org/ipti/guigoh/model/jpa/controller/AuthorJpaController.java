/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.entity.Author;
import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author ipti008
 */
public class AuthorJpaController implements Serializable {

    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();
    
    public AuthorJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Author author) throws RollbackFailureException, Exception {
        if (author.getEducationalObjectCollection() == null) {
            author.setEducationalObjectCollection(new ArrayList<EducationalObject>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<EducationalObject> attachedEducationalObjectCollection = new ArrayList<EducationalObject>();
            for (EducationalObject educationalObjectCollectionEducationalObjectToAttach : author.getEducationalObjectCollection()) {
                educationalObjectCollectionEducationalObjectToAttach = em.getReference(educationalObjectCollectionEducationalObjectToAttach.getClass(), educationalObjectCollectionEducationalObjectToAttach.getId());
                attachedEducationalObjectCollection.add(educationalObjectCollectionEducationalObjectToAttach);
            }
            author.setEducationalObjectCollection(attachedEducationalObjectCollection);
            em.persist(author);
            for (EducationalObject educationalObjectCollectionEducationalObject : author.getEducationalObjectCollection()) {
                educationalObjectCollectionEducationalObject.getAuthorCollection().add(author);
                educationalObjectCollectionEducationalObject = em.merge(educationalObjectCollectionEducationalObject);
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

    public void edit(Author author) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Author persistentAuthor = em.find(Author.class, author.getId());
            Collection<EducationalObject> educationalObjectCollectionOld = persistentAuthor.getEducationalObjectCollection();
            Collection<EducationalObject> educationalObjectCollectionNew = author.getEducationalObjectCollection();
            Collection<EducationalObject> attachedEducationalObjectCollectionNew = new ArrayList<EducationalObject>();
            for (EducationalObject educationalObjectCollectionNewEducationalObjectToAttach : educationalObjectCollectionNew) {
                educationalObjectCollectionNewEducationalObjectToAttach = em.getReference(educationalObjectCollectionNewEducationalObjectToAttach.getClass(), educationalObjectCollectionNewEducationalObjectToAttach.getId());
                attachedEducationalObjectCollectionNew.add(educationalObjectCollectionNewEducationalObjectToAttach);
            }
            educationalObjectCollectionNew = attachedEducationalObjectCollectionNew;
            author.setEducationalObjectCollection(educationalObjectCollectionNew);
            author = em.merge(author);
            for (EducationalObject educationalObjectCollectionOldEducationalObject : educationalObjectCollectionOld) {
                if (!educationalObjectCollectionNew.contains(educationalObjectCollectionOldEducationalObject)) {
                    educationalObjectCollectionOldEducationalObject.getAuthorCollection().remove(author);
                    educationalObjectCollectionOldEducationalObject = em.merge(educationalObjectCollectionOldEducationalObject);
                }
            }
            for (EducationalObject educationalObjectCollectionNewEducationalObject : educationalObjectCollectionNew) {
                if (!educationalObjectCollectionOld.contains(educationalObjectCollectionNewEducationalObject)) {
                    educationalObjectCollectionNewEducationalObject.getAuthorCollection().add(author);
                    educationalObjectCollectionNewEducationalObject = em.merge(educationalObjectCollectionNewEducationalObject);
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
                Integer id = author.getId();
                if (findAuthor(id) == null) {
                    throw new NonexistentEntityException("The author with id " + id + " no longer exists.");
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
            Author author;
            try {
                author = em.getReference(Author.class, id);
                author.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The author with id " + id + " no longer exists.", enfe);
            }
            Collection<EducationalObject> educationalObjectCollection = author.getEducationalObjectCollection();
            for (EducationalObject educationalObjectCollectionEducationalObject : educationalObjectCollection) {
                educationalObjectCollectionEducationalObject.getAuthorCollection().remove(author);
                educationalObjectCollectionEducationalObject = em.merge(educationalObjectCollectionEducationalObject);
            }
            em.remove(author);
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

    public List<Author> findAuthorEntities() {
        return findAuthorEntities(true, -1, -1);
    }

    public List<Author> findAuthorEntities(int maxResults, int firstResult) {
        return findAuthorEntities(false, maxResults, firstResult);
    }

    private List<Author> findAuthorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Author.class));
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

    public Author findAuthor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Author.class, id);
        } finally {
            em.close();
        }
    }

    public int getAuthorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Author> rt = cq.from(Author.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Author> findAuthorsByEducationalObjectId(Integer educationalObjectId){
        EntityManager em = getEntityManager();
        try{
            List<Author> authorList = (List<Author>) em.createNativeQuery("select a.* from (author a"
                    + " join educational_object_author eoa on a.id = eoa.author_id)"
                    + " join educational_object eo on eo.id = eoa.educational_object_id"
                    + " where eo.id = " + educationalObjectId + " order by a.name", Author.class).getResultList();
            return authorList;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
