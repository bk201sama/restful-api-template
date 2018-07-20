package com.sce.data.gaia.core

import com.sce.data.gaia.constant.ErrorMsgs
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.util.DigestUtils

/**
 * @author bk201
 */
class CustomAuthenticationProvider(private val userDetailsService: UserDetailsService, private val bCryptPasswordEncoder: BCryptPasswordEncoder) : AuthenticationProvider {

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        val name = authentication.name
        val password = authentication.credentials.toString()
        val userDetails = userDetailsService.loadUserByUsername(name)
        if (null != userDetails) {
            val encodePassword = DigestUtils.md5DigestAsHex(password.toByteArray())
            return if (userDetails.password == encodePassword) {
                UsernamePasswordAuthenticationToken(name, password, userDetails.authorities)
            } else {
                throw BadCredentialsException(ErrorMsgs.AUTH_FAILED)
            }
        } else {
            throw UsernameNotFoundException(ErrorMsgs.AUTH_FAILED)
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }

}
