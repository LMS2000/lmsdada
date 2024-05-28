package com.lms.lmsdada.service.facade;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import com.lms.lmsdada.constant.SqlConstant;
import com.lms.lmsdada.constant.UserConstant;
import com.lms.lmsdada.dao.dto.app.DeleteAppDTO;
import com.lms.lmsdada.dao.dto.app.QueryAppDTO;
import com.lms.lmsdada.dao.dto.app.ReviewAppDTO;
import com.lms.lmsdada.dao.dto.app.UpdateAppDTO;
import com.lms.lmsdada.dao.dto.question.QueryQuestionDTO;
import com.lms.lmsdada.dao.entity.App;
import com.lms.lmsdada.dao.entity.Question;
import com.lms.lmsdada.dao.entity.User;
import com.lms.lmsdada.dao.enums.ReviewStatusEnum;
import com.lms.lmsdada.dao.vo.AppVO;
import com.lms.lmsdada.dao.vo.QuestionVO;
import com.lms.lmsdada.dao.vo.UserVO;
import com.lms.lmsdada.service.AppService;
import com.lms.lmsdada.service.UserService;
import com.lms.lmsdada.utils.SqlUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lms.lmsdada.dao.factory.AppFactory.APP_CONVERTER;
import static com.lms.lmsdada.dao.factory.UserFactory.USER_CONVERTER;

@Service
@RequiredArgsConstructor
public class AppServiceFacadeImpl {


    private final AppService  appService;
    private final UserService userService;



