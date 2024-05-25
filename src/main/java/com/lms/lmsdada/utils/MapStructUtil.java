package com.lms.lmsdada.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import com.lms.lmsdada.dao.dto.question.QuestionContentDTO;
import org.mapstruct.Named;

import java.util.List;

public class MapStructUtil {


    /**
     * 转换为modelconfig
     *
     * @param json
     * @return
     */
    @Named("convertToQuestionContents")
    public static List<QuestionContentDTO> convertToQuestionContents(String json) {
        if(StrUtil.isEmpty(json)){
            return null;
        }
        return JSONUtil.toList(json, QuestionContentDTO.class);
    }

    @Named("convertToResultProp")
    public static List<String> convertToresultProp(String json){
        if(StrUtil.isEmpty(json)){
            return null;
        }
        return JSONUtil.toList(json,String.class);
    }
    @Named("convertToChoices")
    public static List<String> convertToChoices(String json){
        if(StrUtil.isEmpty(json)){
            return null;
        }
        return JSONUtil.toList(json,String.class);
    }
    public static <T> T convertToClass(String source, Class<?> clazz) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            JavaType javaType = typeFactory.constructType(clazz);
            return objectMapper.readValue(source, javaType);
        } catch (JsonProcessingException e) {
            throw new BusinessException(HttpCode.OPERATION_ERROR,e.getMessage());
        }
    }
}
