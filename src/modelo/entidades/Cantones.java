/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author EddyA
 */
@Entity
@Table(name = "cantones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cantones.findAll", query = "SELECT c FROM Cantones c"),
    @NamedQuery(name = "Cantones.findByCodcanton", query = "SELECT c FROM Cantones c WHERE c.codcanton = :codcanton"),
    @NamedQuery(name = "Cantones.findByNombrecanton", query = "SELECT c FROM Cantones c WHERE c.nombrecanton = :nombrecanton")})
public class Cantones implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codcanton")
    private Integer codcanton;
    @Column(name = "nombrecanton")
    private String nombrecanton;
    @OneToMany(mappedBy = "codcanton")
    private List<Sucursal> sucursalList;

    public Cantones() {
    }

    public Cantones(Integer codcanton) {
        this.codcanton = codcanton;
    }

    public Integer getCodcanton() {
        return codcanton;
    }

    public void setCodcanton(Integer codcanton) {
        this.codcanton = codcanton;
    }

    public String getNombrecanton() {
        return nombrecanton;
    }

    public void setNombrecanton(String nombrecanton) {
        this.nombrecanton = nombrecanton;
    }

    @XmlTransient
    public List<Sucursal> getSucursalList() {
        return sucursalList;
    }

    public void setSucursalList(List<Sucursal> sucursalList) {
        this.sucursalList = sucursalList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codcanton != null ? codcanton.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cantones)) {
            return false;
        }
        Cantones other = (Cantones) object;
        if ((this.codcanton == null && other.codcanton != null) || (this.codcanton != null && !this.codcanton.equals(other.codcanton))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.Cantones[ codcanton=" + codcanton + " ]";
    }
    
}
