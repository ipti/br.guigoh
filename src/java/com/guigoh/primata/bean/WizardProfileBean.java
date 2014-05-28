/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.AuthorizationBO;
import com.guigoh.primata.bo.AvailabilityBO;
import com.guigoh.primata.bo.CityBO;
import com.guigoh.primata.bo.CountryBO;
import com.guigoh.primata.bo.EducationsBO;
import com.guigoh.primata.bo.EducationsLocationBO;
import com.guigoh.primata.bo.EducationsNameBO;
import com.guigoh.primata.bo.ExperiencesBO;
import com.guigoh.primata.bo.ExperiencesLocationBO;
import com.guigoh.primata.bo.InterestsBO;
import com.guigoh.primata.bo.InterestsTypeBO;
import com.guigoh.primata.bo.OccupationsBO;
import com.guigoh.primata.bo.OccupationsTypeBO;
import com.guigoh.primata.bo.ScholarityBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.bo.StateBO;
import com.guigoh.primata.bo.util.translator.ConfigReader;
import com.guigoh.primata.bo.util.translator.Translator;
import com.guigoh.primata.entity.Authorization;
import com.guigoh.primata.entity.Availability;
import com.guigoh.primata.entity.City;
import com.guigoh.primata.entity.Country;
import com.guigoh.primata.entity.Educations;
import com.guigoh.primata.entity.EducationsLocation;
import com.guigoh.primata.entity.EducationsName;
import com.guigoh.primata.entity.Experiences;
import com.guigoh.primata.entity.ExperiencesLocation;
import com.guigoh.primata.entity.Interests;
import com.guigoh.primata.entity.InterestsType;
import com.guigoh.primata.entity.Occupations;
import com.guigoh.primata.entity.OccupationsType;
import com.guigoh.primata.entity.Scholarity;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.State;
import com.guigoh.primata.entity.Users;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Joe
 */
@SessionScoped
@ManagedBean(name = "wizardProfileBean")
public class WizardProfileBean implements Serializable {

    public static final String BRAZIL = "Brasil";
    public static final String SERGIPE = "Sergipe";
    private SocialProfile socialProfile;
    private Users user;
    private List<Experiences> experiencesList;
    private List<Educations> educationsList;
    private List<Experiences> experiencesListAll;
    private List<Educations> educationsListAll;
    private List<EducationsName> educationsNameListAll;
    private List<EducationsLocation> educationsLocationListAll;
    private List<ExperiencesLocation> experiencesLocationListAll;
    private List<Availability> availabitityListAll;
    private Integer showpanel;
    private List<Interests> interestsList;
    private List<Interests> themesList;
    private List<Interests> moviesList;
    private List<Interests> musicsList;
    private List<Interests> booksList;
    private List<Interests> hobbiesList;
    private List<Interests> sportsList;
    private List<InterestsType> interestsTypeList;
    private List<Interests> interestsListAll;
    private List<Interests> themesListAll;
    private Integer countryId;
    private Integer stateId;
    private Integer cityId;
    private Integer countryExperienceId;
    private Integer stateExperienceId;
    private Integer countryEducationId;
    private Integer stateEducationId;
    private List<State> stateList;
    private List<Country> countryList;
    private List<City> cityList;
    private List<State> stateEducationList;
    private List<Country> countryEducationList;
    private List<State> stateExperienceList;
    private List<Country> countryExperienceList;
    private List<Interests> themesListFixo;
    private Educations education;
    private Experiences experience;
    private List<Integer> booksAddList;
    private List<Integer> musicsAddList;
    private List<Integer> moviesAddList;
    private static String[] autoCompleteThemes;
    private String books;
    private String musics;
    private String movies;
    private String sports;
    private String hobbies;
    private String occupations;
    private String educationName;
    private String experencesLocation;
    private String educationLocation;
    private List<Interests> booksListAll;
    private List<Interests> musicsListAll;
    private List<Interests> moviesListAll;
    private List<Interests> sportsListAll;
    private List<Interests> hobbiesListAll;
    private List<Occupations> occupationsListAll;
    private List<OccupationsType> occupationsTypeList;
    private Integer occupationsTypeId;
    private Integer[] multiThemeList;
    private List<Scholarity> scholarityList;
    private String educationDataBegin;
    private String educationDataEnd;
    private String experienceDataBegin;
    private String experienceDataEnd;
    private ConfigReader cr;
    private Translator trans;

