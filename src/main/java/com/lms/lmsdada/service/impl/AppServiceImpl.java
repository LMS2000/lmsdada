package com.lms.lmsdada.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lms.contants.HttpCode;
import com.lms.lmsdada.constant.SqlConstant;
import com.lms.exception.BusinessException;
import com.lms.lmsdada.dao.dto.app.CreateAppDTO;
import com.lms.lmsdada.dao.dto.app.QueryAppDTO;
import com.lms.lmsdada.dao.dto.app.UpdateAppDTO;
import com.lms.lmsdada.dao.entity.App;
import com.lms.lmsdada.dao.enums.AppScoringStrategyEnum;
import com.lms.lmsdada.dao.enums.AppTypeEnum;
import com.lms.lmsdada.dao.enums.ReviewStatusEnum;
import com.lms.lmsdada.dao.vo.AppVO;
import com.lms.lmsdada.mapper.AppMapper;
import com.lms.lmsdada.service.AppService;
import com.lms.lmsdada.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lms.lmsdada.dao.factory.AppFactory.APP_CONVERTER;

/**
 * 应用 服务实现类
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {
    /**
     * 根据id获取明细
     *
     * @param id
     * @return
     */
    @Override
    public AppVO selectById(Long id) {
        return APP_CONVERTER.toAppVO(baseMapper.selectById(id));
    }

    /***
     *   根据参数 查询数据
     *   @param dto
     *  @return
     */
    @Override
    public Page<AppVO> selectRecordPageReviewed(QueryAppDTO dto) {
        long current = dto.getCurrent();
        long size = dto.getPageSize();

        Page<App> appPage = this.page(new Page<>(current, size),
                getQueryWrapper(dto));
        List<AppVO> appVOList = APP_CONVERTER.toListAppVO(appPage.getRecords());
        Page<AppVO> appVOPage = new Page<>(current, size);
        appVOPage.setRecords(appVOList);
        appVOPage.setTotal(appVOList.size());
        return appVOPage;
    }



    /***
     *  根据参数 分页查询数据
     *  @param dto
     *  @return
     */
    @Override
    public List<AppVO> selectRecordList(QueryAppDTO dto) {
        App app = new App();
        BeanUtil.copyProperties(dto, app);
        List<App> appList = baseMapper.selectList(new QueryWrapper<>(app));
        return APP_CONVERTER.toListAppVO(appList);
    }

    /**
     * 根据参数修改数据
     *
     * @param dto
     * @return
     */
    @Override
    public Boolean update(UpdateAppDTO dto) {
        String appName = dto.getAppName();
        Integer reviewStatus = dto.getReviewStatus();
        if (StringUtils.isNotBlank(appName)) {
            BusinessException.throwIf(appName.length() > 80, HttpCode.PARAMS_ERROR, "应用名称要小于 80");
        }
        if (reviewStatus != null) {
            ReviewStatusEnum reviewStatusEnum = ReviewStatusEnum.getEnumByValue(reviewStatus);
            BusinessException.throwIf(reviewStatusEnum == null, HttpCode.PARAMS_ERROR, "审核状态非法");
        }
        AppVO appVO = selectById(dto.getId());
        BusinessException.throwIf(ObjectUtil.isEmpty(appVO), "修改内容不存在");
        App app = new App();
        BeanUtil.copyProperties(dto, app);
        return updateById(app);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @Override
    public Boolean deleteById(Long id) {
        AppVO appVO = selectById(id);
        BusinessException.throwIf(ObjectUtil.isEmpty(appVO), "删除内容不存在");
        return deleteById(id);
    }

    /**
     * 创建数据
     *
     * @param dto
     * @return
     */
    @Override
    public Long create(CreateAppDTO dto,Long uid) {

        App app = new App();
        BeanUtils.copyProperties(dto, app);
        Integer appType = dto.getAppType();
        Integer scoringStrategy = dto.getScoringStrategy();

        // 数据校验
        AppTypeEnum appTypeEnum = AppTypeEnum.getEnumByValue(appType);
        BusinessException.throwIf(ObjectUtil.isEmpty(appTypeEnum),HttpCode.PARAMS_ERROR,"应用类别非法");
        AppScoringStrategyEnum scoringStrategyEnum = AppScoringStrategyEnum.getEnumByValue(scoringStrategy);
        BusinessException.throwIf(ObjectUtil.isEmpty(scoringStrategyEnum),HttpCode.PARAMS_ERROR,"应用评分策略非法");
        // 填充默认值
        app.setUserId(uid);
        app.setReviewStatus(ReviewStatusEnum.REVIEWING.getValue());
        // 写入数据库
        boolean result = this.save(app);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        return app.getId();
    }

    /**
     * 封装查询参数
     *
     * @return
     */
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
            queryWrapper.and(qw -> qw.like("app_name", searchText).or().like("appDesc", searchText));
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
}
