package com.sce.data.gaia.dao


import com.sce.data.gaia.dao.domain.CustomUser
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * system user dao
 * @author bk201
 */
interface UserRepository : JpaRepository<CustomUser, Long> {
    /**
     * find user by USERNAME
     * @param userName not nick name,is only
     * @return CustomUser
     */
    fun findByUserName(userName: String): CustomUser

    /**
     * delete by username and password
     * @param userName not nick name
     */
    fun deleteByUserNameAndPassword(userName: String, password: String)

    /**
     * update by USERNAME
     * @param userName userName condition
     * @param password modifyed param
     * @return CustomUser
     */
    @Modifying
    @Query("update CustomUser t1 set t1.password=:password where t1.userName = :userName")
    fun updatePasswordByUserName(@Param("userName") userName: String, @Param("password") password: String): Int

}