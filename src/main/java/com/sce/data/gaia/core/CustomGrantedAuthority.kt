package com.sce.data.gaia.core

import org.springframework.security.core.GrantedAuthority

/**
 * @author bk201
 */
class CustomGrantedAuthority(private val authority: String) : GrantedAuthority {


    override fun getAuthority(): String {
        return authority
    }
}
