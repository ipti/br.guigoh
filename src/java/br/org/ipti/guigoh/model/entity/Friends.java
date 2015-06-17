/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
@Table(name = "friends")
@XmlRootElement
@Cacheable(false)
@NamedQueries({
    @NamedQuery(name = "Friends.findAll", query = "SELECT f FROM Friends f"),
    @NamedQuery(name = "Friends.findByTokenFriend1", query = "SELECT f FROM Friends f WHERE f.friendsPK.tokenFriend1 = :tokenFriend1"),
    @NamedQuery(name = "Friends.findByTokenFriend2", query = "SELECT f FROM Friends f WHERE f.friendsPK.tokenFriend2 = :tokenFriend2"),
    @NamedQuery(name = "Friends.findByStatus", query = "SELECT f FROM Friends f WHERE f.status = :status"),
    @NamedQuery(name = "Friends.findByMessage", query = "SELECT f FROM Friends f WHERE f.message = :message"),
    @NamedQuery(name = "Friends.findByRecommenders", query = "SELECT f FROM Friends f WHERE f.recommenders = :recommenders")})
public class Friends implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FriendsPK friendsPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "status")
    private String status;
    @Size(max = 100)
    @Column(name = "message")
    private String message;
    @Column(name = "recommenders")
    private Integer recommenders;
    @JoinColumn(name = "token_friend_1", referencedColumnName = "token", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users tokenFriend1;
    @JoinColumn(name = "token_recommender", referencedColumnName = "token")
    @ManyToOne
    private Users tokenRecommender;
    @JoinColumn(name = "token_friend_2", referencedColumnName = "token", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users tokenFriend2;

    public Friends() {
    }

    public Friends(FriendsPK friendsPK) {
        this.friendsPK = friendsPK;
    }

    public Friends(FriendsPK friendsPK, String status, int reference) {
        this.friendsPK = friendsPK;
        this.status = status;
    }

    public Friends(String tokenFriend1, String tokenFriend2) {
        this.friendsPK = new FriendsPK(tokenFriend1, tokenFriend2);
    }

    public FriendsPK getFriendsPK() {
        return friendsPK;
    }

    public void setFriendsPK(FriendsPK friendsPK) {
        this.friendsPK = friendsPK;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getRecommenders() {
        return recommenders;
    }

    public void setRecommenders(Integer recommenders) {
        this.recommenders = recommenders;
    }

    public Users getTokenFriend1() {
        return tokenFriend1;
    }

    public void setTokenFriend1(Users tokenFriend1) {
        this.tokenFriend1 = tokenFriend1;
    }

    public Users getTokenRecommender() {
        return tokenRecommender;
    }

    public void setTokenRecommender(Users tokenRecommender) {
        this.tokenRecommender = tokenRecommender;
    }

    public Users getTokenFriend2() {
        return tokenFriend2;
    }

    public void setTokenFriend2(Users tokenFriend2) {
        this.tokenFriend2 = tokenFriend2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (friendsPK != null ? friendsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Friends)) {
            return false;
        }
        Friends other = (Friends) object;
        if ((this.friendsPK == null && other.friendsPK != null) || (this.friendsPK != null && !this.friendsPK.equals(other.friendsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.Friends[ friendsPK=" + friendsPK + " ]";
    }
    
}
