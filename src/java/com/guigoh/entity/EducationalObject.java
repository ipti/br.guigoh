/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.entity;

import com.guigoh.entity.Interests;
import com.guigoh.entity.SocialProfile;
import com.guigoh.entity.Tags;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ipti008
 */
@Entity
@Table(name = "educational_object")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EducationalObject.findAll", query = "SELECT e FROM EducationalObject e"),
    @NamedQuery(name = "EducationalObject.findById", query = "SELECT e FROM EducationalObject e WHERE e.id = :id"),
    @NamedQuery(name = "EducationalObject.findByName", query = "SELECT e FROM EducationalObject e WHERE e.name = :name")})
public class EducationalObject implements Serializable {
    @Size(max = 150)
    @Column(name = "image")
    private String image;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "status")
    private String status;
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
    @JoinTable(name = "educational_object_tag", joinColumns = {
        @JoinColumn(name = "educational_object_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "tag_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Tags> tagsCollection;
    @ManyToMany(mappedBy = "educationalObjectCollection")
    private Collection<Author> authorCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "educationalObjectId")
    private Collection<EducationalObjectMedia> educationalObjectMediaCollection;
    @JoinColumn(name = "social_profile_id", referencedColumnName = "social_profile_id")
    @ManyToOne(optional = false)
    private SocialProfile socialProfileId;
    @JoinColumn(name = "theme_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Interests themeId;

    public EducationalObject() {
    }

    public EducationalObject(Integer id) {
        this.id = id;
    }

    public EducationalObject(Integer id, String name) {
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
    public Collection<Tags> getTagsCollection() {
        return tagsCollection;
    }

    public void setTagsCollection(Collection<Tags> tagsCollection) {
        this.tagsCollection = tagsCollection;
    }

    @XmlTransient
    public Collection<Author> getAuthorCollection() {
        return authorCollection;
    }

    public void setAuthorCollection(Collection<Author> authorCollection) {
        this.authorCollection = authorCollection;
    }

    @XmlTransient
    public Collection<EducationalObjectMedia> getEducationalObjectMediaCollection() {
        return educationalObjectMediaCollection;
    }

    public void setEducationalObjectMediaCollection(Collection<EducationalObjectMedia> educationalObjectMediaCollection) {
        this.educationalObjectMediaCollection = educationalObjectMediaCollection;
    }

    public SocialProfile getSocialProfileId() {
        return socialProfileId;
    }

    public void setSocialProfileId(SocialProfile socialProfileId) {
        this.socialProfileId = socialProfileId;
    }

    public Interests getThemeId() {
        return themeId;
    }

    public void setThemeId(Interests themeId) {
        this.themeId = themeId;
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
        if (!(object instanceof EducationalObject)) {
            return false;
        }
        EducationalObject other = (EducationalObject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.guigoh.entity.EducationalObject[ id=" + id + " ]";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
}
