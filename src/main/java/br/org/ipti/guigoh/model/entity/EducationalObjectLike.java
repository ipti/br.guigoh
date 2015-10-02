/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author iptipc008
 */
@Entity
@Table(name = "educational_object_like")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EducationalObjectLike.findAll", query = "SELECT e FROM EducationalObjectLike e"),
    @NamedQuery(name = "EducationalObjectLike.findByEducationalObjectFk", query = "SELECT e FROM EducationalObjectLike e WHERE e.educationalObjectLikePK.educationalObjectFk = :educationalObjectFk"),
    @NamedQuery(name = "EducationalObjectLike.findBySocialProfileFk", query = "SELECT e FROM EducationalObjectLike e WHERE e.educationalObjectLikePK.socialProfileFk = :socialProfileFk"),
    @NamedQuery(name = "EducationalObjectLike.findByDate", query = "SELECT e FROM EducationalObjectLike e WHERE e.date = :date")})
public class EducationalObjectLike implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EducationalObjectLikePK educationalObjectLikePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "educational_object_fk", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EducationalObject educationalObject;
    @JoinColumn(name = "social_profile_fk", referencedColumnName = "social_profile_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private SocialProfile socialProfile;

    public EducationalObjectLike() {
    }

    public EducationalObjectLike(EducationalObjectLikePK educationalObjectLikePK) {
        this.educationalObjectLikePK = educationalObjectLikePK;
    }

    public EducationalObjectLike(EducationalObjectLikePK educationalObjectLikePK, Date date, EducationalObject educationalObject, SocialProfile socialProfile) {
        this.educationalObjectLikePK = educationalObjectLikePK;
        this.date = date;
        this.educationalObject = educationalObject;
        this.socialProfile = socialProfile;
    }

    public EducationalObjectLike(int educationalObjectFk, int socialProfileFk) {
        this.educationalObjectLikePK = new EducationalObjectLikePK(educationalObjectFk, socialProfileFk);
    }

    public EducationalObjectLikePK getEducationalObjectLikePK() {
        return educationalObjectLikePK;
    }

    public void setEducationalObjectLikePK(EducationalObjectLikePK educationalObjectLikePK) {
        this.educationalObjectLikePK = educationalObjectLikePK;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EducationalObject getEducationalObject() {
        return educationalObject;
    }

    public void setEducationalObject(EducationalObject educationalObject) {
        this.educationalObject = educationalObject;
    }

    public SocialProfile getSocialProfile() {
        return socialProfile;
    }

    public void setSocialProfile(SocialProfile socialProfile) {
        this.socialProfile = socialProfile;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (educationalObjectLikePK != null ? educationalObjectLikePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EducationalObjectLike)) {
            return false;
        }
        EducationalObjectLike other = (EducationalObjectLike) object;
        if ((this.educationalObjectLikePK == null && other.educationalObjectLikePK != null) || (this.educationalObjectLikePK != null && !this.educationalObjectLikePK.equals(other.educationalObjectLikePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.EducationalObjectLike[ educationalObjectLikePK=" + educationalObjectLikePK + " ]";
    }
    
}
