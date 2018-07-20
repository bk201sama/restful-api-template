package com.sce.data.gaia.service.impl

import com.alicp.jetcache.anno.CacheInvalidate
import com.alicp.jetcache.anno.CacheType
import com.alicp.jetcache.anno.CacheUpdate
import com.alicp.jetcache.anno.Cached
import com.google.common.base.Strings
import com.sce.data.gaia.constant.CacheNames
import com.sce.data.gaia.constant.ServiceNames
import com.sce.data.gaia.dao.UserRepository
import com.sce.data.gaia.dao.domain.CustomUser
import com.sce.data.gaia.service.UsersService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.DigestUtils

import java.util.concurrent.TimeUnit

/**
 * @author bk201
 */
@Service(ServiceNames.usersService)
class UsersServiceImpl @Autowired
constructor(private val userRepository: UserRepository) : UsersService {

    @Cached(name = CacheNames.USER, key = "#userName", timeUnit = TimeUnit.MINUTES, cacheType = CacheType.BOTH, localExpire = 5)
    override fun getUser(userName: String): CustomUser {
        return userRepository.findByUserName(userName)
    }

    @CacheInvalidate(name = CacheNames.USER, key = "#customUser.userName")
    @Transactional(rollbackFor = [(Exception::class)])
    override fun deleteUser(customUser: CustomUser) {
        if (!Strings.isNullOrEmpty(customUser.userName) && !Strings.isNullOrEmpty(customUser.password)) {
            userRepository.deleteByUserNameAndPassword(customUser.userName!!, DigestUtils.md5DigestAsHex(customUser.password!!.toByteArray()))
        }
    }

    @Transactional(rollbackFor = [(Exception::class)])
    override fun addUser(customUser: CustomUser): CustomUser {
        return if (!Strings.isNullOrEmpty(customUser.userName) && !Strings.isNullOrEmpty(customUser.password)) {
            customUser.password = DigestUtils.md5DigestAsHex(customUser.password!!.toByteArray())
            return userRepository.saveAndFlush(customUser)
        } else {
            customUser
        }
    }

    @CacheUpdate(name = CacheNames.USER, key = "#customUser.userName", value = "#customUser")
    @Transactional(rollbackFor = [(Exception::class)])
    override fun updateUser(customUser: CustomUser): Int {
        return if (!Strings.isNullOrEmpty(customUser.userName) && !Strings.isNullOrEmpty(customUser.password)) {
            userRepository.updatePasswordByUserName(customUser.userName!!, DigestUtils.md5DigestAsHex(customUser.password!!.toByteArray()))
        } else {
            0
        }
    }
}
