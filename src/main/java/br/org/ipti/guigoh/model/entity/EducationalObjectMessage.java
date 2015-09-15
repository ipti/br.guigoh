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
@Table(name = "educational_object_message")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EducationalObjectMessage.findAll", query = "SELECT e FROM EducationalObjectMessage e"),
    @NamedQuery(name = "EducationalObjectMessage.findById", query = "SELECT e FROM EducationalObjectMessage e WHERE e.id = :id"),
    @NamedQuery(name = "EducationalObjectMessage.findByMessage", query = "SELECT e FROM EducationalObjectMessage e WHERE e.message = :message"),
    @NamedQuery(name = "EducationalObjectMessage.findByDate", query = "SELECT e FROM EducationalObjectMessage e WHERE e.date = :date")})
public class EducationalObjectMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "message")
    private String message;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "educational_object_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EducationalObject educationalObjectFk;
    @JoinColumn(name = "social_profile_fk", referencedColumnName = "social_profile_id")
    @ManyToOne(optional = false)
    private SocialProfile socialProfileFk;

    public EducationalObjectMessage() {
    }

    public EducationalObjectMessage(Integer id) {
        this.id = id;
    }

    public EducationalObjectMessage(Integer id, String message, Date date) {
        this.id = id;
        this.message = message;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EducationalObject getEducationalObjectFk() {
        return educationalObjectFk;
    }

    public void setEducationalObjectFk(EducationalObject educationalObjectFk) {
        this.educationalObjectFk = educationalObjectFk;
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
        if (!(object instanceof EducationalObjectMessage)) {
            return false;
        }
        EducationalObjectMessage other = (EducationalObjectMessage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.EducationalObjectMessage[ id=" + id + " ]";
    }
    
}
