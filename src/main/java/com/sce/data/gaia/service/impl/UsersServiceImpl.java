package com.sce.data.gaia.service.impl;

import com.sce.data.gaia.constant.CacheNames;
import com.sce.data.gaia.constant.ServiceNames;
import com.sce.data.gaia.dao.UserRepository;
import com.sce.data.gaia.dao.domain.CustomUser;
import com.sce.data.gaia.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

/**
 * @author bk201
 */
@Service(ServiceNames.usersService)
public class UsersServiceImpl implements UsersService {
    private UserRepository userRepository;

    @Autowired
    public UsersServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(value = CacheNames.USER, key = "#userName")
    public CustomUser getUser(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    @CacheEvict(value = CacheNames.USER, key = "#customUser.userName")
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(CustomUser customUser) {
        userRepository.deleteByUserNameAndPassword(customUser.getUserName(), DigestUtils.md5DigestAsHex(customUser.getPassword().getBytes()));
    }

    @Override
    @CachePut(value = CacheNames.USER, key = "#customUser.userName")
    @Transactional(rollbackFor = Exception.class)
    public CustomUser addUser(CustomUser customUser) {
        customUser.setPassword(DigestUtils.md5DigestAsHex((customUser.getPassword()).getBytes()));
        return userRepository.saveAndFlush(customUser);
    }

    @Override
    @CacheEvict(value = CacheNames.USER, key = "#customUser.userName")
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(CustomUser customUser) {
        customUser.setPassword(DigestUtils.md5DigestAsHex((customUser.getPassword()).getBytes()));
        return userRepository.updatePasswordByUserName(customUser.getUserName(), customUser.getPassword());
    }
}
