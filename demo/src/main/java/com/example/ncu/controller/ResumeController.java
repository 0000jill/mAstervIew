package com.example.ncu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ncu.domain.Expertise;
import com.example.ncu.domain.Resume;
import com.example.ncu.domain.ResumeWithExpertise;
import com.example.ncu.domain.Special;
import com.example.ncu.domain.Workexp;
import com.example.ncu.service.ExpertiseService;
import com.example.ncu.service.ResumeService;
import com.example.ncu.service.SpecialService;
import com.example.ncu.service.WorkexpService;

@RestController
@RequestMapping("/resume")
public class ResumeController {
    @Autowired
    private ResumeService rs;
    @Autowired
    private ExpertiseService es;
    @Autowired
    private SpecialService ss;
    @Autowired 
    private WorkexpService ws;
    
    
    /*
     * 新增履歷
     * post http://140.115.87.155:8080/resume/createResumeWithExpertise2/{member_id}
     * {
    "resume": {
    		"title": "",
            "gender": "",
            "age": "",
            "hobby": "",
            "schoolName": "",
            "academic": "",
            "departmentName": "",
            "academicPeriod": "",
            "status": "",
            "preferredType": "",
            "workingHours": "",
            "position": ""
    },
    "specialList": [
        {
            "specialName": "",
            "specialDescription": ""
        }
    ],
    "workexpList": [
        {
            "workPeriod": "",
            "workexpDescription": "",
            "workexpName": ""
        }
    ],
    "expertiseList": [
        {
            "expertiseName": "",
            "expertiseDescription": ""
        }
    ]
}
     *成功回傳:201跟"成功"
     */
    @PostMapping("/createResumeWithExpertise2/{member_id}")
    public ResponseEntity<String> createResumeWithExpertise2(@PathVariable("member_id") Integer member_id, @RequestBody ResumeWithExpertise data) {
    	Resume resume = data.getResume();
    	List<Expertise> expertiseList = data.getExpertiseList();
    	List<Special> specialList = data.getSpecialList();
    	List<Workexp> workexpList = data.getWorkexpList();

        // 執行將履歷資料插入 resume 資料表的操作
        int resume_id = rs.createResume(member_id, resume);
        // 遍歷專業技能列表
        for (Expertise expertise : expertiseList) {
        	// 將生成的 resume_id 設置給 expertise 物件
        	expertise.setResumeId(resume_id);
        	es.createExpertise(resume_id, expertise);
        }
        // 遍歷特殊經歷列表
        for (Special special : specialList) {
        	// 將生成的 resume_id 設置給 expertise 物件
        	special.setResumeId(resume_id);
        	ss.createSpecial(resume_id, special);
        }
        // 遍歷工作經歷列表
        for (Workexp workexp : workexpList) {
        	// 將生成的 resume_id 設置給 expertise 物件
        	workexp.setResumeId(resume_id);
        	ws.createWorkexp(resume_id, workexp);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("成功");
    }
    

    /*
     * 新增履歷
     * Post http://140.115.87.155:8080/resume/createResume/{member_id}
     * {
            "gender": "",
            "age": "",
            "hobby": "",
            "schoolName": "",
            "academic": "",
            "departmentName": "",
            "academicPeriod": "",  // 會長這樣2020-09~2024-06
            "status": "",
            "preferredType": "",
            "workingHours": "",
            "position": ""
        }
     * 成功回傳:201跟"成功"
     */
    @PostMapping("/createResume/{member_id}")
    public ResponseEntity<String> createResume(@PathVariable("member_id") Integer member_id,  @RequestBody Resume r){
        rs.createResume(member_id, r);
        return ResponseEntity.status(HttpStatus.CREATED).body("成功");
    }
    /*
     * 新增履歷的專長
     * Post http://140.115.87.155:8080/resume/createExpertise/{resume_id}
     * {
            "expertiseName": "",
            "expertiseDescription": ""
        }
     * 成功回傳:201跟"成功"
     */
    @PostMapping("/createExpertise/{resume_id}")
    public ResponseEntity<String> createExpertise(@PathVariable("resume_id") Integer resume_id, @RequestBody Expertise e){
    	es.createExpertise(resume_id, e);
    	
    	return ResponseEntity.status(HttpStatus.CREATED).body("成功");
    }
    /*
     * 新增履歷的特殊經歷
     * Post http://140.115.87.155:8080/resume/createSpecial/{resume_id}
     * {
            "specialName": "",
            "specialDescription": ""
        }
     * 成功回傳:201跟"成功"
     */
    @PostMapping("/createSpecial/{resume_id}")
    public ResponseEntity<String> createSpecial(@PathVariable("resume_id") Integer resume_id, @RequestBody Special s){
    	ss.createSpecial(resume_id, s);
    	
    	return ResponseEntity.status(HttpStatus.CREATED).body("成功");
    }
    /*
     * 新增履歷的工作經歷
     * Post http://140.115.87.155:8080/resume/createWorkexp/{resume_id}
     * {
            "workexpName": "",
            "workexpDescription": ""
        }
     * 成功回傳:201跟"成功"
     */
    @PostMapping("/createWorkexp/{resume_id}")
    public ResponseEntity<String> createWorkexp(@PathVariable("resume_id") Integer resume_id, @RequestBody Workexp w){
    	ws.createWorkexp(resume_id, w);
    	
    	return ResponseEntity.status(HttpStatus.CREATED).body("成功");
    }
    
    
    /*
     * 查詢某位會員的所有履歷
     * Get http://140.115.87.155:8080/resume/findResumes/{member_id}
     * 
     * 成功回傳:200跟履歷title,member_id,resume_id
     */
    @GetMapping("/findResumes/{member_id}")  
    public ResponseEntity<List<Map<String, Object>>> getResumes(@PathVariable("member_id") Integer member_id) {
        List<Map<String, Object>> r =rs.findByMemberId(member_id);
        return ResponseEntity.status(HttpStatus.OK).body(r);
    }
    
    
    /*
     * 查詢某個履歷 
     * Get http://140.115.87.132:8080/resume/findResume/{resume_id}
     * 
     * 成功回傳:200跟查詢的履歷
     */
    @GetMapping("/findResume/{resume_id}")
    public ResponseEntity<Map<String, Object>> getResume(@PathVariable("resume_id") Integer resume_id) {
        List<Map<String, Object>> r = rs.findById(resume_id);
        List<Map<String, Object>> e = es.findById(resume_id);
        List<Map<String, Object>> s = ss.findById(resume_id);
        List<Map<String, Object>> w = ws.findById(resume_id);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("resume", r);
        responseMap.put("expertise", e);
        responseMap.put("special", s);
        responseMap.put("workexp", w);
        return ResponseEntity.status(HttpStatus.OK).body(responseMap);
        //return ResponseEntity.status(HttpStatus.OK).body(r);
    }
    
    
    /*
     * 修改履歷 
     * Put http://140.115.87.1:8080/resume/updateAllResume/{resume_id}
     * {
    "resume": {
            "gender": "",
            "age": "",
            "hobby": "",
            "schoolName": "",
            "academic": "",
            "departmentName": "",
            "academicPeriod": "",
            "status": "",
            "preferredType": "",
            "workingHours": "",
            "position": ""
    },
    "specialList": [
        {
            "specialName": "",
            "specialDescription": ""
        }
    ],
    "workexpList": [
        {
            "workPeriod": "",
            "workexpDescription": "",
            "workexpName": ""
        }
    ],
    "expertiseList": [
        {
            "expertiseName": "",
            "expertiseDescription": ""
        }
    ]
}
     * 成功回傳:200跟影響的行數(1才是對的
     */
    @PutMapping("/updateAllResume/{resume_id}")
    public ResponseEntity<Integer> updateAllResume(@PathVariable("resume_id") Integer resume_id, @RequestBody ResumeWithExpertise data){
    	Resume resume = data.getResume();
    	List<Expertise> expertiseList = data.getExpertiseList();
    	List<Special> specialList = data.getSpecialList();
    	List<Workexp> workexpList = data.getWorkexpList();

        int newr = rs.update(resume_id, resume);
        // 遍歷專業技能列表
        for (Expertise expertise : expertiseList) {
        	es.update(resume_id, expertise);
        }
        // 遍歷特殊經歷列表
        for (Special special : specialList) {
        	ss.update(resume_id, special);
        }
        // 遍歷工作經歷列表
        for (Workexp workexp : workexpList) {
        	ws.update(resume_id, workexp);
        }
        return ResponseEntity.status(HttpStatus.OK).body(newr);
    }
    
    /*
     * 修改履歷 
     * Put http://140.115.87.132:8080/resume/updateResume/{resume_id}
     * {
            "gender": "",
            "age": ,
            "hobby": "",
            "schoolName": "",
            "academic": "",
            "departmentName": "",
            "academicPeriod": "",
            "status": "",
            "preferredType": "",
            "workingHours": "",
            "position": ""
        }
     * 成功回傳:200跟影響的行數(1才是對的
     */
    @PutMapping("/updateResume/{resume_id}")
    public ResponseEntity<Integer> updateResume(@PathVariable("resume_id") Integer resume_id,@RequestBody Resume r){
        //if (r.getId() == null) {
        //   throw new BizException("更新操作id不能为空");
        //}
        int newr = rs.update(resume_id, r);
        if(newr == 0)
        {return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(newr); }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(newr);}
    }
    /*
     * 修改履歷的專長 
     * Put http://140.115.87.155:8080/resume/updateExpertise/{resume_id}
     * {
            "expertiseName": "",
            "expertiseDescription": ""
        }
     * 成功回傳:200跟影響的行數(1才是對的
     */
    @PutMapping("/updateExpertise/{expertise_id}")
    public ResponseEntity<Integer> updateExpertise(@PathVariable("expertise_id") Integer expertise_id,@RequestBody Expertise e){
        //if (r.getId() == null) {
        //   throw new BizException("更新操作id不能为空");
        //}
        int newe = es.update(expertise_id, e);
        if(newe == 0)
        {return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(newe); }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(newe);}
    }
    
}