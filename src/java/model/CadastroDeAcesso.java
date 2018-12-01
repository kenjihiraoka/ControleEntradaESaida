/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kenji
 */
@Entity
@Table(name = "cadastro_de_acesso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CadastroDeAcesso.findAll", query = "SELECT c FROM CadastroDeAcesso c")
    , @NamedQuery(name = "CadastroDeAcesso.findById", query = "SELECT c FROM CadastroDeAcesso c WHERE c.id = :id")
    , @NamedQuery(name = "CadastroDeAcesso.findByEntrada", query = "SELECT c FROM CadastroDeAcesso c WHERE c.entrada = :entrada")
    , @NamedQuery(name = "CadastroDeAcesso.findBySaida", query = "SELECT c FROM CadastroDeAcesso c WHERE c.saida = :saida")})
public class CadastroDeAcesso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "entrada")
    @Temporal(TemporalType.DATE)
    private Date entrada;
    @Column(name = "saida")
    @Temporal(TemporalType.DATE)
    private Date saida;
    @JoinColumn(name = "fk_identificacao", referencedColumnName = "id")
    @ManyToOne
    private Identificacao fkIdentificacao;

    public CadastroDeAcesso() {
    }

    public CadastroDeAcesso(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getEntrada() {
        return entrada;
    }

    public void setEntrada(Date entrada) {
        this.entrada = entrada;
    }

    public Date getSaida() {
        return saida;
    }

    public void setSaida(Date saida) {
        this.saida = saida;
    }

    public Identificacao getFkIdentificacao() {
        return fkIdentificacao;
    }

    public void setFkIdentificacao(Identificacao fkIdentificacao) {
        this.fkIdentificacao = fkIdentificacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CadastroDeAcesso)) {
            return false;
        }
        CadastroDeAcesso other = (CadastroDeAcesso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.CadastroDeAcesso[ id=" + id + " ]";
    }
    
}
