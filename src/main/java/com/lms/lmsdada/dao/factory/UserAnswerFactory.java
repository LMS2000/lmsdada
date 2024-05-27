package com.lms.lmsdada.dao.factory;

import com.lms.lmsdada.dao.entity.UserAnswer;
import com.lms.lmsdada.dao.vo.UserAnswerVO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * userAnswer字段转换类
 *
 * @author LMS2000
 * @since 2024-05-23
 */
public class UserAnswerFactory {
    public static final UserAnswerFactory.UserAnswerConverter USERANSWER_CONVERTER = Mappers.getMapper(UserAnswerFactory.UserAnswerConverter.class);

    @Mapper
    public interface UserAnswerConverter {
        @Mappings({

        })
        @Mapping(target = "choices",  expression = "java(com.lms.lmsdada.utils.MapStructUtil.convertToChoices(entity.getChoices()))")

        UserAnswerVO toUserAnswerVO(UserAnswer entity);
        @IterableMapping(qualifiedByName = "toUserAnswerVO")
        List<UserAnswerVO> toListUserAnswerVO(List<UserAnswer> entityList);
    }

}
