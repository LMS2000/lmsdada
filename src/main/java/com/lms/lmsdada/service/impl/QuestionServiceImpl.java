package com.lms.lmsdada.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lms.lmsdada.constant.SqlConstant;
import com.lms.contants.HttpCode;

import com.lms.exception.BusinessException;
import com.lms.lmsdada.dao.dto.question.CreateQuestionDTO;
import com.lms.lmsdada.dao.dto.question.QueryQuestionDTO;
import com.lms.lmsdada.dao.dto.question.QuestionContentDTO;
import com.lms.lmsdada.dao.dto.question.UpdateQuestionDTO;
import com.lms.lmsdada.dao.entity.Question;
import com.lms.lmsdada.dao.vo.QuestionVO;

import com.lms.lmsdada.mapper.QuestionMapper;
import com.lms.lmsdada.service.QuestionService;
import com.lms.lmsdada.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lms.lmsdada.dao.factory.QuestionFactory.QUESTION_CONVERTER;

/**
 * 题目 服务实现类
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
    /**
     * 根据id获取明细
     *
     * @param id
     * @return
     */
    @Override
    public QuestionVO selectById(Long id) {
        return QUESTION_CONVERTER.toQuestionVO(baseMapper.selectById(id));
    }

    /***
     *   根据参数 查询数据
     *   @param dto
     *  @return
     */




    /***
     *  根据参数 分页查询数据
     *  @param dto
     *  @return
     */
    @Override
    public List<QuestionVO> selectRecordList(QueryQuestionDTO dto) {
        Question question = new Question();
        BeanUtil.copyProperties(dto, question);
        List<Question> questionList = baseMapper.selectList(new QueryWrapper<>(question));
        return QUESTION_CONVERTER.toListQuestionVO(questionList);
    }

    /**
     * 根据参数修改数据
     *
     * @param dto
     * @return
     */
    @Override
    public Boolean update(Question dto) {
        QuestionVO questionVO = selectById(dto.getId());
        BusinessException.throwIf(ObjectUtil.isEmpty(questionVO), "修改内容不存在");
        Question question = new Question();
        BeanUtil.copyProperties(dto, question);
        return updateById(question);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @Override
    public Boolean deleteById(Long id) {
        QuestionVO questionVO = selectById(id);
        BusinessException.throwIf(ObjectUtil.isEmpty(questionVO), "删除内容不存在");
        return deleteById(id);
    }

    /**
     * 创建数据
     *
     * @param dto
     * @return
     */
    @Override
    public Long create(CreateQuestionDTO dto,Long uid) {
        Question question = new Question();
        BeanUtils.copyProperties(dto, question);
        List<QuestionContentDTO> questionContentDTO = dto.getQuestionContent();
        question.setQuestionContent(JSONUtil.toJsonStr(questionContentDTO));
        // 数据校验
        // 填充默认值

        question.setUserId(uid);
        // 写入数据库
        boolean result = this.save(question);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        return question.getId();
    }


}
