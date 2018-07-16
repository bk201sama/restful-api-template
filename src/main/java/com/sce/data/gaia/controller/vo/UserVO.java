package com.sce.data.gaia.controller.vo;

import com.sce.data.gaia.dao.domain.CustomUser;
import lombok.Data;

/**
 * 用户信息
 * @author
 */
@Data
public class UserVO {
    private String username;
    public UserVO(CustomUser customUser){
        if(customUser!=null){
            this.username = customUser.getUserName();
        }
    }
}
