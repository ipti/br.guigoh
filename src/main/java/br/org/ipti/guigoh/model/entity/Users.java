/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author IPTI
 */
@Entity
@Table(name = "users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findByUsername", query = "SELECT u FROM Users u WHERE u.username = :username"),
    @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password"),
    @NamedQuery(name = "Users.findByToken", query = "SELECT u FROM Users u WHERE u.token = :token"),
    @NamedQuery(name = "Users.findBySecretAnswer", query = "SELECT u FROM Users u WHERE u.secretAnswer = :secretAnswer"),
    @NamedQuery(name = "Users.findByAlias", query = "SELECT u FROM Users u WHERE u.alias = :alias"),
    @NamedQuery(name = "Users.findByStatus", query = "SELECT u FROM Users u WHERE u.status = :status")})
public class Users implements Serializable {
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "users")
    private UserAuthorization userAuthorization;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "token")
    private String token;
    @Basic(optional = false)
    @NotNull
    @Size(max = 20)
    @Column(name = "secret_answer")
    private String secretAnswer;
    @Size(max = 20)
    @Column(name = "alias")
    private String alias;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "status")
    private String status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tokenFriend1")
    private Collection<Friends> friendsCollection;
    @OneToMany(mappedBy = "tokenRecommender")
    private Collection<Friends> friendsCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tokenFriend2")
    private Collection<Friends> friendsCollection2;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "users")
    private SocialProfile socialProfile;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "users")
    private EmailActivation emailActivation;
    @JoinColumn(name = "secret_question_id", referencedColumnName = "id")
    @ManyToOne
    private SecretQuestion secretQuestionId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "users")
    private Collection<UserContactInfo> userContactInfoCollection;

    public Users() {
    }

    public Users(String username) {
        this.username = username;
    }

    public Users(String username, String password, String token, String secretAnswer, String status) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.secretAnswer = secretAnswer;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<Friends> getFriendsCollection() {
        return friendsCollection;
    }

    public void setFriendsCollection(Collection<Friends> friendsCollection) {
        this.friendsCollection = friendsCollection;
    }

    @XmlTransient
    public Collection<Friends> getFriendsCollection1() {
        return friendsCollection1;
    }

    public void setFriendsCollection1(Collection<Friends> friendsCollection1) {
        this.friendsCollection1 = friendsCollection1;
    }

    @XmlTransient
    public Collection<Friends> getFriendsCollection2() {
        return friendsCollection2;
    }

    public void setFriendsCollection2(Collection<Friends> friendsCollection2) {
        this.friendsCollection2 = friendsCollection2;
    }

    public SocialProfile getSocialProfile() {
        return socialProfile;
    }

    public void setSocialProfile(SocialProfile socialProfile) {
        this.socialProfile = socialProfile;
    }

    public EmailActivation getEmailActivation() {
        return emailActivation;
    }

    public void setEmailActivation(EmailActivation emailActivation) {
        this.emailActivation = emailActivation;
    }

    public SecretQuestion getSecretQuestionId() {
        return secretQuestionId;
    }

    public void setSecretQuestionId(SecretQuestion secretQuestionId) {
        this.secretQuestionId = secretQuestionId;
    }

    @XmlTransient
    public Collection<UserContactInfo> getUserContactInfoCollection() {
        return userContactInfoCollection;
    }

    public void setUserContactInfoCollection(Collection<UserContactInfo> userContactInfoCollection) {
        this.userContactInfoCollection = userContactInfoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.Users[ username=" + username + " ]";
    }

    public UserAuthorization getUserAuthorization() {
        return userAuthorization;
    }

    public void setUserAuthorization(UserAuthorization userAuthorization) {
        this.userAuthorization = userAuthorization;
    }
    
}
