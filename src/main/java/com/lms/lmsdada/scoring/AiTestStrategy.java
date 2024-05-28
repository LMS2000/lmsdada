package com.lms.lmsdada.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lms.lmsdada.config.AiManager;
import com.lms.lmsdada.constant.AIConstant;
import com.lms.lmsdada.dao.dto.question.QuestionAnswerDTO;
import com.lms.lmsdada.dao.dto.question.QuestionContentDTO;
import com.lms.lmsdada.dao.entity.App;
import com.lms.lmsdada.dao.entity.Question;
import com.lms.lmsdada.dao.entity.UserAnswer;
import com.lms.lmsdada.dao.vo.QuestionVO;
import com.lms.lmsdada.service.QuestionService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.lms.lmsdada.dao.factory.QuestionFactory.QUESTION_CONVERTER;

@ScoringStrategyConfig(appType = 1, scoringStrategy = 1)
public class AiTestStrategy implements ScoringStrategy {
    @Resource
    private QuestionService questionService;

    @Resource
    private AiManager aiManager;
    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        Long appId = app.getId();
        // 1. 根据 id 查询到题目
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, appId)
        );
        QuestionVO questionVO = QUESTION_CONVERTER.toQuestionVO(question);
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();

        // 2. 调用 AI 获取结果
        // 封装 Prompt
        String userMessage = getAiTestScoringUserMessage(app, questionContent, choices);
        // AI 生成
        String result = aiManager.doSyncStableRequest(AIConstant.GENERATE_QUESTION_SYSTEM_MESSAGE, userMessage);
        // 截取需要的 JSON 信息
        int start = result.indexOf("{");
        int end = result.lastIndexOf("}");
        String json = result.substring(start, end + 1);

        // 3. 构造返回值，填充答案对象的属性
        UserAnswer userAnswer = JSONUtil.toBean(json, UserAnswer.class);
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        return userAnswer;
    }
    /**
     * AI 评分用户消息封装
     *
     * @param app
     * @param questionContentDTOList
     * @param choices
     * @return
     */
    private String getAiTestScoringUserMessage(App app, List<QuestionContentDTO> questionContentDTOList, List<String> choices) {
        StringBuilder userMessage = new StringBuilder();
        userMessage.append(app.getAppName()).append("\n");
        userMessage.append(app.getAppDesc()).append("\n");
        List<QuestionAnswerDTO> questionAnswerDTOList = new ArrayList<>();
        for (int i = 0; i < questionContentDTOList.size(); i++) {
            QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
            questionAnswerDTO.setTitle(questionContentDTOList.get(i).getTitle());
            questionAnswerDTO.setUserAnswer(choices.get(i));
            questionAnswerDTOList.add(questionAnswerDTO);
        }
        userMessage.append(JSONUtil.toJsonStr(questionAnswerDTOList));
        return userMessage.toString();
    }
}
