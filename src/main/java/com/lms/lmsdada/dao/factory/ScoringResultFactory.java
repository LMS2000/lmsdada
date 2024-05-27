package com.lms.lmsdada.dao.factory;

import com.lms.lmsdada.dao.entity.ScoringResult;
import com.lms.lmsdada.dao.vo.ScoringResultVO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * scoringResult字段转换类
 *
 * @author LMS2000
 * @since 2024-05-23
 */
public class ScoringResultFactory {
    public static final ScoringResultFactory.ScoringResultConverter SCORINGRESULT_CONVERTER = Mappers.getMapper(ScoringResultFactory.ScoringResultConverter.class);

    @Mapper
    public interface ScoringResultConverter {
        @Mappings({

        })
        @Mapping(target = "resultProp",  expression = "java(com.lms.lmsdada.utils.MapStructUtil.convertToResultProp(entity.getResultProp()))")
        ScoringResultVO toScoringResultVO(ScoringResult entity);
        @IterableMapping(qualifiedByName = "toScoringResultVO")
        List<ScoringResultVO> toListScoringResultVO(List<ScoringResult> entityList);
    }

}
