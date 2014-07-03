/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bean;

import com.guigoh.bo.UserAuthorizationBO;
import com.guigoh.bo.UsersBO;
import com.guigoh.bo.util.CookieService;
import com.guigoh.entity.UserAuthorization;
import com.guigoh.entity.Users;
import java.io.Serializable;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author IPTI
 */
public class AuthorizeListener implements PhaseListener{

    @Override
    public void afterPhase(PhaseEvent event) {
        // Obtém o contexto atual
        FacesContext context = event.getFacesContext();
        // Obtém a página que atualmente está interagindo com o ciclo
        // Se for a página 'login.jsp' seta a variável como true
        boolean isLoginPage = context.getViewRoot().getViewId().lastIndexOf("login") > -1 ? true : false;
        boolean isRegisterPage = context.getViewRoot().getViewId().lastIndexOf("register") > -1 ? true : false;
        boolean isWizardPage = context.getViewRoot().getViewId().lastIndexOf("wizardProfile") > -1 ? true : false;
        boolean isEmailPage = context.getViewRoot().getViewId().lastIndexOf("confirmEmail") > -1 ? true : false;
        // Obtém a sessão atual
        // Resgata o nome do usuário logado
        Users user = new Users();
        user.setUsername(CookieService.getCookie("user"));
        user.setToken(CookieService.getCookie("token"));
        Boolean firstAccess = false;
        Boolean activeAccess = false;
        Boolean inactiveAccess = false;
        Boolean pendingAccess = false;
        Boolean confirmed = false;
        Boolean pending = false;
        if (user.getUsername() != null) {
            UsersBO userBO = new UsersBO();
            Users usertemp = userBO.findUsers(user);

            confirmed = (usertemp.getStatus().equals("CA"));
            pending = (usertemp.getStatus().equals("CP"));


            UserAuthorizationBO authorizationBO = new UserAuthorizationBO();
            UserAuthorization authorization = authorizationBO.findAuthorizationByTokenId(usertemp.getToken());
            if (authorization != null) {
                firstAccess = (authorization.getStatus().equals("FC"));
                activeAccess = (authorization.getStatus().equals("AC"));
                inactiveAccess = (authorization.getStatus().equals("IC"));
                pendingAccess = (authorization.getStatus().equals("PC"));
            }
        }
        // Verifica se o usuário não está na página de registro
        if (!isRegisterPage) {
            if (!isEmailPage) {
                if ((!isLoginPage && pending)
                        || (!isLoginPage && confirmed && inactiveAccess)
                        || !isLoginPage && confirmed && pendingAccess) {
                    NavigationHandler nh = context.getApplication().getNavigationHandler();
                    nh.handleNavigation(context, null, "email");
                }
            }
            if (!isWizardPage) {
                if (!isLoginPage && confirmed && firstAccess) {
                    NavigationHandler nh = context.getApplication().getNavigationHandler();
                    nh.handleNavigation(context, null, "wizard");
                }
            }

            if (user.getUsername() != null) {
                // Verifica se o usuário está logado e se não está na página de login
                if (!isLoginPage && ((user.getUsername().equals("")) || (user.getUsername().isEmpty()))) {
                    // Redireciona o fluxo para a página de login
                    NavigationHandler nh = context.getApplication().getNavigationHandler();
                    nh.handleNavigation(context, null, "logout");
                } else if (isLoginPage && !user.getUsername().equals("")) {
                    // Se o usuário logado tentar acessar a página de login ele é
                    // redirecionado para a página inicial
                    NavigationHandler nh = context.getApplication().getNavigationHandler();
                    nh.handleNavigation(context, null, "islogged");
                }
            } else {
                if (!isLoginPage) {
                    NavigationHandler nh = context.getApplication().getNavigationHandler();
                    nh.handleNavigation(context, null, "logout");
                }
            }
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
