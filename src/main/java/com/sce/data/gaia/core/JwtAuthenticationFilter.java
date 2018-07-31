package com.sce.data.gaia.core;

import com.google.common.base.Strings;
import com.sce.data.gaia.constant.CommonConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bk201
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private String signKey;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, String signKey) {
        super(authenticationManager);
        this.signKey = signKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(CommonConstant.TOKEN);
        String body = request.getParameter(CommonConstant.TOKEN);
        if (header == null&&body == null) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(CommonConstant.TOKEN)==null?request.getParameter(CommonConstant.TOKEN):request.getHeader(CommonConstant.TOKEN);
        if (token != null) {
            Jws<Claims> jwsList = Jwts.parser()
                    .setSigningKey(signKey)
                    .parseClaimsJws(token);
            String user = String.valueOf(jwsList.getBody()
                    .getOrDefault(CommonConstant.USERNAME, CommonConstant.EMPTRY_STR));
            List<String> roles = (List<String>) jwsList.getBody().getOrDefault(CommonConstant.ROLES, new ArrayList<>());
            List<GrantedAuthority> authorities = roles.stream().map(CustomGrantedAuthority::new).collect(Collectors.toList());
            if (!Strings.isNullOrEmpty(user)) {
                return new UsernamePasswordAuthenticationToken(user, null, authorities);
            }
        }
        return null;
    }


}
