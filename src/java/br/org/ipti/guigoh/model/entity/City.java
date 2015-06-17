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
@Table(name = "city")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "City.findAll", query = "SELECT c FROM City c"),
    @NamedQuery(name = "City.findById", query = "SELECT c FROM City c WHERE c.id = :id"),
    @NamedQuery(name = "City.findByName", query = "SELECT c FROM City c WHERE c.name = :name"),
    @NamedQuery(name = "City.findByAcronyms", query = "SELECT c FROM City c WHERE c.acronyms = :acronyms")})
public class City implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "acronyms")
    private String acronyms;
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private State stateId;
    @OneToMany(mappedBy = "cityId")
    private Collection<SocialProfile> socialProfileCollection;
    @OneToMany(mappedBy = "cityId")
    private Collection<Experiences> experiencesCollection;
    @OneToMany(mappedBy = "cityId")
    private Collection<Educations> educationsCollection;

    public City() {
    }

    public City(Integer id) {
        this.id = id;
    }

    public City(Integer id, String name, String acronyms) {
        this.id = id;
        this.name = name;
        this.acronyms = acronyms;
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

    public String getAcronyms() {
        return acronyms;
    }

    public void setAcronyms(String acronyms) {
        this.acronyms = acronyms;
    }

    public State getStateId() {
        return stateId;
    }

    public void setStateId(State stateId) {
        this.stateId = stateId;
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

    @XmlTransient
    public Collection<Educations> getEducationsCollection() {
        return educationsCollection;
    }

    public void setEducationsCollection(Collection<Educations> educationsCollection) {
        this.educationsCollection = educationsCollection;
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
        if (!(object instanceof City)) {
            return false;
        }
        City other = (City) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.City[ id=" + id + " ]";
    }
    
}
