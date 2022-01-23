package com.sombra.promotion.configuration;

import com.sombra.promotion.enums.RoleEnum;
import com.sombra.promotion.filter.TokenVerificationFilter;
import com.sombra.promotion.filter.UserAuthenticationFilter;
import com.sombra.promotion.security.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserAuthenticationFilter userAuthenticationFilter;
    @Autowired
    private TokenVerificationFilter tokenVerificationFilter;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final String USER = RoleEnum.ROLE_USER.getAuthority();
        final String SUPER_ADMIN = RoleEnum.ROLE_SUPER_ADMIN.getAuthority();

        http
                .csrf().disable().cors()
                .and()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .addFilterBefore(tokenVerificationFilter, BasicAuthenticationFilter.class)
                .addFilterAt(userAuthenticationFilter, BasicAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/api/public/**").hasAnyAuthority(SUPER_ADMIN)
                .antMatchers("/api/super_admin/**").hasAnyAuthority(SUPER_ADMIN)
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .and()
                .logout();
    }
}
