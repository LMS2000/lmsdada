package com.lms.lmsdada.dao.dto.question;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
/**
 * 删除请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
@ApiModel(value = "DeleteQuestionDTO", description = "删除题目")
public class DeleteQuestionDTO implements Serializable {
    /**
     * id
     */
    @NotNull(message = "id不能为空")
    @Positive(message = "id不合法")
    private Long id;

    private static final long serialVersionUID = 1L;
}