    /**
     * 删除应用
     * @param deleteAppDTO
     * @param uid
     * @return
     */
    public Boolean deleteApp(DeleteAppDTO deleteAppDTO,Long uid){
        User user = userService.getById(uid);
        long id = deleteAppDTO.getId();
        // 判断是否存在
        App oldApp = appService.getById(id);
        BusinessException.throwIf(oldApp == null, HttpCode.NOT_FOUND_ERROR);
        // 修改数据时，有参数则校验
        // 补充校验规则
        // 仅本人或管理员可删除
        if (!oldApp.getUserId().equals(user.getId()) && !UserConstant.ADMIN_ROLE.equals(user.getUserRole())) {
            throw new BusinessException(HttpCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = appService.deleteById(id);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        return Boolean.TRUE;
    }

    public Boolean editApp(UpdateAppDTO updateAppDTO,Long uid){
        // 在此处将实体类和 DTO 进行转换
        App app = new App();
        BeanUtils.copyProperties(updateAppDTO, app);
        String appName = app.getAppName();
        Integer reviewStatus = app.getReviewStatus();
        // 数据校验
        // 修改数据时，有参数则校验
        // 补充校验规则
        if (StringUtils.isNotBlank(appName)) {
            BusinessException.throwIf(appName.length() > 80, HttpCode.PARAMS_ERROR, "应用名称要小于 80");
        }
        if (reviewStatus != null) {
            ReviewStatusEnum reviewStatusEnum = ReviewStatusEnum.getEnumByValue(reviewStatus);
            BusinessException.throwIf(ObjectUtil.isEmpty(reviewStatusEnum), HttpCode.PARAMS_ERROR, "审核状态非法");
        }

        User loginUser = userService.getById(uid);
        // 判断是否存在
        long id = updateAppDTO.getId();
        App oldApp = appService.getById(id);
        BusinessException.throwIf(ObjectUtil.isEmpty(oldApp), HttpCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldApp.getUserId().equals(loginUser.getId()) && !UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole())) {
            throw new BusinessException(HttpCode.NO_AUTH_ERROR);
        }
        // 重置审核状态
        app.setReviewStatus(ReviewStatusEnum.REVIEWING.getValue());
        // 操作数据库
        boolean result = appService.updateById(app);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        return Boolean.TRUE;
    }

    /**
     * 审核应用
     * @param reviewAppDTO
     * @param uid
     * @return
     */
    public Boolean reviewApp(ReviewAppDTO reviewAppDTO,Long uid){
        Long id = reviewAppDTO.getId();
        Integer reviewStatus = reviewAppDTO.getReviewStatus();
        // 校验
        ReviewStatusEnum reviewStatusEnum = ReviewStatusEnum.getEnumByValue(reviewStatus);
        BusinessException.throwIf(ObjectUtil.isEmpty(reviewStatusEnum),HttpCode.PARAMS_ERROR,"审核状态非法");
        // 判断是否存在
        App oldApp = appService.getById(id);
        BusinessException.throwIf(ObjectUtil.isEmpty(oldApp), HttpCode.NOT_FOUND_ERROR);
        // 已是该状态
        if (oldApp.getReviewStatus().equals(reviewStatus)) {
           return Boolean.TRUE;
        }
        // 更新审核状态
        User loginUser = userService.getById(uid);
        App app = new App();
        app.setId(id);
        app.setReviewStatus(reviewStatus);
        app.setReviewMessage(reviewAppDTO.getReviewMessage());
        app.setReviewerId(loginUser.getId());
        app.setReviewTime(new Date());
        boolean result = appService.updateById(app);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        return Boolean.TRUE;
    }


    /**
     * 根据id获取应用
     * @param id
     * @return
     */
    public AppVO getAppVoById(Long id){
        AppVO appVO = appService.selectById(id);
        UserVO userVO = userService.selectById(appVO.getUserId());
        appVO.setUser(userVO);
        return appVO;
    }

    /**
     * 分页查询(用户)
     * @param queryAppDTO
     * @param uid
     * @return
     */
    public IPage<AppVO> getPageVOForUser(QueryAppDTO queryAppDTO, Long uid){
        queryAppDTO.setUserId(uid);
        long current = queryAppDTO.getCurrent();
        long size = queryAppDTO.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size > 20, HttpCode.PARAMS_ERROR);
        // 查询数据库
        Page<App> questionPage = appService.page(new Page<>(current, size),
                getQueryWrapper(queryAppDTO));
        // 获取封装类
        return getAppVOPage(questionPage);
    }
    /**
     * 分页查询(管理员)
     * @param queryAppDTO
     * @return
     */
    public IPage<AppVO> getPageVOForAdmin(QueryAppDTO queryAppDTO){
        long current = queryAppDTO.getCurrent();
        long size = queryAppDTO.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size > 20, HttpCode.PARAMS_ERROR);
        // 查询数据库
        Page<App> questionPage = appService.page(new Page<>(current, size),
                getQueryWrapper(queryAppDTO));
        // 获取封装类
        return getAppVOPage(questionPage);
    }

    private QueryWrapper<App> getQueryWrapper(QueryAppDTO appQueryRequest) {
        QueryWrapper<App> queryWrapper = new QueryWrapper<>();
        if (appQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String appDesc = appQueryRequest.getAppDesc();
        String appIcon = appQueryRequest.getAppIcon();
        Integer appType = appQueryRequest.getAppType();
        Integer scoringStrategy = appQueryRequest.getScoringStrategy();
        Integer reviewStatus = appQueryRequest.getReviewStatus();
        String reviewMessage = appQueryRequest.getReviewMessage();
        Long reviewerId = appQueryRequest.getReviewerId();
        Long userId = appQueryRequest.getUserId();
        Long notId = appQueryRequest.getNotId();
        String searchText = appQueryRequest.getSearchText();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();

        // 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("app_name", searchText).or().like("app_desc", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(appName), "app_name", appName);
        queryWrapper.like(StringUtils.isNotBlank(appDesc), "app_desc", appDesc);
        queryWrapper.like(StringUtils.isNotBlank(reviewMessage), "review_message", reviewMessage);
        // 精确查询
        queryWrapper.eq(StringUtils.isNotBlank(appIcon), "app_icon", appIcon);
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(appType), "app_type", appType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(scoringStrategy), "scoring_strategy", scoringStrategy);
        queryWrapper.eq(ObjectUtils.isNotEmpty(reviewStatus), "review_status", reviewStatus);
        queryWrapper.eq(ObjectUtils.isNotEmpty(reviewerId), "reviewer_id", reviewerId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "user_id", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(SqlConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    public Page<AppVO> getAppVOPage(Page<App> appPage) {
        List<App> appList = appPage.getRecords();
        Page<AppVO> appVOPage = new Page<>(appPage.getCurrent(), appPage.getSize(), appPage.getTotal());
        if (CollUtil.isEmpty(appList)) {
            return appVOPage;
        }
        // 对象列表 => 封装对象列表
        List<AppVO> appVOList = appList.stream().map(APP_CONVERTER::toAppVO).collect(Collectors.toList());

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = appList.stream().map(App::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        appVOList.forEach(appVO -> {
            Long userId = appVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            appVO.setUser(USER_CONVERTER.toUserVO(user));
        });
        // endregion

        appVOPage.setRecords(appVOList);
        return appVOPage;
    }

}
