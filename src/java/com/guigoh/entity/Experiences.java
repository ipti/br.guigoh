/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.entity;

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
@Table(name = "experiences")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Experiences.findAll", query = "SELECT e FROM Experiences e"),
    @NamedQuery(name = "Experiences.findById", query = "SELECT e FROM Experiences e WHERE e.experiencesPK.id = :id"),
    @NamedQuery(name = "Experiences.findByTokenId", query = "SELECT e FROM Experiences e WHERE e.experiencesPK.tokenId = :tokenId"),
    @NamedQuery(name = "Experiences.findByDataBegin", query = "SELECT e FROM Experiences e WHERE e.dataBegin = :dataBegin"),
    @NamedQuery(name = "Experiences.findByDataEnd", query = "SELECT e FROM Experiences e WHERE e.dataEnd = :dataEnd")})
public class Experiences implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ExperiencesPK experiencesPK;
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
    private Occupations nameId;
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @ManyToOne
    private ExperiencesLocation locationId;
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    @ManyToOne
    private Country countryId;
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    @ManyToOne
    private City cityId;

    public Experiences() {
    }

    public Experiences(ExperiencesPK experiencesPK) {
        this.experiencesPK = experiencesPK;
    }

    public Experiences(int id, String tokenId) {
        this.experiencesPK = new ExperiencesPK(id, tokenId);
    }

    public ExperiencesPK getExperiencesPK() {
        return experiencesPK;
    }

    public void setExperiencesPK(ExperiencesPK experiencesPK) {
        this.experiencesPK = experiencesPK;
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

    public Occupations getNameId() {
        return nameId;
    }

    public void setNameId(Occupations nameId) {
        this.nameId = nameId;
    }

    public ExperiencesLocation getLocationId() {
        return locationId;
    }

    public void setLocationId(ExperiencesLocation locationId) {
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
        hash += (experiencesPK != null ? experiencesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Experiences)) {
            return false;
        }
        Experiences other = (Experiences) object;
        if ((this.experiencesPK == null && other.experiencesPK != null) || (this.experiencesPK != null && !this.experiencesPK.equals(other.experiencesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.guigoh.entity.Experiences[ experiencesPK=" + experiencesPK + " ]";
    }
    
}
