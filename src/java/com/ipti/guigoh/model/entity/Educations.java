/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipti.guigoh.model.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author IPTI
 */
@Entity
@Table(name = "educations")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Educations.findAll", query = "SELECT e FROM Educations e"),
    @NamedQuery(name = "Educations.findById", query = "SELECT e FROM Educations e WHERE e.educationsPK.id = :id"),
    @NamedQuery(name = "Educations.findByTokenId", query = "SELECT e FROM Educations e WHERE e.educationsPK.tokenId = :tokenId"),
    @NamedQuery(name = "Educations.findByDataBegin", query = "SELECT e FROM Educations e WHERE e.dataBegin = :dataBegin"),
    @NamedQuery(name = "Educations.findByDataEnd", query = "SELECT e FROM Educations e WHERE e.dataEnd = :dataEnd")})
public class Educations implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EducationsPK educationsPK;
    @Column(name = "data_begin")
    @Temporal(TemporalType.DATE)
    private Date dataBegin;
    @Column(name = "data_end")
    @Temporal(TemporalType.DATE)
    private Date dataEnd;
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    @ManyToOne
    private State stateId;
    @JoinColumn(name = "token_id", referencedColumnName = "token_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private SocialProfile socialProfile;
    @JoinColumn(name = "name_id", referencedColumnName = "id")
    @ManyToOne
    private EducationsName nameId;
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @ManyToOne
    private EducationsLocation locationId;
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    @ManyToOne
    private Country countryId;
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    @ManyToOne
    private City cityId;

    public Educations() {
    }

    public Educations(EducationsPK educationsPK) {
        this.educationsPK = educationsPK;
    }

    public Educations(int id, String tokenId) {
        this.educationsPK = new EducationsPK(id, tokenId);
    }

    public EducationsPK getEducationsPK() {
        return educationsPK;
    }

    public void setEducationsPK(EducationsPK educationsPK) {
        this.educationsPK = educationsPK;
    }

    public Date getDataBegin() {
        return dataBegin;
    }

    public void setDataBegin(Date dataBegin) {
        this.dataBegin = dataBegin;
    }

    public Date getDataEnd() {
        return dataEnd;
    }

    public void setDataEnd(Date dataEnd) {
        this.dataEnd = dataEnd;
    }

    public State getStateId() {
        return stateId;
    }

    public void setStateId(State stateId) {
        this.stateId = stateId;
    }

    public SocialProfile getSocialProfile() {
        return socialProfile;
    }

    public void setSocialProfile(SocialProfile socialProfile) {
        this.socialProfile = socialProfile;
    }

    public EducationsName getNameId() {
        return nameId;
    }

    public void setNameId(EducationsName nameId) {
        this.nameId = nameId;
    }

    public EducationsLocation getLocationId() {
        return locationId;
    }

    public void setLocationId(EducationsLocation locationId) {
        this.locationId = locationId;
    }

    public Country getCountryId() {
        return countryId;
    }

    public void setCountryId(Country countryId) {
        this.countryId = countryId;
    }

    public City getCityId() {
        return cityId;
    }

    public void setCityId(City cityId) {
        this.cityId = cityId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (educationsPK != null ? educationsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Educations)) {
            return false;
        }
        Educations other = (Educations) object;
        if ((this.educationsPK == null && other.educationsPK != null) || (this.educationsPK != null && !this.educationsPK.equals(other.educationsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ipti.guigoh.model.entity.Educations[ educationsPK=" + educationsPK + " ]";
    }
    
}
