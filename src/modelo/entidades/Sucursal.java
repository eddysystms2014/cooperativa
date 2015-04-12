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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "sucursal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sucursal.findAll", query = "SELECT s FROM Sucursal s"),
    @NamedQuery(name = "Sucursal.findByCodsu", query = "SELECT s FROM Sucursal s WHERE s.codsu = :codsu"),
    @NamedQuery(name = "Sucursal.findByNombresu", query = "SELECT s FROM Sucursal s WHERE s.nombresu = :nombresu"),
    @NamedQuery(name = "Sucursal.findByDir", query = "SELECT s FROM Sucursal s WHERE s.dir = :dir"),
    @NamedQuery(name = "Sucursal.findByCiudad", query = "SELECT s FROM Sucursal s WHERE s.ciudad = :ciudad"),
    @NamedQuery(name = "Sucursal.findByGerente", query = "SELECT s FROM Sucursal s WHERE s.gerente = :gerente")})
public class Sucursal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codsu")
    private Integer codsu;
    @Column(name = "nombresu")
    private String nombresu;
    @Column(name = "dir")
    private String dir;
    @Column(name = "ciudad")
    private String ciudad;
    @Column(name = "gerente")
    private String gerente;
    @OneToMany(mappedBy = "codsu")
    private List<Cliente> clienteList;
    @JoinColumn(name = "codcanton", referencedColumnName = "codcanton")
    @ManyToOne
    private Cantones codcanton;
    @OneToMany(mappedBy = "codsu")
    private List<Transaccion> transaccionList;
    @OneToMany(mappedBy = "codsu")
    private List<Ctaahorro> ctaahorroList;
    @OneToMany(mappedBy = "codsu")
    private List<Cajero> cajeroList;

    public Sucursal() {
    }

    public Sucursal(Integer codsu) {
        this.codsu = codsu;
    }

    public Integer getCodsu() {
        return codsu;
    }

    public void setCodsu(Integer codsu) {
        this.codsu = codsu;
    }

    public String getNombresu() {
        return nombresu;
    }

    public void setNombresu(String nombresu) {
        this.nombresu = nombresu;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getGerente() {
        return gerente;
    }

    public void setGerente(String gerente) {
        this.gerente = gerente;
    }

    @XmlTransient
    public List<Cliente> getClienteList() {
        return clienteList;
    }

    public void setClienteList(List<Cliente> clienteList) {
        this.clienteList = clienteList;
    }

    public Cantones getCodcanton() {
        return codcanton;
    }

    public void setCodcanton(Cantones codcanton) {
        this.codcanton = codcanton;
    }

    @XmlTransient
    public List<Transaccion> getTransaccionList() {
        return transaccionList;
    }

    public void setTransaccionList(List<Transaccion> transaccionList) {
        this.transaccionList = transaccionList;
    }

    @XmlTransient
    public List<Ctaahorro> getCtaahorroList() {
        return ctaahorroList;
    }

    public void setCtaahorroList(List<Ctaahorro> ctaahorroList) {
        this.ctaahorroList = ctaahorroList;
    }

    @XmlTransient
    public List<Cajero> getCajeroList() {
        return cajeroList;
    }

    public void setCajeroList(List<Cajero> cajeroList) {
        this.cajeroList = cajeroList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codsu != null ? codsu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sucursal)) {
            return false;
        }
        Sucursal other = (Sucursal) object;
        if ((this.codsu == null && other.codsu != null) || (this.codsu != null && !this.codsu.equals(other.codsu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.Sucursal[ codsu=" + codsu + " ]";
    }
    
}
