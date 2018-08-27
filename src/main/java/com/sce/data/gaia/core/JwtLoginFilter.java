package com.sce.data.gaia.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.sce.data.gaia.constant.CommonConstant;
import com.sce.data.gaia.utils.HttpUtils;
import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import es.moki.ratelimitj.inmemory.request.InMemorySlidingWindowRequestRateLimiter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import java.time.Duration;
import java.util.*;

/**
 * @author bk201
 */
@Slf4j
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private String signKey;
    private Long expire;
    private Set<RequestLimitRule> rules;
    private RequestRateLimiter requestRateLimiter;
    private final int limitSec = 1;
    private final int limitRequest = 5;

    public JwtLoginFilter(AuthenticationManager authenticationManager, String signKey, Long expire) {
        this.authenticationManager = authenticationManager;
        this.signKey = signKey;
        this.expire = expire;
        this.rules = Collections.singleton(RequestLimitRule.of(Duration.ofSeconds(limitSec), limitRequest));
        this.requestRateLimiter  = new InMemorySlidingWindowRequestRateLimiter(rules);
}

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse rep) throws AuthenticationException {
        try {
            StringBuilder clientIpSb = new StringBuilder("remote-ip:");
            clientIpSb.append(HttpUtils.getClientIp(req));
            String key = clientIpSb.toString();
            if (requestRateLimiter.overLimitWhenIncremented(key)){
                rep.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                log.error("{} limited by server,now rules rate is {}request/{}sec",clientIpSb.toString(),limitRequest,limitSec);
                return null;
            }
            else {
                Map<String,String> map = new ObjectMapper().readValue(req.getInputStream(), Map.class);
                return authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                map.get(CommonConstant.USERNAME),
                                map.get(CommonConstant.PASSWORD),
                                new ArrayList<>())
                );
            }

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