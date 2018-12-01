/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.CadastroDeAcesso;
import model.Identificacao;

/**
 *
 * @author kenji
 */
public class CadastroDeAcessoDAO implements Serializable {

    public CadastroDeAcessoDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CadastroDeAcesso cadastroDeAcesso) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Identificacao fkIdentificacao = cadastroDeAcesso.getFkIdentificacao();
            if (fkIdentificacao != null) {
                fkIdentificacao = em.getReference(fkIdentificacao.getClass(), fkIdentificacao.getId());
                cadastroDeAcesso.setFkIdentificacao(fkIdentificacao);
            }
            em.persist(cadastroDeAcesso);
            if (fkIdentificacao != null) {
                fkIdentificacao.getCadastroDeAcessoCollection().add(cadastroDeAcesso);
                fkIdentificacao = em.merge(fkIdentificacao);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CadastroDeAcesso cadastroDeAcesso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CadastroDeAcesso persistentCadastroDeAcesso = em.find(CadastroDeAcesso.class, cadastroDeAcesso.getId());
            Identificacao fkIdentificacaoOld = persistentCadastroDeAcesso.getFkIdentificacao();
            Identificacao fkIdentificacaoNew = cadastroDeAcesso.getFkIdentificacao();
            if (fkIdentificacaoNew != null) {
                fkIdentificacaoNew = em.getReference(fkIdentificacaoNew.getClass(), fkIdentificacaoNew.getId());
                cadastroDeAcesso.setFkIdentificacao(fkIdentificacaoNew);
            }
            cadastroDeAcesso = em.merge(cadastroDeAcesso);
            if (fkIdentificacaoOld != null && !fkIdentificacaoOld.equals(fkIdentificacaoNew)) {
                fkIdentificacaoOld.getCadastroDeAcessoCollection().remove(cadastroDeAcesso);
                fkIdentificacaoOld = em.merge(fkIdentificacaoOld);
            }
            if (fkIdentificacaoNew != null && !fkIdentificacaoNew.equals(fkIdentificacaoOld)) {
                fkIdentificacaoNew.getCadastroDeAcessoCollection().add(cadastroDeAcesso);
                fkIdentificacaoNew = em.merge(fkIdentificacaoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cadastroDeAcesso.getId();
                if (findCadastroDeAcesso(id) == null) {
                    throw new NonexistentEntityException("The cadastroDeAcesso with id " + id + " no longer exists.");
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
            CadastroDeAcesso cadastroDeAcesso;
            try {
                cadastroDeAcesso = em.getReference(CadastroDeAcesso.class, id);
                cadastroDeAcesso.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cadastroDeAcesso with id " + id + " no longer exists.", enfe);
            }
            Identificacao fkIdentificacao = cadastroDeAcesso.getFkIdentificacao();
            if (fkIdentificacao != null) {
                fkIdentificacao.getCadastroDeAcessoCollection().remove(cadastroDeAcesso);
                fkIdentificacao = em.merge(fkIdentificacao);
            }
            em.remove(cadastroDeAcesso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CadastroDeAcesso> findCadastroDeAcessoEntities() {
        return findCadastroDeAcessoEntities(true, -1, -1);
    }

    public List<CadastroDeAcesso> findCadastroDeAcessoEntities(int maxResults, int firstResult) {
        return findCadastroDeAcessoEntities(false, maxResults, firstResult);
    }

    private List<CadastroDeAcesso> findCadastroDeAcessoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CadastroDeAcesso.class));
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

    public CadastroDeAcesso findCadastroDeAcesso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CadastroDeAcesso.class, id);
        } finally {
            em.close();
        }
    }

    public int getCadastroDeAcessoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CadastroDeAcesso> rt = cq.from(CadastroDeAcesso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
