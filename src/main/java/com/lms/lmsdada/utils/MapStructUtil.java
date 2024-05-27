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
     * @param questionContents
     * @return
     */
    @Named("convertToQuestionContents")
    public static List<QuestionContentDTO> convertToQuestionContents(String questionContents) {
        if(StrUtil.isEmpty(questionContents)){
            return null;
        }
        return JSONUtil.toList(questionContents, QuestionContentDTO.class);
    }



    /**
     * 将 json字符串的tags转换为List<String>
     *
     * @param tags
     * @return
     */
    @Named("convertToList")
    public static List<String> convertToList(String tags) {
        if(StrUtil.isEmpty(tags)){
            return null;
        }
        return convertToClass(tags, List.class);
    }
    @Named("convertToChoices")
    public static List<String> convertToChoices(String choices){
        if(StrUtil.isEmpty(choices)){
            return null;
        }
        return JSONUtil.toList(choices,String.class);
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
    @Named("convertToResultProp")
    public static List<String> convertToResultProp(String resultProp) {
        if(StrUtil.isEmpty(resultProp)){
            return null;
        }
        return JSONUtil.toList(resultProp,String.class);
    }
}
