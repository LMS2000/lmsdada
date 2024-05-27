package com.lms.lmsdada.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lms.common.BaseResponse;
import com.lms.lmsdada.common.ResultUtils;
import com.lms.lmsdada.constant.UserConstant;
import com.lms.lmsdada.dao.dto.scoringresult.CreateScoringResultDTO;
import com.lms.lmsdada.dao.dto.scoringresult.DeleteScoringResultDTO;
import com.lms.lmsdada.dao.dto.scoringresult.QueryScoringResultDTO;
import com.lms.lmsdada.dao.dto.scoringresult.UpdateScoringResultDTO;
import com.lms.lmsdada.dao.vo.ScoringResultVO;
import com.lms.lmsdada.service.ScoringResultService;
import com.lms.lmsdada.service.facade.ScoringResultServiceFacadeImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 评分结果 控制层
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@RestController
@RequestMapping("/scoring/result")
public class ScoringResultController {

    @Resource
    private ScoringResultService scoringResultService;


    @Resource
    private ScoringResultServiceFacadeImpl scoringResultServiceFacade;

    /**
     * 创建
     *
     * @param dto
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "创建(用户)")
    public BaseResponse<Long> create(@Valid @RequestBody CreateScoringResultDTO dto) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return ResultUtils.success(scoringResultService.create(dto,loginId));
    }

    /**
     * 分页查询(用户)
     *
     * @param dto
     * @return
     */
    @PostMapping("/page/my")
    @ApiOperation(value = "分页查询(用户)")
    public BaseResponse<IPage<ScoringResultVO>> pageUser(@RequestBody QueryScoringResultDTO dto) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return ResultUtils.success(scoringResultServiceFacade.getPageVOForUser(dto,loginId));
    }
    /**
     * 分页查询(管理员)
     *
     * @param dto
     * @return
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询(管理员)")
    public BaseResponse<IPage<ScoringResultVO>> pageAdmin(@RequestBody QueryScoringResultDTO dto) {
        return ResultUtils.success(scoringResultServiceFacade.getPageVOForAdmin(dto));
    }


    /**
     * 查询列表
     *
     * @param dto
     * @return
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表")
    public BaseResponse<List<ScoringResultVO>> list(@RequestBody QueryScoringResultDTO dto) {
        return ResultUtils.success(scoringResultService.selectRecordList(dto));
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
    public BaseResponse<Boolean> update(@RequestBody UpdateScoringResultDTO dto) {
        return ResultUtils.success(scoringResultServiceFacade.updateScoringResult(dto));
    }

    /**
     * 修改（用户）
     *
     * @param dto
     * @return
     */
    @PostMapping("/edit")
    @SaCheckLogin
    @ApiOperation(value = "修改（用户）")
    public BaseResponse<Boolean> edit(@Valid @RequestBody UpdateScoringResultDTO dto) {
        return ResultUtils.success(scoringResultServiceFacade.editScoringResult(dto,Long.parseLong((String) StpUtil.getLoginId())));
    }
    /**
     * 删除
     *
     * @return
     */
    @PostMapping("/delete")
    @SaCheckLogin
    @ApiOperation(value = "删除")
    public BaseResponse<Boolean> delete(@Valid @RequestBody DeleteScoringResultDTO scoringResultDTO) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return ResultUtils.success(scoringResultServiceFacade.deleteScoringResult(scoringResultDTO,loginId));
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询详情")
    public BaseResponse<ScoringResultVO> selectById(@PathVariable @Positive(message = "id不合法") Long id) {
        return ResultUtils.success(scoringResultServiceFacade.getScoringResultById(id));
    }

}
