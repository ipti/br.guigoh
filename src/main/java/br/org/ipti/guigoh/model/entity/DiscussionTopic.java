/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Joe
 */
@Entity
@Table(name = "discussion_topic")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiscussionTopic.findAll", query = "SELECT d FROM DiscussionTopic d"),
    @NamedQuery(name = "DiscussionTopic.findById", query = "SELECT d FROM DiscussionTopic d WHERE d.id = :id"),
    @NamedQuery(name = "DiscussionTopic.findByData", query = "SELECT d FROM DiscussionTopic d WHERE d.data = :data"),
    @NamedQuery(name = "DiscussionTopic.findByBody", query = "SELECT d FROM DiscussionTopic d WHERE d.body = :body"),
    @NamedQuery(name = "DiscussionTopic.findByStatus", query = "SELECT d FROM DiscussionTopic d WHERE d.status = :status"),
    @NamedQuery(name = "DiscussionTopic.findByTitle", query = "SELECT d FROM DiscussionTopic d WHERE d.title = :title")})
public class DiscussionTopic implements Serializable {
    @Column(name = "views")
    private BigInteger views;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private Character status;
    @JoinColumn(name = "social_profile_id", referencedColumnName = "social_profile_id")
    @ManyToOne(optional = false)
    private SocialProfile socialProfileId;
    @JoinColumn(name = "theme_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Interests themeId;
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
    @Column(name = "body")
    private String body;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "title")
    private String title;
    @JoinTable(name = "topic_tags", joinColumns = {
        @JoinColumn(name = "discussion_topic_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "tags_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Tags> tagsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "discussionTopicId")
    private Collection<DiscussionTopicMsg> discussionTopicMsgCollection;
    
    @OneToMany
    private List<DiscussionTopicFiles> discussionTopicFilesList;

    public DiscussionTopic() {
    }

    public DiscussionTopic(Integer id) {
        this.id = id;
    }

    public DiscussionTopic(Integer id, Date data, String body, char status, String title) {
        this.id = id;
        this.data = data;
        this.body = body;
        this.status = status;
        this.title = title;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlTransient
    public Collection<Tags> getTagsCollection() {
        return tagsCollection;
    }

    public void setTagsCollection(Collection<Tags> tagsCollection) {
        this.tagsCollection = tagsCollection;
    }

    @XmlTransient
    public Collection<DiscussionTopicMsg> getDiscussionTopicMsgCollection() {
        return discussionTopicMsgCollection;
    }

    public void setDiscussionTopicMsgCollection(Collection<DiscussionTopicMsg> discussionTopicMsgCollection) {
        this.discussionTopicMsgCollection = discussionTopicMsgCollection;
    }

    @XmlTransient
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
        if (!(object instanceof DiscussionTopic)) {
            return false;
        }
        DiscussionTopic other = (DiscussionTopic) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.DiscussionTopic[ id=" + id + " ]";
    }

    public SocialProfile getSocialProfileId() {
        return socialProfileId;
    }

    public void setSocialProfileId(SocialProfile socialProfileId) {
        this.socialProfileId = socialProfileId;
    }

    public Interests getThemeId() {
        return themeId;
    }

    public void setThemeId(Interests themeId) {
        this.themeId = themeId;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public BigInteger getViews() {
        return views;
    }

    public void setViews(BigInteger views) {
        this.views = views;
    }
    
}
