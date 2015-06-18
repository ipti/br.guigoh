/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.City;
import br.org.ipti.guigoh.model.entity.Country;
import br.org.ipti.guigoh.model.entity.Educations;
import br.org.ipti.guigoh.model.entity.EducationsLocation;
import br.org.ipti.guigoh.model.entity.EducationsName;
import br.org.ipti.guigoh.model.entity.Experiences;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.entity.InterestsType;
import br.org.ipti.guigoh.model.entity.Occupations;
import br.org.ipti.guigoh.model.entity.OccupationsType;
import br.org.ipti.guigoh.model.entity.Scholarity;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.State;
import br.org.ipti.guigoh.model.jpa.controller.CityJpaController;
import br.org.ipti.guigoh.model.jpa.controller.CountryJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationsLocationJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationsNameJpaController;
import br.org.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.InterestsTypeJpaController;
import br.org.ipti.guigoh.model.jpa.controller.OccupationsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.OccupationsTypeJpaController;
import br.org.ipti.guigoh.model.jpa.controller.ScholarityJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.StateJpaController;
import java.io.Serializable;
import java.util.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author IPTI
 */
@ViewScoped
@ManagedBean(name = "curriculumFinderBean")
public class CurriculumFinderBean implements Serializable{

    private SocialProfile socialProfile;
    private Educations education;
    private Experiences experience;
    private Interests interest;
    private List<SocialProfile> socialProfileList;
    private List<State> stateList;
    private List<Country> countryList;
    private List<City> cityList;
    private List<OccupationsType> occupationTypeList;
    private List<Occupations> occupationList;
    private List<InterestsType> interestTypeList;
    private List<Interests> interestList;
    private List<Scholarity> scholarityList;
    private List<EducationsName> educationNameList;
    private List<EducationsLocation> educationLocationList;
    private Integer countryId;
    private Integer stateId;
    private Integer cityId;
    private Integer occupationId;
    private Integer occupationTypeId;
    private Integer interestId;
    private Integer interestTypeId;
    private Integer scholarityId;
    private Integer educationLocationId;
    private Integer educationNameId;
    private Integer experienceTime;

    public void init() {
        if(!FacesContext.getCurrentInstance().isPostback()){
            socialProfile = new SocialProfile();
            education = new Educations();
            experience = new Experiences();
            interest = new Interests();
            socialProfileList = new ArrayList<>();
            stateList = new ArrayList<>();
            countryList = getCountries();
            occupationTypeList = getOccupationTypes();
            occupationList = new ArrayList<>();
            interestTypeList = getInterestTypes();
            interestList = new ArrayList<>();
            scholarityList = getScholarities();
            cityList = new ArrayList<>();
            educationNameList = getEducationNames();
            educationLocationList = getEducationLocations();
            countryId = 0;
            stateId = 0;
            cityId = 0;
            occupationTypeId = 0;
            occupationId = 0;
            scholarityId = 0;
            interestId = 0;
            interestTypeId = 0;
            educationNameId = 0;
            educationLocationId = 0;
            experienceTime = 0;
        }
    }

    public void searchUsersEvent() {
        Country country = new Country();
        country.setId(countryId);
        socialProfile.setCountryId(country);
        State state = new State();
        state.setId(stateId);
        socialProfile.setStateId(state);
        City city = new City();
        city.setId(cityId);
        socialProfile.setCityId(city);
        Occupations occupation = new Occupations();
        occupation.setId(occupationId);
        OccupationsType occupationsType = new OccupationsType();
        occupationsType.setId(occupationTypeId);
        occupation.setOccupationsTypeId(occupationsType);
        socialProfile.setOccupationsId(occupation);
        Scholarity scholarity = new Scholarity();
        scholarity.setId(scholarityId);
        socialProfile.setScholarityId(scholarity);
        interest.setId(interestId);
        InterestsType interestsType = new InterestsType();
        interestsType.setId(interestTypeId);
        interest.setTypeId(interestsType);
        EducationsName educationsName = new EducationsName();
        educationsName.setId(educationNameId);
        EducationsLocation educationsLocation = new EducationsLocation();
        educationsLocation.setId(educationLocationId);
        education.setNameId(educationsName);
        education.setLocationId(educationsLocation);
        socialProfileList = new ArrayList<>();
        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
        socialProfileList = socialProfileJpaController.loadUserSearchList(socialProfile, education, experienceTime, interest);
        socialProfileList = removeDuplicates(socialProfileList);
    }
    
    private List<SocialProfile> removeDuplicates(List<SocialProfile> socialProfileList){
        List<SocialProfile> socialProfileListAux = new ArrayList<>();
        for (SocialProfile sp : socialProfileList){
            if(!socialProfileListAux.contains(sp)){
                socialProfileListAux.add(sp);
            }
        }
        socialProfileList = socialProfileListAux;
        return socialProfileList;
    }
    
    public String goToProfile(Integer id) {
        return "/profile/viewProfile.xhtml?id=" + id;
    }

    private List<Country> getCountries() {
        CountryJpaController countryJpaController = new CountryJpaController();
        return countryJpaController.findCountryEntities();
    }
    
