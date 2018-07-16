package com.sce.data.gaia.controller;

import com.sce.data.gaia.constant.CommonConstant;
import com.sce.data.gaia.constant.ErrorMsgs;
import com.sce.data.gaia.dao.domain.CustomUser;
import com.sce.data.gaia.controller.vo.UserVO;
import com.sce.data.gaia.service.UsersService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * user restful api ep,will be hide in product
 *
 * @author bk201
 */
@RestController
@RequestMapping("/users")
@Api
public class UserController {
    private final UsersService usersService;

    @Autowired
    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @RequestMapping(value = "/{userName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ApiOperation("获取用户")
    public ResponseEntity<UserVO> getUser(@PathVariable("userName") String userName) {
        return new ResponseEntity<>(new UserVO(usersService.getUser(userName)), HttpStatus.OK);
    }

    @RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ApiOperation("注册用户")
    public ResponseEntity<UserVO> addUser(@RequestBody CustomUser customUser) {
        CustomUser findUser = usersService.getUser(customUser.getUserName());
        if (findUser != null) {
            return new ResponseEntity<>(new UserVO(findUser), HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(new UserVO(usersService.addUser(customUser)), HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    @ApiOperation("更新用户")
    public ResponseEntity<Integer> updateUser(@RequestBody CustomUser customUser) {
        if (usersService.getUser(customUser.getUserName()) == null) {
            return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usersService.updateUser(customUser), HttpStatus.OK);
    }

    @RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.DELETE)
    @ApiOperation("删除用户")
    public ResponseEntity<String> deleteUser(@RequestBody CustomUser customUser) {
        if (usersService.getUser(customUser.getUserName()) == null) {
            return new ResponseEntity<>(CommonConstant.EMPTRY_STR, HttpStatus.NOT_FOUND);
        }
        usersService.deleteUser(customUser);
        if (usersService.getUser(customUser.getUserName()) == null) {
            return new ResponseEntity<>(CommonConstant.EMPTRY_STR, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ErrorMsgs.OPERATE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
