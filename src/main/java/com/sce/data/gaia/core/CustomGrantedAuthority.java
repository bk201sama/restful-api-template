package com.sce.data.gaia.core;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author bk201
 */
@Data
public class CustomGrantedAuthority implements GrantedAuthority {

    private String authority;

    public CustomGrantedAuthority(String authority) {
        this.authority = authority;
    }
}
