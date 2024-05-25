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
import com.lms.lmsdada.dao.dto.question.QueryQuestionDTO;
import com.lms.lmsdada.dao.dto.question.QuestionContentDTO;
import com.lms.lmsdada.dao.dto.scoringresult.DeleteScoringResultDTO;
import com.lms.lmsdada.dao.dto.scoringresult.QueryScoringResultDTO;
import com.lms.lmsdada.dao.dto.scoringresult.UpdateScoringResultDTO;
import com.lms.lmsdada.dao.entity.App;
import com.lms.lmsdada.dao.entity.Question;
import com.lms.lmsdada.dao.entity.ScoringResult;
import com.lms.lmsdada.dao.entity.User;
import com.lms.lmsdada.dao.vo.QuestionVO;
import com.lms.lmsdada.dao.vo.ScoringResultVO;
import com.lms.lmsdada.dao.vo.UserVO;
import com.lms.lmsdada.service.AppService;
import com.lms.lmsdada.service.ScoringResultService;
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

import static com.lms.lmsdada.dao.factory.ScoringResultFactory.SCORINGRESULT_CONVERTER;
import static com.lms.lmsdada.dao.factory.UserFactory.USER_CONVERTER;

@Service
@RequiredArgsConstructor
public class ScoringResultServiceFacadeImpl {


    private final UserService userService;
    private final AppService appService;
    private final ScoringResultService scoringResultService;

