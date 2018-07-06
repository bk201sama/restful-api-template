package com.sce.data.gaia.service.impl;

import com.sce.data.gaia.constant.ServiceName;
import com.sce.data.gaia.dao.UserRepository;
import com.sce.data.gaia.entity.CustomUser;
import com.sce.data.gaia.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

/**
 * @author bk201
 */
@Service(ServiceName.usersService)
public class UsersServiceImpl implements UsersService {
    private UserRepository userRepository;

    @Autowired
    public UsersServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUser getUser(String userName) {
        CustomUser customUser = userRepository.findByUsername(userName);
        return customUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteUser(String userName) {
        userRepository.deleteByUsername(userName);
        return "";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomUser addUser(CustomUser customUser) {
        customUser.setPassword(DigestUtils.md5DigestAsHex((customUser.getPassword()).getBytes()));
        return userRepository.saveAndFlush(customUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(CustomUser customUser) {
        customUser.setPassword(DigestUtils.md5DigestAsHex((customUser.getPassword()).getBytes()));
        return userRepository.updateByUserName(customUser.getUsername(), customUser.getPassword());
    }
}
