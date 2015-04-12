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
@Table(name = "cajero")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cajero.findAll", query = "SELECT c FROM Cajero c"),
    @NamedQuery(name = "Cajero.findByCodca", query = "SELECT c FROM Cajero c WHERE c.codca = :codca"),
    @NamedQuery(name = "Cajero.findByCi", query = "SELECT c FROM Cajero c WHERE c.ci = :ci"),
    @NamedQuery(name = "Cajero.findByNombre", query = "SELECT c FROM Cajero c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Cajero.findByFecha", query = "SELECT c FROM Cajero c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "Cajero.findByTelf", query = "SELECT c FROM Cajero c WHERE c.telf = :telf")})
public class Cajero implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codca")
    private Integer codca;
    @Column(name = "ci")
    private String ci;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "telf")
    private String telf;
    @OneToMany(mappedBy = "codca")
    private List<Transaccion> transaccionList;
    @JoinColumn(name = "codsu", referencedColumnName = "codsu")
    @ManyToOne
    private Sucursal codsu;

    public Cajero() {
    }

    public Cajero(Integer codca) {
        this.codca = codca;
    }

    public Integer getCodca() {
        return codca;
    }

    public void setCodca(Integer codca) {
        this.codca = codca;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTelf() {
        return telf;
    }

    public void setTelf(String telf) {
        this.telf = telf;
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
        hash += (codca != null ? codca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cajero)) {
            return false;
        }
        Cajero other = (Cajero) object;
        if ((this.codca == null && other.codca != null) || (this.codca != null && !this.codca.equals(other.codca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.Cajero[ codca=" + codca + " ]";
    }
    
}
