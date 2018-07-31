package com.sce.data.gaia.controller;

import com.google.common.base.Throwables;
import com.sce.data.gaia.constant.CommonConstant;
import com.sce.data.gaia.constant.ErrorMsgs;
import com.sce.data.gaia.constant.RequestMappingNames;
import com.sce.data.gaia.controller.vo.UserVO;
import com.sce.data.gaia.dao.domain.CustomUser;
import com.sce.data.gaia.service.UsersService;
import com.sce.data.gaia.utils.JasperUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * user restful api ep,will be hide in product
 *
 * @author bk201
 */
@RestController
@RequestMapping(RequestMappingNames.USERS)
@Api
@Slf4j
public class UserController {
    private UsersService usersService;

    @Autowired
    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @RequestMapping(value = "/{userName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ApiOperation(value = "获取用户信息", notes = "通过用户名获取用户信息")
    public ResponseEntity<UserVO> getUser(@PathVariable("userName") @ApiParam(required = true, value = "用户名") String userName) {
        return new ResponseEntity<>(new UserVO(usersService.getUser(userName)), HttpStatus.OK);
    }

    @RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ApiOperation("注册用户")
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Integer> updateUser(@RequestBody CustomUser customUser) {
        if (usersService.getUser(customUser.getUserName()) == null) {
            return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usersService.updateUser(customUser), HttpStatus.OK);
    }

    @RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.DELETE)
    @ApiOperation("删除用户")
    @PreAuthorize("hasAuthority('ADMIN')")
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

    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    @ApiOperation("导出用户pdf")
    @ResponseBody
    public HttpEntity<byte[]> getUserReport() {
        try {
            List<CustomUser> userList = usersService.getAllUsers();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
                    userList,true
            );

            InputStream jasperStream = this.getClass().getResourceAsStream("/jasperreports/user.jasper");
            return JasperUtils.makePdfHttpEntity(jasperStream, new HashMap<>(), dataSource, "userReport");
        } catch (Exception e) {
            log.error(Throwables.getStackTraceAsString(e));
        }
        return new ResponseEntity<>(new byte[0], HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
