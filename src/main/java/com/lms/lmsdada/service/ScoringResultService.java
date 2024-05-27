package com.lms.lmsdada.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lms.lmsdada.dao.entity.ScoringResult;
import com.lms.lmsdada.dao.vo.ScoringResultVO;
import com.lms.lmsdada.dao.dto.scoringresult.QueryScoringResultDTO;
import com.lms.lmsdada.dao.dto.scoringresult.CreateScoringResultDTO;
import com.lms.lmsdada.dao.dto.scoringresult.UpdateScoringResultDTO;

import java.util.List;

/**
 * 评分结果
 *
 * @author LMS2000
 * @since 2024-05-23
 */
public interface ScoringResultService extends IService<ScoringResult> {
    /**
     * 根据主键 查询详情
     *
     * @param id
     * @return
     */
    ScoringResultVO selectById(Long id);



    /***
     *   根据参数 查询数据
     *   @param dto
     *  @return
     */
    List<ScoringResultVO> selectRecordList(QueryScoringResultDTO dto);

    /***
     *   根据主键 更新数据
     *   查询不到数据 BusinessException 异常
     *   @param dto
     *  @return
     */
    Boolean update(UpdateScoringResultDTO dto);


    /***
     *   根据主键 删除数据
     *   查询不到数据 BusinessException 异常
     *   @param id
     *  @return
     */
    Boolean deleteById(Long id);

    /***
     *   插入数据
     *   新检查数据是否传 ，存在返回BusinessException 异常
     *   vo 对象检查必填是否有数据
     *   @param dto
     *  @return
     */
    Long create(CreateScoringResultDTO dto,Long uid);

}