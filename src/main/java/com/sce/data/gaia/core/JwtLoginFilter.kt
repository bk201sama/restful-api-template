package com.sce.data.gaia.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Throwables
import com.sce.data.gaia.constant.CommonConstant
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author bk201
 */
class JwtLoginFilter(authenticationManager: AuthenticationManager, private val signKey: String, private val expire: Long?) : UsernamePasswordAuthenticationFilter() {
    private val log = LoggerFactory.getLogger(JwtLoginFilter::class.java)

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(req: HttpServletRequest, res: HttpServletResponse?): Authentication {
        try {
            val map = ObjectMapper().readValue(req.inputStream, Map::class.java)
            return authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            map[CommonConstant.USERNAME],
                            map[CommonConstant.PASSWORD],
                            ArrayList())
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

    override fun successfulAuthentication(request: HttpServletRequest,
                                          response: HttpServletResponse,
                                          chain: FilterChain?,
                                          auth: Authentication) {
        try {
            val authorities = auth.authorities
            val roleList = ArrayList<String>()
            for (grantedAuthority in authorities) {
                roleList.add(grantedAuthority.authority)
            }
            //set expire
            val expiremiles = System.currentTimeMillis() + expire!!

            val token = Jwts.builder()
                    .claim(CommonConstant.USERNAME, auth.name)
                    .claim(CommonConstant.ROLES, roleList)
                    .setExpiration(Date(expiremiles))
                    .signWith(SignatureAlgorithm.HS512, signKey)
                    .compact()
            // put in the response head
            response.addHeader(CommonConstant.TOKEN, token)
            if (log.isDebugEnabled) {
                log.debug("login success!!!USERNAME is {},rolelist is {},expire is {} ,TOKEN is {}", auth.name, roleList, expiremiles, token)
            }
        } catch (e: Exception) {
            log.error(Throwables.getStackTraceAsString(e))
        }

    }
}