/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipti.guigoh.controller.bean;

import com.guigoh.bo.EmailActivationBO;
import com.guigoh.bo.RoleBO;
import com.guigoh.bo.SocialProfileBO;
import com.guigoh.bo.UserAuthorizationBO;
import com.guigoh.bo.UsersBO;
import com.ipti.guigoh.util.CookieService;
import com.ipti.guigoh.util.MD5Generator;
import com.ipti.guigoh.util.MailService;
import com.ipti.guigoh.util.RandomGenerator;
import com.ipti.guigoh.util.translator.Translator;
import com.ipti.guigoh.model.entity.City;
import com.ipti.guigoh.model.entity.Country;
import com.ipti.guigoh.model.entity.EmailActivation;
import com.ipti.guigoh.model.entity.Language;
import com.ipti.guigoh.model.entity.Networks;
import com.ipti.guigoh.model.entity.Role;
import com.ipti.guigoh.model.entity.SecretQuestion;
import com.ipti.guigoh.model.entity.SocialProfile;
import com.ipti.guigoh.model.entity.State;
import com.ipti.guigoh.model.entity.Subnetwork;
import com.ipti.guigoh.model.entity.UserAuthorization;
import com.ipti.guigoh.model.entity.Users;
import com.ipti.guigoh.model.jpa.controller.CityJpaController;
import com.ipti.guigoh.model.jpa.controller.CountryJpaController;
import com.ipti.guigoh.model.jpa.controller.LanguageJpaController;
import com.ipti.guigoh.model.jpa.controller.NetworksJpaController;
import com.ipti.guigoh.model.jpa.controller.SecretQuestionJpaController;
import com.ipti.guigoh.model.jpa.controller.StateJpaController;
import com.ipti.guigoh.model.jpa.controller.SubnetworkJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.mail.EmailException;

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
    private List<Role> roleList;
    private List<Subnetwork> subnetworkList;
    private String usernameConfirm;
    private String passwordConfirm;
    private Integer countryId;
    private Integer stateId;
    private Integer cityId;
    private Integer roleId;
    private Integer subnetworkId;
    private Integer languageId;
    private String confirmCode;
    private String confirmEmail;
    private String lastName;
    private String panelStatus;
    private String newPassword;
    private String newPasswordConfirm;
    private Boolean visitor;
    private Translator trans;

    private CityJpaController cityJpaController;
    private StateJpaController stateJpaController;
    private CountryJpaController countryJpaController;
    private LanguageJpaController languageJpaController;
    private NetworksJpaController networksJpaController;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            cityJpaController = new CityJpaController();
            stateJpaController = new StateJpaController();
            countryJpaController = new CountryJpaController();
            languageJpaController = new LanguageJpaController();
            networksJpaController = new NetworksJpaController();
            user = new Users();
            socialProfile = new SocialProfile();
            secretQuestion = new SecretQuestion();
            questionsList = new ArrayList<>();
            questionsList = getQuestions();
            stateList = new ArrayList<>();
            countryList = getCountries();
            cityList = new ArrayList<>();
            roleList = new ArrayList<>();
            languageList = getLanguages();
            subnetworkList = getSubnetworks();
            usernameConfirm = "";
            passwordConfirm = "";
            countryId = 0;
            stateId = 0;
            roleId = 0;
            subnetworkId = 0;
            languageId = 0;
            confirmCode = "";
            confirmEmail = "";
            lastName = "";
            panelStatus = "";
            visitor = true;
            trans = new Translator();
            trans.setLocale(CookieService.getCookie("locale"));
            loadDefault();

        }
    }

    private void loadDefault() {
        countryId = countryJpaController.findCountryByName(BRAZIL).getId();
        stateList = stateJpaController.findStatesByCountryId(countryId);
        stateId = stateJpaController.findStateByName(SERGIPE).getId();
        cityList = cityJpaController.findCitiesByStateId(stateId);
        cityId = cityJpaController.findCityByName(ARACAJU).getId();
        roleList = RoleBO.getAll();

    }

    public void register() throws Exception {
        try {
            Users userTest = UsersBO.findUsers(user);
            if (userTest.getToken() != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Usuário já existe."), null));
            } else {
                if (user.getUsername() != null && user.getPassword() != null && !socialProfile.getName().equals("")
                        && !lastName.equals("") && languageId != 0 && secretQuestion.getId() != 0
                        && !(user.getSecretAnswer().equals("")) && countryId != 0 && stateId != 0 && cityId != 0) {
                    if (usernameConfirm.equals(user.getUsername())) {
                        if (passwordConfirm.equals(user.getPassword())) {
                            user.setToken(MD5Generator.generate(user.getUsername() + user.getPassword() + SALT));
                            user.setPassword(MD5Generator.generate(user.getPassword() + SALT));
                            user.setSecretQuestionId(secretQuestion);
                            Country country = new Country();
                            State state = new State();
                            City city = new City();
                            Subnetwork subnetwork = new Subnetwork();
                            Language language = new Language();
                            Role role = new Role();
                            EmailActivation emailactivation = new EmailActivation();
                            emailactivation.setUsername(user.getUsername());
                            emailactivation.setCode(MD5Generator.generate(user.getUsername() + RandomGenerator.generate(5)));
                            country.setId(countryId);
                            socialProfile.setCountryId(country);
                            state.setId(stateId);
                            socialProfile.setStateId(state);
                            city.setId(cityId);
                            socialProfile.setCityId(city);
                            language.setId(languageId);
                            socialProfile.setLanguageId(language);
                            subnetwork.setId(subnetworkId);
                            role.setId(roleId);
                            if (subnetworkId != 0) {
                                socialProfile.setSubnetworkId(subnetwork);
                            }
                            if (roleId != 0) {
                                socialProfile.setRoleId(role);
                            }
                            socialProfile.setTokenId(user.getToken());
                            socialProfile.setPhoto("/resources/images/avatar.png");
                            socialProfile.setName(socialProfile.getName() + " " + lastName);
                            String accountActivation = "Ativação de Conta";
                            String mailtext = "Olá!\n\nObrigado pelo seu interesse em se registrar no Arte com Ciência.\n\nPara concluir o processo será preciso que você clique no link abaixo para ativar sua conta.\n\n";
                            trans.setLocale(languageJpaController.findLanguage(languageId).getAcronym());
                            mailtext = trans.getWord(mailtext);
                            mailtext += "http://artecomciencia.guigoh.com/users/confirmEmail.xhtml?code=" + emailactivation.getCode() + "&user=" + emailactivation.getUsername();
//                            mailtext += "http://rts.guigoh.com:8080/users/confirmEmail.xhtml?code=" + emailactivation.getCode() + "&user=" + emailactivation.getUsername();
                            accountActivation = trans.getWord(accountActivation);
                            MailService.sendMail(mailtext, accountActivation, emailactivation.getUsername());
                            trans.setLocale(CookieService.getCookie("locale"));
                            user.setStatus(CONFIRMATION_PENDING);
                            UsersBO.create(user);
                            EmailActivationBO.create(emailactivation);
                            SocialProfileBO.create(socialProfile);
                            automaticConfirm(user);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, trans.getWord("Usuário registrado com sucesso! Clique em retornar para ir à tela de login."), null));
                            user = new Users();
                            socialProfile = new SocialProfile();
                            lastName = "";
                            usernameConfirm = "";
                            passwordConfirm = "";
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Os campos 'Senha' e 'Confirme senha' devem ser iguais."), null));
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Os campos 'E-mail' e 'Confirme e-mail' devem ser iguais."), null));
                    }

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Não foi possível realizar o cadastro. Verifique os campos abaixo."), null));
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Ocorreu um erro ao realizar o cadastro. Tente novamente."), null));
        }
    }

    public void updateRole() {
        visitor = subnetworkId == 0;
    }

    public String backToLogin() {
        CookieService.eraseCookie();
        return "logout";
    }

    private void automaticConfirm(Users user) {
        List<Networks> networksList = networksJpaController.findNetworksEntities();
        UserAuthorization authorization = new UserAuthorization();
        authorization.setRoles(DEFAULT);
        authorization.setTokenId(user.getToken());
//        if (networksList.size() > 2) {
//            authorization.setNetwork("Guigoh");
//        } else {

        authorization.setNetwork(networksList.get(0).getName());

//        }
//        if (networksList.size() > 2 | networksList.get(0).getType().equals(PUBLIC)) {
//            authorization.setStatus(FIRST_ACCESS);
//        } else if (networksList.get(0).getType().equals(PRIVATE)) {
        authorization.setStatus(PENDING_ACCESS);

//        }
        UserAuthorizationBO.create(authorization);
    }

    public String changePassword() throws Exception {
        EmailActivation emailActivation = EmailActivationBO.findEmailActivationByUsername(confirmEmail);
        Users userToRecover = UsersBO.findUsers(confirmEmail);
        if (newPassword.equals(newPasswordConfirm)) {
            userToRecover.setPassword(MD5Generator.generate(newPassword + SALT));
            UsersBO.edit(userToRecover);
            EmailActivationBO.destroy(emailActivation);
            return "logout";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Não foi possível alterar a senha. Os campos devem ser iguais."), null));
            return "";
        }
    }

    public List<SecretQuestion> getQuestions() {
        SecretQuestionJpaController secretQuestionJpaController = new SecretQuestionJpaController();
        return secretQuestionJpaController.findSecretQuestionEntities();
    }

    private List<Country> getCountries() {
        try {
            return countryJpaController.findCountryEntities();
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }

    private List<Language> getLanguages() {
        return languageJpaController.findLanguageEntities();
    }

    private List<Subnetwork> getSubnetworks() {
        SubnetworkJpaController subnetworkJpaController = new SubnetworkJpaController();
        return subnetworkJpaController.findSubnetworkEntities();
    }

    public void loadState() {
        stateList = stateJpaController.findStatesByCountryId(countryId);
    }

    public void loadCity() {
        cityList = cityJpaController.findCitiesByStateId(stateId);
    }

    public void authenticateUser() {
        Translator tempTrans = new Translator();
        tempTrans.setLocale(CookieService.getCookie("locale"));

        try {
            if (confirmEmail != null && confirmCode != null) {
                Users userConfirm = UsersBO.findUsers(confirmEmail);
                EmailActivation emailActivation = EmailActivationBO.findEmailActivationByUsername(userConfirm.getUsername());
                if (emailActivation.getUsername() != null) {
                    if (userConfirm.getStatus().equals("CA")) {
                        if (emailActivation.getCode().equals(confirmCode)) {
                            panelStatus = "new_pass";
                        }
                    } else if (emailActivation.getCode().equals(confirmCode)) {
                        userConfirm.setStatus(CONFIRMATION_ACCESS);
                        UsersBO.edit(userConfirm);
                        EmailActivationBO.destroy(emailActivation);
                        List<Networks> networksList = networksJpaController.findNetworksEntities();
                        UserAuthorization authorization = new UserAuthorization();
                        authorization.setRoles(DEFAULT);
                        authorization.setTokenId(userConfirm.getToken());
                        //Refazer
                        if (networksList.size() > 2) {
                            authorization.setNetwork("Guigoh");
                        } else {
                            authorization.setNetwork(networksList.get(0).getName());
                        }

//                        if (networksList.size() > 2 | networksList.get(0).getType().equals(PUBLIC)) {
//                            authorization.setStatus(FIRST_ACCESS);
//                        } else if (networksList.get(0).getType().equals(PRIVATE)) {
                        String newUserAccount = "Novo cadastro de usuário";
                        String mailtext = "Um novo usuário se cadastrou no Arte com Ciência e requer autorização.\n\nVisite a página de administrador para visualizar os cadastros com autorização pendente.";
                            //mailtext = trans.getWord(mailtext);
                        //mailtext += "http://rts.guigoh.com:8080/users/confirmEmail.xhtml?code=" + emailactivation.getCode() + "&user=" + emailactivation.getUsername();
                        //mailtext += "http://artecomciencia.guigoh.com/users/confirmEmail.xhtml?code=" + emailactivation.getCode() + "&user=" + user.getUsername();
                        //Modificar http://artecomciencia.guigoh.com/users/confirmEmail.xhtml?code=codigo&user=usuario                                
                        //newUserAccount = trans.getWord(newUserAccount);
                        for (UserAuthorization userAuthorization : UserAuthorizationBO.findAuthorizationsByRole("AD")) {
                                //tempTrans.setLocale(userAuthorization.getUsers().getSocialProfile().getLanguageId().getAcronym());
                            //newUserAccount = tempTrans.getWord(newUserAccount);
                            //mailtext = tempTrans.getWord(mailtext);
                            MailService.sendMail(mailtext, newUserAccount, userAuthorization.getUsers().getUsername());
                        }
                        //tempTrans.setLocale(CookieService.getCookie("locale"));
                        authorization.setStatus(PENDING_ACCESS);
//                        }
                        UserAuthorizationBO.create(authorization);
                        panelStatus = "confirmed_email";
                    }
                    //caindo sempre nesse else
                } else {
                    panelStatus = "active_email";
                }
            } else {
                //funciona sem o "Users"
                Users user = UsersBO.findUsers(CookieService.getCookie("user"));
                if (user.getStatus() != null) {
                    if (user.getStatus().equals("CP")) {
                        panelStatus = "check_email";
                    } else {
                        UserAuthorization authorization = UserAuthorizationBO.findAuthorizationByTokenId(user.getToken());
                        switch (authorization.getStatus()) {
                            case "IC":
                                panelStatus = "inactive";
                                break;
                            case "PC":
                                panelStatus = "pending";
                                break;
                        }
                    }
                    CookieService.eraseCookie();
                }
            }
        } catch (EmailException e) {
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

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public Boolean getVisitor() {
        return visitor;
    }

    public void setVisitor(Boolean visitor) {
        this.visitor = visitor;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
