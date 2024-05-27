package com.lms.lmsdada.dao.factory;

import com.lms.lmsdada.dao.entity.User;
import com.lms.lmsdada.dao.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * user字段转换类
 *
 * @author LMS2000
 * @since 2024-05-23
 */
public class UserFactory {
    public static final UserFactory.UserConverter USER_CONVERTER = Mappers.getMapper(UserFactory.UserConverter.class);

    @Mapper
    public interface UserConverter {
        @Mappings({

        })
        UserVO toUserVO(User entity);

        List<UserVO> toListUserVO(List<User> entityList);
    }

}
