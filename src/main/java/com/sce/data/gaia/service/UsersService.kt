package com.sce.data.gaia.service

import com.sce.data.gaia.dao.domain.CustomUser

/**
 * @author bk201
 */
interface UsersService {
    /**
     * search user by condition
     *
     * @param userName search condition
     * @return CustomUser
     */
    fun getUser(userName: String): CustomUser?

    /**
     * delete user by condition
     *
     * @param customUser delete condition
     */
    fun deleteUser(customUser: CustomUser)

    /**
     * add user
     *
     * @param customUser add user info
     * @return CustomUser
     */
    fun addUser(customUser: CustomUser): CustomUser?

    /**
     * update user
     *
     * @param customUser only update not empty value
     * @return CustomUser
     */
    fun updateUser(customUser: CustomUser): Int
}
