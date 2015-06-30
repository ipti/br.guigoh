/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.util;

/**
 *
 * @author Joe
 */
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class MailService {

    private final static String userAuthentication = "noreply@ipti.org.br";
    private final static String passwordUserAuthentication = "p@s4ipti";
    private final static String sender = "noreply@ipti.org.br";
    private final static String smtp = "smtp.ipti.org.br";
    private final static int smtpPort = 587;
    private final static boolean authentication = false;

    public static void sendMail(String message, String subject, String receiver)
            throws EmailException {

        SimpleEmail email = new SimpleEmail();
        email.setHostName(smtp);
        email.setCharset("UTF-8");
        email.setSmtpPort(smtpPort);
        email.setAuthentication(userAuthentication, passwordUserAuthentication);
        email.setSSLOnConnect(authentication);
        email.addTo(receiver);
        email.setFrom(sender);
        email.setSubject(subject);
        email.setMsg(message);
        email.send();
    }
}