    public void init() {
        if (socialProfile == null) {
            showpanel = 1;
            interestsList = new ArrayList<Interests>();
            interestsTypeList = new ArrayList<InterestsType>();
            themesList = new ArrayList<Interests>();
            booksList = new ArrayList<Interests>();
            musicsList = new ArrayList<Interests>();
            moviesList = new ArrayList<Interests>();
            hobbiesList = new ArrayList<Interests>();
            sportsList = new ArrayList<Interests>();
            socialProfile = new SocialProfile();
            stateList = new ArrayList<State>();
            countryList = new ArrayList<Country>();
            cityList = new ArrayList<City>();
            user = new Users();
            experiencesList = new ArrayList<Experiences>();
            educationsList = new ArrayList<Educations>();
            interestsListAll = new ArrayList<Interests>();
            themesListAll = new ArrayList<Interests>();
            themesListFixo = new ArrayList<Interests>();
            education = new Educations();
            experience = new Experiences();
            booksAddList = new ArrayList<Integer>();
            musicsAddList = new ArrayList<Integer>();
            moviesAddList = new ArrayList<Integer>();
            booksListAll = new ArrayList<Interests>();
            musicsListAll = new ArrayList<Interests>();
            moviesListAll = new ArrayList<Interests>();
            sportsListAll = new ArrayList<Interests>();
            hobbiesListAll = new ArrayList<Interests>();
            occupationsListAll = new ArrayList<Occupations>();
            occupationsTypeList = new ArrayList<OccupationsType>();
            countryEducationList = new ArrayList<Country>();
            countryExperienceList = new ArrayList<Country>();
            stateEducationList = new ArrayList<State>();
            stateExperienceList = new ArrayList<State>();
            educationsNameListAll = new ArrayList<EducationsName>();
            educationsLocationListAll = new ArrayList<EducationsLocation>();
            experiencesLocationListAll = new ArrayList<ExperiencesLocation>();
            multiThemeList = new Integer[6];
            scholarityList = new ArrayList<Scholarity>();
            cr = new ConfigReader();
            trans = new Translator();
            trans.setLocale(cr.getTag("locale"));
            loadWizard();
        }
    }

    private void loadWizard() {
        loadDefault();
        loadUserCookie();
        loadSocialProfile();
        loadExperiencies();
        loadEducations();
        loadAvailability();
        loadInterestsUser();
        loadInterests();
        countryId = socialProfile.getCountryId().getId();
        stateId = socialProfile.getStateId().getId();
        cityId = socialProfile.getCityId().getId();

        getCountrys();
        getStates();
        getCitys();
        //getStatesEducations();
        //getStatesExperiences();
        loadThemes();
        getAllBooks();
        getAllMusics();
        getAllMovies();
        getAllSports();
        getAllHobbies();
        getAllOccupationsType();
        getAllEducationsName();
        getAllEducationsLocationList();
        getAllExperiencesLocationList();
        getAllScholarity();
        autocompleteLabelAndValue();
        loadOccupations();
        getMultiThemes();
    }

    private void loadDefault() {
        Occupations occupationss = new Occupations();
        OccupationsType occupationsType = new OccupationsType();
        occupationss.setOccupationsTypeId(occupationsType);
        experience.setNameId(occupationss);
        experience.setLocationId(new ExperiencesLocation());
        CountryBO countryBO = new CountryBO();
        countryExperienceId = countryBO.getCountryByName(BRAZIL).getId();
        countryEducationId = countryBO.getCountryByName(BRAZIL).getId();
        loadStateExperiences();
        loadStateEducations();
        StateBO stateBO = new StateBO();
        stateExperienceId = stateBO.getStateByName(SERGIPE).getId();
        stateEducationId = stateBO.getStateByName(SERGIPE).getId();
        education.setNameId(new EducationsName());
        education.setLocationId(new EducationsLocation());
    }

