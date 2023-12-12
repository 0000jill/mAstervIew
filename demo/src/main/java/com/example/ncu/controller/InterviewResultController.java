package com.example.ncu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.ncu.Interface.OverallSuggestionInterface;
import com.example.ncu.Interface.ScoreInterface;
import com.example.ncu.Interface.SuggestionInterface;
import com.example.ncu.Interface.TimestampAndPositionInterface;
import com.example.ncu.domain.InterviewResult;
import com.example.ncu.repository.InterviewResultRepository;
import com.example.ncu.service.InterviewResultService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/interviewResult")
public class InterviewResultController {
	@Autowired
    private InterviewResultService interviewResultService;
    @Autowired
    private InterviewResultRepository interviewResultRepository;

    public InterviewResultController(InterviewResultService interviewResultService) {
        super();
        this.interviewResultService = (InterviewResultService) interviewResultService;
    }
    
    
    /*
     * 查詢某個面試結果
     * Get http://140.115.87.155:8080/interviewResult/findByInterviewId/{interviewId}
     * 前端要傳遞的值:無
     * 成功回傳:200跟某個面試結果
     */
    @GetMapping("/findByInterviewId/{interviewId}")
    public ResponseEntity<InterviewResult> getInterviewResultByInterviewId(@PathVariable("interviewId") Integer interviewId){
        return new ResponseEntity<InterviewResult>(interviewResultService.getInterviewResultByInterviewId(interviewId),HttpStatus.OK);     //回傳某個面試結果
    }
    
 // 查詢某位會員的面試次數、最新面試分數、面試平均分數
    // build get Interview times, Latest interview score and Average interview score by memberId REST API
    // http://localhost:8080/interviewResult/findInterviewMessage/[memberId]
    @GetMapping("/findInterviewMessage/{memberId}")
    public ResponseEntity<List<Object>> getInterviewMessageByMemberId(@PathVariable("memberId") Integer memberId) {
        List<Object> response = interviewResultService.getInterviewMessageByMemberId(memberId);
        System.out.println(response);
        if (response.isEmpty()) {
            return new ResponseEntity<List<Object>>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<List<Object>>(response, HttpStatus.OK);
        }
    }
    
