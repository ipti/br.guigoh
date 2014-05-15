/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.EmailActivationBO;
import com.guigoh.primata.bo.LanguageBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.bo.UsersBO;
import com.guigoh.primata.bo.util.MailService;
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

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            user = new Users();
            cr = new ConfigReader();
            trans = new Translator();
            trans.setLocale(cr.getTag("locale"));
            loginStatus = "login";
            userToRecover = new Users();
            email = "";
        }
    }

    public AuthBean() {
    }

    public String login() {
        UsersBO uBO = new UsersBO();
        Users utemp = uBO.findUsers(user);
        if (utemp != null) {
            String salt = "8g9erh9gejh";
            user.setToken(md5(user.getUsername() + user.getPassword() + salt));
            user.setPassword(md5(user.getPassword() + salt));

            if ((user.getUsername().equals(utemp.getUsername())) && (user.getPassword().equals(utemp.getPassword()))) {
                FacesContext context = FacesContext.getCurrentInstance();

                Cookie cookieUser = new Cookie("user", utemp.getUsername());
                cookieUser.setDomain(".guigoh.com");
                cookieUser.setPath("/");
                cookieUser.setMaxAge(86400);
                ((HttpServletResponse) context.getExternalContext().getResponse()).addCookie(cookieUser);

                Cookie cookieToken = new Cookie("token", utemp.getToken());
                cookieToken.setDomain(".guigoh.com");
                cookieToken.setPath("/");
                cookieToken.setMaxAge(86400);
                ((HttpServletResponse) context.getExternalContext().getResponse()).addCookie(cookieToken);

                return "islogged";
            }
        }
        String message = "Login incorreto";
        message = trans.getWord(message);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message+"!", null));
        return "";
    }

    public String logout() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        eraseCookie(request, response);
        return "logout";
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

    public void recoverAccount(String status) {
        loginStatus = status;
    }

    public String sendPassToEmail() {
        try {
            UsersBO userBO = new UsersBO();
            userToRecover = userBO.findUsers(email);
            if (userToRecover != null && userToRecover.getStatus().equals("CA")) {
                EmailActivation emailactivation = new EmailActivation();
                emailactivation.setUsername(userToRecover.getUsername());
                emailactivation.setCode(md5(userToRecover.getUsername() + random(5)));
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
                return "";
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "E-mail não cadastrado/autorizado no guigoh.", null));
                return "";
            }
        } catch (EmailException e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível enviar para este e-mail. Verifique sua conexão.", null));
            return "";
        }
    }

    public String loadQuestion() {
        UsersBO userBO = new UsersBO();
        userToRecover = userBO.findUsers(email);
        if (userToRecover != null) {
            loginStatus = "question";
            return "";
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "E-mail incorreto!", null));
            return "";
        }

    }

    public String checkAnswer() {
        if (secretAnswer.equals(userToRecover.getSecretAnswer())) {
            loginStatus = "success";
            return "";
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            String message = "Resposta incorreta!";
            message = trans.getWord(message);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            return "";
        }
    }

    public String changePassword() throws Exception {
        UsersBO uBO = new UsersBO();
        if (password.equals(passwordConfirm)) {
            userToRecover.setPassword(md5(password + SALT));
            uBO.edit(userToRecover);
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
