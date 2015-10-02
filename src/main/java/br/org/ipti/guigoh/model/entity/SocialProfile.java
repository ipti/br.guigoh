/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.model.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author IPTI
 */
@Entity
@Table(name = "social_profile")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SocialProfile.findAll", query = "SELECT s FROM SocialProfile s"),
    @NamedQuery(name = "SocialProfile.findByName", query = "SELECT s FROM SocialProfile s WHERE s.name = :name"),
    @NamedQuery(name = "SocialProfile.findByTokenId", query = "SELECT s FROM SocialProfile s WHERE s.tokenId = :tokenId"),
    @NamedQuery(name = "SocialProfile.findByDescription", query = "SELECT s FROM SocialProfile s WHERE s.description = :description"),
    @NamedQuery(name = "SocialProfile.findBySocialProfileId", query = "SELECT s FROM SocialProfile s WHERE s.socialProfileId = :socialProfileId"),
    @NamedQuery(name = "SocialProfile.findByPhoto", query = "SELECT s FROM SocialProfile s WHERE s.photo = :photo"),
    @NamedQuery(name = "SocialProfile.findByZipcode", query = "SELECT s FROM SocialProfile s WHERE s.zipcode = :zipcode"),
    @NamedQuery(name = "SocialProfile.findByAddress", query = "SELECT s FROM SocialProfile s WHERE s.address = :address"),
    @NamedQuery(name = "SocialProfile.findByNeighborhood", query = "SELECT s FROM SocialProfile s WHERE s.neighborhood = :neighborhood"),
    @NamedQuery(name = "SocialProfile.findByNumber", query = "SELECT s FROM SocialProfile s WHERE s.number = :number"),
    @NamedQuery(name = "SocialProfile.findByPhone", query = "SELECT s FROM SocialProfile s WHERE s.phone = :phone")})
