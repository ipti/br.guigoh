/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author IPTI
 */
@Embeddable
public class UserContactInfoPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "token_id")
    private String tokenId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "contact_type_id")
    private int contactTypeId;

    public UserContactInfoPK() {
    }

    public UserContactInfoPK(String tokenId, int contactTypeId) {
        this.tokenId = tokenId;
        this.contactTypeId = contactTypeId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public int getContactTypeId() {
        return contactTypeId;
    }

    public void setContactTypeId(int contactTypeId) {
        this.contactTypeId = contactTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tokenId != null ? tokenId.hashCode() : 0);
        hash += (int) contactTypeId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserContactInfoPK)) {
            return false;
        }
        UserContactInfoPK other = (UserContactInfoPK) object;
        if ((this.tokenId == null && other.tokenId != null) || (this.tokenId != null && !this.tokenId.equals(other.tokenId))) {
            return false;
        }
        if (this.contactTypeId != other.contactTypeId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.guigoh.primata.entity.UserContactInfoPK[ tokenId=" + tokenId + ", contactTypeId=" + contactTypeId + " ]";
    }
    
}
