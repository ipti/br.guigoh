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
@Table(name = "user_authorization")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserAuthorization.findAll", query = "SELECT a FROM UserAuthorization a"),
    @NamedQuery(name = "UserAuthorization.findByTokenId", query = "SELECT a FROM UserAuthorization a WHERE a.tokenId = :tokenId"),
    @NamedQuery(name = "UserAuthorization.findByRoles", query = "SELECT a FROM UserAuthorization a WHERE a.roles = :roles"),
    @NamedQuery(name = "UserAuthorization.findByStatus", query = "SELECT a FROM UserAuthorization a WHERE a.status = :status"),
    @NamedQuery(name = "UserAuthorization.findByNetwork", query = "SELECT a FROM UserAuthorization a WHERE a.network = :network")})
public class UserAuthorization implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "token_id")
    private String tokenId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "roles")
    private String roles;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "network")
    private String network;
    @Size(max = 500)
    @Column(name = "inactive_reason")
    private String inactiveReason;
    @JoinColumn(name = "token_id", referencedColumnName = "token", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Users users;

    public UserAuthorization() {
    }

    public UserAuthorization(String tokenId) {
        this.tokenId = tokenId;
    }

    public UserAuthorization(String tokenId, String roles, String status, String network) {
        this.tokenId = tokenId;
        this.roles = roles;
        this.status = status;
        this.network = network;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getInactiveReason() {
        return inactiveReason;
    }

    public void setInactiveReason(String inactiveReason) {
        this.inactiveReason = inactiveReason;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tokenId != null ? tokenId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserAuthorization)) {
            return false;
        }
        UserAuthorization other = (UserAuthorization) object;
        if ((this.tokenId == null && other.tokenId != null) || (this.tokenId != null && !this.tokenId.equals(other.tokenId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.UserAuthorization[ tokenId=" + tokenId + " ]";
    }
    
}
