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
@Table(name = "interests_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InterestsType.findAll", query = "SELECT i FROM InterestsType i"),
    @NamedQuery(name = "InterestsType.findById", query = "SELECT i FROM InterestsType i WHERE i.id = :id"),
    @NamedQuery(name = "InterestsType.findByName", query = "SELECT i FROM InterestsType i WHERE i.name = :name"),
    @NamedQuery(name = "InterestsType.findByType", query = "SELECT i FROM InterestsType i WHERE i.type = :type")})
public class InterestsType implements Serializable {
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
    @Column(name = "type")
    private String type;
    @OneToMany(mappedBy = "typeId")
    private Collection<Interests> interestsCollection;

    public InterestsType() {
    }

    public InterestsType(Integer id) {
        this.id = id;
    }

    public InterestsType(Integer id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlTransient
    public Collection<Interests> getInterestsCollection() {
        return interestsCollection;
    }

    public void setInterestsCollection(Collection<Interests> interestsCollection) {
        this.interestsCollection = interestsCollection;
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
        if (!(object instanceof InterestsType)) {
            return false;
        }
        InterestsType other = (InterestsType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.InterestsType[ id=" + id + " ]";
    }
    
}
