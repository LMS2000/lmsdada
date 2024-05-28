package com.lms.lmsdada.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lms.common.BaseResponse;
import com.lms.lmsdada.common.ResultUtils;
import com.lms.lmsdada.dao.dto.question.*;
import com.lms.lmsdada.dao.vo.QuestionVO;
import com.lms.lmsdada.service.QuestionService;
import com.lms.lmsdada.service.facade.QuestionServiceFacadeImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 题目 控制层
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionServiceFacadeImpl questionServiceFacade;

    /**
     * 创建(用户)
     *
     * @param dto
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "创建(用户)")
    public BaseResponse<Long> create(@RequestBody CreateQuestionDTO dto) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return ResultUtils.success(questionService.create(dto, loginId));
    }

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询")
    public BaseResponse<IPage<QuestionVO>> page(@RequestBody QueryQuestionDTO dto) {
        return ResultUtils.success(questionServiceFacade.getQuestionPageVO(dto));
    }
    /**
     * 分页查询（用户自己）
     *
     * @param dto
     * @return
     */
    @PostMapping("/page/my")
    @ApiOperation(value = "分页查询（用户自己）")
    public BaseResponse<IPage<QuestionVO>> pageForMe(@RequestBody QueryQuestionDTO dto) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return ResultUtils.success(questionServiceFacade.getPageVOForUser(dto,loginId));
    }
    /**
     * 查询列表
     *
     * @param dto
     * @return
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表")
    public BaseResponse<List<QuestionVO>> list(@RequestBody QueryQuestionDTO dto) {
        return ResultUtils.success(questionService.selectRecordList(dto));
    }

    /**
     * 修改（用户）
     *
     * @param dto
     * @return
     */
    @PostMapping("/edit")
    @ApiOperation(value = "修改（用户）")
    public BaseResponse<Boolean> update(@Valid  @RequestBody EditQuestionDTO dto) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return ResultUtils.success(questionServiceFacade.editQuestion(dto,loginId));
    }

    /**
     * 修改(管理员)
     * @param dto
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改(管理员)")
    public BaseResponse<Boolean> updateWithAdmin(@Valid  @RequestBody EditQuestionDTO dto){
        return ResultUtils.success(questionServiceFacade.editQuestionAdmin(dto));
    }

    /**
     * 删除题目(用户)
     * @param dto
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除题目(用户)")
    public BaseResponse<Boolean> delete(@RequestBody DeleteQuestionDTO dto) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return ResultUtils.success(questionServiceFacade.deleteQuestion(dto,loginId));
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询详情")
    public BaseResponse<QuestionVO> selectById(@PathVariable @Positive(message = "id不合法") Long id) {
        return ResultUtils.success(questionServiceFacade.getQuestionVOById(id));
    }

    /**
     * AI生成题目
     * @param aiGenerateQuestionDTO
     * @return
     */
    @PostMapping("/ai/generate")
    @ApiOperation(value = "AI生成")
    public List<QuestionContentDTO> aiGenerateQuestion(@RequestBody AiGenerateQuestionDTO aiGenerateQuestionDTO) {
        return questionServiceFacade.aiGenerateQuestion(aiGenerateQuestionDTO);
    }

}
