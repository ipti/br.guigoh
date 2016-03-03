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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "interests")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Interests.findAll", query = "SELECT i FROM Interests i"),
    @NamedQuery(name = "Interests.findById", query = "SELECT i FROM Interests i WHERE i.id = :id"),
    @NamedQuery(name = "Interests.findByName", query = "SELECT i FROM Interests i WHERE i.name = :name")})
public class Interests implements Serializable {

    @JoinTable(name = "user_reviser", joinColumns = {
        @JoinColumn(name = "interests_id_fk", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "token_id_fk", referencedColumnName = "token")})
    @ManyToMany
    private Collection<Users> usersCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "themeId")
    private Collection<EducationalObject> educationalObjectCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "themeId")
    private Collection<DiscussionTopic> discussionTopicCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "name")
    private String name;
    
    public Interests() {
    }

    public Interests(Integer id) {
        this.id = id;
    }

    public Interests(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Interests)) {
            return false;
        }
        Interests other = (Interests) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.Interests[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<DiscussionTopic> getDiscussionTopicCollection() {
        return discussionTopicCollection;
    }

    public void setDiscussionTopicCollection(Collection<DiscussionTopic> discussionTopicCollection) {
        this.discussionTopicCollection = discussionTopicCollection;
    }

    @XmlTransient
    public Collection<EducationalObject> getEducationalObjectCollection() {
        return educationalObjectCollection;
    }

    public void setEducationalObjectCollection(Collection<EducationalObject> educationalObjectCollection) {
        this.educationalObjectCollection = educationalObjectCollection;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }
    
}