@SqlResultSetMapping(  
    name = "SocialProfileAuthorization",
entities = {
    @EntityResult(entityClass = SocialProfile.class),
    @EntityResult(entityClass = UserAuthorization.class)
})
public class SocialProfile implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "socialProfile")
    private Collection<EducationalObjectLike> educationalObjectLikeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "socialProfileFk")
    private Collection<EducationalObjectMessage> educationalObjectMessageCollection;
    @Size(max = 10)
    @Column(name = "birth_date")
    private String birthDate;
    @Size(max = 150)
    @Column(name = "matters_of_interest")
    private String mattersOfInterest;
    @Size(max = 150)
    @Column(name = "musics")
    private String musics;
    @Size(max = 150)
    @Column(name = "books")
    private String books;
    @Size(max = 150)
    @Column(name = "movies")
    private String movies;
    @Size(max = 150)
    @Column(name = "sports")
    private String sports;
    @Size(max = 150)
    @Column(name = "hobbies")
    private String hobbies;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "socialProfileId")
    private Collection<EducationalObject> educationalObjectCollection;
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne
    private Role roleId;
    @Column(name = "gender")
    private Character gender;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "socialProfileId")
    private Collection<DiscussionTopicWarnings> discussionTopicWarningsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "socialProfileId")
    private Collection<DiscussionTopic> discussionTopicCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "socialProfileId")
    private Collection<DiscussionTopicMsg> discussionTopicMsgCollection;

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size( max = 50)
    @Column(name = "name")
    private String name;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size( max = 32)
    @Column(name = "token_id")
    private String tokenId;
    @Size(max = 200)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_profile_id")
    private int socialProfileId;
    @Size(max = 200)
    @Column(name = "photo")
    private String photo;
    @Size(max = 200)
    @Column(name = "cover_photo")
    private String coverPhoto;
    @Size(max = 20)
    @Column(name = "zipcode")
    private String zipcode;
    @Size(max = 100)
    @Column(name = "address")
    private String address;
    @Size(max = 50)
    @Column(name = "neighborhood")
    private String neighborhood;
    @Size(max = 10)
    @Column(name = "number")
    private String number;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Formato de telefone/fax inválido, deve ser xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 15)
    @Column(name = "phone")
    private String phone;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "socialProfile")
    private SocialProfileVisibility socialProfileVisibility;
    @JoinColumn(name = "token_id", referencedColumnName = "token", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Users users;
    @JoinColumn(name = "subnetwork_id", referencedColumnName = "id")
    @ManyToOne
    private Subnetwork subnetworkId;
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    @ManyToOne
    private State stateId;
    @JoinColumn(name = "scholarity_id", referencedColumnName = "id")
    @ManyToOne
    private Scholarity scholarityId;
    @JoinColumn(name = "occupations_id", referencedColumnName = "id")
    @ManyToOne
    private Occupations occupationsId;
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    @ManyToOne
    private Language languageId;
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    @ManyToOne
    private Country countryId;
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    @ManyToOne
    private City cityId;
    @JoinColumn(name = "availability_id", referencedColumnName = "id")
    @ManyToOne
    private Availability availabilityId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "socialProfile")
    private Collection<Experiences> experiencesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "socialProfile")
    private Collection<Educations> educationsCollection;

    public SocialProfile() {
    }

    public SocialProfile(String tokenId) {
        this.tokenId = tokenId;
    }

    public SocialProfile(String tokenId, String name, int socialProfileId) {
        this.tokenId = tokenId;
        this.name = name;
        this.socialProfileId = socialProfileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSocialProfileId() {
        return socialProfileId;
    }

    public void setSocialProfileId(int socialProfileId) {
        this.socialProfileId = socialProfileId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public SocialProfileVisibility getSocialProfileVisibility() {
        return socialProfileVisibility;
    }

    public void setSocialProfileVisibility(SocialProfileVisibility socialProfileVisibility) {
        this.socialProfileVisibility = socialProfileVisibility;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Subnetwork getSubnetworkId() {
        return subnetworkId;
    }

    public void setSubnetworkId(Subnetwork subnetworkId) {
        this.subnetworkId = subnetworkId;
    }

    public State getStateId() {
        return stateId;
    }

    public void setStateId(State stateId) {
        this.stateId = stateId;
    }

    public Scholarity getScholarityId() {
        return scholarityId;
    }

    public void setScholarityId(Scholarity scholarityId) {
        this.scholarityId = scholarityId;
    }

    public Occupations getOccupationsId() {
        return occupationsId;
    }

    public void setOccupationsId(Occupations occupationsId) {
        this.occupationsId = occupationsId;
    }

    public Language getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Language languageId) {
        this.languageId = languageId;
    }

    public Country getCountryId() {
        return countryId;
    }

    public void setCountryId(Country countryId) {
        this.countryId = countryId;
    }

    public City getCityId() {
        return cityId;
    }

    public void setCityId(City cityId) {
        this.cityId = cityId;
    }

    public Availability getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(Availability availabilityId) {
        this.availabilityId = availabilityId;
    }

    @XmlTransient
    public Collection<Experiences> getExperiencesCollection() {
        return experiencesCollection;
    }

    public void setExperiencesCollection(Collection<Experiences> experiencesCollection) {
        this.experiencesCollection = experiencesCollection;
    }

    @XmlTransient
    public Collection<Educations> getEducationsCollection() {
        return educationsCollection;
    }

    public void setEducationsCollection(Collection<Educations> educationsCollection) {
        this.educationsCollection = educationsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tokenId != null ? tokenId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SocialProfile)) {
            return false;
        }
        SocialProfile other = (SocialProfile) object;
        if ((this.tokenId == null && other.tokenId != null) || (this.tokenId != null && !this.tokenId.equals(other.tokenId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.ipti.guigoh.model.entity.SocialProfile[ tokenId=" + tokenId + " ]";
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    @XmlTransient
    public Collection<DiscussionTopicWarnings> getDiscussionTopicWarningsCollection() {
        return discussionTopicWarningsCollection;
    }

    public void setDiscussionTopicWarningsCollection(Collection<DiscussionTopicWarnings> discussionTopicWarningsCollection) {
        this.discussionTopicWarningsCollection = discussionTopicWarningsCollection;
    }

    @XmlTransient
    public Collection<DiscussionTopic> getDiscussionTopicCollection() {
        return discussionTopicCollection;
    }

    public void setDiscussionTopicCollection(Collection<DiscussionTopic> discussionTopicCollection) {
        this.discussionTopicCollection = discussionTopicCollection;
    }

    @XmlTransient
    public Collection<DiscussionTopicMsg> getDiscussionTopicMsgCollection() {
        return discussionTopicMsgCollection;
    }

    public void setDiscussionTopicMsgCollection(Collection<DiscussionTopicMsg> discussionTopicMsgCollection) {
        this.discussionTopicMsgCollection = discussionTopicMsgCollection;
    }

    public Role getRoleId() {
        return roleId;
    }

    public void setRoleId(Role roleId) {
        this.roleId = roleId;
    }

    @XmlTransient
    public Collection<EducationalObject> getEducationalObjectCollection() {
        return educationalObjectCollection;
    }

    public void setEducationalObjectCollection(Collection<EducationalObject> educationalObjectCollection) {
        this.educationalObjectCollection = educationalObjectCollection;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getMattersOfInterest() {
        return mattersOfInterest;
    }

    public void setMattersOfInterest(String mattersOfInterest) {
        this.mattersOfInterest = mattersOfInterest;
    }

    public String getMusics() {
        return musics;
    }

    public void setMusics(String musics) {
        this.musics = musics;
    }

    public String getBooks() {
        return books;
    }

    public void setBooks(String books) {
        this.books = books;
    }

    public String getMovies() {
        return movies;
    }

    public void setMovies(String movies) {
        this.movies = movies;
    }

    public String getSports() {
        return sports;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    @XmlTransient
    public Collection<EducationalObjectMessage> getEducationalObjectMessageCollection() {
        return educationalObjectMessageCollection;
    }

    public void setEducationalObjectMessageCollection(Collection<EducationalObjectMessage> educationalObjectMessageCollection) {
        this.educationalObjectMessageCollection = educationalObjectMessageCollection;
    }

    @XmlTransient
    public Collection<EducationalObjectLike> getEducationalObjectLikeCollection() {
        return educationalObjectLikeCollection;
    }

    public void setEducationalObjectLikeCollection(Collection<EducationalObjectLike> educationalObjectLikeCollection) {
        this.educationalObjectLikeCollection = educationalObjectLikeCollection;
    }
    
}
