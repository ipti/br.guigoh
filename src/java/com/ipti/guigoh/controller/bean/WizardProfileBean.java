/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipti.guigoh.controller.bean;

import com.guigoh.bo.EducationsBO;
import com.guigoh.bo.EducationsLocationBO;
import com.guigoh.bo.EducationsNameBO;
import com.guigoh.bo.ExperiencesBO;
import com.guigoh.bo.ExperiencesLocationBO;
import com.guigoh.bo.InterestsBO;
import com.guigoh.bo.InterestsTypeBO;
import com.guigoh.bo.OccupationsBO;
import com.guigoh.bo.SocialProfileBO;
import com.guigoh.bo.UserAuthorizationBO;
import com.ipti.guigoh.model.entity.Availability;
import com.ipti.guigoh.model.entity.City;
import com.ipti.guigoh.model.entity.Country;
import com.ipti.guigoh.model.entity.Educations;
import com.ipti.guigoh.model.entity.EducationsLocation;
import com.ipti.guigoh.model.entity.EducationsName;
import com.ipti.guigoh.model.entity.Experiences;
import com.ipti.guigoh.model.entity.ExperiencesLocation;
import com.ipti.guigoh.model.entity.Interests;
import com.ipti.guigoh.model.entity.InterestsType;
import com.ipti.guigoh.model.entity.Occupations;
import com.ipti.guigoh.model.entity.OccupationsType;
import com.ipti.guigoh.model.entity.Scholarity;
import com.ipti.guigoh.model.entity.SocialProfile;
import com.ipti.guigoh.model.entity.State;
import com.ipti.guigoh.model.entity.UserAuthorization;
import com.ipti.guigoh.model.entity.Users;
import com.ipti.guigoh.model.jpa.controller.AvailabilityJpaController;
import com.ipti.guigoh.model.jpa.controller.CityJpaController;
import com.ipti.guigoh.model.jpa.controller.CountryJpaController;
import com.ipti.guigoh.model.jpa.controller.OccupationsTypeJpaController;
import com.ipti.guigoh.model.jpa.controller.ScholarityJpaController;
import com.ipti.guigoh.model.jpa.controller.StateJpaController;
import com.ipti.guigoh.util.CookieService;
import com.ipti.guigoh.util.translator.Translator;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
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
    private Translator trans;

    public void init() {
            if (socialProfile == null) {
                showpanel = 1;
                interestsList = new ArrayList<>();
                interestsTypeList = new ArrayList<>();
                themesList = new ArrayList<>();
                booksList = new ArrayList<>();
                musicsList = new ArrayList<>();
                moviesList = new ArrayList<>();
                hobbiesList = new ArrayList<>();
                sportsList = new ArrayList<>();
                socialProfile = new SocialProfile();
                stateList = new ArrayList<>();
                countryList = new ArrayList<>();
                cityList = new ArrayList<>();
                user = new Users();
                experiencesList = new ArrayList<>();
                educationsList = new ArrayList<>();
                interestsListAll = new ArrayList<>();
                themesListAll = new ArrayList<>();
                themesListFixo = new ArrayList<>();
                education = new Educations();
                experience = new Experiences();
                booksAddList = new ArrayList<>();
                musicsAddList = new ArrayList<>();
                moviesAddList = new ArrayList<>();
                booksListAll = new ArrayList<>();
                musicsListAll = new ArrayList<>();
                moviesListAll = new ArrayList<>();
                sportsListAll = new ArrayList<>();
                hobbiesListAll = new ArrayList<>();
                occupationsListAll = new ArrayList<>();
                occupationsTypeList = new ArrayList<>();
                countryEducationList = new ArrayList<>();
                countryExperienceList = new ArrayList<>();
                stateEducationList = new ArrayList<>();
                stateExperienceList = new ArrayList<>();
                educationsNameListAll = new ArrayList<>();
                educationsLocationListAll = new ArrayList<>();
                experiencesLocationListAll = new ArrayList<>();
                multiThemeList = new Integer[6];
                scholarityList = new ArrayList<>();
                trans = new Translator();
                trans.setLocale(CookieService.getCookie("locale"));
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
        CountryJpaController countryJpaController = new CountryJpaController();
        countryExperienceId = countryJpaController.findCountryByName(BRAZIL).getId();
        countryEducationId = countryJpaController.findCountryByName(BRAZIL).getId();
        loadStateExperiences();
        loadStateEducations();
        StateJpaController stateJpaController = new StateJpaController();
        stateExperienceId = stateJpaController.findStateByName(SERGIPE).getId();
        stateEducationId = stateJpaController.findStateByName(SERGIPE).getId();
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
        socialProfile = SocialProfileBO.findSocialProfile(user.getToken());
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
        experiencesList = ExperiencesBO.findExperiencesByTokenId(user.getToken());
    }

    private void loadEducations() {
        educationsList = EducationsBO.findEducationsByTokenId(user.getToken());
    }

    private void getAllEducationsName() {
        educationsNameListAll = EducationsNameBO.getAll();
    }

    private void getAllEducationsLocationList() {
        educationsLocationListAll = EducationsLocationBO.getAll();
    }

    private void getAllExperiencesLocationList() {
        experiencesLocationListAll = ExperiencesLocationBO.getAll();
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

                EducationsLocation educationsLocationt = EducationsLocationBO.findEducationsLocationByName(education.getLocationId());

                if (educationsLocationt == null) {
                    education.getLocationId().setId(0);
                    EducationsLocationBO.create(education.getLocationId());
                    educationsLocationt = EducationsLocationBO.findEducationsLocationByName(education.getLocationId());
                }

                EducationsName educationsNamet = EducationsNameBO.findEducationsNameByName(education.getNameId());

                if (educationsNamet == null) {
                    education.getNameId().setId(0);
                    EducationsNameBO.create(education.getNameId());
                    educationsNamet = EducationsNameBO.findEducationsNameByName(education.getNameId());
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
                    EducationsBO.createInsert(education);
                    education = new Educations();
                    education.setNameId(new EducationsName());
                    education.setLocationId(new EducationsLocation());
                    educationDataBegin = "";
                    educationDataEnd = "";
                    loadEducations();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, trans.getWord("Educação adicionada com sucesso!"), null));
                }
            }
        } catch (ParseException e) {
        }
    }

    public void addExperiences() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Boolean cont = true;
            if ((experience.getNameId().getName() != null && experience.getNameId().getName().length() != 0
                    && experience.getLocationId().getName() != null && experience.getLocationId().getName().length() != 0)) {

                ExperiencesLocation experiencesLocationt = ExperiencesLocationBO.findExperiencesLocationByName(experience.getLocationId());

                if (experiencesLocationt == null) {
                    experience.getLocationId().setId(0);
                    ExperiencesLocationBO.create(experience.getLocationId());
                    experiencesLocationt = ExperiencesLocationBO.findExperiencesLocationByName(experience.getLocationId());
                }

                if (experience.getNameId().getOccupationsTypeId().getId() == 0) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("O campo ÁREA DE ATIVIDADE é obrigatório"), null));
                    cont = false;
                }
                Occupations occupationst = OccupationsBO.findOccupationsByNameByType(experience.getNameId());
                if (occupationst.getId() == null && cont) {
                    //experience.getNameId().setId(0);
                    OccupationsBO.createInsert(experience.getNameId());
                    occupationst = OccupationsBO.findOccupationsByNameByType(experience.getNameId());
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
                    ExperiencesBO.createInsert(experience);
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
        } catch (ParseException e) {
        }
    }

    private void loadAvailability() {
        AvailabilityJpaController availabilityJpaController = new AvailabilityJpaController();
        availabitityListAll = availabilityJpaController.findAvailabilityEntities();
    }

    private void checkInterestsList(List<Interests> interestsList, String type) {
        InterestsType interestsType = InterestsTypeBO.findInterestsTypeByName(type);
        Interests interestsTemp = new Interests();
        for (Interests interests : interestsList) {
            if (interests != null) {
                interestsTemp = InterestsBO.findInterestsByInterestsName(interests.getName());
                if (interestsTemp.getId() == null) {

                    interests.setId(0);
                    interests.setTypeId(interestsType);
                    InterestsBO.create(interests);

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
                    occupationst = OccupationsBO.findOccupationsByNameByType(socialProfile.getOccupationsId());
                    if (occupationst.getId() == null) {
                        socialProfile.getOccupationsId().setId(0);
                        OccupationsBO.create(socialProfile.getOccupationsId());
                        occupationst = OccupationsBO.findOccupationsByNameByType(socialProfile.getOccupationsId());
                    }

                }

                socialProfile.setOccupationsId(occupationst);
                socialProfile.getCountryId().setId(countryId);
                socialProfile.getStateId().setId(stateId);
                socialProfile.getCityId().setId(cityId);
            }
            if (panel == 2) {
                InterestsBO.destroyInterestsBySocialProfile(socialProfile);
                InterestsBO.createInterestsBySocialProfileByIds(multiThemeList, socialProfile);
                checkInterestsList(booksList, "Books");
                checkInterestsList(musicsList, "Musics");
                checkInterestsList(moviesList, "Movies");
                checkInterestsList(sportsList, "Sports");
                checkInterestsList(hobbiesList, "Hobbies");

                InterestsBO.createInterestsBySocialProfileByInterest(booksList, socialProfile);
                InterestsBO.createInterestsBySocialProfileByInterest(sportsList, socialProfile);
                InterestsBO.createInterestsBySocialProfileByInterest(moviesList, socialProfile);
                InterestsBO.createInterestsBySocialProfileByInterest(hobbiesList, socialProfile);
                InterestsBO.createInterestsBySocialProfileByInterest(musicsList, socialProfile);
            } else {
                SocialProfileBO socialProfileBO = new SocialProfileBO();

                if (socialProfile.getAvailabilityId().getId() == 0) {
                    socialProfile.setAvailabilityId(null);
                }
                if (socialProfile.getScholarityId().getId() == 0) {
                    socialProfile.setScholarityId(null);
                }

                SocialProfileBO.edit(socialProfile);

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
            if (Objects.equals(interestsType.getId(), id)) {
                return interestsType.getType();
            }
        }
        return "";
    }

    private void loadInterestsUser() {
        if (socialProfile != null) {
            interestsList = InterestsBO.findInterests(socialProfile.getSocialProfileId());
            interestsTypeList = InterestsTypeBO.findInterestsType();

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
            return it;
        }
    }

    public String skip(int panel) {
        try {
            editWizard(panel);
            UserAuthorization authorization = UserAuthorizationBO.findAuthorizationByTokenId(user.getToken());
            authorization.setStatus("AC");
            UserAuthorizationBO.edit(authorization);
            return "profile";
        } catch (Exception e) {
            return "";
        }
    }

    private void loadInterests() {
        interestsListAll = InterestsBO.getAll();
    }

    private void loadThemes() {
        themesListAll = InterestsBO.findInterestsByInterestsTypeName("Themes");
    }

    public List<String> loadAutocompleteInterests() {
        try {

            List<String> label = new ArrayList<>();
            for (Interests interests : interestsListAll) {
                label.add(interests.getName());
            }
            return label;

        } catch (Exception e) {
            return new ArrayList<>();

        }
    }

    private void getCountrys() {
        CountryJpaController countryJpaController = new CountryJpaController();
        countryList = countryJpaController.findCountryEntities();
        countryEducationList = countryList;
        countryExperienceList = countryList;
    }

    private void getStates() {
        StateJpaController stateJpaController = new StateJpaController();
        stateList = stateJpaController.findStatesByCountryId(countryId);
    }

    private void getCitys() {
        CityJpaController cityJpaController = new CityJpaController();
        cityList = cityJpaController.findCitiesByStateId(stateId);
    }

    public void loadState() {
        try {
            StateJpaController stateJpaController = new StateJpaController();
            stateList = stateJpaController.findStatesByCountryId(countryId);
            cityId = 0;
            stateId = 0;
            cityList = new ArrayList<>();
        } catch (Exception e) {
        }
    }

    public void loadStateEducations() {
        try {
            StateJpaController stateJpaController = new StateJpaController();
            stateEducationList = stateJpaController.findStatesByCountryId(countryEducationId);
            stateEducationId = 0;
        } catch (Exception e) {
        }
    }

    public void loadStateExperiences() {
        try {
            StateJpaController stateJpaController = new StateJpaController();
            stateExperienceList = stateJpaController.findStatesByCountryId(countryExperienceId);
            stateExperienceId = 0;
        } catch (Exception e) {
        }
    }

    public void loadCity() {
        try {
            CityJpaController cityJpaController = new CityJpaController();
            cityList = cityJpaController.findCitiesByStateId(stateId);
            cityId = 0;
        } catch (Exception e) {
        }
    }

    public void loadOccupations() {
        try {
            if (socialProfile.getOccupationsId().getOccupationsTypeId() != null) {
                if (socialProfile.getOccupationsId().getOccupationsTypeId().getId() == 0) {
                    socialProfile.getOccupationsId().setName("");
                }
            }

            //occupations = convertAutoCompleteOccupations(occupationsBO.findOccupationsByType(socialProfile.getOccupationsId().getOccupationsTypeId().getId()));
            occupations = convertAutoCompleteOccupations(OccupationsBO.getAll());
            //socialProfile.getOccupationsId().setName("");
        } catch (Exception e) {
        }
    }

    private void getAllOccupationsType() {
        OccupationsTypeJpaController occupationsTypeJpaController = new OccupationsTypeJpaController();
        occupationsTypeList = occupationsTypeJpaController.findOccupationsTypeEntities();
    }

    private void getAllBooks() {
        booksListAll = InterestsBO.findInterestsByInterestsTypeName("Books");
    }

    private void getAllMusics() {
        musicsListAll = InterestsBO.findInterestsByInterestsTypeName("Musics");
    }

    private void getAllMovies() {
        moviesListAll = InterestsBO.findInterestsByInterestsTypeName("Movies");
    }

    private void getAllSports() {
        sportsListAll = InterestsBO.findInterestsByInterestsTypeName("Sports");
    }

    private void getAllHobbies() {
        hobbiesListAll = InterestsBO.findInterestsByInterestsTypeName("Hobbies");
    }

    private void getAllScholarity() {
        ScholarityJpaController scholarityJpaController = new ScholarityJpaController();
        scholarityList = scholarityJpaController.findScholarityEntities();
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
        }
    }

    public void removeExperience(Experiences exp) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            experiencesList.remove(exp);
            ExperiencesBO.removeExperience(exp);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, trans.getWord("Experiência removida com sucesso!"), null));
        } catch (Exception e) {
        }
    }

    public void removeEducation(Educations edu) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            educationsList.remove(edu);
            EducationsBO.removeEducation(edu);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, trans.getWord("Educação removida com sucesso!"), null));
        } catch (Exception e) {
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
