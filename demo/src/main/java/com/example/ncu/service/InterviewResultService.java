package com.example.ncu.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ncu.Interface.OverallSuggestionInterface;
import com.example.ncu.Interface.ScoreInterface;
import com.example.ncu.Interface.SuggestionInterface;
import com.example.ncu.Interface.TimestampAndPositionInterface;
import com.example.ncu.domain.InterviewResult;

@Service
public interface InterviewResultService {

	InterviewResult saveInterviewResult(Integer memberIdInterviewResult, Integer resumeIdInterviewResult, InterviewResult interviewResult) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    InterviewResult getInterviewResultByInterviewId(Integer interviewId);
    
    List<TimestampAndPositionInterface> getTimestampAndPositionByResumeId(Integer resumeId);
    Optional<ScoreInterface> getScoreByInterviewId(Integer interviewId);
    Optional<SuggestionInterface> getSuggestionByInterviewId(Integer interviewId);
    Optional<OverallSuggestionInterface> getOverallSuggestionByInterviewId(Integer interviewId);
    List<String> getQuestionAnswerByInterviewId(Integer interviewId);
    
    Integer getInterviewTimesByMemberId(Integer memberId);
    String getLatestInterviewByMemberId(Integer memberId);
    String getAverageScoreByMemberId(Integer memberId);
    List<Object> getInterviewMessageByMemberId(Integer memberId);
    
    InterviewResult updateAnswerText(InterviewResult existingInterviewResult, Map<String, Object> updates);
    InterviewResult updateScoreSuggestion(InterviewResult existingInterviewResult, String suggestion);
    
    
/*
    List<InterviewResult> getAllInterviewResults();
    void deleteInterviewResult(int memberId);
    List<InterviewResult> getInterviewResultByMemberId(Integer memberId);
*/
}

