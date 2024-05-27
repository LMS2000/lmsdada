package com.lms.lmsdada.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lms.common.BaseResponse;
import com.lms.lmsdada.common.ResultUtils;
import com.lms.lmsdada.constant.UserConstant;
import com.lms.lmsdada.dao.dto.useranswer.CreateUserAnswerDTO;
import com.lms.lmsdada.dao.dto.useranswer.DeleteUserAnswerDTO;
import com.lms.lmsdada.dao.dto.useranswer.QueryUserAnswerDTO;
import com.lms.lmsdada.dao.dto.useranswer.UpdateUserAnswerDTO;
import com.lms.lmsdada.dao.vo.UserAnswerVO;
import com.lms.lmsdada.service.UserAnswerService;
import com.lms.lmsdada.service.facade.UserAnswerServiceFacadeImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 用户答题记录 控制层
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@RestController
@RequestMapping("/userAnswer")
public class UserAnswerController {

    @Resource
    private UserAnswerService userAnswerService;

    @Resource
    private UserAnswerServiceFacadeImpl userAnswerServiceFacade;
    /**
     * 创建
     *
     * @param dto
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "创建（用户）")
    public BaseResponse<Long> create(@RequestBody CreateUserAnswerDTO dto) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return ResultUtils.success(userAnswerService.create(dto,loginId));
    }

    /**
     * 分页查询（管理员）
     *
     * @param dto
     * @return
     */
    @PostMapping("/page")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @ApiOperation(value = "分页查询（管理员）")
    public BaseResponse<IPage<UserAnswerVO>> pageAdmin(@RequestBody QueryUserAnswerDTO dto) {
        return ResultUtils.success(userAnswerServiceFacade.getPageVOForAdmin(dto));
    }
    /**
     * 分页查询（用户）
     *
     * @param dto
     * @return
     */
    @PostMapping("/page/my")
    @SaCheckLogin
    @ApiOperation(value = "分页查询（用户）")
    public BaseResponse<IPage<UserAnswerVO>> pageUser(@RequestBody QueryUserAnswerDTO dto) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return ResultUtils.success(userAnswerServiceFacade.getPageVOForUser(dto,loginId));
    }

    /**
     * 查询列表
     *
     * @param dto
     * @return
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表")
    public BaseResponse<List<UserAnswerVO>> list(@RequestBody QueryUserAnswerDTO dto) {
        return ResultUtils.success(userAnswerService.selectRecordList(dto));
    }

    /**
     * 修改
     *
     * @param dto
     * @return
     */
    @PostMapping("/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @ApiOperation(value = "修改")
    public BaseResponse<Boolean> update(@Valid  @RequestBody UpdateUserAnswerDTO dto) {
        return ResultUtils.success(userAnswerService.update(dto));
    }

    /**
     * 编辑用户答案
     * @param dto
     * @return
     */
    @PostMapping("/edit")
    @SaCheckLogin
    @ApiOperation(value = "编辑用户答案")
    public Boolean editUserAnswer(@Valid @RequestBody UpdateUserAnswerDTO dto) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return userAnswerServiceFacade.editUserAnswer(dto,loginId);
    }

    /**
     * 删除
     *
     * @param dto
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除")
    public BaseResponse<Boolean> delete(@Valid @RequestBody DeleteUserAnswerDTO dto) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return ResultUtils.success(userAnswerServiceFacade.deleteUserAnswer(dto,loginId));
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询详情")
    public BaseResponse<UserAnswerVO> selectById(@PathVariable @Positive(message = "id不合法") Long id) {
        return ResultUtils.success(userAnswerServiceFacade.getUserAnswerById(id));
    }

}
