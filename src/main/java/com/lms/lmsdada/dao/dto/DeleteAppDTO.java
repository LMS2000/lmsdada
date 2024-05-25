package com.lms.lmsdada.dao.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * 删除请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
@ApiModel(value = "DeleteAppDTO", description = "删除应用")
public class DeleteAppDTO implements Serializable {

    /**
     * id
     */@Positive(message = "id不合法")
    private Long id;

    private static final long serialVersionUID = 1L;
}
