/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author iptipc008
 */
@Embeddable
public class EducationalObjectLikePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "educational_object_fk")
    private int educationalObjectFk;
    @Basic(optional = false)
    @NotNull
    @Column(name = "social_profile_fk")
    private int socialProfileFk;

    public EducationalObjectLikePK() {
    }

    public EducationalObjectLikePK(int educationalObjectFk, int socialProfileFk) {
        this.educationalObjectFk = educationalObjectFk;
        this.socialProfileFk = socialProfileFk;
    }

    public int getEducationalObjectFk() {
        return educationalObjectFk;
    }

    public void setEducationalObjectFk(int educationalObjectFk) {
        this.educationalObjectFk = educationalObjectFk;
    }

    public int getSocialProfileFk() {
        return socialProfileFk;
    }

    public void setSocialProfileFk(int socialProfileFk) {
        this.socialProfileFk = socialProfileFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) educationalObjectFk;
        hash += (int) socialProfileFk;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EducationalObjectLikePK)) {
            return false;
        }
        EducationalObjectLikePK other = (EducationalObjectLikePK) object;
        if (this.educationalObjectFk != other.educationalObjectFk) {
            return false;
        }
        if (this.socialProfileFk != other.socialProfileFk) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.EducationalObjectLikePK[ educationalObjectFk=" + educationalObjectFk + ", socialProfileFk=" + socialProfileFk + " ]";
    }
    
}