    private void loadUserCookie() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().trim().equalsIgnoreCase("user")) {
                    user.setUsername(cookie.getValue());
                } else if (cookie.getName().trim().equalsIgnoreCase("token")) {
                    user.setToken(cookie.getValue());
                }
            }
        }
    }

    private void loadSocialProfile() {
        SocialProfileBO socialProfileBO = new SocialProfileBO();
        socialProfile = socialProfileBO.findSocialProfile(user.getToken());
        if (socialProfile.getOccupationsId() == null) {
            Occupations occupations_s = new Occupations();
            OccupationsType occupationsType = new OccupationsType();
            occupationsType.setId(0);
            occupations_s.setOccupationsTypeId(occupationsType);
            socialProfile.setOccupationsId(occupations_s);
        }
        if (socialProfile.getAvailabilityId() == null) {
            Availability availability = new Availability();
            availability.setId(0);
            socialProfile.setAvailabilityId(availability);
        }
        if (socialProfile.getScholarityId() == null) {
            Scholarity scholarity = new Scholarity();
            scholarity.setId(0);
            socialProfile.setScholarityId(scholarity);
        }
    }

    private void loadExperiencies() {
        ExperiencesBO experiencesBO = new ExperiencesBO();
        experiencesList = experiencesBO.findExperiencesByTokenId(user.getToken());
    }

    private void loadEducations() {
        EducationsBO educationsBO = new EducationsBO();
        educationsList = educationsBO.findEducationsByTokenId(user.getToken());
    }

    private void getAllEducationsName() {
        EducationsNameBO educationsNameBO = new EducationsNameBO();
        educationsNameListAll = educationsNameBO.getAll();
    }

    private void getAllEducationsLocationList() {
        EducationsLocationBO educationsLocationBO = new EducationsLocationBO();
        educationsLocationListAll = educationsLocationBO.getAll();
    }

    private void getAllExperiencesLocationList() {
        ExperiencesLocationBO experiencesLocationBO = new ExperiencesLocationBO();
        experiencesLocationListAll = experiencesLocationBO.getAll();
    }

    public void addEducations() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Boolean cont = true;
            if (countryEducationId == null) {
                context.addMessage("education_name", new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Selecione o País"), null));
                cont = false;
            }
            if (stateEducationId == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Selecione o Estado"), null));
                cont = false;
            }
            if ((education.getNameId().getName() != null && education.getNameId().getName().length() != 0
                    && education.getLocationId().getName() != null && education.getLocationId().getName().length() != 0)) {
                EducationsBO educationsBO = new EducationsBO();
                Country country = new Country();

                EducationsLocationBO educationsLocationBO = new EducationsLocationBO();
                EducationsLocation educationsLocationt = educationsLocationBO.findEducationsLocationByName(education.getLocationId());

                if (educationsLocationt == null) {
                    education.getLocationId().setId(0);
                    educationsLocationBO.create(education.getLocationId());
                    educationsLocationt = educationsLocationBO.findEducationsLocationByName(education.getLocationId());
                }

                EducationsNameBO educationsNameBO = new EducationsNameBO();
                EducationsName educationsNamet = educationsNameBO.findEducationsNameByName(education.getNameId());

                if (educationsNamet == null) {
                    education.getNameId().setId(0);
                    educationsNameBO.create(education.getNameId());
                    educationsNamet = educationsNameBO.findEducationsNameByName(education.getNameId());
                }


                education.setNameId(educationsNamet);
                education.setLocationId(educationsLocationt);

                country.setId(countryEducationId);
                education.setCountryId(country);

                State state = new State();
                state.setId(stateEducationId);
                education.setStateId(state);
                education.setSocialProfile(socialProfile);
                Date dataBegin = new SimpleDateFormat("dd/MM/yyyy").parse("01/" + educationDataBegin);
                education.setDataBegin(dataBegin);
                Date dataEnd = new SimpleDateFormat("dd/MM/yyyy").parse("01/" + educationDataEnd);
                education.setDataEnd(dataEnd);
                if (education.getDataBegin().before(education.getDataEnd())) {
                    education.setDataBegin(new Date(education.getDataBegin().getTime() + 600 * 60 * 1000));
                    education.setDataEnd(new Date(education.getDataEnd().getTime() + 600 * 60 * 1000));
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Verifique as datas"), null));
                    cont = false;
                }
                if (cont == true) {
                    educationsBO.createInsert(education);
                    education = new Educations();
                    education.setNameId(new EducationsName());
                    education.setLocationId(new EducationsLocation());
                    educationDataBegin = "";
                    educationDataEnd = "";
                    loadEducations();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, trans.getWord("Educação adicionada com sucesso!"), null));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addExperiences() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Boolean cont = true;
            if ((experience.getNameId().getName() != null && experience.getNameId().getName().length() != 0
                    && experience.getLocationId().getName() != null && experience.getLocationId().getName().length() != 0)) {
                ExperiencesBO experiencesBO = new ExperiencesBO();

                ExperiencesLocationBO experiencesLocationBO = new ExperiencesLocationBO();
                ExperiencesLocation experiencesLocationt = experiencesLocationBO.findExperiencesLocationByName(experience.getLocationId());

                if (experiencesLocationt == null) {
                    experience.getLocationId().setId(0);
                    experiencesLocationBO.create(experience.getLocationId());
                    experiencesLocationt = experiencesLocationBO.findExperiencesLocationByName(experience.getLocationId());
                }

                if (experience.getNameId().getOccupationsTypeId().getId() == 0) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("O campo ÁREA DE ATIVIDADE é obrigatório"), null));
                    cont = false;
                }
                OccupationsBO occupationsBO = new OccupationsBO();
                Occupations occupationst = occupationsBO.findOccupationsByNameByType(experience.getNameId());
                if (occupationst.getId() == null && cont) {
                    //experience.getNameId().setId(0);
                    occupationsBO.createInsert(experience.getNameId());
                    occupationst = occupationsBO.findOccupationsByNameByType(experience.getNameId());
                }

                experience.setNameId(occupationst);
                experience.setLocationId(experiencesLocationt);
                if (countryExperienceId == 0) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Selecione o país"), null));
                    cont = false;
                }
                Country country = new Country();
                country.setId(countryExperienceId);
                experience.setCountryId(country);
                if (stateExperienceId == 0) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Selecione o estado"), null));
                    cont = false;
                }
                State state = new State();
                state.setId(stateExperienceId);
                experience.setStateId(state);
                experience.setSocialProfile(socialProfile);
                Date dataBegin = new SimpleDateFormat("dd/MM/yyyy").parse("01/" + experienceDataBegin);
                experience.setDataBegin(dataBegin);
                Date dataEnd = new SimpleDateFormat("dd/MM/yyyy").parse("01/" + experienceDataEnd);
                experience.setDataEnd(dataEnd);
                if (experience.getDataBegin().after(experience.getDataEnd())) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Verifique as datas"), null));
                    cont = false;
                } else {
                    experience.setDataBegin(new Date(experience.getDataBegin().getTime() + 600 * 60 * 1000));
                    experience.setDataEnd(new Date(experience.getDataEnd().getTime() + 600 * 60 * 1000));
                }
                if (cont == true) {
                    experiencesBO.createInsert(experience);
                    experience = new Experiences();
                    OccupationsType occupationsType = new OccupationsType();
                    Occupations occupationsT = new Occupations();
                    occupationsT.setOccupationsTypeId(occupationsType);
                    experience.setNameId(occupationsT);
                    experience.setLocationId(new ExperiencesLocation());
                    experienceDataBegin = "";
                    experienceDataEnd = "";
                    loadExperiencies();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, trans.getWord("Experiência adicionada com sucesso!"), null));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAvailability() {
        AvailabilityBO availabilityBO = new AvailabilityBO();
        availabitityListAll = availabilityBO.getAll();
    }

    private void checkInterestsList(List<Interests> interestsList, String type) {
        InterestsBO interestsBO = new InterestsBO();
        InterestsTypeBO interestsTypeBO = new InterestsTypeBO();
        InterestsType interestsType = interestsTypeBO.findInterestsTypeByName(type);
        Interests interestsTemp = new Interests();
        for (Interests interests : interestsList) {
            if (interests != null) {
                interestsTemp = interestsBO.findInterestsByInterestsName(interests.getName());
                if (interestsTemp.getId() == null) {

                    interests.setId(0);
                    interests.setTypeId(interestsType);
                    interestsBO.create(interests);

                }
            }
        }
    }

    public String editWizard(int panel) {
        try {
            if (panel == 1) {
                FacesContext context = FacesContext.getCurrentInstance();
                Occupations occupationst = new Occupations();
                OccupationsType occupationsType = new OccupationsType();
                occupationst.setOccupationsTypeId(occupationsType);
                if (!socialProfile.getOccupationsId().getName().equals("")) {
                    if (socialProfile.getOccupationsId().getOccupationsTypeId().getId() == 0) {
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Selecione a área de ocupação"), null));
                        showpanel = 1;
                        return "";
                    }
                }
                if (!socialProfile.getOccupationsId().getName().equals("")) {
                    OccupationsBO occupationsBO = new OccupationsBO();
                    occupationst = occupationsBO.findOccupationsByNameByType(socialProfile.getOccupationsId());
                    if (occupationst.getId() == null) {
                        socialProfile.getOccupationsId().setId(0);
                        occupationsBO.create(socialProfile.getOccupationsId());
                        occupationst = occupationsBO.findOccupationsByNameByType(socialProfile.getOccupationsId());
                    }

                }

                socialProfile.setOccupationsId(occupationst);
                socialProfile.getCountryId().setId(countryId);
                socialProfile.getStateId().setId(stateId);
                socialProfile.getCityId().setId(cityId);
            }
            if (panel == 2) {
                InterestsBO interestsBO = new InterestsBO();
                interestsBO.destroyInterestsBySocialProfile(socialProfile);
                interestsBO.createInterestsBySocialProfileByIds(multiThemeList, socialProfile);
                checkInterestsList(booksList, "Books");
                checkInterestsList(musicsList, "Musics");
                checkInterestsList(moviesList, "Movies");
                checkInterestsList(sportsList, "Sports");
                checkInterestsList(hobbiesList, "Hobbies");

                interestsBO.createInterestsBySocialProfileByInterest(booksList, socialProfile);
                interestsBO.createInterestsBySocialProfileByInterest(sportsList, socialProfile);
                interestsBO.createInterestsBySocialProfileByInterest(moviesList, socialProfile);
                interestsBO.createInterestsBySocialProfileByInterest(hobbiesList, socialProfile);
                interestsBO.createInterestsBySocialProfileByInterest(musicsList, socialProfile);
            } else {
                SocialProfileBO socialProfileBO = new SocialProfileBO();

                if (socialProfile.getAvailabilityId().getId() == 0) {
                    socialProfile.setAvailabilityId(null);
                }
                if (socialProfile.getScholarityId().getId() == 0) {
                    socialProfile.setScholarityId(null);
                }

                socialProfileBO.edit(socialProfile);

                if (socialProfile.getAvailabilityId() == null) {
                    Availability availability = new Availability();
                    availability.setId(0);
                    socialProfile.setAvailabilityId(availability);
                }
                if (socialProfile.getScholarityId() == null) {
                    Scholarity scholarity = new Scholarity();
                    scholarity.setId(0);
                    socialProfile.setScholarityId(scholarity);
                }
            }
            return "wizard";
        } catch (Exception e) {
            return "";
        }
    }

    private String findInterestTypeById(Integer id) {
        for (InterestsType interestsType : interestsTypeList) {
            if (interestsType.getId() == id) {
                return interestsType.getType();
            }
        }
        return "";
    }

    private void loadInterestsUser() {
        if (socialProfile != null) {
            InterestsBO interestsBO = new InterestsBO();
            interestsList = interestsBO.findInterests(socialProfile.getSocialProfileId());
            InterestsTypeBO interestsTypeBO = new InterestsTypeBO();
            interestsTypeList = interestsTypeBO.findInterestsType();

            for (Interests interests : interestsList) {
                if (findInterestTypeById(interests.getTypeId().getId()).equals("CI")) {
                    if (interests.getTypeId().getName().equals("Musics")) {
                        musicsList.add(interests);
                    }
                    if (interests.getTypeId().getName().equals("Movies")) {
                        moviesList.add(interests);
                    }
                    if (interests.getTypeId().getName().equals("Books")) {
                        booksList.add(interests);
                    }
                }
                if (findInterestTypeById(interests.getTypeId().getId()).equals("SH")) {
                    if (interests.getTypeId().getName().equals("Sports")) {
                        sportsList.add(interests);
                    }
                    if (interests.getTypeId().getName().equals("Hobbies")) {
                        hobbiesList.add(interests);
                    }
                }
                if (findInterestTypeById(interests.getTypeId().getId()).equals("TH")) {
                    themesList.add(interests);
                }
            }
        }
        //HashMap<String, List<Interests>> hashmap = new HashMap<String, List<Interests>>();
        //hashmap.put("Musics", musicsList);
        //hashmap.put("Movies", moviesList);
        // hashmap.put("Books", booksList);
        //hashmap.put("Sports", sportsList);
        //hashmap.put("Hobbies", hobbiesList);
        //hashmap.put("Themes", themesList);
        musicsList = completeInterests(musicsList);
        moviesList = completeInterests(moviesList);
        booksList = completeInterests(booksList);
        sportsList = completeInterests(sportsList);
        hobbiesList = completeInterests(hobbiesList);
    }

    public List<Interests> completeInterests(List<Interests> it) {
        try {
            for (int cont = it.size(); cont < 3; cont++) {
                Interests interests = new Interests();
                it.add(interests);
            }
            return it;
        } catch (Exception e) {
            e.printStackTrace();
            return it;
        }
    }

    public String skip(int panel) {
        try {
            editWizard(panel);
            AuthorizationBO authorizationBO = new AuthorizationBO();
            Authorization authorization = authorizationBO.findAuthorizationByTokenId(user.getToken());
            authorization.setStatus("AC");
            authorizationBO.edit(authorization);
            return "profile";
        } catch (Exception e) {
            return "";
        }
    }

    private void loadInterests() {
        InterestsBO interestsBO = new InterestsBO();
        interestsListAll = interestsBO.getAll();
    }

    private void loadThemes() {
        InterestsBO interestsBO = new InterestsBO();
        themesListAll = interestsBO.findInterestsByInterestsTypeName("Themes");
    }

    public List<String> loadAutocompleteInterests() {
        try {

            List<String> label = new ArrayList<String>();
            for (Interests interests : interestsListAll) {
                label.add(interests.getName());
            }
            return label;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<String>();

        }
    }

    private void getCountrys() {
        CountryBO countryBO = new CountryBO();
        countryList = countryBO.getAll();
        countryEducationList = countryList;
        countryExperienceList = countryList;
    }

    private void getStates() {
        StateBO stateBO = new StateBO();
        stateList = stateBO.findStatesByCountryId(countryId);
    }

    private void getCitys() {
        CityBO cityBO = new CityBO();
        cityList = cityBO.findCitiesByStateId(stateId);
    }

    public void loadState() {
        try {
            StateBO stateBO = new StateBO();
            stateList = stateBO.findStatesByCountryId(countryId);
            cityId = 0;
            stateId = 0;
            cityList = new ArrayList<City>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadStateEducations() {
        try {
            StateBO stateBO = new StateBO();
            stateEducationList = stateBO.findStatesByCountryId(countryEducationId);
            stateEducationId = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadStateExperiences() {
        try {
            StateBO stateBO = new StateBO();
            stateExperienceList = stateBO.findStatesByCountryId(countryExperienceId);
            stateExperienceId = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadCity() {
        try {
            CityBO cityBO = new CityBO();
            cityList = cityBO.findCitiesByStateId(stateId);
            cityId = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadOccupations() {
        try {
            if (socialProfile.getOccupationsId().getOccupationsTypeId() != null) {
                if (socialProfile.getOccupationsId().getOccupationsTypeId().getId() == 0) {
                    socialProfile.getOccupationsId().setName("");
                }
            }

            OccupationsBO occupationsBO = new OccupationsBO();
            //occupations = convertAutoCompleteOccupations(occupationsBO.findOccupationsByType(socialProfile.getOccupationsId().getOccupationsTypeId().getId()));
            occupations = convertAutoCompleteOccupations(occupationsBO.getAll());
            //socialProfile.getOccupationsId().setName("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAllOccupationsType() {
        OccupationsTypeBO occupationsTypeBO = new OccupationsTypeBO();
        occupationsTypeList = occupationsTypeBO.getAll();
    }

    private void getAllBooks() {
        InterestsBO interestsBO = new InterestsBO();
        booksListAll = interestsBO.findInterestsByInterestsTypeName("Books");
    }

    private void getAllMusics() {
        InterestsBO interestsBO = new InterestsBO();
        musicsListAll = interestsBO.findInterestsByInterestsTypeName("Musics");
    }

    private void getAllMovies() {
        InterestsBO interestsBO = new InterestsBO();
        moviesListAll = interestsBO.findInterestsByInterestsTypeName("Movies");
    }

    private void getAllSports() {
        InterestsBO interestsBO = new InterestsBO();
        sportsListAll = interestsBO.findInterestsByInterestsTypeName("Sports");
    }

    private void getAllHobbies() {
        InterestsBO interestsBO = new InterestsBO();
        hobbiesListAll = interestsBO.findInterestsByInterestsTypeName("Hobbies");
    }

    private void getAllScholarity() {
        ScholarityBO scholarityBO = new ScholarityBO();
        scholarityList = scholarityBO.getAll();
    }

    public void autocompleteLabelAndValue() {
        try {
            books = convertAutoCompleteInterests(booksListAll);
            musics = convertAutoCompleteInterests(musicsListAll);
            movies = convertAutoCompleteInterests(moviesListAll);
            sports = convertAutoCompleteInterests(sportsListAll);
            hobbies = convertAutoCompleteInterests(hobbiesListAll);
            experencesLocation = convertAutoCompleteExperiencesLocation(experiencesLocationListAll);
            educationName = convertAutoCompleteEducations(educationsNameListAll);
            educationLocation = convertAutoCompleteEducationsLocation(educationsLocationListAll);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeExperience(Experiences exp) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            experiencesList.remove(exp);
            ExperiencesBO experiencesBO = new ExperiencesBO();
            experiencesBO.removeExperience(exp);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, trans.getWord("Experiência removida com sucesso!"), null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeEducation(Educations edu) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            educationsList.remove(edu);
            EducationsBO educationsBO = new EducationsBO();
            educationsBO.removeEducation(edu);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, trans.getWord("Educação removida com sucesso!"), null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertAutoCompleteEducations(List<EducationsName> educationsNameListAll) {
        if (educationsNameListAll.isEmpty()) {
            return "\"\"";
        }
        String s = "[";
        for (EducationsName educationsName : educationsNameListAll) {
            s = s + "{";
            s = s + "\"value\":\"" + educationsName.getId() + "\",\"label\":\"" + educationsName.getName() + "\"";
            s = s + "},";
        }
        s = s.substring(0, s.length() - 1);
        s = s + "]";
        //" [ {\"value\":\"1\",\"label\":\"Um\"},{\"value\":\"2\",\"label\":\"Dois\"} ]";
        return s;
    }

    private String convertAutoCompleteEducationsLocation(List<EducationsLocation> educationsLocationListAll) {
        if (educationsLocationListAll.isEmpty()) {
            return "\"\"";
        }
        String s = "[";
        for (EducationsLocation educationLocations : educationsLocationListAll) {
            s = s + "{";
            s = s + "\"value\":\"" + educationLocations.getId() + "\",\"label\":\"" + educationLocations.getName() + "\"";
            s = s + "},";
        }
        s = s.substring(0, s.length() - 1);
        s = s + "]";
        //" [ {\"value\":\"1\",\"label\":\"Um\"},{\"value\":\"2\",\"label\":\"Dois\"} ]";
        return s;
    }

    private String convertAutoCompleteExperiencesLocation(List<ExperiencesLocation> experiencesLocationListAll) {
        if (experiencesLocationListAll.isEmpty()) {
            return "\"\"";
        }
        String s = "[";
        for (ExperiencesLocation experiencesLocation : experiencesLocationListAll) {
            s = s + "{";
            s = s + "\"value\":\"" + experiencesLocation.getId() + "\",\"label\":\"" + experiencesLocation.getName() + "\"";
            s = s + "},";
        }
        s = s.substring(0, s.length() - 1);
        s = s + "]";
        //" [ {\"value\":\"1\",\"label\":\"Um\"},{\"value\":\"2\",\"label\":\"Dois\"} ]";
        return s;
    }

    private String convertAutoCompleteOccupations(List<Occupations> occupationsListCom) {
        if (occupationsListCom.isEmpty()) {
            return "\"\"";
        }
        String s = "[";
        for (Occupations occupations2 : occupationsListCom) {
            s = s + "{";
            s = s + "\"value\":\"" + occupations2.getId() + "\",\"label\":\"" + occupations2.getName() + "\"";
            s = s + "},";
        }
        s = s.substring(0, s.length() - 1);
        s = s + "]";
        //" [ {\"value\":\"1\",\"label\":\"Um\"},{\"value\":\"2\",\"label\":\"Dois\"} ]";
        return s;
    }

    private String convertAutoCompleteInterests(List<Interests> interestsListCom) {
        if (interestsListCom.isEmpty()) {
            return "";
        }
        String s = "[";
        for (Interests interests : interestsListCom) {
            s = s + "{";
            s = s + "\"value\":\"" + interests.getId() + "\",\"label\":\"" + interests.getName() + "\"";
            s = s + "},";
        }
        s = s.substring(0, s.length() - 1);
        s = s + "]";
        //" [ {\"value\":\"1\",\"label\":\"Um\"},{\"value\":\"2\",\"label\":\"Dois\"} ]";
        return s;
    }

    private void getMultiThemes() {

        for (int i = 0; i < themesList.size(); i++) {
            multiThemeList[i] = themesList.get(i).getId();
        }
    }

    public SocialProfile getSocialProfile() {
        return socialProfile;
    }

    public void setSocialProfile(SocialProfile socialProfile) {
        this.socialProfile = socialProfile;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Experiences> getExperiencesList() {
        return experiencesList;
    }

    public void setExperiencesList(List<Experiences> experiencesList) {
        this.experiencesList = experiencesList;
    }

    public List<Educations> getEducationsList() {
        return educationsList;
    }

    public void setEducationsList(List<Educations> educationsList) {
        this.educationsList = educationsList;
    }

    public List<Experiences> getExperiencesListAll() {
        return experiencesListAll;
    }

    public void setExperiencesListAll(List<Experiences> experiencesListAll) {
        this.experiencesListAll = experiencesListAll;
    }

    public List<Educations> getEducationsListAll() {
        return educationsListAll;
    }

    public void setEducationsListAll(List<Educations> educationsListAll) {
        this.educationsListAll = educationsListAll;
    }

    public List<Availability> getAvailabitityListAll() {
        return availabitityListAll;
    }

    public void setAvailabitityListAll(List<Availability> availabitityListAll) {
        this.availabitityListAll = availabitityListAll;
    }

    public Integer getShowpanel() {
        return showpanel;
    }

    public void setShowpanel(Integer showpanel) {
        this.showpanel = showpanel;
    }

    public List<Interests> getInterestsList() {
        return interestsList;
    }

    public void setInterestsList(List<Interests> interestsList) {
        this.interestsList = interestsList;
    }

    public List<Interests> getThemesList() {
        return themesList;
    }

    public void setThemesList(List<Interests> themesList) {
        this.themesList = themesList;
    }

    public List<Interests> getMoviesList() {
        return moviesList;
    }

    public void setMoviesList(List<Interests> moviesList) {
        this.moviesList = moviesList;
    }

    public List<Interests> getMusicsList() {
        return musicsList;
    }

    public void setMusicsList(List<Interests> musicsList) {
        this.musicsList = musicsList;
    }

    public List<Interests> getBooksList() {
        return booksList;
    }

    public void setBooksList(List<Interests> booksList) {
        this.booksList = booksList;
    }

    public List<Interests> getHobbiesList() {
        return hobbiesList;
    }

    public void setHobbiesList(List<Interests> hobbiesList) {
        this.hobbiesList = hobbiesList;
    }

    public List<Interests> getSportsList() {
        return sportsList;
    }

    public void setSportsList(List<Interests> sportsList) {
        this.sportsList = sportsList;
    }

    public List<InterestsType> getInterestsTypeList() {
        return interestsTypeList;
    }

    public void setInterestsTypeList(List<InterestsType> interestsTypeList) {
        this.interestsTypeList = interestsTypeList;
    }

    public List<Interests> getInterestsListAll() {
        return interestsListAll;
    }

    public void setInterestsListAll(List<Interests> interestsListAll) {
        this.interestsListAll = interestsListAll;
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

    public List<Interests> getThemesListAll() {
        return themesListAll;
    }

    public void setThemesListAll(List<Interests> themesListAll) {
        this.themesListAll = themesListAll;
    }

    public List<Interests> getThemesListFixo() {
        return themesListFixo;
    }

    public void setThemesListFixo(List<Interests> themesListFixo) {
        this.themesListFixo = themesListFixo;
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

    public List<Integer> getBooksAddList() {
        return booksAddList;
    }

    public void setBooksAddList(List<Integer> booksAddList) {
        this.booksAddList = booksAddList;
    }

    public List<Integer> getMusicsAddList() {
        return musicsAddList;
    }

    public void setMusicsAddList(List<Integer> musicsAddList) {
        this.musicsAddList = musicsAddList;
    }

    public List<Integer> getMoviesAddList() {
        return moviesAddList;
    }

    public void setMoviesAddList(List<Integer> moviesAddList) {
        this.moviesAddList = moviesAddList;
    }

    public static String[] getAutoCompleteThemes() {
        return autoCompleteThemes;
    }

    public static void setAutoCompleteThemes(String[] autoCompleteThemes) {
        WizardProfileBean.autoCompleteThemes = autoCompleteThemes;
    }

    public String getBooks() {
        return books;
    }

    public void setBooks(String books) {
        this.books = books;
    }

    public String getMusics() {
        return musics;
    }

    public void setMusics(String musics) {
        this.musics = musics;
    }

    public String getMovies() {
        return movies;
    }

    public void setMovies(String movies) {
        this.movies = movies;
    }

    public List<Interests> getBooksListAll() {
        return booksListAll;
    }

    public void setBooksListAll(List<Interests> booksListAll) {
        this.booksListAll = booksListAll;
    }

    public List<Interests> getMusicsListAll() {
        return musicsListAll;
    }

    public void setMusicsListAll(List<Interests> musicsListAll) {
        this.musicsListAll = musicsListAll;
    }

    public List<Interests> getMoviesListAll() {
        return moviesListAll;
    }

    public void setMoviesListAll(List<Interests> moviesListAll) {
        this.moviesListAll = moviesListAll;
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

    public String getOccupations() {
        return occupations;
    }

    public void setOccupations(String occupations) {
        this.occupations = occupations;
    }

    public List<Interests> getSportsListAll() {
        return sportsListAll;
    }

    public void setSportsListAll(List<Interests> sportsListAll) {
        this.sportsListAll = sportsListAll;
    }

    public List<Interests> getHobbiesListAll() {
        return hobbiesListAll;
    }

    public void setHobbiesListAll(List<Interests> hobbiesListAll) {
        this.hobbiesListAll = hobbiesListAll;
    }

    public List<Occupations> getOccupationsListAll() {
        return occupationsListAll;
    }

    public void setOccupationsListAll(List<Occupations> occupationsListAll) {
        this.occupationsListAll = occupationsListAll;
    }

    public List<OccupationsType> getOccupationsTypeList() {
        return occupationsTypeList;
    }

    public void setOccupationsTypeList(List<OccupationsType> occupationsTypeList) {
        this.occupationsTypeList = occupationsTypeList;
    }

    public Integer getOccupationsTypeId() {
        return occupationsTypeId;
    }

    public void setOccupationsTypeId(Integer occupationsTypeId) {
        this.occupationsTypeId = occupationsTypeId;
    }

    public Integer getCountryExperienceId() {
        return countryExperienceId;
    }

    public void setCountryExperienceId(Integer countryExperienceId) {
        this.countryExperienceId = countryExperienceId;
    }

    public Integer getStateExperienceId() {
        return stateExperienceId;
    }

    public void setStateExperienceId(Integer stateExperienceId) {
        this.stateExperienceId = stateExperienceId;
    }

    public Integer getCountryEducationId() {
        return countryEducationId;
    }

    public void setCountryEducationId(Integer countryEducationId) {
        this.countryEducationId = countryEducationId;
    }

    public Integer getStateEducationId() {
        return stateEducationId;
    }

    public void setStateEducationId(Integer stateEducationId) {
        this.stateEducationId = stateEducationId;
    }

    public List<State> getStateEducationList() {
        return stateEducationList;
    }

    public void setStateEducationList(List<State> stateEducationList) {
        this.stateEducationList = stateEducationList;
    }

    public List<Country> getCountryEducationList() {
        return countryEducationList;
    }

    public void setCountryEducationList(List<Country> countryEducationList) {
        this.countryEducationList = countryEducationList;
    }

    public List<State> getStateExperienceList() {
        return stateExperienceList;
    }

    public void setStateExperienceList(List<State> stateExperienceList) {
        this.stateExperienceList = stateExperienceList;
    }

    public List<Country> getCountryExperienceList() {
        return countryExperienceList;
    }

    public void setCountryExperienceList(List<Country> countryExperienceList) {
        this.countryExperienceList = countryExperienceList;
    }

    public List<EducationsName> getEducationsNameListAll() {
        return educationsNameListAll;
    }

    public void setEducationsNameListAll(List<EducationsName> educationsNameListAll) {
        this.educationsNameListAll = educationsNameListAll;
    }

    public List<EducationsLocation> getEducationsLocationListAll() {
        return educationsLocationListAll;
    }

    public void setEducationsLocationListAll(List<EducationsLocation> educationsLocationListAll) {
        this.educationsLocationListAll = educationsLocationListAll;
    }

    public List<ExperiencesLocation> getExperiencesLocationListAll() {
        return experiencesLocationListAll;
    }

    public void setExperiencesLocationListAll(List<ExperiencesLocation> experiencesLocationListAll) {
        this.experiencesLocationListAll = experiencesLocationListAll;
    }

    public String getEducationName() {
        return educationName;
    }

    public void setEducationName(String educationName) {
        this.educationName = educationName;
    }

    public String getExperencesLocation() {
        return experencesLocation;
    }

    public void setExperencesLocation(String experencesLocation) {
        this.experencesLocation = experencesLocation;
    }

    public String getEducationLocation() {
        return educationLocation;
    }

    public void setEducationLocation(String educationLocation) {
        this.educationLocation = educationLocation;
    }

    public Integer[] getMultiThemeList() {
        return multiThemeList;
    }

    public void setMultiThemeList(Integer[] multiThemeList) {
        this.multiThemeList = multiThemeList;
    }

    public List<Scholarity> getScholarityList() {
        return scholarityList;
    }

    public void setScholarityList(List<Scholarity> scholarityList) {
        this.scholarityList = scholarityList;
    }

    public String getEducationDataBegin() {
        return educationDataBegin;
    }

    public void setEducationDataBegin(String educationDataBegin) {
        this.educationDataBegin = educationDataBegin;
    }

    public String getEducationDataEnd() {
        return educationDataEnd;
    }

    public void setEducationDataEnd(String educationDataEnd) {
        this.educationDataEnd = educationDataEnd;
    }

    public String getExperienceDataBegin() {
        return experienceDataBegin;
    }

    public void setExperienceDataBegin(String experienceDataBegin) {
        this.experienceDataBegin = experienceDataBegin;
    }

    public String getExperienceDataEnd() {
        return experienceDataEnd;
    }

    public void setExperienceDataEnd(String experienceDataEnd) {
        this.experienceDataEnd = experienceDataEnd;
    }
}
