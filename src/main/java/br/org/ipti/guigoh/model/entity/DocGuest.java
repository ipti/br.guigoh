/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.entity;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author iptipc008
 */
@Entity
@Table(name = "doc_guest")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocGuest.findAll", query = "SELECT d FROM DocGuest d"),
    @NamedQuery(name = "DocGuest.findById", query = "SELECT d FROM DocGuest d WHERE d.id = :id"),
    @NamedQuery(name = "DocGuest.findByPermission", query = "SELECT d FROM DocGuest d WHERE d.permission = :permission")})
public class DocGuest implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "permission")
    private String permission;
    @JoinColumn(name = "doc_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Doc docFk;
    @JoinColumn(name = "social_profile_fk", referencedColumnName = "social_profile_id")
    @ManyToOne(optional = false)
    private SocialProfile socialProfileFk;

    public DocGuest() {
    }

    public DocGuest(Integer id) {
        this.id = id;
    }

    public DocGuest(Integer id, String permission) {
        this.id = id;
        this.permission = permission;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Doc getDocFk() {
        return docFk;
    }

    public void setDocFk(Doc docFk) {
        this.docFk = docFk;
    }

    public SocialProfile getSocialProfileFk() {
        return socialProfileFk;
    }

    public void setSocialProfileFk(SocialProfile socialProfileFk) {
        this.socialProfileFk = socialProfileFk;
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
        if (!(object instanceof DocGuest)) {
            return false;
        }
        DocGuest other = (DocGuest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.DocGuest[ id=" + id + " ]";
    }
    
}
