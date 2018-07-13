package com.sce.data.gaia.service.impl;

import com.sce.data.gaia.constant.ServiceNames;
import com.sce.data.gaia.core.CustomGrantedAuthority;
import com.sce.data.gaia.dao.UserRepository;
import com.sce.data.gaia.dao.domain.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
 * @author bk201
 */
@Service(ServiceNames.customUserDetailsService)
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private  UserRepository userRepository;

    @Autowired
    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(value = "users",key = "#userName")
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //find user information by user name (not nick name)
        CustomUser customUser = userRepository.findByUserName(userName);
        if(customUser == null){
            throw new UsernameNotFoundException(userName);
        }
        String[] roleNameArray = customUser.getRoleNames().split(",");
        List<GrantedAuthority> authorities = Arrays.stream(roleNameArray).map(CustomGrantedAuthority::new).collect(Collectors.toList());
        return new User(customUser.getUserName(), customUser.getPassword(),authorities);
    }

}
