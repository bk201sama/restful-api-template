package com.sce.data.gaia.controller

import com.google.common.base.Strings
import com.sce.data.gaia.constant.CommonConstant
import com.sce.data.gaia.constant.ErrorMsgs
import com.sce.data.gaia.constant.RequestMappingNames
import com.sce.data.gaia.constant.RoleRulers
import com.sce.data.gaia.controller.vo.UserVO
import com.sce.data.gaia.dao.domain.CustomUser
import com.sce.data.gaia.service.UsersService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * user restful api ep,will be hide in product
 *
 * @author bk201
 */
@RestController
@RequestMapping(RequestMappingNames.USERS)
@Api
class UserController @Autowired
constructor(val usersService: UsersService) {

    @RequestMapping(value = ["/{userName}"], produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)], method = [(RequestMethod.GET)])
    @ApiOperation(value = "获取用户信息", notes = "通过用户名获取用户信息")
    fun getUser(@PathVariable("userName") @ApiParam(required = true, value = "用户名") userName: String): ResponseEntity<UserVO> {
        return ResponseEntity(UserVO(usersService.getUser(userName)), HttpStatus.OK)
    }

    @RequestMapping(value = ["/"], produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)], consumes = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE), method = arrayOf(RequestMethod.POST))
    @ApiOperation("注册用户")
    @PreAuthorize(RoleRulers.ONLY_ADMIN)
    fun addUser(@RequestBody customUser: CustomUser): ResponseEntity<UserVO> {
        val findUser = usersService.getUser(customUser.userName.orEmpty())
        return if (findUser != null) {
            ResponseEntity(UserVO(findUser), HttpStatus.CONFLICT)
        } else {
            ResponseEntity(UserVO(usersService.addUser(customUser)), HttpStatus.CREATED)
        }
    }

    @RequestMapping(value = ["/"], produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)], consumes = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE), method = arrayOf(RequestMethod.PUT))
    @ApiOperation("更新用户")
    @PreAuthorize(RoleRulers.ONLY_ADMIN)
    fun updateUser(@RequestBody customUser: CustomUser): ResponseEntity<Int> {
        return if (usersService.getUser(customUser.userName.orEmpty()) == null) {
            ResponseEntity(0, HttpStatus.NOT_FOUND)
        } else ResponseEntity(usersService.updateUser(customUser), HttpStatus.OK)
    }

    @RequestMapping(value = ["/"], produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)], method = arrayOf(RequestMethod.DELETE))
    @ApiOperation("删除用户")
    @PreAuthorize(RoleRulers.ONLY_ADMIN)
    fun deleteUser(@RequestBody customUser: CustomUser): ResponseEntity<String> {
        if (usersService.getUser(customUser.userName.orEmpty()) == null) {
            return ResponseEntity(CommonConstant.EMPTRY_STR, HttpStatus.NOT_FOUND)
        }
        usersService.deleteUser(customUser)
        return if (usersService.getUser(customUser.userName.orEmpty()) == null) {
            ResponseEntity(CommonConstant.EMPTRY_STR, HttpStatus.OK)
        } else {
            ResponseEntity(ErrorMsgs.OPERATE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }
}
