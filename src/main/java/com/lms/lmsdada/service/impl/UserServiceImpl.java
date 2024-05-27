package com.lms.lmsdada.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lms.contants.HttpCode;
import com.lms.lmsdada.constant.SqlConstant;
import com.lms.exception.BusinessException;
import com.lms.lmsdada.dao.dto.user.*;
import com.lms.lmsdada.dao.entity.User;
import com.lms.lmsdada.dao.vo.LoginUserVO;
import com.lms.lmsdada.dao.vo.UserVO;
import com.lms.lmsdada.mapper.UserMapper;
import com.lms.lmsdada.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.lms.lmsdada.constant.UserConstant.DEFAULT_ROLE;
import static com.lms.lmsdada.constant.UserConstant.SALT;
import static com.lms.lmsdada.dao.factory.UserFactory.USER_CONVERTER;

/**
 * 用户 服务实现类
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    /**
     * 根据id获取明细
     *
     * @param id
     * @return
     */
    @Override
    public UserVO selectById(Long id) {
        return USER_CONVERTER.toUserVO(baseMapper.selectById(id));
    }

    /***
     *   根据参数 查询数据
     *   @param dto
     *  @return
     */
    @Override
    public Page<UserVO> selectRecordPage(QueryUserDTO dto) {
        long current = dto.getCurrent();
        long size = dto.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size > 20, HttpCode.PARAMS_ERROR);
        Page<User> userPage = this.page(new Page<>(current, size),
                getQueryWrapper(dto));
        List<UserVO> userVOList = USER_CONVERTER.toListUserVO(userPage.getRecords());
        Page<UserVO> userVOPage = new Page<>(current, size);
        userVOPage.setRecords(userVOList);
        userVOPage.setTotal(userVOList.size());
        return userVOPage;
    }

    /***
     *  根据参数 分页查询数据
     *  @param dto
     *  @return
     */
    @Override
    public List<UserVO> selectRecordList(QueryUserDTO dto) {
        User user = new User();
        BeanUtil.copyProperties(dto, user);
        List<User> userList = baseMapper.selectList(new QueryWrapper<>(user));
        return USER_CONVERTER.toListUserVO(userList);
    }

    /**
     * 根据参数修改数据
     *
     * @param dto
     * @return
     */
    @Override
    public Boolean update(UpdateUserDTO dto) {
        UserVO userVO = selectById(dto.getId());
        BusinessException.throwIf(ObjectUtil.isEmpty(userVO), "修改内容不存在");
        User user = new User();
        BeanUtil.copyProperties(dto, user);
        return updateById(user);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @Override
    public Boolean deleteById(Long id) {
        UserVO userVO = selectById(id);
        BusinessException.throwIf(ObjectUtil.isEmpty(userVO), "删除内容不存在");
        return deleteById(id);
    }

    /**
     * 创建数据
     *
     * @param dto
     * @return
     */
    @Override
    public Long create(CreateUserDTO dto) {
        User user = new User();
        BeanUtil.copyProperties(dto, user);
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + user.getPassword()).getBytes());
        user.setPassword(encryptPassword);
        save(user);
        return user.getId();
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        return null;
    }

    @Override
    public UserVO getLoginUserVO(User user) {
        return null;
    }



    @Override
    public LoginUserVO userLogin(UserLoginDTO userLoginRequest) {
        String username = userLoginRequest.getUsername();
        String userPassword = userLoginRequest.getPassword();
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("username",username).eq("password",encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (ObjectUtil.isEmpty(user)) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(HttpCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        LoginUserVO loginUserVO=new LoginUserVO();
        BeanUtil.copyProperties(user,loginUserVO);
        // 3. 记录用户的登录态
        return loginUserVO;
    }





    @Override
    public Long userRegister(UserRegisterDTO userRegisterRequest) {
        // 1. 校验
        String username = userRegisterRequest.getUsername();
        String userPassword = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String nickname = userRegisterRequest.getNickname();
        BusinessException.throwIf(!userPassword.equals(checkPassword), "两次密码不一致");
        //使用同步块来保证在高并发的环境下,同一时间有很多人用同一个账号注册冲突
        synchronized (username.intern()) {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username", username);
            Long aLong = this.baseMapper.selectCount(wrapper);
            BusinessException.throwIf(aLong > 0, "账号已存在！");
            // 3. 插入数据
            User user = new User();
            user.setUsername(username);
            user.setNickname(nickname);
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            user.setPassword(encryptPassword);
            user.setUserRole(DEFAULT_ROLE);
            int insert = this.baseMapper.insert(user);
            BusinessException.throwIf(insert < 0, "注册失败");
            return user.getId();
        }
    }

    @Override
    public LoginUserVO getUserVO(User user) {
      LoginUserVO loginUserVO=new LoginUserVO();
      BeanUtil.copyProperties(user,loginUserVO);
      return loginUserVO;
    }

    @Override
    public Boolean updateCurrentUser(UpdateUserDTO userUpdateRequest) {
        User user = new User();
        //防止恶意修改
        user.setUserRole("");
        BeanUtils.copyProperties(userUpdateRequest, user);
        return this.updateById(user);
    }

    /**
     * 封装查询参数
     *
     * @param dto
     * @return
     */
    private QueryWrapper<User> getQueryWrapper(QueryUserDTO dto) {
        BusinessException.throwIf(dto == null, HttpCode.NOT_FOUND_ERROR);
        User userQuery = new User();
        BeanUtils.copyProperties(dto, userQuery);
        String sortField = dto.getSortField();
        String sortOrder = dto.getSortOrder();
        QueryWrapper<User> wrapper = new QueryWrapper<>(userQuery);
        wrapper.orderBy(StringUtils.isNotBlank(sortField)
                , sortOrder.equals(SqlConstant.SORT_ORDER_ASC), sortField);
        return wrapper;
    }
}
