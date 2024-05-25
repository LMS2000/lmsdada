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
import com.lms.lmsdada.dao.dto.question.DeleteQuestionDTO;
import com.lms.lmsdada.dao.dto.question.EditQuestionDTO;
import com.lms.lmsdada.dao.dto.question.QuestionContentDTO;
import com.lms.lmsdada.dao.dto.scoringresult.QueryScoringResultDTO;
import com.lms.lmsdada.dao.dto.useranswer.CreateUserAnswerDTO;
import com.lms.lmsdada.dao.dto.useranswer.DeleteUserAnswerDTO;
import com.lms.lmsdada.dao.dto.useranswer.QueryUserAnswerDTO;
import com.lms.lmsdada.dao.dto.useranswer.UpdateUserAnswerDTO;
import com.lms.lmsdada.dao.entity.*;
import com.lms.lmsdada.dao.enums.ReviewStatusEnum;
import com.lms.lmsdada.dao.vo.ScoringResultVO;
import com.lms.lmsdada.dao.vo.UserAnswerVO;
import com.lms.lmsdada.dao.vo.UserVO;
import com.lms.lmsdada.service.AppService;
import com.lms.lmsdada.service.UserAnswerService;
import com.lms.lmsdada.service.UserService;
import com.lms.lmsdada.utils.SqlUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lms.lmsdada.dao.factory.UserAnswerFactory.USERANSWER_CONVERTER;
import static com.lms.lmsdada.dao.factory.UserFactory.USER_CONVERTER;

@Service
@RequiredArgsConstructor
public class UserAnswerServiceFacadeImpl {

    private final UserService userService;
    private final UserAnswerService userAnswerService;

    private final AppService appService;

