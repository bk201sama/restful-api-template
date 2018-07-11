package com.sce.data.gaia.service.impl;

import com.sce.data.gaia.constant.ServiceNames;
import com.sce.data.gaia.dao.UserRepository;
import com.sce.data.gaia.entity.CustomUser;
import com.sce.data.gaia.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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
    public CustomUser getUser(String userName) {
        CustomUser customUser = userRepository.findByUserName(userName);
        return customUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteUser(String userName) {
        userRepository.deleteByUserName(userName);
        return "";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomUser addUser(CustomUser customUser) {
        customUser.setPassword(DigestUtils.md5DigestAsHex((customUser.getPassword()).getBytes()));
        return userRepository.saveAndFlush(customUser);
    }

    @Override
    @CacheEvict(value = "users",key = "#customUser.username")
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(CustomUser customUser) {
        customUser.setPassword(DigestUtils.md5DigestAsHex((customUser.getPassword()).getBytes()));
        return userRepository.updateByUserName(customUser.getUserName(), customUser.getPassword());
    }
}
