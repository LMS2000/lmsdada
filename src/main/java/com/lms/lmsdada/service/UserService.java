package com.lms.lmsdada.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lms.lmsdada.dao.dto.user.*;
import com.lms.lmsdada.dao.entity.User;
import com.lms.lmsdada.dao.vo.LoginUserVO;
import com.lms.lmsdada.dao.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户
 *
 * @author LMS2000
 * @since 2024-05-23
 */
public interface UserService extends IService<User> {
    /**
     * 根据主键 查询详情
     *
     * @param id
     * @return
     */
    UserVO selectById(Long id);

    /***
     *   根据参数 查询数据
     *   分页
     *   @param dto
     *  @return
     */
    IPage<UserVO> selectRecordPage(QueryUserDTO dto);

    /***
     *   根据参数 查询数据
     *   @param dto
     *  @return
     */
    List<UserVO> selectRecordList(QueryUserDTO dto);

    /***
     *   根据主键 更新数据
     *   查询不到数据 BusinessException 异常
     *   @param dto
     *  @return
     */
    Boolean update(UpdateUserDTO dto);


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
    Long create(CreateUserDTO dto);

    User getLoginUser(HttpServletRequest request);

    UserVO getLoginUserVO(User user);



    LoginUserVO userLogin(UserLoginDTO userLoginRequest);


    Long userRegister(UserRegisterDTO userRegisterRequest);

    LoginUserVO getUserVO(User user);

    Boolean updateCurrentUser(UpdateUserDTO userUpdateRequest);
}