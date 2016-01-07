/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author iptipc008
 */
@Entity
@Table(name = "doc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Doc.findAll", query = "SELECT d FROM Doc d"),
    @NamedQuery(name = "Doc.findById", query = "SELECT d FROM Doc d WHERE d.id = :id"),
    @NamedQuery(name = "Doc.findByDoc", query = "SELECT d FROM Doc d WHERE d.doc = :doc"),
    @NamedQuery(name = "Doc.findByDate", query = "SELECT d FROM Doc d WHERE d.date = :date"),
    @NamedQuery(name = "Doc.findByStatus", query = "SELECT d FROM Doc d WHERE d.status = :status")})
public class Doc implements Serializable {
    @Size(max = 100)
    @NotNull
    @Column(name = "title")
    private String title;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "doc")
    private String doc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private Character status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "docFk")
    private Collection<DocGuest> docGuestCollection;
    @JoinColumn(name = "creator_social_profile_fk", referencedColumnName = "social_profile_id")
    @ManyToOne(optional = false)
    private SocialProfile creatorSocialProfileFk;
    @JoinColumn(name = "editor_social_profile_fk", referencedColumnName = "social_profile_id")
    @ManyToOne(optional = false)
    private SocialProfile editorSocialProfileFk;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "docFk")
    private Collection<DocHistory> docHistoryCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "docFk")
    private Collection<DocMessage> docMessageCollection;

    public Doc() {
    }

    public Doc(Integer id) {
        this.id = id;
    }

    public Doc(Integer id, String doc, Date date, Character status) {
        this.id = id;
        this.doc = doc;
        this.date = date;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<DocGuest> getDocGuestCollection() {
        return docGuestCollection;
    }

    public void setDocGuestCollection(Collection<DocGuest> docGuestCollection) {
        this.docGuestCollection = docGuestCollection;
    }

    public SocialProfile getCreatorSocialProfileFk() {
        return creatorSocialProfileFk;
    }

    public void setCreatorSocialProfileFk(SocialProfile creatorSocialProfileFk) {
        this.creatorSocialProfileFk = creatorSocialProfileFk;
    }

    public SocialProfile getEditorSocialProfileFk() {
        return editorSocialProfileFk;
    }

    public void setEditorSocialProfileFk(SocialProfile editorSocialProfileFk) {
        this.editorSocialProfileFk = editorSocialProfileFk;
    }

    @XmlTransient
    public Collection<DocHistory> getDocHistoryCollection() {
        return docHistoryCollection;
    }

    public void setDocHistoryCollection(Collection<DocHistory> docHistoryCollection) {
        this.docHistoryCollection = docHistoryCollection;
    }

    @XmlTransient
    public Collection<DocMessage> getDocMessageCollection() {
        return docMessageCollection;
    }

    public void setDocMessageCollection(Collection<DocMessage> docMessageCollection) {
        this.docMessageCollection = docMessageCollection;
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
        if (!(object instanceof Doc)) {
            return false;
        }
        Doc other = (Doc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.Doc[ id=" + id + " ]";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
}
