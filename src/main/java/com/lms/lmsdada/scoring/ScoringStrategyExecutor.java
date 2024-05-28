package com.lms.lmsdada.scoring;

import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import com.lms.lmsdada.dao.entity.App;
import com.lms.lmsdada.dao.entity.UserAnswer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.rowset.serial.SerialException;
import java.util.List;

/**
 * 评分策略执行器
 */
@Service
public class ScoringStrategyExecutor {

    // 策略列表
    @Resource
    private List<ScoringStrategy> scoringStrategyList;


    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        Integer appType = app.getAppType();
        Integer scoringStrategy = app.getScoringStrategy();
        if (appType == null || scoringStrategy == null) {
            throw new BusinessException(HttpCode.SYSTEM_ERROR, "应用配置有误，未找到匹配的策略");
        }

        for (ScoringStrategy strategy : scoringStrategyList) {
            if (strategy.getClass().isAnnotationPresent(ScoringStrategyConfig.class)) {
                ScoringStrategyConfig config = strategy.getClass().getAnnotation(ScoringStrategyConfig.class);
                if (config.scoringStrategy() == scoringStrategy && config.appType() == appType) {
                    return strategy.doScore(choices, app);
                }
            }
        }
        throw new BusinessException(HttpCode.SYSTEM_ERROR, "应用配置有误，未找到匹配的策略");

    }
}
