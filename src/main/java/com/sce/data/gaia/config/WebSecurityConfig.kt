package com.sce.data.gaia.config

import com.sce.data.gaia.constant.CommonConstant
import com.sce.data.gaia.constant.DefaultValues
import com.sce.data.gaia.constant.ServiceNames
import com.sce.data.gaia.core.CustomAuthenticationProvider
import com.sce.data.gaia.core.JwtAuthenticationFilter
import com.sce.data.gaia.core.JwtLoginFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 *
 * spring security config
 * @author bk201
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig @Autowired
constructor(@param:Qualifier(ServiceNames.customUserDetailsService)val userDetailsService: UserDetailsService,val bCryptPasswordEncoder: BCryptPasswordEncoder) : WebSecurityConfigurerAdapter() {
    @Value("\${gaia.jwt.signing.Key}")
    private val signKey: String = DefaultValues.DEFAULT_SIGN_KEY
    @Value("\${gaia.jwt.expire.miles}")
    private val expire: Long = DefaultValues.DEFAULT_EXPIRE

    val jwtLoginFilter: JwtLoginFilter
        @Throws(Exception::class)
        get() = JwtLoginFilter(authenticationManager(), signKey, expire)

    val jwtAuthenticationFilter: JwtAuthenticationFilter
        @Throws(Exception::class)
        get() = JwtAuthenticationFilter(authenticationManager(), signKey)

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(*CommonConstant.AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(jwtLoginFilter)
                .addFilter(jwtAuthenticationFilter)
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(CustomAuthenticationProvider(userDetailsService, bCryptPasswordEncoder))
    }

}
