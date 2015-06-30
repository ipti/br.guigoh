/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author IPTI
 */
@Entity
@Table(name = "social_profile_visibility")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SocialProfileVisibility.findAll", query = "SELECT s FROM SocialProfileVisibility s"),
    @NamedQuery(name = "SocialProfileVisibility.findBySocialProfileId", query = "SELECT s FROM SocialProfileVisibility s WHERE s.socialProfileId = :socialProfileId"),
    @NamedQuery(name = "SocialProfileVisibility.findByField", query = "SELECT s FROM SocialProfileVisibility s WHERE s.field = :field"),
    @NamedQuery(name = "SocialProfileVisibility.findByVisibility", query = "SELECT s FROM SocialProfileVisibility s WHERE s.visibility = :visibility")})
public class SocialProfileVisibility implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "social_profile_id")
    private Integer socialProfileId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "field")
    private String field;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "visibility")
    private String visibility;
    @JoinColumn(name = "social_profile_id", referencedColumnName = "social_profile_id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private SocialProfile socialProfile;

    public SocialProfileVisibility() {
    }

    public SocialProfileVisibility(Integer socialProfileId) {
        this.socialProfileId = socialProfileId;
    }

    public SocialProfileVisibility(Integer socialProfileId, String field, String visibility) {
        this.socialProfileId = socialProfileId;
        this.field = field;
        this.visibility = visibility;
    }

    public Integer getSocialProfileId() {
        return socialProfileId;
    }

    public void setSocialProfileId(Integer socialProfileId) {
        this.socialProfileId = socialProfileId;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
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
        hash += (socialProfileId != null ? socialProfileId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SocialProfileVisibility)) {
            return false;
        }
        SocialProfileVisibility other = (SocialProfileVisibility) object;
        if ((this.socialProfileId == null && other.socialProfileId != null) || (this.socialProfileId != null && !this.socialProfileId.equals(other.socialProfileId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.SocialProfileVisibility[ socialProfileId=" + socialProfileId + " ]";
    }
    
}
