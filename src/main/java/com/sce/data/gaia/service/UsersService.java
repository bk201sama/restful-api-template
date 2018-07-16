package com.sce.data.gaia.service;

import com.sce.data.gaia.dao.domain.CustomUser;

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
     * @param customUser delete condition
     */
    void deleteUser(CustomUser customUser);

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
