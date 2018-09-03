package com.sce.data.gaia.service.impl;

import com.sce.data.gaia.constant.ServiceNames;
import com.sce.data.gaia.core.CustomGrantedAuthority;
import com.sce.data.gaia.dao.domain.CustomUser;
import com.sce.data.gaia.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * get user information including role by user name
 *
 * @author bk201
 */
@Service(ServiceNames.CUSTOM_USER_DETAILS_SERVICE)
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private  UsersService usersService;

    @Autowired
    public CustomUserDetailsServiceImpl(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //find user information by user name (not nick name)
        CustomUser customUser = usersService.getUser(userName);
        if (customUser == null) {
            throw new UsernameNotFoundException(userName);
        }
        String[] roleNameArray = customUser.getRoleNames().split(",");
        List<GrantedAuthority> authorities = Arrays.stream(roleNameArray).map(CustomGrantedAuthority::new).collect(Collectors.toList());
        return new User(customUser.getUserName(), customUser.getPassword(), authorities);
    }

}
