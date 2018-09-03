package com.sce.data.gaia.config;

import com.sce.data.gaia.constant.CommonConstant;
import com.sce.data.gaia.constant.ServiceNames;
import com.sce.data.gaia.core.CustomAuthenticationProvider;
import com.sce.data.gaia.core.JwtAuthenticationFilter;
import com.sce.data.gaia.core.JwtLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
/**
 *
 * spring security config
 * @author bk201
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    @Value("${gaia.jwt.signing.Key}")
    private String signKey;
    @Value("${gaia.jwt.expire.miles}")
    private Long expire;

    private UserDetailsService userDetailsService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebSecurityConfig(@Qualifier(ServiceNames.CUSTOM_USER_DETAILS_SERVICE) UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                //don not create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(CommonConstant.AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(getJWTLoginFilter())
                .addFilter(getJWTAuthenticationFilter())
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new CustomAuthenticationProvider(userDetailsService,bCryptPasswordEncoder));
    }

    public JwtLoginFilter getJWTLoginFilter() throws Exception {
        return new JwtLoginFilter(authenticationManager(),signKey,expire);
    }

    public JwtAuthenticationFilter getJWTAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager(),signKey);
    }
}
