package com.lms.lmsdada.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lms.lmsdada.dao.entity.Question;
import com.lms.lmsdada.dao.vo.QuestionVO;
import com.lms.lmsdada.dao.dto.question.QueryQuestionDTO;
import com.lms.lmsdada.dao.dto.question.CreateQuestionDTO;
import com.lms.lmsdada.dao.dto.question.UpdateQuestionDTO;

import java.util.List;

/**
 * 题目
 *
 * @author LMS2000
 * @since 2024-05-23
 */
public interface QuestionService extends IService<Question> {
    /**
     * 根据主键 查询详情
     *
     * @param id
     * @return
     */
    QuestionVO selectById(Long id);


    /***
     *   根据参数 查询数据
     *   @param dto
     *  @return
     */
    List<QuestionVO> selectRecordList(QueryQuestionDTO dto);

    /***
     *   根据主键 更新数据
     *   查询不到数据 BusinessException 异常
     *   @param dto
     *  @return
     */
    Boolean update(Question dto);


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
    Long create(CreateQuestionDTO dto,Long uid);

}