/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.IdentificacaoDAO;
import dao.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.Identificacao;

/**
 *
 * @author kenji
 */
@ManagedBean
@ViewScoped
public class IdentificacaoBean {

    /**
     * Creates a new instance of IdentificacaoBean
     */
    
    private Identificacao identificacao;
    private IdentificacaoDAO dao;
    private List<Identificacao> identificacoes;
    
    public IdentificacaoBean() {
        identificacao = new Identificacao();
        identificacoes = new ArrayList<>();
        dao = new IdentificacaoDAO(javax.persistence.Persistence.createEntityManagerFactory("ControleEntradaSaida2PU"));
    }
    
    public Identificacao getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(Identificacao identificacao) {
        this.identificacao = identificacao;
    }

    public IdentificacaoDAO getDao() {
        return dao;
    }

    public void setDao(IdentificacaoDAO dao) {
        this.dao = dao;
    }

    public List<Identificacao> getIdentificacoes() {
        return identificacoes;
    }

    public void setIdentificacoes(List<Identificacao> identificacoes) {
        this.identificacoes = identificacoes;
    }

    public void salvar() {
        this.dao.create(this.identificacao);
        this.identificacao = new Identificacao();
    }
    
    public List<Identificacao> listar() {
        this.identificacoes = dao.findIdentificacaoEntities();
        return identificacoes;
    }
    
    public void excluir(Identificacao c) {
        try {
            dao.destroy(c.getId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(IdentificacaoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
