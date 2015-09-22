/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "discussion_topic_files")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiscussionTopicFiles.findAll", query = "SELECT d FROM DiscussionTopicFiles d"),
    @NamedQuery(name = "DiscussionTopicFiles.findById", query = "SELECT d FROM DiscussionTopicFiles d WHERE d.id = :id"),
    @NamedQuery(name = "DiscussionTopicFiles.findByFkId", query = "SELECT d FROM DiscussionTopicFiles d WHERE d.fkId = :fkId"),
    @NamedQuery(name = "DiscussionTopicFiles.findByFkType", query = "SELECT d FROM DiscussionTopicFiles d WHERE d.fkType = :fkType"),
    @NamedQuery(name = "DiscussionTopicFiles.findByFilepath", query = "SELECT d FROM DiscussionTopicFiles d WHERE d.filepath = :filepath"),
    @NamedQuery(name = "DiscussionTopicFiles.findByFileType", query = "SELECT d FROM DiscussionTopicFiles d WHERE d.fileType = :fileType"),
    @NamedQuery(name = "DiscussionTopicFiles.findByFileName", query = "SELECT d FROM DiscussionTopicFiles d WHERE d.fileName = :fileName")})
public class DiscussionTopicFiles implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "fk_type")
    private Character fkType;
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
    @Size(min = 1, max = 500)
    @Column(name = "filepath")
    private String filepath;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "file_type")
    private String fileType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "file_name")
    private String fileName;
    
    

    public DiscussionTopicFiles() {
    }

    public DiscussionTopicFiles(Integer id) {
        this.id = id;
    }

    public DiscussionTopicFiles(Integer id, int fkId, char fkType, String filepath, String fileType, String fileName) {
        this.id = id;
        this.fkId = fkId;
        this.fkType = fkType;
        this.filepath = filepath;
        this.fileType = fileType;
        this.fileName = fileName;
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

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
        if (!(object instanceof DiscussionTopicFiles)) {
            return false;
        }
        DiscussionTopicFiles other = (DiscussionTopicFiles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.DiscussionTopicFiles[ id=" + id + " ]";
    }
    
}
