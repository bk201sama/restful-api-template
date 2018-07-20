package com.sce.data.gaia.core

import com.google.common.base.Strings
import com.sce.data.gaia.constant.CommonConstant
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException
import java.util.ArrayList
import java.util.stream.Collectors

/**
 * @author bk201
 */
class JwtAuthenticationFilter(authenticationManager: AuthenticationManager, private val signKey: String) : BasicAuthenticationFilter(authenticationManager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val header = request.getHeader(CommonConstant.TOKEN)

        if (header == null) {
            chain.doFilter(request, response)
            return
        }

        val authentication = getAuthentication(request)

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)

    }

    private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val token = request.getHeader(CommonConstant.TOKEN)
        if (token != null) {
            val jwsList = Jwts.parser()
                    .setSigningKey(signKey)
                    .parseClaimsJws(token)
            val user = jwsList.body.getOrDefault(CommonConstant.USERNAME, CommonConstant.EMPTRY_STR).toString()
            val roles = jwsList.body.getOrDefault(CommonConstant.ROLES, ArrayList<Any>()) as List<String>
            val authorities = roles.map { CustomGrantedAuthority(it) }
            if (!Strings.isNullOrEmpty(user)) {
                return UsernamePasswordAuthenticationToken(user, null, authorities)
            }
        }
        return null
    }


}
