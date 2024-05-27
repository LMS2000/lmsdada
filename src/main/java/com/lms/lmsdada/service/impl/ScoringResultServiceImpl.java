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
import com.lms.lmsdada.dao.dto.scoringresult.CreateScoringResultDTO;
import com.lms.lmsdada.dao.dto.scoringresult.QueryScoringResultDTO;
import com.lms.lmsdada.dao.dto.scoringresult.UpdateScoringResultDTO;
import com.lms.lmsdada.dao.entity.ScoringResult;
import com.lms.lmsdada.dao.vo.ScoringResultVO;
import com.lms.lmsdada.service.ScoringResultService;
import com.lms.lmsdada.mapper.ScoringResultMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lms.lmsdada.dao.factory.ScoringResultFactory.SCORINGRESULT_CONVERTER;

/**
 * 评分结果 服务实现类
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Service
public class ScoringResultServiceImpl extends ServiceImpl<ScoringResultMapper, ScoringResult> implements ScoringResultService {
    /**
     * 根据id获取明细
     *
     * @param id
     * @return
     */
    @Override
    public ScoringResultVO selectById(Long id) {
        return SCORINGRESULT_CONVERTER.toScoringResultVO(baseMapper.selectById(id));
    }

    /***
     *  根据参数 分页查询数据
     *  @param dto
     *  @return
     */
    @Override
    public List<ScoringResultVO> selectRecordList(QueryScoringResultDTO dto) {
        ScoringResult scoringResult = new ScoringResult();
        BeanUtil.copyProperties(dto, scoringResult);
        List<ScoringResult> scoringResultList = baseMapper.selectList(new QueryWrapper<>(scoringResult));
        return SCORINGRESULT_CONVERTER.toListScoringResultVO(scoringResultList);
    }

    /**
     * 根据参数修改数据
     *
     * @param dto
     * @return
     */
    @Override
    public Boolean update(UpdateScoringResultDTO dto) {
        // 在此处将实体类和 DTO 进行转换
        ScoringResult scoringResult = new ScoringResult();
        BeanUtils.copyProperties(dto, scoringResult);
        List<String> resultProp = dto.getResultProp();
        scoringResult.setResultProp(JSONUtil.toJsonStr(resultProp));
        // 数据校验
        // 判断是否存在
        long id = dto.getId();
        ScoringResult oldScoringResult = this.getById(id);
        BusinessException.throwIf(ObjectUtil.isEmpty(oldScoringResult), HttpCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = this.updateById(scoringResult);
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
        ScoringResultVO scoringResultVO = selectById(id);
        BusinessException.throwIf(ObjectUtil.isEmpty(scoringResultVO), "删除内容不存在");
        return deleteById(id);
    }

    /**
     * 创建数据
     *
     * @param dto
     * @return
     */
    @Override
    public Long create(CreateScoringResultDTO dto,Long uid) {
        ScoringResult scoringResult = new ScoringResult();
        BeanUtils.copyProperties(dto, scoringResult);
        List<String> resultProp = dto.getResultProp();
        scoringResult.setResultProp(JSONUtil.toJsonStr(resultProp));
        // 填充默认值
        scoringResult.setUserId(uid);
        // 写入数据库
        boolean result = this.save(scoringResult);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        // 返回新写入的数据 id
         return  scoringResult.getId();
    }


}
