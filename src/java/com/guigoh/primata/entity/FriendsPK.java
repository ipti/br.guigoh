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
public class FriendsPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "token_friend_1")
    private String tokenFriend1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "token_friend_2")
    private String tokenFriend2;

    public FriendsPK() {
    }

    public FriendsPK(String tokenFriend1, String tokenFriend2) {
        this.tokenFriend1 = tokenFriend1;
        this.tokenFriend2 = tokenFriend2;
    }

    public String getTokenFriend1() {
        return tokenFriend1;
    }

    public void setTokenFriend1(String tokenFriend1) {
        this.tokenFriend1 = tokenFriend1;
    }

    public String getTokenFriend2() {
        return tokenFriend2;
    }

    public void setTokenFriend2(String tokenFriend2) {
        this.tokenFriend2 = tokenFriend2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tokenFriend1 != null ? tokenFriend1.hashCode() : 0);
        hash += (tokenFriend2 != null ? tokenFriend2.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FriendsPK)) {
            return false;
        }
        FriendsPK other = (FriendsPK) object;
        if ((this.tokenFriend1 == null && other.tokenFriend1 != null) || (this.tokenFriend1 != null && !this.tokenFriend1.equals(other.tokenFriend1))) {
            return false;
        }
        if ((this.tokenFriend2 == null && other.tokenFriend2 != null) || (this.tokenFriend2 != null && !this.tokenFriend2.equals(other.tokenFriend2))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.guigoh.primata.entity.FriendsPK[ tokenFriend1=" + tokenFriend1 + ", tokenFriend2=" + tokenFriend2 + " ]";
    }
    
}
