package com.lms.lmsdada.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.lms.common.BaseResponse;
import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import com.lms.lmsdada.common.ResultUtils;
import com.lms.lmsdada.constant.CheckConstant;
import com.lms.lmsdada.constant.UserConstant;
import com.lms.lmsdada.dao.dto.user.*;
import com.lms.lmsdada.dao.vo.LoginUserVO;
import com.lms.lmsdada.dao.vo.UserVO;
import com.lms.lmsdada.service.UserService;
import com.lms.lmsdada.utils.CreateImageCode;
import com.lms.result.EnableResponseAdvice;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.util.List;

/**
 * 用户 控制层
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@RestController
@RequestMapping("/user")
@EnableResponseAdvice
public class UserController {

    @Resource
    private UserService userService;


    // region 登录相关

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    @ApiOperation(value = "注册")
    public Long userRegister(@Validated @RequestBody UserRegisterDTO userRegisterRequest) {
        return userService.userRegister(userRegisterRequest);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录")
    public LoginUserVO userLogin(@Valid @RequestBody UserLoginDTO userLoginRequest, HttpServletRequest request) {
        try {
            //校验码校验
            String trueCode = (String) request.getSession().getAttribute(CheckConstant.CHECK_CODE_KEY);
            String code = userLoginRequest.getCode();
            BusinessException.throwIf(StrUtil.isEmpty(trueCode) || !trueCode.equals(code), HttpCode.PARAMS_ERROR, "图片校验码不正确");
            LoginUserVO loginUserVO = userService.userLogin(userLoginRequest);
            StpUtil.login(loginUserVO.getId());
            return loginUserVO;
            //脱敏
        } finally {
            request.getSession().removeAttribute(CheckConstant.CHECK_CODE_KEY);
        }
    }

    /**
     * 注销
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "注销")
    public void logout() {
        StpUtil.logout();
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    @GetMapping("/get/login")
    @SaCheckLogin
    @ApiOperation(value = "获取当前登录用户")
    public LoginUserVO getLoginUser() {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return userService.getUserVO(userService.getById(loginId));
    }


    /**
     * 验证码
     * 0 为注册    1 为邮箱验证
     *
     * @param response
     * @param request
     * @throws IOException
     */
    @GetMapping(value = "/checkCode")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "图片验证码 0 为注册    1 为邮箱验证")
    public void checkCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
        CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        HttpSession session = request.getSession();
        session.setAttribute(CheckConstant.CHECK_CODE_KEY, code);
        vCode.write(response.getOutputStream());
    }

    /**
     * 修改
     *
     * @param userUpdateRequest
     * @return
     */
    @PostMapping("/update/current")
    @ApiOperationSupport(order =12)
    @ApiOperation(value = "当前用户修改")
    @SaCheckLogin
    public Boolean updateCurrentUser(@Validated @RequestBody UpdateUserDTO userUpdateRequest) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        userUpdateRequest.setId(loginId);
        return userService.updateCurrentUser(userUpdateRequest);
    }
    /**
     * 创建
     *
     * @param dto
     * @return
     */
    @PostMapping("/add")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @ApiOperation(value = "创建(管理员)")
    public BaseResponse<Long> create(@RequestBody CreateUserDTO dto) {
        return ResultUtils.success(userService.create(dto));
    }

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询(管理员)")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<IPage<UserVO>> page(@RequestBody QueryUserDTO dto) {
        return ResultUtils.success(userService.selectRecordPage(dto));
    }

    /**
     * 查询列表
     *
     * @param dto
     * @return
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表(管理员)")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<List<UserVO>> list(@RequestBody QueryUserDTO dto) {
        return ResultUtils.success(userService.selectRecordList(dto));
    }

    /**
     * 修改
     *
     * @param dto
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改(管理员)")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> update(@Validated @RequestBody UpdateUserDTO dto) {
        return ResultUtils.success(userService.update(dto));
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    @ApiOperation(value = "删除(管理员)")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> delete(@PathVariable @Positive(message = "id不合法") Long id) {
        return ResultUtils.success(userService.deleteById(id));
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询详情(管理员)")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<UserVO> selectById(@PathVariable @Positive(message = "id不合法") Long id) {
        return ResultUtils.success(userService.selectById(id));
    }

}
