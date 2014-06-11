/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.entity;

import com.guigoh.mandril.entity.EducationalObject;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "primata_interests")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Interests.findAll", query = "SELECT i FROM Interests i"),
    @NamedQuery(name = "Interests.findById", query = "SELECT i FROM Interests i WHERE i.id = :id"),
    @NamedQuery(name = "Interests.findByName", query = "SELECT i FROM Interests i WHERE i.name = :name")})
public class Interests implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "themeId")
    private Collection<EducationalObject> educationalObjectCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "themeId")
    private Collection<DiscussionTopic> discussionTopicCollection;
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
    @JoinTable(name = "primata_social_profile_interests", joinColumns = {
        @JoinColumn(name = "interests_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "social_profile_id", referencedColumnName = "social_profile_id")})
    @ManyToMany
    private Collection<SocialProfile> socialProfileCollection;
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    @ManyToOne
    private InterestsType typeId;

    public Interests() {
    }

    public Interests(Integer id) {
        this.id = id;
    }

    public Interests(Integer id, String name) {
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

    @XmlTransient
    public Collection<SocialProfile> getSocialProfileCollection() {
        return socialProfileCollection;
    }

    public void setSocialProfileCollection(Collection<SocialProfile> socialProfileCollection) {
        this.socialProfileCollection = socialProfileCollection;
    }

    public InterestsType getTypeId() {
        return typeId;
    }

    public void setTypeId(InterestsType typeId) {
        this.typeId = typeId;
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
        if (!(object instanceof Interests)) {
            return false;
        }
        Interests other = (Interests) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.guigoh.primata.entity.Interests[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<DiscussionTopic> getDiscussionTopicCollection() {
        return discussionTopicCollection;
    }

    public void setDiscussionTopicCollection(Collection<DiscussionTopic> discussionTopicCollection) {
        this.discussionTopicCollection = discussionTopicCollection;
    }

    @XmlTransient
    public Collection<EducationalObject> getEducationalObjectCollection() {
        return educationalObjectCollection;
    }

    public void setEducationalObjectCollection(Collection<EducationalObject> educationalObjectCollection) {
        this.educationalObjectCollection = educationalObjectCollection;
    }
    
}
