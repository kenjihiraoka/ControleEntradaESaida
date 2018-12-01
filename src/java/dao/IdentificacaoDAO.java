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
import model.Users;
import model.CadastroDeAcesso;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Identificacao;

/**
 *
 * @author kenji
 */
public class IdentificacaoDAO implements Serializable {

    public IdentificacaoDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Identificacao identificacao) {
        if (identificacao.getCadastroDeAcessoCollection() == null) {
            identificacao.setCadastroDeAcessoCollection(new ArrayList<CadastroDeAcesso>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users fkUsuario = identificacao.getFkUsuario();
            if (fkUsuario != null) {
                fkUsuario = em.getReference(fkUsuario.getClass(), fkUsuario.getId());
                identificacao.setFkUsuario(fkUsuario);
            }
            Collection<CadastroDeAcesso> attachedCadastroDeAcessoCollection = new ArrayList<CadastroDeAcesso>();
            for (CadastroDeAcesso cadastroDeAcessoCollectionCadastroDeAcessoToAttach : identificacao.getCadastroDeAcessoCollection()) {
                cadastroDeAcessoCollectionCadastroDeAcessoToAttach = em.getReference(cadastroDeAcessoCollectionCadastroDeAcessoToAttach.getClass(), cadastroDeAcessoCollectionCadastroDeAcessoToAttach.getId());
                attachedCadastroDeAcessoCollection.add(cadastroDeAcessoCollectionCadastroDeAcessoToAttach);
            }
            identificacao.setCadastroDeAcessoCollection(attachedCadastroDeAcessoCollection);
            em.persist(identificacao);
            if (fkUsuario != null) {
                fkUsuario.getIdentificacaoCollection().add(identificacao);
                fkUsuario = em.merge(fkUsuario);
            }
            for (CadastroDeAcesso cadastroDeAcessoCollectionCadastroDeAcesso : identificacao.getCadastroDeAcessoCollection()) {
                Identificacao oldFkIdentificacaoOfCadastroDeAcessoCollectionCadastroDeAcesso = cadastroDeAcessoCollectionCadastroDeAcesso.getFkIdentificacao();
                cadastroDeAcessoCollectionCadastroDeAcesso.setFkIdentificacao(identificacao);
                cadastroDeAcessoCollectionCadastroDeAcesso = em.merge(cadastroDeAcessoCollectionCadastroDeAcesso);
                if (oldFkIdentificacaoOfCadastroDeAcessoCollectionCadastroDeAcesso != null) {
                    oldFkIdentificacaoOfCadastroDeAcessoCollectionCadastroDeAcesso.getCadastroDeAcessoCollection().remove(cadastroDeAcessoCollectionCadastroDeAcesso);
                    oldFkIdentificacaoOfCadastroDeAcessoCollectionCadastroDeAcesso = em.merge(oldFkIdentificacaoOfCadastroDeAcessoCollectionCadastroDeAcesso);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Identificacao identificacao) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Identificacao persistentIdentificacao = em.find(Identificacao.class, identificacao.getId());
            Users fkUsuarioOld = persistentIdentificacao.getFkUsuario();
            Users fkUsuarioNew = identificacao.getFkUsuario();
            Collection<CadastroDeAcesso> cadastroDeAcessoCollectionOld = persistentIdentificacao.getCadastroDeAcessoCollection();
            Collection<CadastroDeAcesso> cadastroDeAcessoCollectionNew = identificacao.getCadastroDeAcessoCollection();
            if (fkUsuarioNew != null) {
                fkUsuarioNew = em.getReference(fkUsuarioNew.getClass(), fkUsuarioNew.getId());
                identificacao.setFkUsuario(fkUsuarioNew);
            }
            Collection<CadastroDeAcesso> attachedCadastroDeAcessoCollectionNew = new ArrayList<CadastroDeAcesso>();
            for (CadastroDeAcesso cadastroDeAcessoCollectionNewCadastroDeAcessoToAttach : cadastroDeAcessoCollectionNew) {
                cadastroDeAcessoCollectionNewCadastroDeAcessoToAttach = em.getReference(cadastroDeAcessoCollectionNewCadastroDeAcessoToAttach.getClass(), cadastroDeAcessoCollectionNewCadastroDeAcessoToAttach.getId());
                attachedCadastroDeAcessoCollectionNew.add(cadastroDeAcessoCollectionNewCadastroDeAcessoToAttach);
            }
            cadastroDeAcessoCollectionNew = attachedCadastroDeAcessoCollectionNew;
            identificacao.setCadastroDeAcessoCollection(cadastroDeAcessoCollectionNew);
            identificacao = em.merge(identificacao);
            if (fkUsuarioOld != null && !fkUsuarioOld.equals(fkUsuarioNew)) {
                fkUsuarioOld.getIdentificacaoCollection().remove(identificacao);
                fkUsuarioOld = em.merge(fkUsuarioOld);
            }
            if (fkUsuarioNew != null && !fkUsuarioNew.equals(fkUsuarioOld)) {
                fkUsuarioNew.getIdentificacaoCollection().add(identificacao);
                fkUsuarioNew = em.merge(fkUsuarioNew);
            }
            for (CadastroDeAcesso cadastroDeAcessoCollectionOldCadastroDeAcesso : cadastroDeAcessoCollectionOld) {
                if (!cadastroDeAcessoCollectionNew.contains(cadastroDeAcessoCollectionOldCadastroDeAcesso)) {
                    cadastroDeAcessoCollectionOldCadastroDeAcesso.setFkIdentificacao(null);
                    cadastroDeAcessoCollectionOldCadastroDeAcesso = em.merge(cadastroDeAcessoCollectionOldCadastroDeAcesso);
                }
            }
            for (CadastroDeAcesso cadastroDeAcessoCollectionNewCadastroDeAcesso : cadastroDeAcessoCollectionNew) {
                if (!cadastroDeAcessoCollectionOld.contains(cadastroDeAcessoCollectionNewCadastroDeAcesso)) {
                    Identificacao oldFkIdentificacaoOfCadastroDeAcessoCollectionNewCadastroDeAcesso = cadastroDeAcessoCollectionNewCadastroDeAcesso.getFkIdentificacao();
                    cadastroDeAcessoCollectionNewCadastroDeAcesso.setFkIdentificacao(identificacao);
                    cadastroDeAcessoCollectionNewCadastroDeAcesso = em.merge(cadastroDeAcessoCollectionNewCadastroDeAcesso);
                    if (oldFkIdentificacaoOfCadastroDeAcessoCollectionNewCadastroDeAcesso != null && !oldFkIdentificacaoOfCadastroDeAcessoCollectionNewCadastroDeAcesso.equals(identificacao)) {
                        oldFkIdentificacaoOfCadastroDeAcessoCollectionNewCadastroDeAcesso.getCadastroDeAcessoCollection().remove(cadastroDeAcessoCollectionNewCadastroDeAcesso);
                        oldFkIdentificacaoOfCadastroDeAcessoCollectionNewCadastroDeAcesso = em.merge(oldFkIdentificacaoOfCadastroDeAcessoCollectionNewCadastroDeAcesso);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = identificacao.getId();
                if (findIdentificacao(id) == null) {
                    throw new NonexistentEntityException("The identificacao with id " + id + " no longer exists.");
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
            Identificacao identificacao;
            try {
                identificacao = em.getReference(Identificacao.class, id);
                identificacao.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The identificacao with id " + id + " no longer exists.", enfe);
            }
            Users fkUsuario = identificacao.getFkUsuario();
            if (fkUsuario != null) {
                fkUsuario.getIdentificacaoCollection().remove(identificacao);
                fkUsuario = em.merge(fkUsuario);
            }
            Collection<CadastroDeAcesso> cadastroDeAcessoCollection = identificacao.getCadastroDeAcessoCollection();
            for (CadastroDeAcesso cadastroDeAcessoCollectionCadastroDeAcesso : cadastroDeAcessoCollection) {
                cadastroDeAcessoCollectionCadastroDeAcesso.setFkIdentificacao(null);
                cadastroDeAcessoCollectionCadastroDeAcesso = em.merge(cadastroDeAcessoCollectionCadastroDeAcesso);
            }
            em.remove(identificacao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Identificacao> findIdentificacaoEntities() {
        return findIdentificacaoEntities(true, -1, -1);
    }

    public List<Identificacao> findIdentificacaoEntities(int maxResults, int firstResult) {
        return findIdentificacaoEntities(false, maxResults, firstResult);
    }

    private List<Identificacao> findIdentificacaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Identificacao.class));
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

    public Identificacao findIdentificacao(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Identificacao.class, id);
        } finally {
            em.close();
        }
    }

    public int getIdentificacaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Identificacao> rt = cq.from(Identificacao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
