package com.sce.data.gaia.service.impl;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import com.sce.data.gaia.constant.CacheNames;
import com.sce.data.gaia.constant.ServiceNames;
import com.sce.data.gaia.controller.vo.UserVO;
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
@Service(ServiceNames.usersService)
public class UsersServiceImpl implements UsersService {
    private UserRepository userRepository;

    @Autowired
    public UsersServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cached(name = CacheNames.USER, key = "#userName",timeUnit = TimeUnit.MINUTES,cacheType = CacheType.BOTH,localExpire = 5)
    @Override
    public CustomUser getUser(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    @CacheInvalidate(name = CacheNames.USER, key = "#customUser.userName")
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
    @CacheUpdate(name = CacheNames.USER, key = "#customUser.userName", value = "#customUser")
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
