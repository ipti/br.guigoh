/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.listener;

import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.model.entity.UserAuthorization;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.UserAuthorizationJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UsersJpaController;
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
        boolean isLoginPage = context.getViewRoot().getViewId().lastIndexOf("login") > -1;
        boolean isRegisterPage = context.getViewRoot().getViewId().lastIndexOf("register") > -1;
        boolean isWizardPage = context.getViewRoot().getViewId().lastIndexOf("wizard-profile") > -1;
        boolean isEmailPage = context.getViewRoot().getViewId().lastIndexOf("confirm-email") > -1;
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
            UsersJpaController usersJpaController = new UsersJpaController();
            Users usertemp = usersJpaController.findUsers(user.getUsername());
            
            confirmed = (usertemp.getStatus().equals("CA"));
            pending = (usertemp.getStatus().equals("CP"));

            UserAuthorizationJpaController userAuthorizationJpaController = new UserAuthorizationJpaController();
            UserAuthorization authorization = userAuthorizationJpaController.findAuthorization(usertemp.getToken());
            if (authorization != null) {
                firstAccess = (authorization.getStatus().equals("FC"));
                activeAccess = (authorization.getStatus().equals("AC"));
                inactiveAccess = (authorization.getStatus().equals("IC"));
                pendingAccess = (authorization.getStatus().equals("PC"));
            }
        }
        // Verifica se o usuário não está na página de registro
        if (!isRegisterPage) {
//            if (!isEmailPage) {
//                if ((!isLoginPage && pending)
//                        || (!isLoginPage && confirmed && inactiveAccess)
//                        || !isLoginPage && confirmed && pendingAccess) {
//                    NavigationHandler nh = context.getApplication().getNavigationHandler();
//                    nh.handleNavigation(context, null, "email");
//                }
//            }
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
                    
                    //nao influencia
                    NavigationHandler nh = context.getApplication().getNavigationHandler();
                    nh.handleNavigation(context, null, "logout");
                } else if (isLoginPage && !user.getUsername().equals("")) {
                    // Se o usuário logado tentar acessar a página de login ele é
                    // redirecionado para a página inicial
                    
                    //nao influencia
                    NavigationHandler nh = context.getApplication().getNavigationHandler();
                    nh.handleNavigation(context, null, "islogged");
                }
//            } else {
//                if (!isLoginPage) {
//                    
//                    //influencia
//                    NavigationHandler nh = context.getApplication().getNavigationHandler();
//                    nh.handleNavigation(context, null, "logout");
//                }
//            }
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
