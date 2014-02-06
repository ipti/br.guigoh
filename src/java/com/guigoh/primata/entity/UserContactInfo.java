/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
 * @author IPTI
 */
@Entity
@Table(name = "primata_user_contact_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserContactInfo.findAll", query = "SELECT u FROM UserContactInfo u"),
    @NamedQuery(name = "UserContactInfo.findByTokenId", query = "SELECT u FROM UserContactInfo u WHERE u.userContactInfoPK.tokenId = :tokenId"),
    @NamedQuery(name = "UserContactInfo.findByContactTypeId", query = "SELECT u FROM UserContactInfo u WHERE u.userContactInfoPK.contactTypeId = :contactTypeId"),
    @NamedQuery(name = "UserContactInfo.findByValue", query = "SELECT u FROM UserContactInfo u WHERE u.value = :value")})
public class UserContactInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UserContactInfoPK userContactInfoPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "value")
    private String value;
    @JoinColumn(name = "token_id", referencedColumnName = "token", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users users;
    @JoinColumn(name = "contact_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ContactType contactType;

    public UserContactInfo() {
    }

    public UserContactInfo(UserContactInfoPK userContactInfoPK) {
        this.userContactInfoPK = userContactInfoPK;
    }

    public UserContactInfo(UserContactInfoPK userContactInfoPK, String value) {
        this.userContactInfoPK = userContactInfoPK;
        this.value = value;
    }

    public UserContactInfo(String tokenId, int contactTypeId) {
        this.userContactInfoPK = new UserContactInfoPK(tokenId, contactTypeId);
    }

    public UserContactInfoPK getUserContactInfoPK() {
        return userContactInfoPK;
    }

    public void setUserContactInfoPK(UserContactInfoPK userContactInfoPK) {
        this.userContactInfoPK = userContactInfoPK;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userContactInfoPK != null ? userContactInfoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserContactInfo)) {
            return false;
        }
        UserContactInfo other = (UserContactInfo) object;
        if ((this.userContactInfoPK == null && other.userContactInfoPK != null) || (this.userContactInfoPK != null && !this.userContactInfoPK.equals(other.userContactInfoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.guigoh.primata.entity.UserContactInfo[ userContactInfoPK=" + userContactInfoPK + " ]";
    }
    
}
