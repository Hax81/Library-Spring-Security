package com.example.library_system.config;

import com.example.library_system.security.logging.AuthLoggingHandlers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity

public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthLoggingHandlers authLoggingHandlers;

    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, AuthLoggingHandlers authLoggingHandlers) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.authLoggingHandlers = authLoggingHandlers;
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authz -> authz

                        //Öppna endpoints
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll() //Alla vanliga personer ska kunna registrera sig och då få "user"-rollen automatiskt. Denna ska alltså vara öppen för alla
                        .requestMatchers(HttpMethod.GET,"/api/books/all").permitAll()//Alla ska kunna se alla böcker i biblioteket utan inloggning

                        //Endpoints kräver inloggning
                        .requestMatchers(HttpMethod.GET, "/api/authors/all").authenticated() //Endast inloggade ska kunna se författare
                        .requestMatchers(HttpMethod.GET, "/api/books/search").authenticated() //Endast inloggade ska kunna använda sökfunktionen.
                        .requestMatchers(HttpMethod.POST,"/api/loans/new").hasAnyRole("USER", "ADMIN") //USER och ADMIN ska kunna skapa nya lån.
                        .requestMatchers(HttpMethod.POST, "/api/loans/{loanId}/return").hasAnyRole("USER", "ADMIN") //USER och ADMIN ska kunna återlämna böcker.

                        //Endpoints kräver inloggning som ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/loans/all").hasRole("ADMIN") //Endast Admin ska kunna se alla lån
                        .requestMatchers(HttpMethod.POST, "/api/users/new").hasRole("ADMIN") //Endast Admin ska kunna tilldela roll när de registrerar användare.
                        .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN") //Endast Admin ska kunna genomföra PUT-operationer
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN") //Endast Admin ska kunna genomföra DELETE-operationer

                        //Allt annat kräver inloggning
                        .anyRequest().authenticated()
        )

                //För att logga in med browser: http://localhost:8080/login
                .formLogin(form -> form
                        .successHandler(authLoggingHandlers)
                        .failureHandler(authLoggingHandlers)
                        .permitAll())

                //För att logga in med Postman: http://localhost:8080/login
                .httpBasic(Customizer.withDefaults())

                .logout(Customizer.withDefaults())
                .logout(logout -> logout //OBS! LÄS PÅ, vad händer?
                        .logoutUrl("/logout")                    // URL till logout dvs localhost:8080/logout
                        .logoutSuccessUrl("/login?logout")       // Redirect efter logout
                        .invalidateHttpSession(true)             // Gör HTTP-sessionen invaliderad
                        .deleteCookies("JSESSIONID") // Tar bort session-cookies
                        .clearAuthentication(true)               // Rensa authentication context
                        .permitAll()                            // Alla ska kunna logga ut, dvs accessa logout
                )
                .sessionManagement(session -> session //OBS LÄS PÅ
                        .maximumSessions(1)                     // Begränsa samtidiga sessioner
                        .maxSessionsPreventsLogin(false)        // Tillåt ny login, kicka gamla
                )

                .csrf(csrf -> csrf.disable());
                //OBS! Har ingen frontend. Använder Postman. Csrf är för närvarande avstängt. Går att demonstrera CSRF i Postman genom att:
                // -Aktivera csrf och försöka försöka göra en state-förändrande request (som en POST). Det kommer stoppas pga token saknas, detta visar att CSRF fungerar.
                // -Därefter får man försöka hämta en csrf-token (t.ex genom en GET till login) och sedan göra en POST på nytt där token (och session-cookie) skickas med.
                //POST ska då fungera.

        return http.build();
    }

        @Bean
        public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean //Säger att objektet DaoAuthenticationProvider ska vara en bean, dvs kan injiceras överallt i programmet
        public DaoAuthenticationProvider authenticationProvider () { /*DaoAuthenticationProvider är en Spring Securityklass som
                                                                       -använder UserDetailsService för att ladda användardata från databasen
                                                                       -använder PasswordEncoder för att verifiera credentials*/

            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

            authenticationProvider.setUserDetailsService(userDetailsService);
            authenticationProvider.setPasswordEncoder(passwordEncoder);

            return authenticationProvider;
        }
}

