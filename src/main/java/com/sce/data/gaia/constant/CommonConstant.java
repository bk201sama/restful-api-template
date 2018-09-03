package com.sce.data.gaia.constant;

/**
 * @author bk201
 */
//xxx need replace with yours
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
    public static final String[] WECHAT_IP_EXCLUDE_LIST = {
            "10.0.*.*"
    };
    public static final String TOKEN = "token";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ROLES = "roles";
    public static final String EMPTRY_STR = "";
    public static final String NULL_STR = "null";
    public static final String WECHAT_BASE_URL = "https://qyapi.weixin.qq.com/cgi-bin/";
    public static final String WECHAT_CORID = "xxx";
    public static final String WECHAT_CORSECRET = "xxx";
    public static final String WECHAT_ACCESS_TOKEN = "access_token";
    public static final String WECHAT_ERROR_CODE = "errcode";
    public static final String WECHAT_ERROR_MSG = "errmsg";
    public static final String WECHAT_AGENT_ID = "xxx";
}
