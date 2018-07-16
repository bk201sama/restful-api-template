package com.sce.data.gaia.constant;

/**
 * @author bk201
 */
public class CommonConstant {
    public static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
    };
    public static final String TOKEN = "token";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ROLES = "roles";
    public static final String EMPTRY_STR = "";

}
