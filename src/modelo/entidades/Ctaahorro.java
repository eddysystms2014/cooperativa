/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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
 * @author EddyA
 */
@Entity
@Table(name = "ctaahorro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ctaahorro.findAll", query = "SELECT c FROM Ctaahorro c"),
    @NamedQuery(name = "Ctaahorro.findByCodcta", query = "SELECT c FROM Ctaahorro c WHERE c.codcta = :codcta"),
    @NamedQuery(name = "Ctaahorro.findBySal", query = "SELECT c FROM Ctaahorro c WHERE c.sal = :sal"),
    @NamedQuery(name = "Ctaahorro.findByFechaap", query = "SELECT c FROM Ctaahorro c WHERE c.fechaap = :fechaap")})
public class Ctaahorro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codcta")
    private Integer codcta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "sal")
    private Double sal;
    @Column(name = "fechaap")
    @Temporal(TemporalType.DATE)
    private Date fechaap;
    @ManyToMany(mappedBy = "ctaahorroList")
    private List<Cliente> clienteList;
    @OneToMany(mappedBy = "codcta")
    private List<Transaccion> transaccionList;
    @JoinColumn(name = "codsu", referencedColumnName = "codsu")
    @ManyToOne
    private Sucursal codsu;

    public Ctaahorro() {
    }

    public Ctaahorro(Integer codcta) {
        this.codcta = codcta;
    }

    public Integer getCodcta() {
        return codcta;
    }

    public void setCodcta(Integer codcta) {
        this.codcta = codcta;
    }

    public Double getSal() {
        return sal;
    }

    public void setSal(Double sal) {
        this.sal = sal;
    }

    public Date getFechaap() {
        return fechaap;
    }

    public void setFechaap(Date fechaap) {
        this.fechaap = fechaap;
    }

    @XmlTransient
    public List<Cliente> getClienteList() {
        return clienteList;
    }

    public void setClienteList(List<Cliente> clienteList) {
        this.clienteList = clienteList;
    }

    @XmlTransient
    public List<Transaccion> getTransaccionList() {
        return transaccionList;
    }

    public void setTransaccionList(List<Transaccion> transaccionList) {
        this.transaccionList = transaccionList;
    }

    public Sucursal getCodsu() {
        return codsu;
    }

    public void setCodsu(Sucursal codsu) {
        this.codsu = codsu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codcta != null ? codcta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ctaahorro)) {
            return false;
        }
        Ctaahorro other = (Ctaahorro) object;
        if ((this.codcta == null && other.codcta != null) || (this.codcta != null && !this.codcta.equals(other.codcta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.Ctaahorro[ codcta=" + codcta + " ]";
    }
    
}
