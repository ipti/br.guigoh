/*
 * To change this template, choose Tools | Templates
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
 * @author Joe
 */
@Entity
@Table(name = "discussion_topic_warnings")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiscussionTopicWarnings.findAll", query = "SELECT d FROM DiscussionTopicWarnings d"),
    @NamedQuery(name = "DiscussionTopicWarnings.findById", query = "SELECT d FROM DiscussionTopicWarnings d WHERE d.id = :id"),
    @NamedQuery(name = "DiscussionTopicWarnings.findByFkId", query = "SELECT d FROM DiscussionTopicWarnings d WHERE d.fkId = :fkId"),
    @NamedQuery(name = "DiscussionTopicWarnings.findByFkType", query = "SELECT d FROM DiscussionTopicWarnings d WHERE d.fkType = :fkType"),
    @NamedQuery(name = "DiscussionTopicWarnings.findByWarnings", query = "SELECT d FROM DiscussionTopicWarnings d WHERE d.warnings = :warnings")})
public class DiscussionTopicWarnings implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "fk_type")
    private Character fkType;
    @JoinColumn(name = "social_profile_id", referencedColumnName = "social_profile_id")
    @ManyToOne(optional = false)
    private SocialProfile socialProfileId;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fk_id")
    private int fkId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "warnings")
    private String warnings;

    public DiscussionTopicWarnings() {
    }

    public DiscussionTopicWarnings(Integer id) {
        this.id = id;
    }

    public DiscussionTopicWarnings(Integer id, int fkId, char fkType, String warnings) {
        this.id = id;
        this.fkId = fkId;
        this.fkType = fkType;
        this.warnings = warnings;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getFkId() {
        return fkId;
    }

    public void setFkId(int fkId) {
        this.fkId = fkId;
    }

    public char getFkType() {
        return fkType;
    }

    public void setFkType(char fkType) {
        this.fkType = fkType;
    }

    public String getWarnings() {
        return warnings;
    }

    public void setWarnings(String warnings) {
        this.warnings = warnings;
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
        if (!(object instanceof DiscussionTopicWarnings)) {
            return false;
        }
        DiscussionTopicWarnings other = (DiscussionTopicWarnings) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.DiscussionTopicWarnings[ id=" + id + " ]";
    }

    public SocialProfile getSocialProfileId() {
        return socialProfileId;
    }

    public void setSocialProfileId(SocialProfile socialProfileId) {
        this.socialProfileId = socialProfileId;
    }
    
}
