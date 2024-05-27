package com.lms.lmsdada.dao.factory;

import com.lms.lmsdada.dao.entity.Question;
import com.lms.lmsdada.dao.vo.QuestionVO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * question字段转换类
 *
 * @author LMS2000
 * @since 2024-05-23
 */
public class QuestionFactory {
    public static final QuestionFactory.QuestionConverter QUESTION_CONVERTER = Mappers.getMapper(QuestionFactory.QuestionConverter.class);

    @Mapper
    public interface QuestionConverter {

        @Mapping(target = "questionContent",  expression = "java(com.lms.lmsdada.utils.MapStructUtil.convertToQuestionContents(entity.getQuestionContent()))")
        @Named("toQuestionVO")
        QuestionVO toQuestionVO(Question entity);
        @IterableMapping(qualifiedByName = "toQuestionVO")
        List<QuestionVO> toListQuestionVO(List<Question> entityList);
    }

}
