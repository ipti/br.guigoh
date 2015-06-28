/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.EmailActivation;
import br.org.ipti.guigoh.model.entity.Networks;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.UserAuthorization;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.EmailActivationJpaController;
import br.org.ipti.guigoh.model.jpa.controller.NetworksJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UserAuthorizationJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UsersJpaController;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.util.MD5Generator;
import br.org.ipti.guigoh.util.MailService;
import br.org.ipti.guigoh.util.RandomGenerator;
import br.org.ipti.guigoh.util.translator.Translator;
import java.io.Serializable;
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
@ManagedBean(name = "loginBean")
public class LoginBean implements Serializable {

    public static final String SALT = "8g9erh9gejh";
    public static final String CONFIRMATION_ACCESS = "CA", DEFAULT = "DE", PENDING_ACCESS = "PC";

    private Users user, userToRecover;
    private Translator trans;

    private String locale, loginStatus, email, secretAnswer, password, passwordConfirm,
            confirmCode, confirmEmail;

    private UsersJpaController usersJpaController;
    private EmailActivationJpaController emailActivationJpaController;

    public void init() throws Exception {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
            if (confirmCode != null && confirmEmail != null) {
                authenticateUser();
            }
        }
    }

    public String login() {
        Users registeredUser = usersJpaController.findUsers(user.getUsername());
        user.setPassword(MD5Generator.generate(user.getPassword() + SALT));
        if (registeredUser != null && user.getPassword().equals(registeredUser.getPassword()) && registeredUser.getStatus().equals("CA")) {
            switch (registeredUser.getAuthorization().getStatus()) {
                case "AC":
                    CookieService.addCookie("user", registeredUser.getUsername());
                    CookieService.addCookie("token", registeredUser.getToken());
                    return "islogged";
                case "PC":
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Sua conta já foi solicitada. Aguarde a confirmação do administrador."), null));
                    return "";
                case "IC":
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Sua conta está desativada. Contate o administrador."), null));
                    return "";
                default:
                    CookieService.addCookie("user", registeredUser.getUsername());
                    CookieService.addCookie("token", registeredUser.getToken());
                    return "wizard";
            }
        } else if (registeredUser != null && user.getPassword().equals(registeredUser.getPassword()) && registeredUser.getStatus().equals("CP")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Confirme seu registro através do seu e-mail."), null));
            return "";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Login incorreto!"), null));
            return "";
        }
    }

    public void recoverAccount(String status) {
        loginStatus = status;
    }

    public String sendPassToEmail() throws RollbackFailureException, Exception {
        try {
            userToRecover = usersJpaController.findUsers(email);
            if (userToRecover != null && userToRecover.getStatus().equals("CA")) {
                EmailActivation emailactivation = new EmailActivation();
                emailactivation.setUsername(userToRecover.getUsername());
                emailactivation.setCode(MD5Generator.generate(userToRecover.getUsername() + RandomGenerator.generate(5)));
                SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
                SocialProfile socialProfile = socialProfileJpaController.findSocialProfile(userToRecover.getToken());
                String mailText = trans.getWord("Olá, ") + socialProfile.getName().split(" ")[0] + trans.getWord("!Recebemos uma solicitação de recuperação de conta através desse e-mail. Se não foi você quem solicitou, ignore esta mensagem. Para concluir o processo, será preciso que você clique no link abaixo. Após ser redirecionado, altere sua senha imediatamente.") + "http://artecomciencia.guigoh.com/auth/login.xhtml?code=" + emailactivation.getCode() + "&user=" + userToRecover.getUsername();
                MailService.sendMail(mailText, trans.getWord("Recuperação de conta"), userToRecover.getUsername());
                if (emailActivationJpaController.findEmailActivationByUsername(userToRecover.getUsername()).getUsername() != null) {
                    emailActivationJpaController.edit(emailactivation);
                } else {
                    emailActivationJpaController.create(emailactivation);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, trans.getWord("Verifique o seu e-mail."), null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("E-mail não cadastrado/autorizado no Guigoh."), null));
            }
        } catch (EmailException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Não foi possível enviar este e-mail. Verifique sua conexão."), null));
        }
        return "";
    }

    public String loadQuestion() {
        userToRecover = usersJpaController.findUsers(email);
        if (userToRecover != null) {
            loginStatus = "question";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("E-mail incorreto!"), null));
        }
        return "";
    }

    public String checkAnswer() {
        if (secretAnswer.toUpperCase().equals(userToRecover.getSecretAnswer().toUpperCase())) {
            loginStatus = "change_password";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Resposta incorreta!"), null));
        }
        return "";
    }

    public String changePassword() throws Exception {
        if (password.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Os campos abaixo não podem estar vazios."), null));
            return "";
        } else if (password.equals(passwordConfirm)) {
            if (confirmEmail != null && confirmCode != null) {
                userToRecover = usersJpaController.findUsers(confirmEmail);
                emailActivationJpaController.destroy(emailActivationJpaController.findEmailActivationByUsername(confirmEmail).getUsername());
            }
            userToRecover.setPassword(MD5Generator.generate(password + SALT));
            usersJpaController.edit(userToRecover);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, trans.getWord("Senha alterada com sucesso!"), null));
            return "logout";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Não foi possível alterar a senha. Os dois campos devem ser iguais."), null));
            return "";
        }
    }

    public void authenticateUser() throws RollbackFailureException, Exception {
        Translator tempTrans = new Translator();
        tempTrans.setLocale(CookieService.getCookie("locale"));
        NetworksJpaController networksJpaController = new NetworksJpaController();
        UserAuthorizationJpaController userAuthorizationJpaController = new UserAuthorizationJpaController();
        try {
                Users userConfirm = usersJpaController.findUsers(confirmEmail);
                EmailActivation emailActivation = emailActivationJpaController.findEmailActivationByUsername(userConfirm.getUsername());
                if (emailActivation.getUsername() != null) {
                    if (userConfirm.getStatus().equals("CA")) {
                        if (emailActivation.getCode().equals(confirmCode)) {
                            loginStatus = "change_password";
                        }
                    } else if (emailActivation.getCode().equals(confirmCode)) {
                        userConfirm.setStatus(CONFIRMATION_ACCESS);
                        usersJpaController.edit(userConfirm);
                        emailActivationJpaController.destroy(emailActivation.getUsername());
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
                        //mailtext += "http://rts.guigoh.com:8080/auth/login.xhtml?code=" + emailactivation.getCode() + "&user=" + emailactivation.getUsername();
                        //mailtext += "http://artecomciencia.guigoh.com/auth/login.xhtml?code=" + emailactivation.getCode() + "&user=" + user.getUsername();
                        //Modificar http://artecomciencia.guigoh.com/auth/login.xhtml?code=codigo&user=usuario                                
                        //newUserAccount = trans.getWord(newUserAccount);
                        for (UserAuthorization userAuthorization : userAuthorizationJpaController.findAuthorizationsByRole("AD")) {
                            //tempTrans.setLocale(userAuthorization.getUsers().getSocialProfile().getLanguageId().getAcronym());
                            //newUserAccount = tempTrans.getWord(newUserAccount);
                            //mailtext = tempTrans.getWord(mailtext);
                            MailService.sendMail(mailtext, newUserAccount, userAuthorization.getUsers().getUsername());
                        }
                        //tempTrans.setLocale(CookieService.getCookie("locale"));
                        authorization.setStatus(PENDING_ACCESS);
//                        }
                        userAuthorizationJpaController.edit(authorization);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, trans.getWord("E-mail autorizado com sucesso!"), null));
                    }
                } 
        } catch (EmailException e) {
        }
    }

    private void initGlobalVariables() {
        usersJpaController = new UsersJpaController();
        emailActivationJpaController = new EmailActivationJpaController();

        locale = CookieService.getCookie("locale") != null ? CookieService.getCookie("locale") : "ptBR";

        user = userToRecover = new Users();
        trans = new Translator();
        trans.setLocale(locale);

        loginStatus = "login";
        email = "";
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public Users getUserToRecover() {
        return userToRecover;
    }

    public void setUserToRecover(Users userToRecover) {
        this.userToRecover = userToRecover;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
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

}
