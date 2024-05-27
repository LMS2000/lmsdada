package com.lms.lmsdada.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lms.contants.HttpCode;
import com.lms.lmsdada.constant.SqlConstant;
import com.lms.exception.BusinessException;
import com.lms.lmsdada.dao.dto.useranswer.CreateUserAnswerDTO;
import com.lms.lmsdada.dao.dto.useranswer.QueryUserAnswerDTO;
import com.lms.lmsdada.dao.dto.useranswer.UpdateUserAnswerDTO;
import com.lms.lmsdada.dao.entity.UserAnswer;
import com.lms.lmsdada.dao.vo.UserAnswerVO;
import com.lms.lmsdada.mapper.UserAnswerMapper;
import com.lms.lmsdada.service.UserAnswerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lms.lmsdada.dao.factory.UserAnswerFactory.USERANSWER_CONVERTER;

/**
 * 用户答题记录 服务实现类
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Service
public class UserAnswerServiceImpl extends ServiceImpl<UserAnswerMapper, UserAnswer> implements UserAnswerService {
    /**
     * 根据id获取明细
     *
     * @param id
     * @return
     */
    @Override
    public UserAnswerVO selectById(Long id) {
        return USERANSWER_CONVERTER.toUserAnswerVO(baseMapper.selectById(id));
    }

    /***
     *   根据参数 查询数据
     *   @param dto
     *  @return
     */
    @Override
    public Page<UserAnswerVO> selectRecordPage(QueryUserAnswerDTO dto) {
        long current = dto.getCurrent();
        long size = dto.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size > 20, HttpCode.PARAMS_ERROR);
        Page<UserAnswer> userAnswerPage = this.page(new Page<>(current, size),
                getQueryWrapper(dto));
        List<UserAnswerVO> userAnswerVOList = USERANSWER_CONVERTER.toListUserAnswerVO(userAnswerPage.getRecords());
        Page<UserAnswerVO> userAnswerVOPage = new Page<>(current, size);
        userAnswerVOPage.setRecords(userAnswerVOList);
        userAnswerVOPage.setTotal(userAnswerVOList.size());
        return userAnswerVOPage;
    }

    /***
     *  根据参数 分页查询数据
     *  @param dto
     *  @return
     */
    @Override
    public List<UserAnswerVO> selectRecordList(QueryUserAnswerDTO dto) {
        UserAnswer userAnswer = new UserAnswer();
        BeanUtil.copyProperties(dto, userAnswer);
        List<UserAnswer> userAnswerList = baseMapper.selectList(new QueryWrapper<>(userAnswer));
        return USERANSWER_CONVERTER.toListUserAnswerVO(userAnswerList);
    }

    /**
     * 根据参数修改数据
     *
     * @param dto
     * @return
     */
    @Override
    public Boolean update(UpdateUserAnswerDTO dto) {
        // 在此处将实体类和 DTO 进行转换
        UserAnswer userAnswer = new UserAnswer();
        BeanUtils.copyProperties(dto, userAnswer);
        List<String> choices = dto.getChoices();
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        // 判断是否存在
        long id = dto.getId();
        UserAnswer oldUserAnswer = this.getById(id);
        BusinessException.throwIf(oldUserAnswer == null, HttpCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = this.updateById(userAnswer);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        return Boolean.TRUE;
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @Override
    public Boolean deleteById(Long id) {
        UserAnswerVO userAnswerVO = selectById(id);
        BusinessException.throwIf(ObjectUtil.isEmpty(userAnswerVO), "删除内容不存在");
        return deleteById(id);
    }

    /**
     * 创建数据
     *
     * @param dto
     * @return
     */
    @Override
    public Long create(CreateUserAnswerDTO dto,Long uid) {
        UserAnswer userAnswer = new UserAnswer();
        BeanUtil.copyProperties(dto, userAnswer);
        save(userAnswer);
        return userAnswer.getId();
    }

    /**
     * 封装查询参数
     *
     * @param dto
     * @return
     */
    private QueryWrapper<UserAnswer> getQueryWrapper(QueryUserAnswerDTO dto) {
        BusinessException.throwIf(dto == null, HttpCode.NOT_FOUND_ERROR);
        UserAnswer UserAnswerQuery = new UserAnswer();
        BeanUtils.copyProperties(dto, UserAnswerQuery);
        String sortField = dto.getSortField();
        String sortOrder = dto.getSortOrder();
        QueryWrapper<UserAnswer> wrapper = new QueryWrapper<>(UserAnswerQuery);
        wrapper.orderBy(StringUtils.isNotBlank(sortField)
                , sortOrder.equals(SqlConstant.SORT_ORDER_ASC), sortField);
        return wrapper;
    }
}
