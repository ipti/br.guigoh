/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.City;
import br.org.ipti.guigoh.model.entity.Country;
import br.org.ipti.guigoh.model.entity.EmailActivation;
import br.org.ipti.guigoh.model.entity.Language;
import br.org.ipti.guigoh.model.entity.Networks;
import br.org.ipti.guigoh.model.entity.Role;
import br.org.ipti.guigoh.model.entity.SecretQuestion;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.State;
import br.org.ipti.guigoh.model.entity.Subnetwork;
import br.org.ipti.guigoh.model.entity.UserAuthorization;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.CityJpaController;
import br.org.ipti.guigoh.model.jpa.controller.CountryJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EmailActivationJpaController;
import br.org.ipti.guigoh.model.jpa.controller.LanguageJpaController;
import br.org.ipti.guigoh.model.jpa.controller.NetworksJpaController;
import br.org.ipti.guigoh.model.jpa.controller.RoleJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SecretQuestionJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.StateJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SubnetworkJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UserAuthorizationJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UsersJpaController;
import br.org.ipti.guigoh.model.jpa.exceptions.PreexistingEntityException;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.util.MD5Generator;
import br.org.ipti.guigoh.util.MailService;
import br.org.ipti.guigoh.util.RandomGenerator;
import br.org.ipti.guigoh.util.translator.Translator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@SessionScoped
@Named
public class LoginCreateBean implements Serializable {

    private static final String SALT = "8g9erh9gejh";
    private static final String CONFIRMATION_PENDING = "CP", DEFAULT = "DE",
            FIRST_ACCESS = "FC", PENDING_ACCESS = "PC",
            PUBLIC = "PU", PRIVATE = "PR";

    private Users user;
    private SocialProfile socialProfile;
    private Translator trans;

    private List<SecretQuestion> questionsList;
    private List<State> stateList;
    private List<Country> countryList;
    private List<City> cityList;
    private List<Language> languageList;
    private List<Role> roleList;
    private List<Subnetwork> subnetworkList;

    private String usernameConfirm, passwordConfirm, lastName;

    private CityJpaController cityJpaController;
    private StateJpaController stateJpaController;
    private CountryJpaController countryJpaController;
    private LanguageJpaController languageJpaController;
    private NetworksJpaController networksJpaController;
    private RoleJpaController roleJpaController;
    private EmailActivationJpaController emailActivationJpaController;
    private UserAuthorizationJpaController userAuthorizationJpaController;
    private UsersJpaController usersJpaController;

    public void init() throws Exception {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
        }
    }

    public void register() throws Exception {
        try {
            Users userTest = usersJpaController.findUsers(user.getUsername());
            if (userTest != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Usuário já existe."), null));
            } else {
                if (user.getUsername() != null && user.getPassword() != null && !socialProfile.getName().equals("")
                        && !lastName.equals("") && socialProfile.getLanguageId() != null && user.getSecretQuestionId() != null
                        && !(user.getSecretAnswer().equals("")) && socialProfile.getCountryId() != null && socialProfile.getStateId() != null && socialProfile.getCityId() != null) {
                    if (usernameConfirm.equals(user.getUsername())) {
                        if (passwordConfirm.equals(user.getPassword())) {
                            user.setToken(MD5Generator.generate(user.getUsername() + user.getPassword() + SALT));
                            user.setPassword(MD5Generator.generate(user.getPassword() + SALT));
                            EmailActivation emailactivation = new EmailActivation();
                            emailactivation.setUsername(user.getUsername());
                            emailactivation.setCode(MD5Generator.generate(user.getUsername() + RandomGenerator.generate(5)));
                            socialProfile.setTokenId(user.getToken());
                            socialProfile.setPhoto("/resources/common/images/avatar.png");
                            socialProfile.setName(socialProfile.getName() + " " + lastName);
                            String accountActivation = "Ativação de Conta";
                            String mailtext = "Olá!\n\nObrigado pelo seu interesse em se registrar no Guigoh.\n\nPara concluir o processo será preciso que você clique no link abaixo para ativar sua conta.\n\n";
                            trans.setLocale(languageJpaController.findLanguage(socialProfile.getLanguageId().getId()).getAcronym());
                            mailtext = trans.getWord(mailtext);
                            mailtext += "http://artecomciencia.guigoh.com/login/auth.xhtml?code=" + emailactivation.getCode() + "&user=" + emailactivation.getUsername();
//                            mailtext += "http://rts.guigoh.com:8080/login/auth.xhtml?code=" + emailactivation.getCode() + "&user=" + emailactivation.getUsername();
                            accountActivation = trans.getWord(accountActivation);
                            MailService.sendMail(mailtext, accountActivation, emailactivation.getUsername());
                            trans.setLocale(CookieService.getCookie("locale"));
                            user.setStatus(CONFIRMATION_PENDING);
                            usersJpaController.create(user);
                            emailActivationJpaController.create(emailactivation);
                            SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
                            socialProfileJpaController.create(socialProfile);
                            automaticConfirm(user);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, trans.getWord("Usuário registrado com sucesso!"), null));
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
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Ocorreu um erro ao realizar o cadastro. Tente novamente."), null));
        }
    }

    private void automaticConfirm(Users user) throws PreexistingEntityException, RollbackFailureException, Exception {
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
        userAuthorizationJpaController.create(authorization);
    }

    public void loadStates() {
        socialProfile.setStateId(null);
        stateList = socialProfile.getCountryId() != null ? stateJpaController.findStatesByCountryId(socialProfile.getCountryId().getId()): new ArrayList<>();
        cityList = new ArrayList<>();
    }

    public void loadCities() {
        socialProfile.setCityId(null);
        cityList = socialProfile.getStateId() != null ? cityJpaController.findCitiesByStateId(socialProfile.getStateId().getId()) : new ArrayList<>();
    }

    private void initGlobalVariables() {
        cityJpaController = new CityJpaController();
        stateJpaController = new StateJpaController();
        countryJpaController = new CountryJpaController();
        languageJpaController = new LanguageJpaController();
        networksJpaController = new NetworksJpaController();
        roleJpaController = new RoleJpaController();
        emailActivationJpaController = new EmailActivationJpaController();
        userAuthorizationJpaController = new UserAuthorizationJpaController();
        usersJpaController = new UsersJpaController();
        SubnetworkJpaController subnetworkJpaController = new SubnetworkJpaController();
        SecretQuestionJpaController secretQuestionJpaController = new SecretQuestionJpaController();
        
        user = new Users();
        socialProfile = new SocialProfile();
        trans = new Translator();
        
        questionsList = secretQuestionJpaController.findSecretQuestionEntities();
        countryList = countryJpaController.findCountryEntities();
        languageList = languageJpaController.findLanguageEntities();
        subnetworkList = subnetworkJpaController.findSubnetworkEntities();
        roleList = roleJpaController.findRoleEntities();
        
        stateList = new ArrayList<>();
        cityList = new ArrayList<>();
        
        usernameConfirm = passwordConfirm = lastName = "";
        
        trans.setLocale(CookieService.getCookie("locale"));
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
}