 // 查詢某位會員的所有面試結果(時間跟職位)
    // build get timestamp and position by memberId REST API
    // http://localhost:8080/interviewResult/findTimestampAndPosition/[resumeId]
    @GetMapping("/findTimestampAndPosition/{resumeId}")
    public ResponseEntity<List<TimestampAndPositionInterface>> getTimestampAndPosition(@PathVariable("resumeId") Integer resumeId) {
        List<TimestampAndPositionInterface> timestampAndPosition = interviewResultService.getTimestampAndPositionByResumeId(resumeId);

        if (!timestampAndPosition.isEmpty()) {
            return new ResponseEntity<>(timestampAndPosition, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
 // 查詢某位會員的某次面試評分
    // build get score by interviewId REST API
    // http://localhost:8080/interviewResult/findScore/[interviewId]
    @GetMapping("/findScore/{interviewId}")
    public ResponseEntity<Optional<ScoreInterface>> getScore(@PathVariable("interviewId") Integer interviewId) {
        Optional<ScoreInterface> score = interviewResultService.getScoreByInterviewId(interviewId);

        if (score.isPresent()) {
            return new ResponseEntity<>(score, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 查詢某位會員的某次面試每個回答的建議
    // build get suggestion by interviewId REST API
    // http://localhost:8080/interviewResult/findSuggestion/[interviewId]
    @GetMapping("/findSuggestion/{interviewId}")
    public ResponseEntity<Optional<SuggestionInterface>> getSuggestion(@PathVariable("interviewId") Integer interviewId) {
        Optional<SuggestionInterface> Suggestion = interviewResultService.getSuggestionByInterviewId(interviewId);

        if (Suggestion.isPresent()) {
            return new ResponseEntity<>(Suggestion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 查詢某位會員的某次面試總評
    // build get overall suggestion by interviewId REST API
    // http://localhost:8080/interviewResult/findOverallSuggestion/[interviewId]
    @GetMapping("/findOverallSuggestion/{interviewId}")
    public ResponseEntity<Optional<OverallSuggestionInterface>> getOverallSuggestion(@PathVariable("interviewId") Integer interviewId) {
        Optional<OverallSuggestionInterface> overallSuggestion = interviewResultService.getOverallSuggestionByInterviewId(interviewId);

        if (overallSuggestion.isPresent()) {
            return new ResponseEntity<>(overallSuggestion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    
 // 更新回答文本
    // build update AnswerText REST API
    // http://localhost:8080/interviewResult/updateAnswerText/[interviewId]
    @PatchMapping("/updateAnswerText/{interviewId}")
    public  ResponseEntity<InterviewResult> updateAnswerText(@PathVariable("interviewId") Integer interviewId, @RequestBody Map<String, Object> updateText){
        InterviewResult existingInterviewResult = interviewResultRepository.findById(interviewId)
                .orElseThrow(() -> new com.example.ncu.Exception.ResourceNotFoundException("InterviewResult", "interviewId", interviewId));

        return new ResponseEntity<InterviewResult>(interviewResultService.updateAnswerText(existingInterviewResult, updateText),HttpStatus.OK);      //回傳修改結果
    }
/*
    // 更新回答音檔
    // build update AnswerAudio REST API
    // http://localhost:8080/interviewResult/updateAnswerAudio/[interviewId]
    @PatchMapping("/updateAnswerAudio/{interviewId}")
    public  ResponseEntity<InterviewResult> updateAnswerAudio(@PathVariable("interviewId") Integer interviewId, @RequestParam("file") MultipartFile updateAudio) throws IOException {
        InterviewResult existingInterviewResult = interviewResultRepository.findById(interviewId)
                .orElseThrow(() -> new com.example.ncu.Exception.ResourceNotFoundException("InterviewResult", "interviewId", interviewId));

        byte[] audioData = updateAudio.getBytes();

        ObjectMapper AudioMapper = new ObjectMapper();
        Map<String, byte[]> audioMap = AudioMapper.readValue(audioData, Map.class);

        return new ResponseEntity<InterviewResult>(interviewResultService.SaveAnswerAudio(existingInterviewResult, audioMap),HttpStatus.OK);      //回傳修改結果
    }
*/
/* 

    //新增面試結果 用不到了 再生成問題時一併新增
    @PostMapping("/createInterviewResult/{memberIdInterviewResult}")
    public ResponseEntity<InterviewResult> saveInterviewResult(@PathVariable("memberIdInterviewResult") Integer memberIdInterviewResult, @RequestBody InterviewResult interviewResult){
        interviewResult.setMemberIdInterviewResult(memberIdInterviewResult);
        InterviewResult savedInterviewResult = interviewResult_Service.saveInterviewResult(memberIdInterviewResult, interviewResult);       // 回傳面試結果

        if (savedInterviewResult != null) {
            return new ResponseEntity<>(savedInterviewResult, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //查詢所有面試結果
    // build get all interviewResults REST API
    // http://140.115.87.155:8080/api/interviewResults/findAll
    @GetMapping("/findAll")
    public List<InterviewResult> getAllInterviewResults(){
        return interviewResult_Service.getAllInterviewResults();        // 回傳所有面試結果
    }
   
    //修改面試結果
    //build update interviewResult REST API
    // http://localhost:8080/api/interviewResults/update/[interviewId]
    @PutMapping("/update/{interviewId}")
    public  ResponseEntity<InterviewResult> updateInterviewResult(@PathVariable("interviewId") int interviewId, @RequestBody InterviewResult interviewResult){
        return new ResponseEntity<InterviewResult>(interviewResult_Service.updateInterviewResult(interviewResult, interviewId),HttpStatus.OK);      //回傳修改結果
    }

    //刪除面試結果
    //build delete interviewResult REST API
    // http://localhost:8080/api/interviewResults/delete/[memberId]
    @DeleteMapping("/delete/{interviewId}")
    public ResponseEntity<String> deleteInterviewResult(@PathVariable("interviewId") int interviewId){

        //delete interviewResult from DB
        interviewResult_Service.deleteInterviewResult(interviewId);

        return new ResponseEntity<String>("InterviewResult deleted successfully!",HttpStatus.OK);       // 回傳刪除結果
    }
*/

}
