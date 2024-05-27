package com.lms.lmsdada.service.facade;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import com.lms.lmsdada.constant.SqlConstant;
import com.lms.lmsdada.constant.UserConstant;
import com.lms.lmsdada.dao.dto.app.DeleteAppDTO;
import com.lms.lmsdada.dao.dto.app.UpdateAppDTO;
import com.lms.lmsdada.dao.dto.question.DeleteQuestionDTO;
import com.lms.lmsdada.dao.dto.question.EditQuestionDTO;
import com.lms.lmsdada.dao.dto.question.QueryQuestionDTO;
import com.lms.lmsdada.dao.dto.question.QuestionContentDTO;
import com.lms.lmsdada.dao.entity.App;
import com.lms.lmsdada.dao.entity.Question;
import com.lms.lmsdada.dao.entity.User;
import com.lms.lmsdada.dao.vo.QuestionVO;
import com.lms.lmsdada.dao.vo.UserVO;
import com.lms.lmsdada.service.AppService;
import com.lms.lmsdada.service.QuestionService;
import com.lms.lmsdada.service.UserService;
import com.lms.lmsdada.utils.SqlUtils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lms.lmsdada.dao.factory.QuestionFactory.QUESTION_CONVERTER;
import static com.lms.lmsdada.dao.factory.UserFactory.USER_CONVERTER;

@Service
@RequiredArgsConstructor
public class QuestionServiceFacadeImpl {

    private  final AppService appService;

    private final UserService userService;

    private final QuestionService questionService;


    /**
     * 删除题目
     * @param deleteQuestionDTO
     * @param uid
     * @return
     */
    public Boolean deleteQuestion(DeleteQuestionDTO deleteQuestionDTO, Long uid){
        User user = userService.getById(uid);
        long id = deleteQuestionDTO.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        BusinessException.throwIf(ObjectUtil.isEmpty(oldQuestion), HttpCode.NOT_FOUND_ERROR);
        // 修改数据时，有参数则校验
        // 补充校验规则
        // 仅本人或管理员可删除
        if (!oldQuestion.getUserId().equals(user.getId()) && !UserConstant.ADMIN_ROLE.equals(user.getUserRole())) {
            throw new BusinessException(HttpCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = questionService.deleteById(id);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        return Boolean.TRUE;
    }

    /**
     * 编辑题目
     * @param dto
     * @param uid
     * @return
     */
    public Boolean editQuestion(EditQuestionDTO dto, Long uid){
        // 在此处将实体类和 DTO 进行转换
        Question question = new Question();
        BeanUtils.copyProperties(dto, question);
        List<QuestionContentDTO> questionContentDTO = dto.getQuestionContent();
        question.setQuestionContent(JSONUtil.toJsonStr(questionContentDTO));
        // 数据校验
        App app = appService.getById(question.getAppId());
        BusinessException.throwIf(ObjectUtil.isEmpty(app),HttpCode.PARAMS_ERROR,"应用不存在");
        User loginUser = userService.getById(uid);
        // 判断是否存在
        long id = dto.getId();
        Question oldQuestion = questionService.getById(id);
        BusinessException.throwIf(ObjectUtil.isEmpty(oldQuestion), HttpCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestion.getUserId().equals(loginUser.getId()) && !UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole())) {
            throw new BusinessException(HttpCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = questionService.update(question);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        return Boolean.TRUE;
    }

    /**
     * 编辑题目
     * @param dto
     * @return
     */
    public Boolean editQuestionAdmin(EditQuestionDTO dto){
        // 在此处将实体类和 DTO 进行转换
        Question question = new Question();
        BeanUtils.copyProperties(dto, question);
        List<QuestionContentDTO> questionContentDTO = dto.getQuestionContent();
        question.setQuestionContent(JSONUtil.toJsonStr(questionContentDTO));
        // 数据校验
        App app = appService.getById(question.getAppId());
        BusinessException.throwIf(ObjectUtil.isEmpty(app),HttpCode.PARAMS_ERROR,"应用不存在");
        // 判断是否存在
        long id = dto.getId();
        Question oldQuestion = questionService.getById(id);
        BusinessException.throwIf(ObjectUtil.isEmpty(oldQuestion), HttpCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = questionService.update(question);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        return Boolean.TRUE;
    }



    public IPage<QuestionVO> getPageVOForUser(QueryQuestionDTO queryQuestionDTO,Long uid){
        queryQuestionDTO.setUserId(uid);
        long current = queryQuestionDTO.getCurrent();
        long size = queryQuestionDTO.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size > 20, HttpCode.PARAMS_ERROR);
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                getQueryWrapper(queryQuestionDTO));
        // 获取封装类
        return getQuestionVOPage(questionPage);
    }

    /**
     * 根据id获取题目
     * @param id
     * @return
     */
    public QuestionVO getQuestionVOById(Long id){
        QuestionVO questionVO = questionService.selectById(id);
        UserVO userVO = userService.selectById(questionVO.getUserId());
        questionVO.setUser(userVO);
        return questionVO;
    }
    public IPage<QuestionVO> getQuestionPageVO(QueryQuestionDTO queryQuestionDTO){
        long current = queryQuestionDTO.getCurrent();
        long size = queryQuestionDTO.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size > 20, HttpCode.PARAMS_ERROR);
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                getQueryWrapper(queryQuestionDTO));
        // 获取封装类
        return getQuestionVOPage(questionPage);
    }


    private Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage){
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollUtil.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionVO> questionVOList = questionList.stream().map(QUESTION_CONVERTER::toQuestionVO).collect(Collectors.toList());
        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        questionVOList.forEach(questionVO -> {
            Long userId = questionVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionVO.setUser( USER_CONVERTER.toUserVO(user));
        });
        // endregion
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }



    /**
     * 封装查询参数
     *
     * @param dto
     * @return
     */
    private QueryWrapper<Question> getQueryWrapper(QueryQuestionDTO dto) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (dto == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = dto.getId();
        String questionContent = dto.getQuestionContent();
        Long appId = dto.getAppId();
        Long userId = dto.getUserId();
        Long notId = dto.getNotId();
        String sortField = dto.getSortField();
        String sortOrder = dto.getSortOrder();

        // 补充需要的查询条件
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(questionContent), "question_content", questionContent);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(appId), "app_id", appId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "user_id", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(SqlConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}
