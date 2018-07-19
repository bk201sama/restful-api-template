package com.sce.data.gaia.core;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author bk201
 */
public class CustomGrantedAuthority implements GrantedAuthority {

    private String authority;

    public CustomGrantedAuthority(String authority) {
        this.authority = authority;
    }


    @Override
    public String getAuthority() {
        return null;
    }
}
