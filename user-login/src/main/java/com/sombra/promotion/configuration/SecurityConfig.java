package com.sombra.promotion.configuration;

import com.sombra.promotion.enums.RoleEnum;
import com.sombra.promotion.filter.JwtAuthorizationFilter;
import com.sombra.promotion.security.JwtAuthenticationProvider;
import com.sombra.promotion.security.RestAuthenticationEntryPoint;
import com.sombra.promotion.service.UserCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;
    @Autowired
    private UserCredentialService userCredentialService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
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
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/api/public/**").permitAll()
                .antMatchers("/api/super_admin/**").hasAnyAuthority(SUPER_ADMIN)
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .and()
                .logout();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(jwtAuthenticationProvider());
    }

    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userCredentialService);
        return provider;
    }
}
