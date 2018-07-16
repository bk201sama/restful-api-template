package com.sce.data.gaia.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.sce.data.gaia.constant.CommonConstant;
import com.sce.data.gaia.entity.UserVO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author bk201
 */
@Slf4j
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private String signKey;
    private Long expire;

    public JwtLoginFilter(AuthenticationManager authenticationManager, String signKey, Long expire) {
        this.authenticationManager = authenticationManager;
        this.signKey = signKey;
        this.expire = expire;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            UserVO userVO = new ObjectMapper().readValue(req.getInputStream(), UserVO.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userVO.getUsername(),
                            userVO.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) {
        try {
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            List<String> roleList = new ArrayList<>();
            for (GrantedAuthority grantedAuthority : authorities) {
                roleList.add(grantedAuthority.getAuthority());
            }
            //set expire
            long expiremiles = System.currentTimeMillis() + expire;

            String token = Jwts.builder()
                    .claim(CommonConstant.USERNAME,auth.getName())
                    .claim(CommonConstant.ROLES,roleList)
                    .setExpiration(new Date(expiremiles))
                    .signWith(SignatureAlgorithm.HS512, signKey)
                    .compact();
            // put in the response head
            response.addHeader(CommonConstant.TOKEN, token);
            if(log.isDebugEnabled()){
                log.debug("login success!!!USERNAME is {},rolelist is {},expire is {} ,TOKEN is {}",auth.getName(),roleList,expiremiles,token);
            }
        } catch (Exception e) {
            log.error(Throwables.getStackTraceAsString(e));
        }
    }
}