package com.sce.data.gaia.service.impl

import com.sce.data.gaia.constant.ServiceNames
import com.sce.data.gaia.core.CustomGrantedAuthority
import com.sce.data.gaia.dao.domain.CustomUser
import com.sce.data.gaia.service.UsersService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

import java.util.Arrays
import java.util.stream.Collectors

/**
 * get user information including role by user name
 *
 * @author bk201
 */
@Service(ServiceNames.customUserDetailsService)
class CustomUserDetailsServiceImpl @Autowired
constructor(private val usersService: UsersService) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(userName: String): UserDetails {
        //find user information by user name (not nick name)
        val customUser = usersService.getUser(userName) ?: throw UsernameNotFoundException(userName)
        val roleNameArray = customUser.roleNames?.split(",")
        val authorities = roleNameArray?.map { CustomGrantedAuthority(it) }?:ArrayList()
        return User(customUser.userName!!, customUser.password!!, authorities)
    }

}