    private List<OccupationsType> getOccupationTypes(){
        OccupationsTypeJpaController occupationsTypeJpaController = new OccupationsTypeJpaController();
        return occupationsTypeJpaController.findOccupationsTypeEntities();
    }
    
    private List<InterestsType> getInterestTypes(){
        InterestsTypeJpaController interestsTypeJpaController = new InterestsTypeJpaController();
        return interestsTypeJpaController.findInterestsTypeEntities();
    }
    
    private List<Scholarity> getScholarities(){
        ScholarityJpaController scholarityJpaController = new ScholarityJpaController();
        return scholarityJpaController.findScholarityEntities();
    }
    
    private List<EducationsName> getEducationNames(){
        EducationsNameJpaController educationsNameJpaController = new EducationsNameJpaController();
        return educationsNameJpaController.findEducationsNameEntities();
    }
    
    private List<EducationsLocation> getEducationLocations(){
        EducationsLocationJpaController educationsLocationJpaController = new EducationsLocationJpaController();
        return educationsLocationJpaController.findEducationsLocationEntities();
    }
    
    public void loadInterests(){
        InterestsJpaController interestsJpaController = new InterestsJpaController();
        interestList = interestsJpaController.findInterestsByInterestsTypeId(interestTypeId);
    }
    
    public void loadOccupations(){
        OccupationsJpaController occupationsJpaController = new OccupationsJpaController();
        occupationList = occupationsJpaController.findOccupationsByType(occupationTypeId);
    }

    public void loadStates() {
        StateJpaController stateJpaController = new StateJpaController();
        stateList = stateJpaController.findStatesByCountryId(countryId);
    }

    public void loadCities() {
        CityJpaController cityJpaController = new CityJpaController();
        cityList = cityJpaController.findCitiesByStateId(stateId);
    }

    public SocialProfile getSocialProfile() {
        return socialProfile;
    }

    public void setSocialProfile(SocialProfile socialProfile) {
        this.socialProfile = socialProfile;
    }

    public Educations getEducation() {
        return education;
    }

    public void setEducation(Educations education) {
        this.education = education;
    }

    public Experiences getExperience() {
        return experience;
    }

    public void setExperience(Experiences experience) {
        this.experience = experience;
    }

    public Interests getInterest() {
        return interest;
    }

    public void setInterest(Interests interest) {
        this.interest = interest;
    }

    public List<SocialProfile> getSocialProfileList() {
        return socialProfileList;
    }

    public void setSocialProfileList(List<SocialProfile> socialProfileList) {
        this.socialProfileList = socialProfileList;
    }

    public List<State> getStateList() {
        return stateList;
    }

    public void setStateList(List<State> stateList) {
        this.stateList = stateList;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(Integer occupationId) {
        this.occupationId = occupationId;
    }

    public List<Occupations> getOccupationList() {
        return occupationList;
    }

    public void setOccupationList(List<Occupations> occupationList) {
        this.occupationList = occupationList;
    }

    public List<Scholarity> getScholarityList() {
        return scholarityList;
    }

    public void setScholarityList(List<Scholarity> scholarityList) {
        this.scholarityList = scholarityList;
    }

    public Integer getScholarityId() {
        return scholarityId;
    }

    public void setScholarityId(Integer scholarityId) {
        this.scholarityId = scholarityId;
    }

    public List<OccupationsType> getOccupationTypeList() {
        return occupationTypeList;
    }

    public void setOccupationTypeList(List<OccupationsType> occupationTypeList) {
        this.occupationTypeList = occupationTypeList;
    }

    public Integer getOccupationTypeId() {
        return occupationTypeId;
    }

    public void setOccupationTypeId(Integer occupationTypeId) {
        this.occupationTypeId = occupationTypeId;
    }

    public List<InterestsType> getInterestTypeList() {
        return interestTypeList;
    }

    public void setInterestTypeList(List<InterestsType> interestTypeList) {
        this.interestTypeList = interestTypeList;
    }

    public List<Interests> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<Interests> interestList) {
        this.interestList = interestList;
    }

    public Integer getInterestId() {
        return interestId;
    }

    public void setInterestId(Integer interestId) {
        this.interestId = interestId;
    }

    public Integer getInterestTypeId() {
        return interestTypeId;
    }

    public void setInterestTypeId(Integer interestTypeId) {
        this.interestTypeId = interestTypeId;
    }

    public Integer getEducationLocationId() {
        return educationLocationId;
    }

    public void setEducationLocationId(Integer educationLocationId) {
        this.educationLocationId = educationLocationId;
    }

    public Integer getEducationNameId() {
        return educationNameId;
    }

    public void setEducationNameId(Integer educationNameId) {
        this.educationNameId = educationNameId;
    }

    public List<EducationsName> getEducationNameList() {
        return educationNameList;
    }

    public void setEducationNameList(List<EducationsName> educationNameList) {
        this.educationNameList = educationNameList;
    }

    public List<EducationsLocation> getEducationLocationList() {
        return educationLocationList;
    }

    public void setEducationLocationList(List<EducationsLocation> educationLocationList) {
        this.educationLocationList = educationLocationList;
    }

    public Integer getExperienceTime() {
        return experienceTime;
    }

    public void setExperienceTime(Integer experienceTime) {
        this.experienceTime = experienceTime;
    }
    
}
