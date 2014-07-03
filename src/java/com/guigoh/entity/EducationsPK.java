/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author IPTI
 */
@Embeddable
public class EducationsPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "token_id")
    private String tokenId;

    public EducationsPK() {
    }

    public EducationsPK(int id, String tokenId) {
        this.id = id;
        this.tokenId = tokenId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (tokenId != null ? tokenId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EducationsPK)) {
            return false;
        }
        EducationsPK other = (EducationsPK) object;
        if (this.id != other.id) {
            return false;
        }
        if ((this.tokenId == null && other.tokenId != null) || (this.tokenId != null && !this.tokenId.equals(other.tokenId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.guigoh.entity.EducationsPK[ id=" + id + ", tokenId=" + tokenId + " ]";
    }
    
}
