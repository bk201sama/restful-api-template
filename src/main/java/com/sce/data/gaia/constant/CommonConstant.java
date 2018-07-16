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
    public static final String TOKEN = "TOKEN";
    public static final String USERNAME = "USERNAME";
    public static final String ROLES = "ROLES";
    public static final String EMPTRY_STR = "";

}
