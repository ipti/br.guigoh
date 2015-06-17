/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author IPTI
 */
@Entity
@Table(name = "networks")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Networks.findAll", query = "SELECT n FROM Networks n"),
    @NamedQuery(name = "Networks.findByName", query = "SELECT n FROM Networks n WHERE n.name = :name"),
    @NamedQuery(name = "Networks.findByIp", query = "SELECT n FROM Networks n WHERE n.ip = :ip"),
    @NamedQuery(name = "Networks.findByType", query = "SELECT n FROM Networks n WHERE n.type = :type")})
public class Networks implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "ip")
    private String ip;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "type")
    private String type;

    public Networks() {
    }

    public Networks(String name) {
        this.name = name;
    }

    public Networks(String name, String ip, String type) {
        this.name = name;
        this.ip = ip;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Networks)) {
            return false;
        }
        Networks other = (Networks) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.Networks[ name=" + name + " ]";
    }
    
}
