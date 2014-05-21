/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.CityBO;
import com.guigoh.primata.bo.CountryBO;
import com.guigoh.primata.bo.EducationsLocationBO;
import com.guigoh.primata.bo.EducationsNameBO;
import com.guigoh.primata.bo.InterestsBO;
import com.guigoh.primata.bo.InterestsTypeBO;
import com.guigoh.primata.bo.OccupationsBO;
import com.guigoh.primata.bo.OccupationsTypeBO;
import com.guigoh.primata.bo.ScholarityBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.bo.StateBO;
import com.guigoh.primata.entity.City;
import com.guigoh.primata.entity.Country;
import com.guigoh.primata.entity.Educations;
import com.guigoh.primata.entity.EducationsLocation;
import com.guigoh.primata.entity.EducationsName;
import com.guigoh.primata.entity.Experiences;
import com.guigoh.primata.entity.Interests;
import com.guigoh.primata.entity.InterestsType;
import com.guigoh.primata.entity.Occupations;
import com.guigoh.primata.entity.OccupationsType;
import com.guigoh.primata.entity.Scholarity;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.State;
import com.guigoh.primata.entity.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author IPTI
 */
@SessionScoped
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
            socialProfileList = new ArrayList<SocialProfile>();
            stateList = new ArrayList<State>();
            countryList = getCountries();
            occupationTypeList = getOccupationTypes();
            occupationList = new ArrayList<Occupations>();
            interestTypeList = getInterestTypes();
            interestList = new ArrayList<Interests>();
            scholarityList = getScholarities();
            cityList = new ArrayList<City>();
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
        SocialProfileBO socialProfileBO = new SocialProfileBO();
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
        socialProfileList = new ArrayList<SocialProfile>();
        socialProfileList = socialProfileBO.loadUserSearchList(socialProfile, education, experienceTime, interest);
        socialProfileList = removeDuplicates(socialProfileList);
    }
    
    private List<SocialProfile> removeDuplicates(List<SocialProfile> socialProfileList){
        List<SocialProfile> socialProfileListAux = new ArrayList<SocialProfile>();
        for (SocialProfile sp : socialProfileList){
            if(!socialProfileListAux.contains(sp)){
                socialProfileListAux.add(sp);
            }
        }
        socialProfileList = socialProfileListAux;
        return socialProfileList;
    }
    
    public String goToProfile(Integer id) {
        return "/primata/profile/viewProfile.xhtml?id=" + id;
    }

    private List<Country> getCountries() {
        CountryBO countryBO = new CountryBO();
        return countryBO.getAll();
    }
    
    private List<OccupationsType> getOccupationTypes(){
        OccupationsTypeBO occupationsBO = new OccupationsTypeBO();
        return occupationsBO.getAll();
    }
    
    private List<Occupations> getOccupations(){
        OccupationsBO occupationsBO = new OccupationsBO();
        return occupationsBO.getAll();
    }
    
    private List<InterestsType> getInterestTypes(){
        InterestsTypeBO  interestsTypeBO = new InterestsTypeBO();
        return interestsTypeBO.findInterestsType();
    }
    
    private List<Scholarity> getScholarities(){
        ScholarityBO scholarityBO = new ScholarityBO();
        return scholarityBO.getAll();
    }
    
    private List<EducationsName> getEducationNames(){
        EducationsNameBO educationsNameBO = new EducationsNameBO();
        return educationsNameBO.getAll();
    }
    
    private List<EducationsLocation> getEducationLocations(){
        EducationsLocationBO educationsLocationBO = new EducationsLocationBO();
        return educationsLocationBO.getAll();
    }
    
    public void loadInterests(){
        InterestsBO interestsBO = new InterestsBO();
        interestList = interestsBO.findInterestsByInterestsTypeId(interestTypeId);
    }
    
    public void loadOccupations(){
        OccupationsBO occupationsBO = new OccupationsBO();
        occupationList = occupationsBO.findOccupationsByType(occupationTypeId);
    }

    public void loadStates() {
        StateBO stateBO = new StateBO();
        stateList = stateBO.findStatesByCountryId(countryId);
    }

    public void loadCities() {
        CityBO cityBO = new CityBO();
        cityList = cityBO.findCitiesByStateId(stateId);
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
