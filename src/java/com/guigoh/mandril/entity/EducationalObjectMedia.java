/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.mandril.entity;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ipti008
 */
@Entity
@Table(name = "mandril_educational_object_media")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EducationalObjectMedia.findAll", query = "SELECT e FROM EducationalObjectMedia e"),
    @NamedQuery(name = "EducationalObjectMedia.findById", query = "SELECT e FROM EducationalObjectMedia e WHERE e.id = :id"),
    @NamedQuery(name = "EducationalObjectMedia.findByMedia", query = "SELECT e FROM EducationalObjectMedia e WHERE e.media = :media")})
public class EducationalObjectMedia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "media")
    private String media;
    @JoinColumn(name = "educational_object_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EducationalObject educationalObjectId;

    public EducationalObjectMedia() {
    }

    public EducationalObjectMedia(Integer id) {
        this.id = id;
    }

    public EducationalObjectMedia(Integer id, String media) {
        this.id = id;
        this.media = media;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public EducationalObject getEducationalObjectId() {
        return educationalObjectId;
    }

    public void setEducationalObjectId(EducationalObject educationalObjectId) {
        this.educationalObjectId = educationalObjectId;
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
        if (!(object instanceof EducationalObjectMedia)) {
            return false;
        }
        EducationalObjectMedia other = (EducationalObjectMedia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.guigoh.primata.entity.EducationalObjectMedia[ id=" + id + " ]";
    }
    
}
