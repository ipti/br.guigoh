/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipti.guigoh.model.entity;

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
@Table(name = "educations_name")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EducationsName.findAll", query = "SELECT e FROM EducationsName e"),
    @NamedQuery(name = "EducationsName.findById", query = "SELECT e FROM EducationsName e WHERE e.id = :id"),
    @NamedQuery(name = "EducationsName.findByName", query = "SELECT e FROM EducationsName e WHERE e.name = :name")})
public class EducationsName implements Serializable {
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
    @JoinColumn(name = "scholarity_id", referencedColumnName = "id")
    @ManyToOne
    private Scholarity scholarityId;
    @OneToMany(mappedBy = "nameId")
    private Collection<Educations> educationsCollection;

    public EducationsName() {
    }

    public EducationsName(Integer id) {
        this.id = id;
    }

    public EducationsName(Integer id, String name) {
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

    public Scholarity getScholarityId() {
        return scholarityId;
    }

    public void setScholarityId(Scholarity scholarityId) {
        this.scholarityId = scholarityId;
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
        if (!(object instanceof EducationsName)) {
            return false;
        }
        EducationsName other = (EducationsName) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ipti.guigoh.model.entity.EducationsName[ id=" + id + " ]";
    }
    
}
