package com.example.ncu.service_impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.ncu.Exception.ResourceNotFoundException;
import com.example.ncu.Interface.OverallSuggestionInterface;
import com.example.ncu.Interface.ScoreInterface;
import com.example.ncu.Interface.SuggestionInterface;
import com.example.ncu.Interface.TimestampAndPositionInterface;
import com.example.ncu.domain.InterviewResult;
import com.example.ncu.repository.InterviewResultRepository;
import com.example.ncu.repository.ResumeRepository;
import com.example.ncu.service.InterviewResultService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;

@org.springframework.stereotype.Service
public class InterviewResultSevice_impl implements InterviewResultService {

	@Autowired
    private InterviewResultRepository interviewResultRepository;
    @Autowired
    private ResumeRepository resumeRepository;

    @Override
    public InterviewResult saveInterviewResult(Integer memberIdInterviewResult, Integer resumeIdInterviewResult, InterviewResult interviewResult) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        try {
            if (interviewResult != null) {
                for (int i = 1; i <= 5; i++) {
                    // 獲取相應的方法名
                    String getQuestionMethodName = "getQuestion" + i;
                    String getAnswerTextMethodName = "getAnswerText" + i;
                    String getAnswerAudioMethodName = "getAnswerAudio" + i;
                    String getAudioUrlMethodName = "getAudioUrl" + i;
                    String getSuggestionMethodName = "getSuggestion" + i;

                    // 獲取相應的方法对象
                    Method getQuestionMethod = interviewResult.getClass().getMethod(getQuestionMethodName);
                    Method getAnswerTextMethod = interviewResult.getClass().getMethod(getAnswerTextMethodName);
                    Method getAnswerAudioMethod = interviewResult.getClass().getMethod(getAnswerAudioMethodName);
                    Method getAudioUrlMethod = interviewResult.getClass().getMethod(getAudioUrlMethodName);
                    Method getSuggestionMethod = interviewResult.getClass().getMethod(getSuggestionMethodName);

                    // 執行相應的方法並檢查是否為 null，如果是，設置為特定值
                    if (getQuestionMethod.invoke(interviewResult) == null) {
                        interviewResult.getClass().getMethod("setQuestion" + i, String.class).invoke(interviewResult, "尚未輸入");
                    }
                    if (getAnswerTextMethod.invoke(interviewResult) == null) {
                        interviewResult.getClass().getMethod("setAnswerText" + i, String.class).invoke(interviewResult, "尚未輸入");
                    }
                    if (getAnswerAudioMethod.invoke(interviewResult) == null) {
                        interviewResult.getClass().getMethod("setAnswerAudio" + i, byte[].class).invoke(interviewResult, "尚未輸入".getBytes());
                    }
                    if (getAudioUrlMethod.invoke(interviewResult) == null) {
                        interviewResult.getClass().getMethod("setAudioUrl" + i, String.class).invoke(interviewResult, "尚未輸入");
                    }
                    if (getSuggestionMethod.invoke(interviewResult) == null) {
                        interviewResult.getClass().getMethod("setSuggestion" + i, String.class).invoke(interviewResult, "尚未輸入");
                    }
                }

                // 檢查 score 是否為 null，如果是，設定為特定值
                if (interviewResult.getScore() == null) {
                    interviewResult.setScore("尚未輸入");
                }
                // 檢查 overallSuggestion 是否為 null，如果是，設定為特定值
                if (interviewResult.getOverallSuggestion() == null) {
                    interviewResult.setOverallSuggestion("尚未輸入");
                }

                // 保存 interviewResult 物件
                return interviewResultRepository.save(interviewResult);

            }
            return interviewResultRepository.save(interviewResult);
        } catch(Exception e) {
            throw e;
        }
    }

    
    @Override
    public List<TimestampAndPositionInterface> getTimestampAndPositionByResumeId(Integer resumeId) {
        return interviewResultRepository.findTimestampAndPositionByResumeId(resumeId);
    }

    @Override
    public Optional<ScoreInterface> getScoreByInterviewId(Integer interviewId) {
        return interviewResultRepository.findScoreByInterviewId(interviewId);
    }

    @Override
    public Optional<SuggestionInterface> getSuggestionByInterviewId(Integer interviewId) {
        return interviewResultRepository.findSuggestionByInterviewId(interviewId);
    }

    @Override
    public Optional<OverallSuggestionInterface> getOverallSuggestionByInterviewId(Integer interviewId) {
        return interviewResultRepository.findOverallSuggestionByInterviewId(interviewId);
    }
    
    @Override
    public List<String> getQuestionAnswerByInterviewId(Integer interviewId) {
        Optional<InterviewResult> interviewResult = interviewResultRepository.findById(interviewId);
        if (interviewResult.isPresent()) {

            List<String> questionAnswer = new ArrayList<>();

            // 初始化 ObjectMapper
            ObjectMapper mapping = new ObjectMapper();

            try {
                InterviewResult resultObject = interviewResult.get(); // 獲取 InterviewResult 對象
                String jsonResult = mapping.writeValueAsString(resultObject); // 將 InterviewResult 對象轉換為 JSON 字符串

                JsonNode jsonNode = mapping.readTree(jsonResult);

                // 讀取問題（question）和回答（answerText）欄位的值
                for (int i = 1; i <= 5; i++) {
                    String questionKey = "question" + i;
                    String answerKey = "answerText" + i;

                    String question = jsonNode.get(questionKey).asText();
                    String answer = jsonNode.get(answerKey).asText();

                    String formattedQuestion = "Question" + i + " = " + question;
                    String formattedAnswer = "Answer" + i + " = " + answer;

                    questionAnswer.add(formattedQuestion);
                    questionAnswer.add(formattedAnswer);
                }

                return questionAnswer;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }else {
            return null;
        }
    }



    @Override
    public InterviewResult getInterviewResultByInterviewId(Integer interviewId) {
        Optional<InterviewResult> Interview_Result = interviewResultRepository.findById(interviewId);
        if (Interview_Result.isPresent()) {
            return Interview_Result.get();
        } else {
            throw new ResourceNotFoundException("InterviewResult", "interviewId", interviewId);
        }

        // return interviewResult_repository.findById(interviewId).orElseThrow(() -> new ResourceNotFoundException("InterviewResult", "interviewId", interviewId));
    }
    
    
    @Override
    public List<Object> getInterviewMessageByMemberId(Integer memberId) {
        List<Object> interviewData = new ArrayList<>();
        Integer interviewTimes = getInterviewTimesByMemberId(memberId);
        String latestInterview = getLatestInterviewByMemberId(memberId);
        String averageScore = getAverageScoreByMemberId(memberId);
        
        if (interviewTimes != -1) {
            interviewData.add(interviewTimes);
        }else {
        	interviewData.add(0);
        }
        if (latestInterview != "-") {
            interviewData.add(latestInterview);
        }else {
        	interviewData.add("-");
        }
        if (averageScore != "-") {
            interviewData.add(averageScore);
        }else {
        	interviewData.add("-");
        }
        return interviewData;
    }


    
    @Override
    public Integer getInterviewTimesByMemberId(Integer memberId){
        List<InterviewResult> Interview_Result = interviewResultRepository.findByMemberIdInterviewResult(memberId);
        if (!Interview_Result.isEmpty()) {

            // 取得面試次數
            Integer totalRowCount = Interview_Result.size();
            return totalRowCount;
        }
        return -1;
    }
    
    @Override
    public String getLatestInterviewByMemberId(Integer memberId){
        List<InterviewResult> Interview_Result = interviewResultRepository.findByMemberIdInterviewResult(memberId);
        if (!Interview_Result.isEmpty()) {

            Integer totalRowCount = Interview_Result.size();

            // 取得最新一筆資料
            InterviewResult firstInterviewResult = Interview_Result.get(totalRowCount - 1);
            // 初始化 ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            // 將 InterviewResult 物件轉換成 JSON 字串
            try {
                String jsonResult = objectMapper.writeValueAsString(firstInterviewResult);

                // 解析 JSON 字串為 JsonNode
                JsonNode jsonNode = objectMapper.readTree(jsonResult);

                // 讀取分數（score）欄位的值
                String score = jsonNode.get("score").asText();

                return score;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }else {
            return "-";
        }
    }



    public List<Integer> extractScores(List<InterviewResult> interviewResults) {
        return interviewResults.stream()
                .map(this::mapScoreToValue) // 使用getScore方法取得每個InterviewResult的分數
                .collect(Collectors.toList());
    }
    private int mapScoreToValue(InterviewResult result) {
        String score = result.getScore();
        Map<String, Integer> gradeToValueMap = createGradeToValueMap();
        return gradeToValueMap.getOrDefault(score, 0); // 若找不到對應的分數，返回0
    }
    private Map<String, Integer> createGradeToValueMap() {
        Map<String, Integer> gradeToValueMap = new HashMap<>();
        gradeToValueMap.put("A++", 10);
        gradeToValueMap.put("A+", 9);
        gradeToValueMap.put("A", 8);
        gradeToValueMap.put("B++", 7);
        gradeToValueMap.put("B+", 6);
        gradeToValueMap.put("B", 5);
        gradeToValueMap.put("C++", 4);
        gradeToValueMap.put("C+", 3);
        gradeToValueMap.put("C", 2);
        gradeToValueMap.put("F", 1);
        return gradeToValueMap;
    }

    private String mapValueToScore(Integer value) {
        Map<Integer, String> valueToGradeMap = createValueToGradeMap();
        return valueToGradeMap.getOrDefault(value, null); // 若找不到對應的分數，返回0
    }
    private Map<Integer, String> createValueToGradeMap() {
        Map<Integer, String> gradeToValueMap = new HashMap<>();
        gradeToValueMap.put(10, "A++");
        gradeToValueMap.put(9, "A+");
        gradeToValueMap.put(8, "A");
        gradeToValueMap.put(7, "B++");
        gradeToValueMap.put(6, "B+");
        gradeToValueMap.put(5, "B");
        gradeToValueMap.put(4, "C++");
        gradeToValueMap.put(3, "C+");
        gradeToValueMap.put(2, "C");
        gradeToValueMap.put(1, "F");
        return gradeToValueMap;
    }

    @Override
    public String getAverageScoreByMemberId(Integer memberId){
        List<InterviewResult> Interview_Result = interviewResultRepository.findByMemberIdInterviewResult(memberId);
        if (!Interview_Result.isEmpty()) {

            List<Integer> scores = extractScores(Interview_Result);

            // 計算平均分數
            Integer averageScore = (int) scores.stream()
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0); // 若無分數，返回0

            // 轉換為 ABCDE
            String averageValue = mapValueToScore(averageScore);
            return averageValue;

        } else {
        //throw new ResourceNotFoundException("InterviewResult", "memberId", memberId);
        	return "-";
        }
    }
    
    @Override
    public InterviewResult updateAnswerText(InterviewResult existingInterviewResult, Map<String, Object> updateText) {

        for (Map.Entry<String, Object> entry : updateText.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            System.out.println("Key: " + fieldName + ", Value: " + fieldValue);

            switch (fieldName) {
                case "answerText1":
                    existingInterviewResult.setAnswerText1((String) fieldValue);
                    break;

                case "answerText2":
                    existingInterviewResult.setAnswerText2((String) fieldValue);
                    break;

                case "answerText3":
                    existingInterviewResult.setAnswerText3((String) fieldValue);
                    break;

                case "answerText4":
                    existingInterviewResult.setAnswerText4((String) fieldValue);
                    break;

                case "answerText5":
                    existingInterviewResult.setAnswerText5((String) fieldValue);
                    break;
            }
        }
        //save existing interviewResult to DB
        // 印出 InterviewResult 的各個屬性值
        System.out.println("InterviewResult ID: " + existingInterviewResult.getInterviewId());
        System.out.println("Answer Text 1: " + existingInterviewResult.getAnswerText1());
        System.out.println("Answer Text 2: " + existingInterviewResult.getAnswerText2());
        System.out.println("Answer Text 3: " + existingInterviewResult.getAnswerText3());
        System.out.println("Answer Text 4: " + existingInterviewResult.getAnswerText4());
        System.out.println("Answer Text 5: " + existingInterviewResult.getAnswerText5());


        interviewResultRepository.save(existingInterviewResult);
        return existingInterviewResult;
    }

    
    @Override
    public InterviewResult updateScoreSuggestion(InterviewResult existingInterviewResult, String ChatgptResponse){

        // 評分
        // 使用正則表達式提取評分
            Pattern scorePattern = Pattern.compile("評分 = ([A-Z+]+)");
        Matcher scoreMatcher = scorePattern.matcher(ChatgptResponse);
        String Score = "";
        if (scoreMatcher.find()) {
            Score = scoreMatcher.group(1);
        }

        // 建議
        // 使用正則表達式分割字符串為每个建議
        Pattern suggestionPattern = Pattern.compile("建議：([^\\n]+)");
        Matcher suggestionMatcher = suggestionPattern.matcher(ChatgptResponse);

        String[] suggestionsArray = new String[5];  // 回傳
        String[] savedSuggestionsArray = new String[5];  // 儲存資料庫

        int y = 0; // 初始分隔符索引

        while (suggestionMatcher.find()) {
            int i = y + 1 ;
            String suggestionText = suggestionMatcher.group(1).trim();
            String separator = "\nAnswer" + i + " 建議: "; // 生成分隔符
            String mergedSuggestion = separator + suggestionText;

            suggestionsArray[y] = mergedSuggestion;
            savedSuggestionsArray[y] = suggestionText;

            y++; // 增加分隔符索引，自動延伸下去
        }

        // 將建議合併成一个字符串，用逗號或其他分隔符分隔
        String mergedSuggestions = String.join("\n", suggestionsArray);

        for (int z = 0; z < savedSuggestionsArray.length; z++) {
            if (z == 0) {
                existingInterviewResult.setSuggestion1(savedSuggestionsArray[z]);
            } else if (z == 1) {
                existingInterviewResult.setSuggestion2(savedSuggestionsArray[z]);
            } else if (z == 2) {
                existingInterviewResult.setSuggestion3(savedSuggestionsArray[z]);
            } else if (z == 3) {
                existingInterviewResult.setSuggestion4(savedSuggestionsArray[z]);
            } else if (z == 4) {
                existingInterviewResult.setSuggestion5(savedSuggestionsArray[z]);
            }
        }

        // 總評
        // 使用正則表達式提取總評
        Pattern OverallSuggestionPattern = Pattern.compile("總評：([^\\n]+)");
        Matcher OverallSuggestionMatcher = OverallSuggestionPattern.matcher(ChatgptResponse);
        String OverallSuggestion = "";
        if (OverallSuggestionMatcher.find()) {
            OverallSuggestion = OverallSuggestionMatcher.group(1);
        }

        System.out.println("\n[資料庫儲存]");
        System.out.println("建議: " + mergedSuggestions);
        System.out.println("總評: " + OverallSuggestion);
        System.out.println("評分: " + Score);

        existingInterviewResult.setScore(Score);
        existingInterviewResult.setOverallSuggestion(OverallSuggestion);

        // 儲存 Score 和 Suggestion 到資料庫
        interviewResultRepository.save(existingInterviewResult);
        return existingInterviewResult;
    }
    
/* 
 
   @Override
    public List<InterviewResult> getAllInterviewResults() {

        try {
            List<InterviewResult> interviewResults = interviewResult_repository.findAll();
            List<InterviewResult> customInterviewResults = new ArrayList<>();
            interviewResults.stream().forEach(e -> {
                InterviewResult interviewResult = new InterviewResult();
                BeanUtils.copyProperties(e, interviewResult);
                customInterviewResults.add(interviewResult);
            });
            return customInterviewResults;
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Override
    public void deleteInterviewResult(int memberId) {

        //check whether an interviewResult exists in a DB or not
        interviewResult_repository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("InterviewResult", "memberId", memberId));
        interviewResult_repository.deleteById(memberId);
    }
    
    @Override
    public List<InterviewResult> getInterviewResultByMemberId(Integer memberId) {
    	List<InterviewResult> interviewResults = interviewResult_repository.findByMemberIdInterviewResult(memberId);
        if (!interviewResults.isEmpty()) {
            return interviewResults;
        } else {
            throw new ResourceNotFoundException("InterviewResult", "memberId", memberId);
        }

        // return interviewResult_repository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("InterviewResult", "memberId", memberId));

    }
*/
}