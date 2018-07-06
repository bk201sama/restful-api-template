package com.sce.data.gaia.dao;


import com.sce.data.gaia.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * system user dao
 * @author bk201
 */
public interface UserRepository extends JpaRepository<CustomUser, Long> {
    /**
     * find user by username
     * @param username not nick name,is only
     * @return CustomUser
     */
    CustomUser findByUsername(String username);

    /**
     * delete by username
     * @param userName not nick name
     */
    void deleteByUsername(String userName);

    /**
     * update by username
     * @param userName userName condition
     * @param password modifyed param
     * @return CustomUser
     */
    @Modifying
    @Query("update CustomUser t1 set t1.password=:password where t1.username = :username")
    int updateByUserName(@Param("username") String userName, @Param("password") String password);

}