    /**
     * 删除结果
     *
     * @param uid
     * @return
     */
    public Boolean deleteScoringResult(DeleteScoringResultDTO deleteScoringResultDTO, Long uid) {
        User user = userService.getById(uid);
        long id = deleteScoringResultDTO.getId();
        // 判断是否存在
        ScoringResult oldScoringResult = scoringResultService.getById(id);
        BusinessException.throwIf(ObjectUtil.isEmpty(oldScoringResult), HttpCode.NOT_FOUND_ERROR);
        // 修改数据时，有参数则校验
        // 补充校验规则
        // 仅本人或管理员可删除
        if (!oldScoringResult.getUserId().equals(user.getId()) && !UserConstant.ADMIN_ROLE.equals(user.getUserRole())) {
            throw new BusinessException(HttpCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = scoringResultService.deleteById(id);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        return Boolean.TRUE;
    }

    /**
     * 更新结果
     *
     * @param dto
     * @return
     */
    public Boolean updateScoringResult(UpdateScoringResultDTO dto) {
        // 在此处将实体类和 DTO 进行转换
        Long appId = dto.getAppId();
        if (appId != null) {
            App app = appService.getById(appId);
            BusinessException.throwIf(ObjectUtil.isEmpty(app), HttpCode.PARAMS_ERROR, "应用不存在");
        }
        return scoringResultService.update(dto);
    }

    /**
     * 根据id获取结果
     *
     * @param id
     * @return
     */
    public ScoringResultVO getScoringResultById(Long id) {
        ScoringResultVO scoringResultVO = scoringResultService.selectById(id);
        UserVO userVO = userService.selectById(scoringResultVO.getUserId());
        scoringResultVO.setUser(userVO);
        return scoringResultVO;
    }

    /**
     * 用户修改结果
     *
     * @param dto
     * @param uid
     * @return
     */
    public Boolean editScoringResult(UpdateScoringResultDTO dto, Long uid) {
        // 在此处将实体类和 DTO 进行转换
        ScoringResult scoringResult = new ScoringResult();
        BeanUtils.copyProperties(dto, scoringResult);
        List<String> resultProp = dto.getResultProp();
        scoringResult.setResultProp(JSONUtil.toJsonStr(resultProp));
        // 数据校验
        Long appId = dto.getAppId();
        if (appId != null) {
            App app = appService.getById(appId);
            BusinessException.throwIf(ObjectUtil.isEmpty(app), HttpCode.PARAMS_ERROR, "应用不存在");
        }
        User loginUser = userService.getById(uid);
        // 判断是否存在
        long id = dto.getId();
        ScoringResult oldScoringResult = scoringResultService.getById(id);
        BusinessException.throwIf(oldScoringResult == null, HttpCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldScoringResult.getUserId().equals(loginUser.getId()) && !UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole())) {
            throw new BusinessException(HttpCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = scoringResultService.updateById(scoringResult);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        return Boolean.TRUE;
    }


    /**
     * 分页查询(用户)
     * @param queryScoringResultDTO
     * @param uid
     * @return
     */
    public IPage<ScoringResultVO> getPageVOForUser(QueryScoringResultDTO queryScoringResultDTO, Long uid){
        queryScoringResultDTO.setUserId(uid);
        long current = queryScoringResultDTO.getCurrent();
        long size = queryScoringResultDTO.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size > 20, HttpCode.PARAMS_ERROR);
        // 查询数据库
        Page<ScoringResult> questionPage = scoringResultService.page(new Page<>(current, size),
                getQueryWrapper(queryScoringResultDTO));
        // 获取封装类
        return getScoringResultVOPage(questionPage);
    }

    /**
     * 分页查询(管理员)
     * @param queryScoringResultDTO
     * @return
     */
    public IPage<ScoringResultVO> getPageVOForAdmin(QueryScoringResultDTO queryScoringResultDTO) {
        long current = queryScoringResultDTO.getCurrent();
        long size = queryScoringResultDTO.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size > 20, HttpCode.PARAMS_ERROR);
        // 查询数据库
        Page<ScoringResult> questionPage = scoringResultService.page(new Page<>(current, size),
                getQueryWrapper(queryScoringResultDTO));
        // 获取封装类
        return getScoringResultVOPage(questionPage);
    }



    private Page<ScoringResultVO> getScoringResultVOPage(Page<ScoringResult> scoringResultPage) {
        List<ScoringResult> scoringResultList = scoringResultPage.getRecords();
        Page<ScoringResultVO> scoringResultVOPage = new Page<>(scoringResultPage.getCurrent(), scoringResultPage.getSize(), scoringResultPage.getTotal());
        if (CollUtil.isEmpty(scoringResultList)) {
            return scoringResultVOPage;
        }
        // 对象列表 => 封装对象列表
        List<ScoringResultVO> scoringResultVOList = scoringResultList.stream().map(SCORINGRESULT_CONVERTER::toScoringResultVO).collect(Collectors.toList());

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = scoringResultList.stream().map(ScoringResult::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        scoringResultVOList.forEach(scoringResultVO -> {
            Long userId = scoringResultVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            scoringResultVO.setUser(USER_CONVERTER.toUserVO(user));
        });
        // endregion
        scoringResultVOPage.setRecords(scoringResultVOList);
        return scoringResultVOPage;
    }

    private QueryWrapper<ScoringResult> getQueryWrapper(QueryScoringResultDTO scoringResultQueryRequest) {
        QueryWrapper<ScoringResult> queryWrapper = new QueryWrapper<>();
        if (scoringResultQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = scoringResultQueryRequest.getId();
        String resultName = scoringResultQueryRequest.getResultName();
        String resultDesc = scoringResultQueryRequest.getResultDesc();
        String resultPicture = scoringResultQueryRequest.getResultPicture();
        String resultProp = scoringResultQueryRequest.getResultProp();
        Integer resultScoreRange = scoringResultQueryRequest.getResultScoreRange();
        Long appId = scoringResultQueryRequest.getAppId();
        Long userId = scoringResultQueryRequest.getUserId();
        Long notId = scoringResultQueryRequest.getNotId();
        String searchText = scoringResultQueryRequest.getSearchText();
        String sortField = scoringResultQueryRequest.getSortField();
        String sortOrder = scoringResultQueryRequest.getSortOrder();

        // 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("result_name", searchText).or().like("resultDesc", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(resultName), "result_name", resultName);
        queryWrapper.like(StringUtils.isNotBlank(resultDesc), "result_desc", resultDesc);
        queryWrapper.like(StringUtils.isNotBlank(resultProp), "result_prop", resultProp);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "user_id", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(appId), "app_id", appId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(resultScoreRange), "result_score_range", resultScoreRange);
        queryWrapper.eq(StringUtils.isNotBlank(resultPicture), "result_picture", resultPicture);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(SqlConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

}
