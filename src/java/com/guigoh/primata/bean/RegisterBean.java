/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.AuthorizationBO;
import com.guigoh.primata.bo.CityBO;
import com.guigoh.primata.bo.CountryBO;
import com.guigoh.primata.bo.EmailActivationBO;
import com.guigoh.primata.bo.LanguageBO;
import com.guigoh.primata.bo.NetworksBO;
import com.guigoh.primata.bo.SecretQuestionBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.bo.StateBO;
import com.guigoh.primata.bo.SubnetworkBO;
import com.guigoh.primata.bo.UsersBO;
import com.guigoh.primata.bo.util.MailService;
import com.guigoh.primata.entity.Authorization;
import com.guigoh.primata.entity.City;
import com.guigoh.primata.entity.Country;
import com.guigoh.primata.entity.EmailActivation;
import com.guigoh.primata.entity.Language;
import com.guigoh.primata.entity.Networks;
import com.guigoh.primata.entity.SecretQuestion;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.State;
import com.guigoh.primata.entity.Subnetwork;
import com.guigoh.primata.entity.Users;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Joerlan Lima
 */
@ViewScoped
@ManagedBean(name = "registerBean")
public class RegisterBean implements Serializable {

    public static final String SALT = "8g9erh9gejh";
    public static final String CONFIRMATION_PENDING = "CP";
    public static final String CONFIRMATION_ACCESS = "CA";
    public static final String DEFAULT = "DE";
    public static final String ACTIVE_ACCESS = "AC";
    public static final String INACTIVE_ACCESS = "IC";
    public static final String FIRST_ACCESS = "FC";
    public static final String PENDING_ACCESS = "PC";
    public static final String PUBLIC = "PU";
    public static final String PRIVATE = "PR";
    public static final String BRAZIL = "Brasil";
    public static final String SERGIPE = "Sergipe";
    public static final String ARACAJU = "Aracaju";
    private Users user;
    private SocialProfile socialProfile;
    private SecretQuestion secretQuestion;
    private List<SecretQuestion> questionsList;
    private List<State> stateList;
    private List<Country> countryList;
    private List<City> cityList;
    private List<Language> languageList;
    private List<Subnetwork> subnetworkList;
    private String usernameConfirm;
    private String passwordConfirm;
    private Integer countryId;
    private Integer stateId;
    private Integer cityId;
    private Integer subnetworkId;
    private Integer languageId;
    private String confirmCode;
    private String confirmEmail;
    private String lastName;
    private String panelStatus;
    private String newPassword;
    private String newPasswordConfirm;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            user = new Users();
            socialProfile = new SocialProfile();
            secretQuestion = new SecretQuestion();
            questionsList = new ArrayList<SecretQuestion>();
            questionsList = getQuestions();
            stateList = new ArrayList<State>();
            countryList = getCountries();
            cityList = new ArrayList<City>();
            languageList = getLanguages();
            subnetworkList = getSubnetworks();
            usernameConfirm = "";
            passwordConfirm = "";
            countryId = 0;
            stateId = 0;
            cityId = 0;
            subnetworkId = 0;
            languageId = 0;
            confirmCode = "";
            confirmEmail = "";
            lastName = "";
            panelStatus = "";
            loadDefault();
        }
    }

    private void loadDefault() {
        CountryBO countryBO = new CountryBO();
        countryId = countryBO.getCountryByName(BRAZIL).getId();

        StateBO stateBO = new StateBO();
        stateList = stateBO.findStatesByCountryId(countryId);
        stateId = stateBO.getStateByName(SERGIPE).getId();

        CityBO cityBO = new CityBO();
        cityList = cityBO.findCitiesByStateId(stateId);
        cityId = cityBO.getCityByName(ARACAJU).getId();

    }

    public String register() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        Boolean registered = false;
        try {
            UsersBO uBO = new UsersBO();
            Users usertest = uBO.findUsers(user);
            if (usertest.getToken() != null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário já existe.", null));
            } else {
                if (user.getUsername() != null && user.getPassword() != null && socialProfile.getName() != null
                        && languageId != 0 && subnetworkId != 0 && secretQuestion.getId() != 0
                        && !(user.getSecretAnswer().equals("")) && countryId != 0 && stateId != 0 && cityId != 0) {
                    if (!user.getUsername().equals("") && !user.getPassword().equals("")) {
                        if (usernameConfirm.equals(user.getUsername())) {

                            if (passwordConfirm.equals(user.getPassword())) {
                                user.setToken(md5(user.getUsername() + user.getPassword() + SALT));

                                user.setPassword(md5(user.getPassword() + SALT));
                                user.setSecretQuestionId(secretQuestion);
                                user.setStatus(CONFIRMATION_PENDING);

                                EmailActivation emailactivation = new EmailActivation();
                                emailactivation.setUsername(user.getUsername());
                                emailactivation.setCode(md5(user.getUsername() + random(5)));
                                EmailActivationBO emailActivationBO = new EmailActivationBO();


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
                                Language language = new Language();
                                language.setId(languageId);
                                socialProfile.setLanguageId(language);
                                Subnetwork subnetwork = new Subnetwork();
                                subnetwork.setId(subnetworkId);
                                socialProfile.setSubnetworkId(subnetwork);
                                socialProfile.setTokenId(user.getToken());
                                socialProfile.setPhoto("/resources/images/avatar.png");
                                socialProfile.setName(socialProfile.getName() + " " + lastName);
                                //Modificar http://artecomciencia.guigoh.com/primata/users/confirmEmail.xhtml?code=codigo&user=usuario
                                String mailtext = "Olá " + socialProfile.getName() + "\n"
                                        + "\n"
                                        + "Obrigado pelo seu interesse em se registrar no Arte com ciência.\n"
                                        + "\n"
                                        + "Para concluir o processo será preciso que você clique no link abaixo para confirmar seu interesse.\n"
                                        + "\n http://artecomciencia.guigoh.com/primata/users/confirmEmail.xhtml?code=" + emailactivation.getCode() + "&user=" + user.getUsername();
                                //MailService.sendMail(mailtext, "Ativação de Conta", user.getUsername());
                                //Linha alterada
                                user.setStatus(CONFIRMATION_ACCESS);
                                uBO.create(user);
                                automaticConfirm(user);
                                socialProfileBO.create(socialProfile);
                                //emailActivationBO.create(emailactivation);

                                //context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário Registrado com sucesso!", null));
                                registered = true;
                            } else {

                                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Os campos 'Senha' e 'Confirme senha' devem ser iguais", null));
                                return "";
                            }
                        } else {
                            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Os campos 'E-mail' e 'Confirme e-mail' devem ser iguais", null));
                            return "";
                        }
                    }
                }
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível realizar o cadastro. Verifique os campos abaixo.", null));
            e.printStackTrace();
            return "";
        }
        if (registered) {
            //context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário Registrado com sucesso!. Verifique o email cadastro para ativação do usuário, clique em voltar para ir para tela de login.", null));
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário Registrado com sucesso!. Clique em voltar para ir para tela de login.", null));
            user = new Users();
            socialProfile = new SocialProfile();
            usernameConfirm = "";
            passwordConfirm = "";
            return "";
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível realizar o cadastro. Verifique os campos abaixo.", null));
            return "";
        }
    }

    private void automaticConfirm(Users user) {
        user.setStatus(CONFIRMATION_ACCESS);

        NetworksBO networksBO = new NetworksBO();
        List<Networks> networksList = networksBO.getAll();
        Authorization authorization = new Authorization();
        authorization.setRoles(DEFAULT);
        authorization.setTokenId(user.getToken());
        //Refazer
        if (networksList.size() > 2) {
            authorization.setNetwork("Guigoh");
        } else {
            authorization.setNetwork(networksList.get(0).getName());
        }

        if (networksList.size() > 2 | networksList.get(0).getType().equals(PUBLIC)) {
            authorization.setStatus(FIRST_ACCESS);
        } else if (networksList.get(0).getType().equals(PRIVATE)) {
            authorization.setStatus(PENDING_ACCESS);
        }
        //
        AuthorizationBO authorizationBO = new AuthorizationBO();
        authorizationBO.create(authorization);
    }

    public String backToLogin() {
        return "logout";
    }

    public String changePassword() throws Exception {
        UsersBO uBO = new UsersBO();
        EmailActivationBO eBO = new EmailActivationBO();
        EmailActivation eA = eBO.findEmailActivationByUsername(confirmEmail);
        Users userToRecover = uBO.findUsers(confirmEmail);
        if (newPassword.equals(newPasswordConfirm)) {
            userToRecover.setPassword(md5(newPassword + SALT));
            uBO.edit(userToRecover);
            eBO.destroy(eA);
            return "logout";
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível alterar a senha, digite os dois campos iguais.", null));
            return "";
        }
    }

    private String random(int max) {
        Random random = new Random(System.currentTimeMillis());
        String stemp = "";
        for (int i = 0; i < max; ++i) {
            stemp += random.nextInt(10);
        }
        return stemp;
    }

    public List<SecretQuestion> getQuestions() {
        SecretQuestionBO secretQuestionBO = new SecretQuestionBO();
        return secretQuestionBO.getAll();
    }

    private List<Country> getCountries() {
        CountryBO countryBO = new CountryBO();
        return countryBO.getAll();
    }

    private List<Language> getLanguages() {
        LanguageBO languageBO = new LanguageBO();
        return languageBO.getAll();
    }

    private List<Subnetwork> getSubnetworks() {
        SubnetworkBO subnetworkBO = new SubnetworkBO();
        return subnetworkBO.getAll();
    }

    public static String md5(String input) {
        String md5 = null;
        if (null == input) {
            return null;
        }
        try {
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");
            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());
            //Converts message digest value in base 16 (hex) 
            md5 = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    public void loadState() {
        StateBO stateBO = new StateBO();
        stateList = stateBO.findStatesByCountryId(countryId);
    }

    public void loadCity() {
        CityBO cityBO = new CityBO();
        cityList = cityBO.findCitiesByStateId(stateId);
    }

    public void authenticateUser() {
        try {
            UsersBO usersBO = new UsersBO();
            AuthorizationBO authorizationBO = new AuthorizationBO();
            EmailActivationBO emailActivationBO = new EmailActivationBO();
            if (confirmEmail != null && confirmCode != null) {
                Users userConfirm = usersBO.findUsers(confirmEmail);
                EmailActivation emailActivation = emailActivationBO.findEmailActivationByUsername(userConfirm.getUsername());
                if (emailActivation.getUsername() != null) {
                    if (userConfirm.getStatus().equals("CA")) {
                        if (emailActivation.getCode().equals(confirmCode)) {
                            panelStatus = "new_pass";
                        }
                    } else if (userConfirm != null) {
                        if (emailActivation.getCode().equals(confirmCode)) {
                            userConfirm.setStatus(CONFIRMATION_ACCESS);
                            usersBO.edit(userConfirm);

                            emailActivationBO.destroy(emailActivation);

                            NetworksBO networksBO = new NetworksBO();
                            List<Networks> networksList = networksBO.getAll();
                            Authorization authorization = new Authorization();
                            authorization.setRoles(DEFAULT);
                            authorization.setTokenId(userConfirm.getToken());
                            //Refazer
                            if (networksList.size() > 2) {
                                authorization.setNetwork("Guigoh");
                            } else {
                                authorization.setNetwork(networksList.get(0).getName());
                            }

                            if (networksList.size() > 2 | networksList.get(0).getType().equals(PUBLIC)) {
                                authorization.setStatus(FIRST_ACCESS);
                            } else if (networksList.get(0).getType().equals(PRIVATE)) {
                                authorization.setStatus(PENDING_ACCESS);
                            }
                            //
                            authorizationBO.create(authorization);

                            panelStatus = "confirmed_email";
                        }
                    }
                } else {
                    panelStatus = "active_email";
                }
            } else {
                Users users = getUserCookie();
                if (users.getStatus() != null) {
                    if (users.getStatus().equals("CP")) {
                        panelStatus = "check_email";
                    } else {
                        Authorization authorization = authorizationBO.findAuthorizationByTokenId(users.getToken());
                        if (authorization.getStatus().equals("IC")) {
                            panelStatus = "inactive";
                        } else if (authorization.getStatus().equals("PC")) {
                            panelStatus = "pending";
                        }
                    }
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
                    HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
                    eraseCookie(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Users getUserCookie() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().trim().equalsIgnoreCase("user")) {
                    UsersBO usersBO = new UsersBO();
                    user = usersBO.findUsers(cookie.getValue());
                }

            }
        }
        return user;
    }

    private void eraseCookie(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                cookies[i].setValue("");
                cookies[i].setPath("/");
                cookies[i].setMaxAge(0);
                cookies[i].setDomain(".guigoh.com");
                resp.addCookie(cookies[i]);
            }
        }
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<SecretQuestion> getQuestionsList() {
        return questionsList;
    }

    public void setQuestionsList(List<SecretQuestion> questions) {
        this.questionsList = questions;
    }

    public SecretQuestion getSecretQuestion() {
        return secretQuestion;
    }

    public void setSecretQuestion(SecretQuestion secretQuestion) {
        this.secretQuestion = secretQuestion;
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

    public SocialProfile getSocialProfile() {
        return socialProfile;
    }

    public void setSocialProfile(SocialProfile socialProfile) {
        this.socialProfile = socialProfile;
    }

    public String getUsernameConfirm() {
        return usernameConfirm;
    }

    public void setUsernameConfirm(String usernameConfirm) {
        this.usernameConfirm = usernameConfirm;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    public List<Language> getLanguageList() {
        return languageList;
    }

    public void setLanguageList(List<Language> languageList) {
        this.languageList = languageList;
    }

    public List<Subnetwork> getSubnetworkList() {
        return subnetworkList;
    }

    public void setSubnetworkList(List<Subnetwork> subnetworkList) {
        this.subnetworkList = subnetworkList;
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

    public Integer getSubnetworkId() {
        return subnetworkId;
    }

    public void setSubnetworkId(Integer subnetworkId) {
        this.subnetworkId = subnetworkId;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public String getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPanelStatus() {
        return panelStatus;
    }

    public void setPanelStatus(String panelStatus) {
        this.panelStatus = panelStatus;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }
}
