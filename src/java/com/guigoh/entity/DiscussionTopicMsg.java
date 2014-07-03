/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author IPTI
 */
@Entity
@Table(name = "discussion_topic_msg")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiscussionTopicMsg.findAll", query = "SELECT d FROM DiscussionTopicMsg d"),
    @NamedQuery(name = "DiscussionTopicMsg.findById", query = "SELECT d FROM DiscussionTopicMsg d WHERE d.id = :id"),
    @NamedQuery(name = "DiscussionTopicMsg.findByData", query = "SELECT d FROM DiscussionTopicMsg d WHERE d.data = :data"),
    @NamedQuery(name = "DiscussionTopicMsg.findByReply", query = "SELECT d FROM DiscussionTopicMsg d WHERE d.reply = :reply"),
    @NamedQuery(name = "DiscussionTopicMsg.findByStatus", query = "SELECT d FROM DiscussionTopicMsg d WHERE d.status = :status")})
public class DiscussionTopicMsg implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private Character status;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "reply")
    private String reply;
    @JoinColumn(name = "social_profile_id", referencedColumnName = "social_profile_id")
    @ManyToOne(optional = false)
    private SocialProfile socialProfileId;
    @JoinColumn(name = "discussion_topic_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DiscussionTopic discussionTopicId;
    
    @OneToMany
    private List<DiscussionTopicFiles> discussionTopicFilesList;

    public DiscussionTopicMsg() {
    }

    public DiscussionTopicMsg(Integer id) {
        this.id = id;
    }

    public DiscussionTopicMsg(Integer id, Date data, String reply, char status) {
        this.id = id;
        this.data = data;
        this.reply = reply;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }


    public SocialProfile getSocialProfileId() {
        return socialProfileId;
    }

    public void setSocialProfileId(SocialProfile socialProfileId) {
        this.socialProfileId = socialProfileId;
    }

    public DiscussionTopic getDiscussionTopicId() {
        return discussionTopicId;
    }

    public void setDiscussionTopicId(DiscussionTopic discussionTopicId) {
        this.discussionTopicId = discussionTopicId;
    }

    public List<DiscussionTopicFiles> getDiscussionTopicFilesList() {
        return discussionTopicFilesList;
    }

    public void setDiscussionTopicFilesList(List<DiscussionTopicFiles> discussionTopicFilesList) {
        this.discussionTopicFilesList = discussionTopicFilesList;
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
        if (!(object instanceof DiscussionTopicMsg)) {
            return false;
        }
        DiscussionTopicMsg other = (DiscussionTopicMsg) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.guigoh.entity.DiscussionTopicMsg[ id=" + id + " ]";
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }
    
}
