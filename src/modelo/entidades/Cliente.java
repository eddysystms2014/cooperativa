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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author EddyA
 */
@Entity
@Table(name = "cliente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c"),
    @NamedQuery(name = "Cliente.findByCodcl", query = "SELECT c FROM Cliente c WHERE c.codcl = :codcl"),
    @NamedQuery(name = "Cliente.findByCi", query = "SELECT c FROM Cliente c WHERE c.ci = :ci"),
    @NamedQuery(name = "Cliente.findByNombre", query = "SELECT c FROM Cliente c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Cliente.findByDir", query = "SELECT c FROM Cliente c WHERE c.dir = :dir"),
    @NamedQuery(name = "Cliente.findByTel", query = "SELECT c FROM Cliente c WHERE c.tel = :tel")})
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codcl")
    private Integer codcl;
    @Column(name = "ci")
    private String ci;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "dir")
    private String dir;
    @Column(name = "tel")
    private String tel;
    @JoinTable(name = "ctacliente", joinColumns = {
        @JoinColumn(name = "codcl", referencedColumnName = "codcl")}, inverseJoinColumns = {
        @JoinColumn(name = "codcta", referencedColumnName = "codcta")})
    @ManyToMany
    private List<Ctaahorro> ctaahorroList;
    @JoinColumn(name = "codsu", referencedColumnName = "codsu")
    @ManyToOne
    private Sucursal codsu;

    public Cliente() {
    }

    public Cliente(Integer codcl) {
        this.codcl = codcl;
    }

    public Integer getCodcl() {
        return codcl;
    }

    public void setCodcl(Integer codcl) {
        this.codcl = codcl;
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

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @XmlTransient
    public List<Ctaahorro> getCtaahorroList() {
        return ctaahorroList;
    }

    public void setCtaahorroList(List<Ctaahorro> ctaahorroList) {
        this.ctaahorroList = ctaahorroList;
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
        hash += (codcl != null ? codcl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.codcl == null && other.codcl != null) || (this.codcl != null && !this.codcl.equals(other.codcl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.Cliente[ codcl=" + codcl + " ]";
    }
    
}
