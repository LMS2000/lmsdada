package com.lms.lmsdada.dao.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件上传请求
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "UploadFileRequest对象", description = "文件上传")
public class UploadFileDTO {
    /**
     * 业务
     */
    @ApiModelProperty(value = "业务类型")
    private String biz;
}
