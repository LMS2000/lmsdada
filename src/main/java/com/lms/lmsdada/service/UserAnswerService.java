package com.lms.lmsdada.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lms.lmsdada.dao.entity.UserAnswer;
import com.lms.lmsdada.dao.vo.UserAnswerVO;
import com.lms.lmsdada.dao.dto.useranswer.QueryUserAnswerDTO;
import com.lms.lmsdada.dao.dto.useranswer.CreateUserAnswerDTO;
import com.lms.lmsdada.dao.dto.useranswer.UpdateUserAnswerDTO;

import java.util.List;

/**
 * 用户答题记录
 *
 * @author LMS2000
 * @since 2024-05-23
 */
public interface UserAnswerService extends IService<UserAnswer> {
    /**
     * 根据主键 查询详情
     *
     * @param id
     * @return
     */
    UserAnswerVO selectById(Long id);

    /***
     *   根据参数 查询数据
     *   分页
     *   @param dto
     *  @return
     */
    IPage<UserAnswerVO> selectRecordPage(QueryUserAnswerDTO dto);

    /***
     *   根据参数 查询数据
     *   @param dto
     *  @return
     */
    List<UserAnswerVO> selectRecordList(QueryUserAnswerDTO dto);

    /***
     *   根据主键 更新数据
     *   查询不到数据 BusinessException 异常
     *   @param dto
     *  @return
     */
    Boolean update(UpdateUserAnswerDTO dto);


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
    Long create(CreateUserAnswerDTO dto,Long uid);

}