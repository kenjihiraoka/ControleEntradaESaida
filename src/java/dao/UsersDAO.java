/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Identificacao;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Users;

/**
 *
 * @author kenji
 */
public class UsersDAO implements Serializable {

    public UsersDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) {
        if (users.getIdentificacaoCollection() == null) {
            users.setIdentificacaoCollection(new ArrayList<Identificacao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Identificacao> attachedIdentificacaoCollection = new ArrayList<Identificacao>();
            for (Identificacao identificacaoCollectionIdentificacaoToAttach : users.getIdentificacaoCollection()) {
                identificacaoCollectionIdentificacaoToAttach = em.getReference(identificacaoCollectionIdentificacaoToAttach.getClass(), identificacaoCollectionIdentificacaoToAttach.getId());
                attachedIdentificacaoCollection.add(identificacaoCollectionIdentificacaoToAttach);
            }
            users.setIdentificacaoCollection(attachedIdentificacaoCollection);
            em.persist(users);
            for (Identificacao identificacaoCollectionIdentificacao : users.getIdentificacaoCollection()) {
                Users oldFkUsuarioOfIdentificacaoCollectionIdentificacao = identificacaoCollectionIdentificacao.getFkUsuario();
                identificacaoCollectionIdentificacao.setFkUsuario(users);
                identificacaoCollectionIdentificacao = em.merge(identificacaoCollectionIdentificacao);
                if (oldFkUsuarioOfIdentificacaoCollectionIdentificacao != null) {
                    oldFkUsuarioOfIdentificacaoCollectionIdentificacao.getIdentificacaoCollection().remove(identificacaoCollectionIdentificacao);
                    oldFkUsuarioOfIdentificacaoCollectionIdentificacao = em.merge(oldFkUsuarioOfIdentificacaoCollectionIdentificacao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getId());
            Collection<Identificacao> identificacaoCollectionOld = persistentUsers.getIdentificacaoCollection();
            Collection<Identificacao> identificacaoCollectionNew = users.getIdentificacaoCollection();
            Collection<Identificacao> attachedIdentificacaoCollectionNew = new ArrayList<Identificacao>();
            for (Identificacao identificacaoCollectionNewIdentificacaoToAttach : identificacaoCollectionNew) {
                identificacaoCollectionNewIdentificacaoToAttach = em.getReference(identificacaoCollectionNewIdentificacaoToAttach.getClass(), identificacaoCollectionNewIdentificacaoToAttach.getId());
                attachedIdentificacaoCollectionNew.add(identificacaoCollectionNewIdentificacaoToAttach);
            }
            identificacaoCollectionNew = attachedIdentificacaoCollectionNew;
            users.setIdentificacaoCollection(identificacaoCollectionNew);
            users = em.merge(users);
            for (Identificacao identificacaoCollectionOldIdentificacao : identificacaoCollectionOld) {
                if (!identificacaoCollectionNew.contains(identificacaoCollectionOldIdentificacao)) {
                    identificacaoCollectionOldIdentificacao.setFkUsuario(null);
                    identificacaoCollectionOldIdentificacao = em.merge(identificacaoCollectionOldIdentificacao);
                }
            }
            for (Identificacao identificacaoCollectionNewIdentificacao : identificacaoCollectionNew) {
                if (!identificacaoCollectionOld.contains(identificacaoCollectionNewIdentificacao)) {
                    Users oldFkUsuarioOfIdentificacaoCollectionNewIdentificacao = identificacaoCollectionNewIdentificacao.getFkUsuario();
                    identificacaoCollectionNewIdentificacao.setFkUsuario(users);
                    identificacaoCollectionNewIdentificacao = em.merge(identificacaoCollectionNewIdentificacao);
                    if (oldFkUsuarioOfIdentificacaoCollectionNewIdentificacao != null && !oldFkUsuarioOfIdentificacaoCollectionNewIdentificacao.equals(users)) {
                        oldFkUsuarioOfIdentificacaoCollectionNewIdentificacao.getIdentificacaoCollection().remove(identificacaoCollectionNewIdentificacao);
                        oldFkUsuarioOfIdentificacaoCollectionNewIdentificacao = em.merge(oldFkUsuarioOfIdentificacaoCollectionNewIdentificacao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = users.getId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            Collection<Identificacao> identificacaoCollection = users.getIdentificacaoCollection();
            for (Identificacao identificacaoCollectionIdentificacao : identificacaoCollection) {
                identificacaoCollectionIdentificacao.setFkUsuario(null);
                identificacaoCollectionIdentificacao = em.merge(identificacaoCollectionIdentificacao);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
