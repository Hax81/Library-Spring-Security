package com.example.library_system.security.logging;

//Detta är för att logga följande:
//-Lyckade inloggningar
//-Misslyckade inloggningsförsök

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthLoggingHandlers implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthLoggingHandlers.class);

//Om Login lyckas
    @Override
    public void onAuthenticationSuccess (HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) throws IOException, ServletException {

        String userName = authentication.getName();
        logger.info("event=login_success user={}", userName); //Strukturerad loggning av säkerhetshändelse
        response.sendRedirect("/api/users/profile");
    }

//Om Login misslyckas
    @Override
    public void onAuthenticationFailure (HttpServletRequest request, HttpServletResponse response,
                                         org.springframework.security.core.AuthenticationException exception)
                                            throws IOException, ServletException {

        String userName = request.getParameter("username");
        logger.warn("event=login_failure user={} reason=\"{}\"", userName, exception.getMessage()); //Strukturerad loggning av säkerhetshändelse
        response.sendRedirect("/login?error=true");
    }
}
/*Strukturerad loggning eftersom loggmeddelandet är utformat på ett sätt som gör det:
-maskinläsbart
-sökbart
-analyserbart

Använder key-value, fälten har semantisk betydelse (ger händelsetyp, användare, anledning till misslyckande), kan indexeras och jämföras över tid
 */