    /**
     * 删除题目
     * @param deleteUserAnswerDTO
     * @param uid
     * @return
     */
    public Boolean deleteUserAnswer(DeleteUserAnswerDTO deleteUserAnswerDTO, Long uid){
        User user = userService.getById(uid);
        long id = deleteUserAnswerDTO.getId();
        // 判断是否存在
        UserAnswer oldUserAnswer = userAnswerService.getById(id);
        BusinessException.throwIf(ObjectUtil.isEmpty(oldUserAnswer), HttpCode.NOT_FOUND_ERROR);
        // 修改数据时，有参数则校验
        // 补充校验规则
        // 仅本人或管理员可删除
        if (!oldUserAnswer.getUserId().equals(user.getId()) && !UserConstant.ADMIN_ROLE.equals(user.getUserRole())) {
            throw new BusinessException(HttpCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = userAnswerService.deleteById(id);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        return Boolean.TRUE;
    }


    /**
     * 创建用户答案
     * @param createUserAnswerDTO
     * @param uid
     * @return
     */
//    public Long createUserAnswer(CreateUserAnswerDTO createUserAnswerDTO,Long uid){
//        UserAnswer userAnswer = new UserAnswer();
//        BeanUtils.copyProperties(createUserAnswerDTO, userAnswer);
//        List<String> choices = createUserAnswerDTO.getChoices();
//        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
//        // 数据校验
//        // 判断 app 是否存在
//        Long appId = createUserAnswerDTO.getAppId();
//        App app = appService.getById(appId);
//        BusinessException.throwIf(app == null, HttpCode.NOT_FOUND_ERROR);
//        if (!ReviewStatusEnum.PASS.equals(ReviewStatusEnum.getEnumByValue(app.getReviewStatus()))) {
//            throw new BusinessException(HttpCode.NO_AUTH_ERROR, "应用未通过审核，无法答题");
//        }
//        // 填充默认值
//
//        userAnswer.setUserId(uid);
//        // 写入数据库
//        boolean result = userAnswerService.save(userAnswer);
//        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
//        // 返回新写入的数据 id
//        long newUserAnswerId = userAnswer.getId();
//        // 调用评分模块
//        try {
//            UserAnswer userAnswerWithResult = scoringStrategyExecutor.doScore(choices, app);
//            userAnswerWithResult.setId(newUserAnswerId);
//            userAnswerService.updateById(userAnswerWithResult);
//        } catch (Exception e) {
//            throw new BusinessException(HttpCode.OPERATION_ERROR, "评分错误");
//        }
//        return newUserAnswerId;
//    }


    /**
     * 根据id获取用户答案
     * @param id
     * @return
     */
    public UserAnswerVO getUserAnswerById(Long id){
        UserAnswerVO userAnswerVO = userAnswerService.selectById(id);
        UserVO userVO = userService.selectById(userAnswerVO.getUserId());
        userAnswerVO.setUser(userVO);
        return userAnswerVO;
    }
    /**
     * 编辑题目
     * @param dto
     * @param uid
     * @return
     */
    public Boolean editUserAnswer(UpdateUserAnswerDTO dto, Long uid){
        UserAnswer userAnswer = new UserAnswer();
        BeanUtils.copyProperties(dto, userAnswer);
        List<String> choices = dto.getChoices();
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        // 数据校验

        App app = appService.getById(dto.getAppId());
        BusinessException.throwIf(ObjectUtil.isEmpty(app),HttpCode.PARAMS_ERROR,"应用不存在");
        User loginUser = userService.getById(uid);
        // 判断是否存在
        long id = dto.getId();
        UserAnswer oldUserAnswer = userAnswerService.getById(id);
        BusinessException.throwIf(oldUserAnswer == null, HttpCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldUserAnswer.getUserId().equals(loginUser.getId()) && !UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole())) {
            throw new BusinessException(HttpCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = userAnswerService.updateById(userAnswer);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        return Boolean.TRUE;
    }

    /**
     * 分页查询(用户)
     * @param queryUserAnswerDTO
     * @param uid
     * @return
     */
    public IPage<UserAnswerVO> getPageVOForUser(QueryUserAnswerDTO queryUserAnswerDTO, Long uid){
        queryUserAnswerDTO.setUserId(uid);
        long current = queryUserAnswerDTO.getCurrent();
        long size = queryUserAnswerDTO.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size > 20, HttpCode.PARAMS_ERROR);
        // 查询数据库
        Page<UserAnswer> questionPage = userAnswerService.page(new Page<>(current, size),
                getQueryWrapper(queryUserAnswerDTO));
        // 获取封装类
        return getUserAnswerVOPage(questionPage);
    }

    /**
     * 分页查询(管理员)
     * @param queryUserAnswerDTO
     * @param uid
     * @return
     */
    public IPage<UserAnswerVO> getPageVOForAdmin(QueryUserAnswerDTO queryUserAnswerDTO){
        long current = queryUserAnswerDTO.getCurrent();
        long size = queryUserAnswerDTO.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size > 20, HttpCode.PARAMS_ERROR);
        // 查询数据库
        Page<UserAnswer> questionPage = userAnswerService.page(new Page<>(current, size),
                getQueryWrapper(queryUserAnswerDTO));
        // 获取封装类
        return getUserAnswerVOPage(questionPage);
    }


    private Page<UserAnswerVO> getUserAnswerVOPage(Page<UserAnswer> userAnswerPage) {
        List<UserAnswer> userAnswerList = userAnswerPage.getRecords();
        Page<UserAnswerVO> userAnswerVOPage = new Page<>(userAnswerPage.getCurrent(), userAnswerPage.getSize(), userAnswerPage.getTotal());
        if (CollUtil.isEmpty(userAnswerList)) {
            return userAnswerVOPage;
        }
        // 对象列表 => 封装对象列表
        List<UserAnswerVO> userAnswerVOList = userAnswerList.stream().map(USERANSWER_CONVERTER::toUserAnswerVO).collect(Collectors.toList());

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = userAnswerList.stream().map(UserAnswer::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        userAnswerVOList.forEach(userAnswerVO -> {
            Long userId = userAnswerVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }

            userAnswerVO.setUser(USER_CONVERTER.toUserVO(user));
        });
        // endregion
        userAnswerVOPage.setRecords(userAnswerVOList);
        return userAnswerVOPage;
    }

    private QueryWrapper<UserAnswer> getQueryWrapper(QueryUserAnswerDTO userAnswerQueryRequest) {
        QueryWrapper<UserAnswer> queryWrapper = new QueryWrapper<>();
        if (userAnswerQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = userAnswerQueryRequest.getId();
        Long appId = userAnswerQueryRequest.getAppId();
        Integer appType = userAnswerQueryRequest.getAppType();
        Integer scoringStrategy = userAnswerQueryRequest.getScoringStrategy();
        String choices = userAnswerQueryRequest.getChoices();
        Long resultId = userAnswerQueryRequest.getResultId();
        String resultName = userAnswerQueryRequest.getResultName();
        String resultDesc = userAnswerQueryRequest.getResultDesc();
        String resultPicture = userAnswerQueryRequest.getResultPicture();
        Integer resultScore = userAnswerQueryRequest.getResultScore();
        Long userId = userAnswerQueryRequest.getUserId();
        Long notId = userAnswerQueryRequest.getNotId();
        String searchText = userAnswerQueryRequest.getSearchText();
        String sortField = userAnswerQueryRequest.getSortField();
        String sortOrder = userAnswerQueryRequest.getSortOrder();

        // 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("resultName", searchText).or().like("resultDesc", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(choices), "choices", choices);
        queryWrapper.like(StringUtils.isNotBlank(resultName), "resultName", resultName);
        queryWrapper.like(StringUtils.isNotBlank(resultDesc), "resultDesc", resultDesc);
        queryWrapper.like(StringUtils.isNotBlank(resultPicture), "resultPicture", resultPicture);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(resultId), "resultId", resultId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(appId), "appId", appId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(appType), "appType", appType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(resultScore), "resultScore", resultScore);
        queryWrapper.eq(ObjectUtils.isNotEmpty(scoringStrategy), "scoringStrategy", scoringStrategy);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(SqlConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

}