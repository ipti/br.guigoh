/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.entity;

import java.io.Serializable;
import java.util.Collection;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author IPTI
 */
@Entity
@Table(name = "scholarity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Scholarity.findAll", query = "SELECT s FROM Scholarity s"),
    @NamedQuery(name = "Scholarity.findById", query = "SELECT s FROM Scholarity s WHERE s.id = :id"),
    @NamedQuery(name = "Scholarity.findByDescription", query = "SELECT s FROM Scholarity s WHERE s.description = :description")})
public class Scholarity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "scholarityId")
    private Collection<EducationsName> educationsNameCollection;
    @OneToMany(mappedBy = "scholarityId")
    private Collection<SocialProfile> socialProfileCollection;

    public Scholarity() {
    }

    public Scholarity(Integer id) {
        this.id = id;
    }

    public Scholarity(Integer id, String description) {
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
    public Collection<EducationsName> getEducationsNameCollection() {
        return educationsNameCollection;
    }

    public void setEducationsNameCollection(Collection<EducationsName> educationsNameCollection) {
        this.educationsNameCollection = educationsNameCollection;
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
        if (!(object instanceof Scholarity)) {
            return false;
        }
        Scholarity other = (Scholarity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.guigoh.entity.Scholarity[ id=" + id + " ]";
    }
    
}
