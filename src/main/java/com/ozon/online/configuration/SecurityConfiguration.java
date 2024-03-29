package com.ozon.online.configuration;

import com.ozon.online.service.UserService;
import com.ozon.online.util.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    // NOT AUTH

        // AUTH
    private static final String REGISTER = "/api/v1/auth/register";
    private static final String LOGIN = "/api/v1/auth/login";
    private static final String VERIFY_CODE = "/api/v1/auth/verifycode";
        // PRODUCT
    private static final String ALL_PRODUCTS = "/api/v1/product/products";
    private static final String CORRECT_PRODUCT = "/api/v1/product/correctproduct";
    private static final String SEARCH_PRODUCT = "/api/v1/product/search";
    private static final String SORT_PRODUCT = "/api/v1/product/sort";


    // USER
    private static final String CHANGE_NICKNAME = "/api/v1/safety/changenickname";
    private static final String CHANGE_PASSWORD = "/api/v1/safety/changepassword";
    private static final String CREATE2FA = "/api/v1/auth/create2FA";
    private static final String SET_AVATAR = "/api/v1/profile/set";
    private static final String GET_AVATAR = "/api/v1/profile/avatar";


    // PRODUCT
    private static final String CREATE_PRODUCT = "/api/v1/product/create";
    private static final String MY_PRODUCTS = "/api/v1/product/myproducts";

    // CHAT
    private static final String SEND_MESSAGE = "/api/v1/chat/chats/send";
    private static final String OPEN_CHAT = "/api/v1/chat/chats";
    private static final String MY_CHATS = "/api/v1/chat/my";
    private static final String DELETE_CHAT = "/api/v1/chat/delete/{id}";

    // REVIEW
    private static final String GET_MY_REVIEWS = "/profile/reviews";
    private static final String AVERAGE = "/user/average";
    private static final String ADD_REVIEW = "/user/create/{id}";

    // ADMIN
    private static final String USERS = "/admin/users";
    private static final String SET_USER_ROLE = "/userrole/{id}";
    private static final String SET_ADMIN_ROLE = "/adminrole/{id}";


    private final PasswordEncoderConfiguration passwordEncoderConfiguration;
    private final UserService userService;
    private final JwtRequestFilter jwtRequestFilter;



    private void sharedSecurityConfiguration(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .securityMatcher("http://localhost:3000/**")
                .sessionManagement(httpSecuritySessionManagementConfigurer -> {
                    httpSecuritySessionManagementConfigurer
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                });

    }

    @Bean
    public SecurityFilterChain securityFilterChainUsersAPI(HttpSecurity httpSecurity) throws Exception {
        sharedSecurityConfiguration(httpSecurity);
        httpSecurity
                .securityMatcher(CREATE2FA, CHANGE_NICKNAME, SEND_MESSAGE, OPEN_CHAT, MY_CHATS, DELETE_CHAT,
                        CHANGE_PASSWORD, SET_AVATAR, GET_AVATAR, CREATE_PRODUCT, MY_PRODUCTS,
                        ADD_REVIEW, AVERAGE)
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().hasRole("USER");
                })
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChainAdminsAPI(HttpSecurity httpSecurity) throws Exception {
        sharedSecurityConfiguration(httpSecurity);
        httpSecurity
                .securityMatcher(SET_ADMIN_ROLE, SET_USER_ROLE)
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().hasRole("ADMIN");
                })
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoderConfiguration.passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

