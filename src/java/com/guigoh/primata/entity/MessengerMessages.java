/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * @author IPTI
 */
@Entity
@Table(name = "primata_messenger_messages")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MessengerMessages.findAll", query = "SELECT m FROM MessengerMessages m"),
    @NamedQuery(name = "MessengerMessages.findById", query = "SELECT m FROM MessengerMessages m WHERE m.id = :id"),
    @NamedQuery(name = "MessengerMessages.findBySocialProfileIdSender", query = "SELECT m FROM MessengerMessages m WHERE m.socialProfileIdSender = :socialProfileIdSender"),
    @NamedQuery(name = "MessengerMessages.findBySocialProfileIdReceiver", query = "SELECT m FROM MessengerMessages m WHERE m.socialProfileIdReceiver = :socialProfileIdReceiver"),
    @NamedQuery(name = "MessengerMessages.findByMessageDelivered", query = "SELECT m FROM MessengerMessages m WHERE m.messageDelivered = :messageDelivered"),
    @NamedQuery(name = "MessengerMessages.findByMessage", query = "SELECT m FROM MessengerMessages m WHERE m.message = :message"),
    @NamedQuery(name = "MessengerMessages.findByMessageDate", query = "SELECT m FROM MessengerMessages m WHERE m.messageDate = :messageDate")})
public class MessengerMessages implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "social_profile_id_sender")
    private int socialProfileIdSender;
    @Basic(optional = false)
    @NotNull
    @Column(name = "social_profile_id_receiver")
    private int socialProfileIdReceiver;
    @Basic(optional = false)
    @NotNull
    @Column(name = "message_delivered")
    private char messageDelivered;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "message")
    private String message;
    @Basic(optional = false)
    @NotNull
    @Column(name = "message_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date messageDate;

    public MessengerMessages() {
    }

    public MessengerMessages(Integer id) {
        this.id = id;
    }

    public MessengerMessages(Integer id, int socialProfileIdSender, int socialProfileIdReceiver, char messageDelivered, String message, Date messageDate) {
        this.id = id;
        this.socialProfileIdSender = socialProfileIdSender;
        this.socialProfileIdReceiver = socialProfileIdReceiver;
        this.messageDelivered = messageDelivered;
        this.message = message;
        this.messageDate = messageDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSocialProfileIdSender() {
        return socialProfileIdSender;
    }

    public void setSocialProfileIdSender(int socialProfileIdSender) {
        this.socialProfileIdSender = socialProfileIdSender;
    }

    public int getSocialProfileIdReceiver() {
        return socialProfileIdReceiver;
    }

    public void setSocialProfileIdReceiver(int socialProfileIdReceiver) {
        this.socialProfileIdReceiver = socialProfileIdReceiver;
    }

    public char getMessageDelivered() {
        return messageDelivered;
    }

    public void setMessageDelivered(char messageDelivered) {
        this.messageDelivered = messageDelivered;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
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
        if (!(object instanceof MessengerMessages)) {
            return false;
        }
        MessengerMessages other = (MessengerMessages) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.guigoh.primata.entity.MessengerMessages[ id=" + id + " ]";
    }
    
}
