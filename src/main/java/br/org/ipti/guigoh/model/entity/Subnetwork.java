/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.entity;

import java.io.Serializable;
import java.util.Collection;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author IPTI
 */
@Entity
@Table(name = "subnetwork")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Subnetwork.findAll", query = "SELECT s FROM Subnetwork s"),
    @NamedQuery(name = "Subnetwork.findById", query = "SELECT s FROM Subnetwork s WHERE s.id = :id"),
    @NamedQuery(name = "Subnetwork.findByDescription", query = "SELECT s FROM Subnetwork s WHERE s.description = :description")})
public class Subnetwork implements Serializable {

    @JoinColumn(name = "city_fk", referencedColumnName = "id")
    @ManyToOne
    private City cityFk;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "subnetworkId")
    private Collection<SocialProfile> socialProfileCollection;

    public Subnetwork() {
    }

    public Subnetwork(Integer id) {
        this.id = id;
    }

    public Subnetwork(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<SocialProfile> getSocialProfileCollection() {
        return socialProfileCollection;
    }

    public void setSocialProfileCollection(Collection<SocialProfile> socialProfileCollection) {
        this.socialProfileCollection = socialProfileCollection;
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
        if (!(object instanceof Subnetwork)) {
            return false;
        }
        Subnetwork other = (Subnetwork) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.Subnetwork[ id=" + id + " ]";
    }

    public City getCityFk() {
        return cityFk;
    }

    public void setCityFk(City cityFk) {
        this.cityFk = cityFk;
    }
    
}
