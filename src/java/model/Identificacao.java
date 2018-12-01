/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kenji
 */
@Entity
@Table(name = "identificacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Identificacao.findAll", query = "SELECT i FROM Identificacao i")
    , @NamedQuery(name = "Identificacao.findById", query = "SELECT i FROM Identificacao i WHERE i.id = :id")
    , @NamedQuery(name = "Identificacao.findByDataValidade", query = "SELECT i FROM Identificacao i WHERE i.dataValidade = :dataValidade")})
public class Identificacao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "data_validade")
    @Temporal(TemporalType.DATE)
    private Date dataValidade;
    @OneToMany(mappedBy = "fkIdentificacao")
    private Collection<CadastroDeAcesso> cadastroDeAcessoCollection;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users fkUsuario;

    public Identificacao() {
    }

    public Identificacao(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    @XmlTransient
    public Collection<CadastroDeAcesso> getCadastroDeAcessoCollection() {
        return cadastroDeAcessoCollection;
    }

    public void setCadastroDeAcessoCollection(Collection<CadastroDeAcesso> cadastroDeAcessoCollection) {
        this.cadastroDeAcessoCollection = cadastroDeAcessoCollection;
    }

    public Users getFkUsuario() {
        return fkUsuario;
    }

    public void setFkUsuario(Users fkUsuario) {
        this.fkUsuario = fkUsuario;
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
        if (!(object instanceof Identificacao)) {
            return false;
        }
        Identificacao other = (Identificacao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Identificacao[ id=" + id + " ]";
    }
    
}
