/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author IPTI
 */
@Entity
@Table(name = "primata_messenger_status")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MessengerStatus.findAll", query = "SELECT m FROM MessengerStatus m"),
    @NamedQuery(name = "MessengerStatus.findBySocialProfileId", query = "SELECT m FROM MessengerStatus m WHERE m.socialProfileId = :socialProfileId"),
    @NamedQuery(name = "MessengerStatus.findByLastPing", query = "SELECT m FROM MessengerStatus m WHERE m.lastPing = :lastPing")})
public class MessengerStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "social_profile_id")
    private Integer socialProfileId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "last_ping")
    private double lastPing;

    public MessengerStatus() {
    }

    public MessengerStatus(Integer socialProfileId) {
        this.socialProfileId = socialProfileId;
    }

    public MessengerStatus(Integer socialProfileId, double lastPing) {
        this.socialProfileId = socialProfileId;
        this.lastPing = lastPing;
    }

    public Integer getSocialProfileId() {
        return socialProfileId;
    }

    public void setSocialProfileId(Integer socialProfileId) {
        this.socialProfileId = socialProfileId;
    }

    public double getLastPing() {
        return lastPing;
    }

    public void setLastPing(double lastPing) {
        this.lastPing = lastPing;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (socialProfileId != null ? socialProfileId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MessengerStatus)) {
            return false;
        }
        MessengerStatus other = (MessengerStatus) object;
        if ((this.socialProfileId == null && other.socialProfileId != null) || (this.socialProfileId != null && !this.socialProfileId.equals(other.socialProfileId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.guigoh.primata.entity.MessengerStatus[ socialProfileId=" + socialProfileId + " ]";
    }
    
}
