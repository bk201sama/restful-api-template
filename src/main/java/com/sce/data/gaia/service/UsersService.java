package com.sce.data.gaia.service;

import com.sce.data.gaia.entity.CustomUser;

/**
 * @author bk201
 */
public interface UsersService {
    /**
     * search user by condition
     * @param userName search condition
     * @return CustomUser
     */
    CustomUser getUser(String userName);

    /**
     * delete user by condition
     * @param userName delete condition
     * @return String (noting)
     */
    String deleteUser(String userName);

    /**
     * add user
     * @param customUser add user info
     * @return CustomUser
     */
    CustomUser addUser(CustomUser customUser);

    /**
     * update user
     * @param customUser only update not empty value
     * @return CustomUser
     */
    int updateUser(CustomUser customUser);
}
