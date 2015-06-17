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
@Table(name = "occupations")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Occupations.findAll", query = "SELECT o FROM Occupations o"),
    @NamedQuery(name = "Occupations.findById", query = "SELECT o FROM Occupations o WHERE o.id = :id"),
    @NamedQuery(name = "Occupations.findByName", query = "SELECT o FROM Occupations o WHERE o.name = :name")})
public class Occupations implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "occupations_type_id", referencedColumnName = "id")
    @ManyToOne
    private OccupationsType occupationsTypeId;
    @OneToMany(mappedBy = "occupationsId")
    private Collection<SocialProfile> socialProfileCollection;
    @OneToMany(mappedBy = "nameId")
    private Collection<Experiences> experiencesCollection;

    public Occupations() {
    }

    public Occupations(Integer id) {
        this.id = id;
    }

    public Occupations(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OccupationsType getOccupationsTypeId() {
        return occupationsTypeId;
    }

    public void setOccupationsTypeId(OccupationsType occupationsTypeId) {
        this.occupationsTypeId = occupationsTypeId;
    }

    @XmlTransient
    public Collection<SocialProfile> getSocialProfileCollection() {
        return socialProfileCollection;
    }

    public void setSocialProfileCollection(Collection<SocialProfile> socialProfileCollection) {
        this.socialProfileCollection = socialProfileCollection;
    }

    @XmlTransient
    public Collection<Experiences> getExperiencesCollection() {
        return experiencesCollection;
    }

    public void setExperiencesCollection(Collection<Experiences> experiencesCollection) {
        this.experiencesCollection = experiencesCollection;
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
        if (!(object instanceof Occupations)) {
            return false;
        }
        Occupations other = (Occupations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.Occupations[ id=" + id + " ]";
    }
    
}
