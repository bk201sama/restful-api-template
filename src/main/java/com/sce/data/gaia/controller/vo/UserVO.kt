package com.sce.data.gaia.controller.vo

import com.sce.data.gaia.dao.domain.CustomUser

/**
 * 用户信息
 * @author
 */
class UserVO(customUser: CustomUser?) {
    var username: String? = null
    init {
        if (customUser != null) {
            this.username = customUser.userName
        }
    }
}
