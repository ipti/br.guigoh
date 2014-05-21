/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.EmailActivationBO;
import com.guigoh.primata.bo.LanguageBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.bo.UsersBO;
import com.guigoh.primata.bo.util.CookieService;
import com.guigoh.primata.bo.util.MD5Generator;
import com.guigoh.primata.bo.util.MailService;
import com.guigoh.primata.bo.util.RandomGenerator;
import com.guigoh.primata.bo.util.translator.ConfigReader;
import com.guigoh.primata.bo.util.translator.Translator;
import com.guigoh.primata.entity.EmailActivation;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.Users;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.mail.EmailException;

/**
 *
 * @author Joerlan Lima
 */
@SessionScoped
@ManagedBean(name = "authBean")
public class AuthBean implements Serializable {

    public static final String SALT = "8g9erh9gejh";
    private Users user;
    private String loginStatus;
    private String email;
    private String secretAnswer;
    private Users userToRecover;
    private String password;
    private String passwordConfirm;
    private ConfigReader cr;
    private Translator trans;
    private UsersBO uBO;
    private String message;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            user = new Users();
            cr = new ConfigReader();
            trans = new Translator();
            trans.setLocale(cr.getTag("locale"));
            loginStatus = "login";
            userToRecover = new Users();
            email = "";
            uBO = new UsersBO();
        }
    }

    public String login() {
        Users registeredUser = uBO.findUsers(user);
        user.setPassword(MD5Generator.generate(user.getPassword() + SALT));
        if (user.getPassword().equals(registeredUser.getPassword())) {
            CookieService.addCookie("user", registeredUser.getUsername());
            CookieService.addCookie("token", registeredUser.getToken());
            return "islogged";
        }
        message = "Login incorreto";
        message = trans.getWord(message);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message + "!", null));
        return "";
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
            userToRecover = uBO.findUsers(email);
            if (userToRecover != null && userToRecover.getStatus().equals("CA")) {
                EmailActivation emailactivation = new EmailActivation();
                emailactivation.setUsername(userToRecover.getUsername());
                emailactivation.setCode(MD5Generator.generate(userToRecover.getUsername() + RandomGenerator.generate(5)));
                EmailActivationBO emailActivationBO = new EmailActivationBO();
                SocialProfileBO spBO = new SocialProfileBO();
                SocialProfile socialProfile = spBO.findSocialProfile(userToRecover.getToken());
                String mailText = "Olá " + socialProfile.getName() + "!\n"
                        + "\n"
                        + "Recebemos uma solicitação de recuperação de conta através desse e-mail. Caso não tenha sido você, ignore-a.\n"
                        + "\n"
                        + "Para concluir o processo, será preciso que você clique no link abaixo.\n"
                        + "\n http://artecomciencia.guigoh.com/primata/users/confirmEmail.xhtml?code=" + emailactivation.getCode() + "&user=" + userToRecover.getUsername();
                MailService.sendMail(mailText, "Account Recovery", userToRecover.getUsername());
                emailActivationBO.create(emailactivation);
                loginStatus = "pass_sent";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "E-mail não cadastrado/autorizado no guigoh.", null));
            }
        } catch (EmailException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível enviar para este e-mail. Verifique sua conexão.", null));
        }
        return "";
    }

    public String loadQuestion() {
        userToRecover = uBO.findUsers(email);
        if (userToRecover != null) {
            loginStatus = "question";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "E-mail incorreto!", null));
        }
        return "";
    }

    public String checkAnswer() {
        if (secretAnswer.equals(userToRecover.getSecretAnswer())) {
            loginStatus = "success";
        } else {
            message = "Resposta incorreta!";
            message = trans.getWord(message);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
        }
        return "";
    }

    public String changePassword() throws Exception {
        if (password.equals(passwordConfirm)) {
            userToRecover.setPassword(MD5Generator.generate(password + SALT));
            uBO.edit(userToRecover);
            return "logout";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível alterar a senha, digite os dois campos iguais.", null));
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
