/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author iptipc008
 */
@Entity
@Table(name = "doc_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocHistory.findAll", query = "SELECT d FROM DocHistory d"),
    @NamedQuery(name = "DocHistory.findById", query = "SELECT d FROM DocHistory d WHERE d.id = :id"),
    @NamedQuery(name = "DocHistory.findByDoc", query = "SELECT d FROM DocHistory d WHERE d.doc = :doc"),
    @NamedQuery(name = "DocHistory.findByDate", query = "SELECT d FROM DocHistory d WHERE d.date = :date")})
public class DocHistory implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
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
    @JoinColumn(name = "doc_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Doc docFk;
    @JoinColumn(name = "editor_social_profile_fk", referencedColumnName = "social_profile_id")
    @ManyToOne(optional = false)
    private SocialProfile editorSocialProfileFk;

    public DocHistory() {
    }

    public DocHistory(Integer id) {
        this.id = id;
    }

    public DocHistory(Integer id, String doc, Date date) {
        this.id = id;
        this.doc = doc;
        this.date = date;
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

    public Doc getDocFk() {
        return docFk;
    }

    public void setDocFk(Doc docFk) {
        this.docFk = docFk;
    }

    public SocialProfile getEditorSocialProfileFk() {
        return editorSocialProfileFk;
    }

    public void setEditorSocialProfileFk(SocialProfile editorSocialProfileFk) {
        this.editorSocialProfileFk = editorSocialProfileFk;
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
        if (!(object instanceof DocHistory)) {
            return false;
        }
        DocHistory other = (DocHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.DocHistory[ id=" + id + " ]";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
}
