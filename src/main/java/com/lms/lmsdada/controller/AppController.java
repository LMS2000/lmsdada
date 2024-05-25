package com.lms.lmsdada.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lms.common.BaseResponse;
import com.lms.lmsdada.common.ResultUtils;
import com.lms.lmsdada.constant.UserConstant;
import com.lms.lmsdada.dao.dto.app.*;
import com.lms.lmsdada.dao.vo.AppVO;
import com.lms.lmsdada.service.AppService;
import com.lms.lmsdada.service.facade.AppServiceFacadeImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 应用 控制层
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Resource
    private AppService appService;

    @Resource
    private AppServiceFacadeImpl appServiceFacade;

    /**
     * 创建
     *
     * @param dto
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "创建(用户)")
    @SaCheckLogin
    public BaseResponse<Long> create(@Valid @RequestBody CreateAppDTO dto) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return ResultUtils.success(appService.create(dto,loginId));
    }

    /**
     * 分页查询（管理员）
     *
     * @param dto
     * @return
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询(管理员)")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<IPage<AppVO>> page(@RequestBody QueryAppDTO dto) {
        return ResultUtils.success(appServiceFacade.getPageVOForAdmin(dto));
    }

    /**
     * 分页查询（用户）
     *
     * @param dto
     * @return
     */
    @PostMapping("/page/my")
    @ApiOperation(value = "分页查询(用户)")
    @SaCheckLogin
    public BaseResponse<IPage<AppVO>> pageWithLoginUser(@RequestBody QueryAppDTO dto) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return ResultUtils.success(appServiceFacade.getPageVOForUser(dto,loginId));
    }


    /**
     * 查询列表
     *
     * @param dto
     * @return
     */
    @PostMapping("/list")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @ApiOperation(value = "查询(管理员)")
    public BaseResponse<List<AppVO>> list(@RequestBody QueryAppDTO dto) {
        return ResultUtils.success(appService.selectRecordList(dto));
    }

    /**
     * 修改
     *
     * @param dto
     * @return
     */
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @PostMapping("/update")
    @ApiOperation(value = "修改(管理员)")
    public BaseResponse<Boolean> update(@Valid @RequestBody UpdateAppDTO dto) {
        return ResultUtils.success(appService.update(dto));
    }

    /**
     * 编辑应用(用户)
     * @param dto
     * @return
     */
    @SaCheckLogin
    @PostMapping("/edit")
    @ApiOperation(value = "编辑(用户)")
    public Boolean edit(@Valid @RequestBody UpdateAppDTO dto){
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return appServiceFacade.editApp(dto,loginId);
    }

    /**
     * 删除
     *
     * @return
     */
    @PostMapping("/delete")
    @SaCheckLogin
    @ApiOperation(value = "删除(用户)")
    public BaseResponse<Boolean> delete(@Valid @RequestBody DeleteAppDTO deleteAppDTO) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return ResultUtils.success(appServiceFacade.deleteApp(deleteAppDTO,loginId));
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询")
    public BaseResponse<AppVO> selectById(@PathVariable @Positive(message = "id不合法") Long id) {
        return ResultUtils.success(appServiceFacade.getAppVoById(id));
    }


    /**
     * 审核应用(管理眼)
     * @param reviewAppDTO
     * @return
     */
    @PostMapping("/review")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @ApiOperation(value = "审核应用(管理员)")
    public Boolean reviewApp(@Valid @RequestBody ReviewAppDTO reviewAppDTO){
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return appServiceFacade.reviewApp(reviewAppDTO,loginId);
    }
}
