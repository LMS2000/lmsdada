package com.lms.lmsdada.service.facade;

import cn.hutool.core.util.ObjectUtil;
import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import com.lms.lmsdada.constant.UserConstant;
import com.lms.lmsdada.dao.dto.CreateAppDTO;
import com.lms.lmsdada.dao.dto.DeleteAppDTO;
import com.lms.lmsdada.dao.entity.App;
import com.lms.lmsdada.dao.entity.User;
import com.lms.lmsdada.dao.enums.AppScoringStrategyEnum;
import com.lms.lmsdada.dao.enums.AppTypeEnum;
import com.lms.lmsdada.dao.enums.ReviewStatusEnum;
import com.lms.lmsdada.service.AppService;
import com.lms.lmsdada.service.UserService;
import com.lms.lmsdada.service.impl.AppServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppServiceFacadeImpl {


    private final AppService  appService;
    private final UserService userService;
    public Long addApp(CreateAppDTO createAppDTO,Long uid){
        App app = new App();
        BeanUtils.copyProperties(createAppDTO, app);
        Integer appType = createAppDTO.getAppType();
        Integer scoringStrategy = createAppDTO.getScoringStrategy();

        // 数据校验
        AppTypeEnum appTypeEnum = AppTypeEnum.getEnumByValue(appType);
        BusinessException.throwIf(ObjectUtil.isEmpty(appTypeEnum),HttpCode.PARAMS_ERROR,"应用类别非法");
        AppScoringStrategyEnum scoringStrategyEnum = AppScoringStrategyEnum.getEnumByValue(scoringStrategy);
        BusinessException.throwIf(ObjectUtil.isEmpty(scoringStrategyEnum),HttpCode.PARAMS_ERROR,"应用评分策略非法");
        // 填充默认值
        User loginUser = userService.getById(uid);
        app.setUserId(loginUser.getId());
        app.setReviewStatus(ReviewStatusEnum.REVIEWING.getValue());
        // 写入数据库
        boolean result = appService.save(app);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        // 返回新写入的数据 id
      return app.getId();
    }

    public Boolean deleteApp(DeleteAppDTO deleteAppDTO,Long uid){
        User user = userService.getById(uid);
        long id = deleteAppDTO.getId();
        // 判断是否存在
        App oldApp = appService.getById(id);
        BusinessException.throwIf(oldApp == null, HttpCode.NOT_FOUND_ERROR);
        // 修改数据时，有参数则校验
        // 补充校验规则
        // 仅本人或管理员可删除
        if (!oldApp.getUserId().equals(user.getId()) && !UserConstant.ADMIN_ROLE.equals(user.getUserRole())) {
            throw new BusinessException(HttpCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = appService.deleteById(id);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        return Boolean.TRUE;
    }

}
