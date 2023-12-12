package com.example.ncu.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ncu.Interface.OverallSuggestionInterface;
import com.example.ncu.Interface.ScoreInterface;
import com.example.ncu.Interface.SuggestionInterface;
import com.example.ncu.Interface.TimestampAndPositionInterface;
import com.example.ncu.domain.InterviewResult;

@Repository
public interface InterviewResultRepository extends JpaRepository<InterviewResult, Integer> {

	public boolean existsById(Integer interviewId);
    List<InterviewResult> findByMemberIdInterviewResult(Integer memberId);
    
    @Query("SELECT ir.interviewId as interviewId, ir.timestamp as timestamp, r.position as position " +
            "FROM InterviewResult ir " +
            "JOIN ir.resumeByResumeIdInterviewResult r " +
            "WHERE r.resume_id = :resumeId")
    List<TimestampAndPositionInterface> findTimestampAndPositionByResumeId(@Param("resumeId") Integer resumeId);

    @Query("SELECT ir.score as score " +
            "FROM InterviewResult ir " +
            "WHERE ir.interviewId = :interviewId")
    Optional<ScoreInterface> findScoreByInterviewId(@Param("interviewId") Integer interviewId);

    @Query("SELECT ir.suggestion1 as suggestion1, ir.suggestion2 as suggestion2, " +
            "ir.suggestion3 as suggestion3, ir.suggestion4 as suggestion4, " +
            "ir.suggestion5 as suggestion5 " +
            "FROM InterviewResult ir " +
            "WHERE ir.interviewId = :interviewId")
    Optional<SuggestionInterface> findSuggestionByInterviewId(@Param("interviewId") Integer interviewId);

    @Query("SELECT ir.overallSuggestion as overallSuggestion " +
            "FROM InterviewResult ir " +
            "WHERE ir.interviewId = :interviewId")
    Optional<OverallSuggestionInterface> findOverallSuggestionByInterviewId(@Param("interviewId") Integer interviewId);
}
