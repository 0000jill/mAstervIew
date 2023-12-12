package com.example.ncu.domain;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "interview_result", schema = "ncu")
public class InterviewResult {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "interview_id", nullable = false)
    private Integer interviewId;

    @CreationTimestamp
    @Column(name = "timestamp", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp timestamp;

    @Basic
    @Column(name = "question_1", nullable = false)
    private String question1;
    @Basic
    @Column(name = "answer_text_1", nullable = false)
    private String answerText1;

    @Basic
    @Column(name = "audio_url_1", nullable = false)
    private String audioUrl1;
    @Lob
    @Basic
    @Column(name = "suggestion_1", columnDefinition = "LONGTEXT", nullable = false)
    private String suggestion1;

    @Basic
    @Column(name = "question_2", nullable = false)
    private String question2;
    @Basic
    @Column(name = "answer_text_2", nullable = false)
    private String answerText2;

    @Basic
    @Column(name = "audio_url_2", nullable = false)
    private String audioUrl2;
    @Lob
    @Basic
    @Column(name = "suggestion_2", columnDefinition = "LONGTEXT", nullable = false)
    private String suggestion2;

    @Basic
    @Column(name = "question_3", nullable = false)
    private String question3;
    @Basic
    @Column(name = "answer_text_3", nullable = false)
    private String answerText3;

    @Basic
    @Column(name = "audio_url_3", nullable = false)
    private String audioUrl3;
    @Lob
    @Basic
    @Column(name = "suggestion_3", columnDefinition = "LONGTEXT", nullable = false)
    private String suggestion3;

    @Basic
    @Column(name = "question_4", nullable = false)
    private String question4;
    @Basic
    @Column(name = "answer_text_4", nullable = false)
    private String answerText4;

    @Basic
    @Column(name = "audio_url_4", nullable = false)
    private String audioUrl4;
    @Lob
    @Basic
    @Column(name = "suggestion_4", columnDefinition = "LONGTEXT", nullable = false)
    private String suggestion4;

    @Basic
    @Column(name = "question_5", nullable = false)
    private String question5;
    @Basic
    @Column(name = "answer_text_5", nullable = false)
    private String answerText5;

    @Basic
    @Column(name = "audio_url_5", nullable = false)
    private String audioUrl5;
    @Lob
    @Basic
    @Column(name = "suggestion_5", columnDefinition = "LONGTEXT", nullable = false)
    private String suggestion5;

    @Basic
    @Column(name = "score", nullable = false)
    private String score;
    @Lob
    @Basic
    @Column(name = "overall_suggestion", columnDefinition = "LONGTEXT", nullable = false)
    private String overallSuggestion;

    @JsonIgnore
    @Basic
    @Column(name = "member_id_interview_result", nullable = false)
    private Integer memberIdInterviewResult;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "member_id_interview_result", referencedColumnName = "member_id", nullable = false, insertable = false, updatable = false)
    private Member memberByMemberIdInterviewResult;

    @JsonIgnore
    @Basic
    @Column(name = "resume_id_interview_result", nullable = false)
    private Integer resumeIdInterviewResult;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "resume_id_interview_result", referencedColumnName = "resume_id", nullable = false, insertable = false, updatable = false)
    private Resume resumeByResumeIdInterviewResult;

    public Integer getInterviewId() {
        return interviewId;
    }
    public void setInterviewId(Integer interviewId) {
        this.interviewId = interviewId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getQuestion1() {
        return question1;
    }
    public void setQuestion1(String question1) {
        this.question1 = question1;
    }
    public String getAnswerText1() {
        return answerText1;
    }
    public void setAnswerText1(String answerText1) {
        this.answerText1 = answerText1;
    }

    public String getAudioUrl1() {
        return audioUrl1;
    }
    public void setAudioUrl1(String audioUrl1) {
        this.audioUrl1 = audioUrl1;
    }
    public String getSuggestion1() {
        return suggestion1;
    }
    public void setSuggestion1(String suggestion1) {
        this.suggestion1 = suggestion1;
    }

    public String getQuestion2() {
        return question2;
    }
    public void setQuestion2(String question2) {
        this.question2 = question2;
    }
    public String getAnswerText2() {
        return answerText2;
    }
    public void setAnswerText2(String answerText2) {
        this.answerText2 = answerText2;
    }

    public String getAudioUrl2() {
        return audioUrl2;
    }
    public void setAudioUrl2(String audioUrl2) {
        this.audioUrl2 = audioUrl2;
    }
    public String getSuggestion2() {
        return suggestion2;
    }
    public void setSuggestion2(String suggestion2) {
        this.suggestion2 = suggestion2;
    }

    public String getQuestion3() {
        return question3;
    }
    public void setQuestion3(String question3) {
        this.question3 = question3;
    }
    public String getAnswerText3() {
        return answerText3;
    }
    public void setAnswerText3(String answerText3) {
        this.answerText3 = answerText3;
    }

    public String getAudioUrl3() {
        return audioUrl3;
    }
    public void setAudioUrl3(String audioUrl3) {
        this.audioUrl3 = audioUrl3;
    }
    public String getSuggestion3() {
        return suggestion3;
    }
    public void setSuggestion3(String suggestion3) {
        this.suggestion3 = suggestion3;
    }


    public String getQuestion4() {
        return question4;
    }
    public void setQuestion4(String question4) {
        this.question4 = question4;
    }
    public String getAnswerText4() {
        return answerText4;
    }
    public void setAnswerText4(String answerText4) {
        this.answerText4 = answerText4;
    }

    public String getAudioUrl4() {
        return audioUrl4;
    }
    public void setAudioUrl4(String audioUrl4) {
        this.audioUrl4 = audioUrl4;
    }
    public String getSuggestion4() {
        return suggestion4;
    }
    public void setSuggestion4(String suggestion4) {
        this.suggestion4 = suggestion4;
    }


    public String getQuestion5() {
        return question5;
    }
    public void setQuestion5(String question5) {
        this.question5 = question5;
    }
    public String getAnswerText5() {
        return answerText5;
    }
    public void setAnswerText5(String answerText5) {
        this.answerText5 = answerText5;
    }

    public String getAudioUrl5() {
        return audioUrl5;
    }
    public void setAudioUrl5(String audioUrl5) {
        this.audioUrl5 = audioUrl5;
    }
    public String getSuggestion5() {
        return suggestion5;
    }
    public void setSuggestion5(String suggestion5) {
        this.suggestion5 = suggestion5;
    }

    public String getScore() {
        return score;
    }
    public void setScore(String score) {
        this.score = score;
    }

    public String getOverallSuggestion() {
        return overallSuggestion;
    }
    public void setOverallSuggestion(String overallSuggestion) {
        this.overallSuggestion = overallSuggestion;
    }

    public Integer getMemberIdInterviewResult() {
        return memberIdInterviewResult;
    }
    public void setMemberIdInterviewResult(Integer memberIdInterviewResult) {
        this.memberIdInterviewResult = memberIdInterviewResult;
    }

    public Integer getResumeIdInterviewResult() {
        return resumeIdInterviewResult;
    }
    public void setResumeIdInterviewResult(Integer resumeIdInterviewResult) {
        this.resumeIdInterviewResult = resumeIdInterviewResult;
    }

    public Member getMemberByMemberIdInterviewResult() {
        return memberByMemberIdInterviewResult;
    }

    public void setMemberByMemberIdInterviewResult(Member memberByMemberIdInterviewResult) {
        this.memberByMemberIdInterviewResult = memberByMemberIdInterviewResult;
    }

    public Resume getResumeByResumeIdInterviewResult() {
        return resumeByResumeIdInterviewResult;
    }

    public void setResumeByResumeIdInterviewResult(Resume resumeByResumeIdInterviewResult) {
        this.resumeByResumeIdInterviewResult = resumeByResumeIdInterviewResult;
    }
}