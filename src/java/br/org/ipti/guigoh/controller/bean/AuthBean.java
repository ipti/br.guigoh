/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import com.guigoh.bo.EmailActivationBO;
import com.guigoh.bo.SocialProfileBO;
import com.guigoh.bo.UsersBO;
import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.util.MD5Generator;
import br.org.ipti.guigoh.util.MailService;
import br.org.ipti.guigoh.util.RandomGenerator;
import br.org.ipti.guigoh.util.translator.Translator;
import br.org.ipti.guigoh.model.entity.EmailActivation;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.Users;
import java.io.Serializable;
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
@ManagedBean(name = "authBean")
public class AuthBean implements Serializable {

    public static final String SALT = "8g9erh9gejh";
    private Users user;
    private String locale;
    private String loginStatus;
    private String email;
    private String secretAnswer;
    private Users userToRecover;
    private String password;
    private String passwordConfirm;
    private Translator trans;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            user = new Users();
            locale = CookieService.getCookie("locale") != null ? CookieService.getCookie("locale") : "ptBR";
            trans = new Translator();
            trans.setLocale(locale);
            loginStatus = "login";
            userToRecover = new Users();
            email = "";
        }
    }

    public String login() {
        Users registeredUser = UsersBO.findUsers(user);
        user.setPassword(MD5Generator.generate(user.getPassword() + SALT));
        if (user.getPassword().equals(registeredUser.getPassword()) && registeredUser.getStatus().equals("CA")) {
            switch (registeredUser.getAuthorization().getStatus()) {
                case "AC":
                    CookieService.addCookie("user", registeredUser.getUsername());
                    CookieService.addCookie("token", registeredUser.getToken());
                    return "islogged";
                case "PC":
                    loginStatus = "pending";
                    return "";
                case "IC":
                    loginStatus = "inactive";
                    return "";
                default:
                    CookieService.addCookie("user", registeredUser.getUsername());
                    CookieService.addCookie("token", registeredUser.getToken());
                    return "wizard";
            }
        } else if (user.getPassword().equals(registeredUser.getPassword()) && registeredUser.getStatus().equals("CP")) {
            loginStatus = "check_email";
            return "";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, trans.getWord("Login incorreto!"), null));
            return "";
        }
    }

    public String logout() {
        CookieService.eraseCookie();
        return "logout";
    }

    public void recoverAccount(String status) {
        loginStatus = status;
    }

    public String sendPassToEmail() {
        try {
            userToRecover = UsersBO.findUsers(email);
            if (userToRecover.getUsername() != null && userToRecover.getStatus().equals("CA")) {
                EmailActivation emailactivation = new EmailActivation();
                emailactivation.setUsername(userToRecover.getUsername());
                emailactivation.setCode(MD5Generator.generate(userToRecover.getUsername() + RandomGenerator.generate(5)));
                SocialProfile socialProfile = SocialProfileBO.findSocialProfile(userToRecover.getToken());
                String mailText = trans.getWord("Olá, ") + socialProfile.getName().split(" ")[0] + trans.getWord("!Recebemos uma solicitação de recuperação de conta através desse e-mail. Se não foi você quem solicitou, ignore esta mensagem. Para concluir o processo, será preciso que você clique no link abaixo. Após ser redirecionado, altere sua senha imediatamente.") + "http://artecomciencia.guigoh.com/users/confirmEmail.xhtml?code=" + emailactivation.getCode() + "&user=" + userToRecover.getUsername();
                MailService.sendMail(mailText, trans.getWord("Recuperação de conta"), userToRecover.getUsername());
                EmailActivationBO.create(emailactivation);
                loginStatus = "pass_sent";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("E-mail não cadastrado/autorizado no Guigoh."), null));
            }
        } catch (EmailException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Não foi possível enviar este e-mail. Verifique sua conexão."), null));
        }
        return "";
    }

    public String loadQuestion() {
        userToRecover = UsersBO.findUsers(email);
        if (userToRecover.getUsername() != null) {
            loginStatus = "question";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("E-mail incorreto!"), null));
        }
        return "";
    }

    public String checkAnswer() {
        if (secretAnswer.equals(userToRecover.getSecretAnswer())) {
            loginStatus = "success";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Resposta incorreta!"), null));
        }
        return "";
    }

    public String changePassword() throws Exception {
        if (password.equals(passwordConfirm)) {
            userToRecover.setPassword(MD5Generator.generate(password + SALT));
            UsersBO.edit(userToRecover);
            return "logout";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, trans.getWord("Não foi possível alterar a senha. Os dois campos devem ser iguais."), null));
            return "";
        }

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
}
