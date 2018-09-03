package com.sce.data.gaia.service.impl;

import com.alicp.jetcache.anno.*;
import com.sce.data.gaia.constant.CommonConstant;
import com.sce.data.gaia.constant.ServiceNames;
import com.sce.data.gaia.dao.UserRepository;
import com.sce.data.gaia.dao.domain.CustomUser;
import com.sce.data.gaia.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author bk201
 */
@Service(ServiceNames.USERS_SERVICE)
public class UsersServiceImpl implements UsersService {
    private UserRepository userRepository;

    @Autowired
    public UsersServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cached(cacheType = CacheType.BOTH)
    @CachePenetrationProtect
    @Override
    public CustomUser getUser(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(CustomUser customUser) {
        userRepository.deleteByUserNameAndPassword(customUser.getUserName(), DigestUtils.md5DigestAsHex(customUser.getPassword().getBytes()));
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
        return userRepository.updatePasswordByUserName(customUser.getUserName(), customUser.getPassword());
    }

    @Override
    public List<CustomUser> getAllUsers() {
        return userRepository.findAll();
    }
}
