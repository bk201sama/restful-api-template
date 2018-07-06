package com.sce.data.gaia.service.impl;

import com.sce.data.gaia.constant.ServiceName;
import com.sce.data.gaia.dao.UserRepository;
import com.sce.data.gaia.entity.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

/**
 * @author bk201
 */
@Service(ServiceName.customUserDetailsService)
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private  UserRepository userRepository;

    @Autowired
    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(value = "users",key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser customUser = userRepository.findByUsername(username);
        if(customUser == null){
            throw new UsernameNotFoundException(username);
        }
        return new User(customUser.getUsername(), customUser.getPassword(), emptyList());
    }

}
