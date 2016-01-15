/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.jpa.controller;

import br.org.ipti.guigoh.model.entity.Doc;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.DocGuest;
import java.util.ArrayList;
import java.util.Collection;
import br.org.ipti.guigoh.model.entity.DocHistory;
import br.org.ipti.guigoh.model.entity.DocMessage;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.IllegalOrphanException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.model.jpa.util.PersistenceUnit;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author iptipc008
 */
public class DocJpaController implements Serializable {

    public DocJpaController() {
    }
    private transient EntityManagerFactory emf = PersistenceUnit.getEMF();

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Doc doc) throws RollbackFailureException, Exception {
        if (doc.getDocGuestCollection() == null) {
            doc.setDocGuestCollection(new ArrayList<DocGuest>());
        }
        if (doc.getDocHistoryCollection() == null) {
            doc.setDocHistoryCollection(new ArrayList<DocHistory>());
        }
        if (doc.getDocMessageCollection() == null) {
            doc.setDocMessageCollection(new ArrayList<DocMessage>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SocialProfile creatorSocialProfileFk = doc.getCreatorSocialProfileFk();
            if (creatorSocialProfileFk != null) {
                creatorSocialProfileFk = em.getReference(creatorSocialProfileFk.getClass(), creatorSocialProfileFk.getTokenId());
                doc.setCreatorSocialProfileFk(creatorSocialProfileFk);
            }
            SocialProfile editorSocialProfileFk = doc.getEditorSocialProfileFk();
            if (editorSocialProfileFk != null) {
                editorSocialProfileFk = em.getReference(editorSocialProfileFk.getClass(), editorSocialProfileFk.getTokenId());
                doc.setEditorSocialProfileFk(editorSocialProfileFk);
            }
            Collection<DocGuest> attachedDocGuestCollection = new ArrayList<DocGuest>();
            for (DocGuest docGuestCollectionDocGuestToAttach : doc.getDocGuestCollection()) {
                docGuestCollectionDocGuestToAttach = em.getReference(docGuestCollectionDocGuestToAttach.getClass(), docGuestCollectionDocGuestToAttach.getId());
                attachedDocGuestCollection.add(docGuestCollectionDocGuestToAttach);
            }
            doc.setDocGuestCollection(attachedDocGuestCollection);
            Collection<DocHistory> attachedDocHistoryCollection = new ArrayList<DocHistory>();
            for (DocHistory docHistoryCollectionDocHistoryToAttach : doc.getDocHistoryCollection()) {
                docHistoryCollectionDocHistoryToAttach = em.getReference(docHistoryCollectionDocHistoryToAttach.getClass(), docHistoryCollectionDocHistoryToAttach.getId());
                attachedDocHistoryCollection.add(docHistoryCollectionDocHistoryToAttach);
            }
            doc.setDocHistoryCollection(attachedDocHistoryCollection);
            Collection<DocMessage> attachedDocMessageCollection = new ArrayList<DocMessage>();
            for (DocMessage docMessageCollectionDocMessageToAttach : doc.getDocMessageCollection()) {
                docMessageCollectionDocMessageToAttach = em.getReference(docMessageCollectionDocMessageToAttach.getClass(), docMessageCollectionDocMessageToAttach.getId());
                attachedDocMessageCollection.add(docMessageCollectionDocMessageToAttach);
            }
            doc.setDocMessageCollection(attachedDocMessageCollection);
            em.persist(doc);
            if (creatorSocialProfileFk != null) {
                creatorSocialProfileFk.getDocCollection().add(doc);
                creatorSocialProfileFk = em.merge(creatorSocialProfileFk);
            }
            if (editorSocialProfileFk != null) {
                editorSocialProfileFk.getDocCollection().add(doc);
                editorSocialProfileFk = em.merge(editorSocialProfileFk);
            }
            for (DocGuest docGuestCollectionDocGuest : doc.getDocGuestCollection()) {
                Doc oldDocFkOfDocGuestCollectionDocGuest = docGuestCollectionDocGuest.getDocFk();
                docGuestCollectionDocGuest.setDocFk(doc);
                docGuestCollectionDocGuest = em.merge(docGuestCollectionDocGuest);
                if (oldDocFkOfDocGuestCollectionDocGuest != null) {
                    oldDocFkOfDocGuestCollectionDocGuest.getDocGuestCollection().remove(docGuestCollectionDocGuest);
                    oldDocFkOfDocGuestCollectionDocGuest = em.merge(oldDocFkOfDocGuestCollectionDocGuest);
                }
            }
            for (DocHistory docHistoryCollectionDocHistory : doc.getDocHistoryCollection()) {
                Doc oldDocFkOfDocHistoryCollectionDocHistory = docHistoryCollectionDocHistory.getDocFk();
                docHistoryCollectionDocHistory.setDocFk(doc);
                docHistoryCollectionDocHistory = em.merge(docHistoryCollectionDocHistory);
                if (oldDocFkOfDocHistoryCollectionDocHistory != null) {
                    oldDocFkOfDocHistoryCollectionDocHistory.getDocHistoryCollection().remove(docHistoryCollectionDocHistory);
                    oldDocFkOfDocHistoryCollectionDocHistory = em.merge(oldDocFkOfDocHistoryCollectionDocHistory);
                }
            }
            for (DocMessage docMessageCollectionDocMessage : doc.getDocMessageCollection()) {
                Doc oldDocFkOfDocMessageCollectionDocMessage = docMessageCollectionDocMessage.getDocFk();
                docMessageCollectionDocMessage.setDocFk(doc);
                docMessageCollectionDocMessage = em.merge(docMessageCollectionDocMessage);
                if (oldDocFkOfDocMessageCollectionDocMessage != null) {
                    oldDocFkOfDocMessageCollectionDocMessage.getDocMessageCollection().remove(docMessageCollectionDocMessage);
                    oldDocFkOfDocMessageCollectionDocMessage = em.merge(oldDocFkOfDocMessageCollectionDocMessage);
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

    public void edit(Doc doc) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Doc persistentDoc = em.find(Doc.class, doc.getId());
            SocialProfile creatorSocialProfileFkOld = persistentDoc.getCreatorSocialProfileFk();
            SocialProfile creatorSocialProfileFkNew = doc.getCreatorSocialProfileFk();
            SocialProfile editorSocialProfileFkOld = persistentDoc.getEditorSocialProfileFk();
            SocialProfile editorSocialProfileFkNew = doc.getEditorSocialProfileFk();
            Collection<DocGuest> docGuestCollectionOld = persistentDoc.getDocGuestCollection();
            Collection<DocGuest> docGuestCollectionNew = doc.getDocGuestCollection();
            Collection<DocHistory> docHistoryCollectionOld = persistentDoc.getDocHistoryCollection();
            Collection<DocHistory> docHistoryCollectionNew = doc.getDocHistoryCollection();
            Collection<DocMessage> docMessageCollectionOld = persistentDoc.getDocMessageCollection();
            Collection<DocMessage> docMessageCollectionNew = doc.getDocMessageCollection();
            List<String> illegalOrphanMessages = null;
            for (DocGuest docGuestCollectionOldDocGuest : docGuestCollectionOld) {
                if (!docGuestCollectionNew.contains(docGuestCollectionOldDocGuest)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DocGuest " + docGuestCollectionOldDocGuest + " since its docFk field is not nullable.");
                }
            }
            for (DocHistory docHistoryCollectionOldDocHistory : docHistoryCollectionOld) {
                if (!docHistoryCollectionNew.contains(docHistoryCollectionOldDocHistory)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DocHistory " + docHistoryCollectionOldDocHistory + " since its docFk field is not nullable.");
                }
            }
            for (DocMessage docMessageCollectionOldDocMessage : docMessageCollectionOld) {
                if (!docMessageCollectionNew.contains(docMessageCollectionOldDocMessage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DocMessage " + docMessageCollectionOldDocMessage + " since its docFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (creatorSocialProfileFkNew != null) {
                creatorSocialProfileFkNew = em.getReference(creatorSocialProfileFkNew.getClass(), creatorSocialProfileFkNew.getTokenId());
                doc.setCreatorSocialProfileFk(creatorSocialProfileFkNew);
            }
            if (editorSocialProfileFkNew != null) {
                editorSocialProfileFkNew = em.getReference(editorSocialProfileFkNew.getClass(), editorSocialProfileFkNew.getTokenId());
                doc.setEditorSocialProfileFk(editorSocialProfileFkNew);
            }
            Collection<DocGuest> attachedDocGuestCollectionNew = new ArrayList<DocGuest>();
            for (DocGuest docGuestCollectionNewDocGuestToAttach : docGuestCollectionNew) {
                docGuestCollectionNewDocGuestToAttach = em.getReference(docGuestCollectionNewDocGuestToAttach.getClass(), docGuestCollectionNewDocGuestToAttach.getId());
                attachedDocGuestCollectionNew.add(docGuestCollectionNewDocGuestToAttach);
            }
            docGuestCollectionNew = attachedDocGuestCollectionNew;
            doc.setDocGuestCollection(docGuestCollectionNew);
            Collection<DocHistory> attachedDocHistoryCollectionNew = new ArrayList<DocHistory>();
            for (DocHistory docHistoryCollectionNewDocHistoryToAttach : docHistoryCollectionNew) {
                docHistoryCollectionNewDocHistoryToAttach = em.getReference(docHistoryCollectionNewDocHistoryToAttach.getClass(), docHistoryCollectionNewDocHistoryToAttach.getId());
                attachedDocHistoryCollectionNew.add(docHistoryCollectionNewDocHistoryToAttach);
            }
            docHistoryCollectionNew = attachedDocHistoryCollectionNew;
            doc.setDocHistoryCollection(docHistoryCollectionNew);
            Collection<DocMessage> attachedDocMessageCollectionNew = new ArrayList<DocMessage>();
            for (DocMessage docMessageCollectionNewDocMessageToAttach : docMessageCollectionNew) {
                docMessageCollectionNewDocMessageToAttach = em.getReference(docMessageCollectionNewDocMessageToAttach.getClass(), docMessageCollectionNewDocMessageToAttach.getId());
                attachedDocMessageCollectionNew.add(docMessageCollectionNewDocMessageToAttach);
            }
            docMessageCollectionNew = attachedDocMessageCollectionNew;
            doc.setDocMessageCollection(docMessageCollectionNew);
            doc = em.merge(doc);
            if (creatorSocialProfileFkOld != null && !creatorSocialProfileFkOld.equals(creatorSocialProfileFkNew)) {
                creatorSocialProfileFkOld.getDocCollection().remove(doc);
                creatorSocialProfileFkOld = em.merge(creatorSocialProfileFkOld);
            }
            if (creatorSocialProfileFkNew != null && !creatorSocialProfileFkNew.equals(creatorSocialProfileFkOld)) {
                creatorSocialProfileFkNew.getDocCollection().add(doc);
                creatorSocialProfileFkNew = em.merge(creatorSocialProfileFkNew);
            }
            if (editorSocialProfileFkOld != null && !editorSocialProfileFkOld.equals(editorSocialProfileFkNew)) {
                editorSocialProfileFkOld.getDocCollection().remove(doc);
                editorSocialProfileFkOld = em.merge(editorSocialProfileFkOld);
            }
            if (editorSocialProfileFkNew != null && !editorSocialProfileFkNew.equals(editorSocialProfileFkOld)) {
                editorSocialProfileFkNew.getDocCollection().add(doc);
                editorSocialProfileFkNew = em.merge(editorSocialProfileFkNew);
            }
            for (DocGuest docGuestCollectionNewDocGuest : docGuestCollectionNew) {
                if (!docGuestCollectionOld.contains(docGuestCollectionNewDocGuest)) {
                    Doc oldDocFkOfDocGuestCollectionNewDocGuest = docGuestCollectionNewDocGuest.getDocFk();
                    docGuestCollectionNewDocGuest.setDocFk(doc);
                    docGuestCollectionNewDocGuest = em.merge(docGuestCollectionNewDocGuest);
                    if (oldDocFkOfDocGuestCollectionNewDocGuest != null && !oldDocFkOfDocGuestCollectionNewDocGuest.equals(doc)) {
                        oldDocFkOfDocGuestCollectionNewDocGuest.getDocGuestCollection().remove(docGuestCollectionNewDocGuest);
                        oldDocFkOfDocGuestCollectionNewDocGuest = em.merge(oldDocFkOfDocGuestCollectionNewDocGuest);
                    }
                }
            }
            for (DocHistory docHistoryCollectionNewDocHistory : docHistoryCollectionNew) {
                if (!docHistoryCollectionOld.contains(docHistoryCollectionNewDocHistory)) {
                    Doc oldDocFkOfDocHistoryCollectionNewDocHistory = docHistoryCollectionNewDocHistory.getDocFk();
                    docHistoryCollectionNewDocHistory.setDocFk(doc);
                    docHistoryCollectionNewDocHistory = em.merge(docHistoryCollectionNewDocHistory);
                    if (oldDocFkOfDocHistoryCollectionNewDocHistory != null && !oldDocFkOfDocHistoryCollectionNewDocHistory.equals(doc)) {
                        oldDocFkOfDocHistoryCollectionNewDocHistory.getDocHistoryCollection().remove(docHistoryCollectionNewDocHistory);
                        oldDocFkOfDocHistoryCollectionNewDocHistory = em.merge(oldDocFkOfDocHistoryCollectionNewDocHistory);
                    }
                }
            }
            for (DocMessage docMessageCollectionNewDocMessage : docMessageCollectionNew) {
                if (!docMessageCollectionOld.contains(docMessageCollectionNewDocMessage)) {
                    Doc oldDocFkOfDocMessageCollectionNewDocMessage = docMessageCollectionNewDocMessage.getDocFk();
                    docMessageCollectionNewDocMessage.setDocFk(doc);
                    docMessageCollectionNewDocMessage = em.merge(docMessageCollectionNewDocMessage);
                    if (oldDocFkOfDocMessageCollectionNewDocMessage != null && !oldDocFkOfDocMessageCollectionNewDocMessage.equals(doc)) {
                        oldDocFkOfDocMessageCollectionNewDocMessage.getDocMessageCollection().remove(docMessageCollectionNewDocMessage);
                        oldDocFkOfDocMessageCollectionNewDocMessage = em.merge(oldDocFkOfDocMessageCollectionNewDocMessage);
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
                Integer id = doc.getId();
                if (findDoc(id) == null) {
                    throw new NonexistentEntityException("The doc with id " + id + " no longer exists.");
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
            Doc doc;
            try {
                doc = em.getReference(Doc.class, id);
                doc.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The doc with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DocGuest> docGuestCollectionOrphanCheck = doc.getDocGuestCollection();
            for (DocGuest docGuestCollectionOrphanCheckDocGuest : docGuestCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Doc (" + doc + ") cannot be destroyed since the DocGuest " + docGuestCollectionOrphanCheckDocGuest + " in its docGuestCollection field has a non-nullable docFk field.");
            }
            Collection<DocHistory> docHistoryCollectionOrphanCheck = doc.getDocHistoryCollection();
            for (DocHistory docHistoryCollectionOrphanCheckDocHistory : docHistoryCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Doc (" + doc + ") cannot be destroyed since the DocHistory " + docHistoryCollectionOrphanCheckDocHistory + " in its docHistoryCollection field has a non-nullable docFk field.");
            }
            Collection<DocMessage> docMessageCollectionOrphanCheck = doc.getDocMessageCollection();
            for (DocMessage docMessageCollectionOrphanCheckDocMessage : docMessageCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Doc (" + doc + ") cannot be destroyed since the DocMessage " + docMessageCollectionOrphanCheckDocMessage + " in its docMessageCollection field has a non-nullable docFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            SocialProfile creatorSocialProfileFk = doc.getCreatorSocialProfileFk();
            if (creatorSocialProfileFk != null) {
                creatorSocialProfileFk.getDocCollection().remove(doc);
                creatorSocialProfileFk = em.merge(creatorSocialProfileFk);
            }
            SocialProfile editorSocialProfileFk = doc.getEditorSocialProfileFk();
            if (editorSocialProfileFk != null) {
                editorSocialProfileFk.getDocCollection().remove(doc);
                editorSocialProfileFk = em.merge(editorSocialProfileFk);
            }
            em.remove(doc);
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

    public List<Doc> findDocEntities() {
        return findDocEntities(true, -1, -1);
    }

    public List<Doc> findDocEntities(int maxResults, int firstResult) {
        return findDocEntities(false, maxResults, firstResult);
    }

    private List<Doc> findDocEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Doc.class));
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

    public Doc findDoc(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Doc.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Doc> rt = cq.from(Doc.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Doc> findBySocialProfileId(Integer socialProfileId) {
        EntityManager em = getEntityManager();
        try {
            List<Doc> docList = (List<Doc>) em.createNativeQuery("select distinct d.* from doc d "
                    + "left join doc_guest dg on d.id = dg.doc_fk "
                    + "where d.creator_social_profile_fk = '" + socialProfileId + "' or dg.social_profile_fk = '" + socialProfileId + "'", Doc.class).getResultList();
            if (docList == null) {
                return new ArrayList<>();
            }
            return docList;
        } finally {
            em.close();
        }
    }
}
