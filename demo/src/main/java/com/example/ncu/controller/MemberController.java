package com.example.ncu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.ncu.domain.Career;
import com.example.ncu.domain.Member;
import com.example.ncu.repository.MemberRepository;
import com.example.ncu.service.CareerService;
import com.example.ncu.service.MemberService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private MemberService service;
    @Autowired
    private CareerService cs;

    public MemberController(MemberService service) {
        super();
        this.service = service;
    }

    /*
     * 新增會員
     * Post http://140.115.87.155:8080/member/addMember
     * {"name":"","email":"","password":""}
     * 成功回傳:201跟成功
     */
    @PostMapping("/addMember")
    public ResponseEntity<String> saveMember(@RequestBody Member member){
    	
    	String name = member.getName();
        String email = member.getEmail();
        String password = member.getPassword();
    	if (service.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email重複註冊"); // 400 email重複註冊
        }
    	
    	Member newMember = new Member();
        newMember.setName(name);
        newMember.setEmail(email);
        newMember.setPassword(password);
        service.saveMember(member);
        return ResponseEntity.status(HttpStatus.CREATED).body("註冊成功"); // 成功
    }
    
    
    
    /*
     * 確認登入資料
     * Get http://140.115.87.155:8080/member/loginConfirm
     * {"email":"","password":""}
     * 成功回傳:200
     */
    @PostMapping("/loginConfirm")
    public ResponseEntity<Integer> loginConfirm(@RequestBody Member member){
    	String email = member.getEmail();
        String password = member.getPassword();
    	if (service.authenticateUser(email, password)) {
    		Member authenticatedMember = service.getMemberByEmail(email);
            return ResponseEntity.ok(authenticatedMember.getMemberId()); // 登入成功，返回使用者的 memberId
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(-1); // 登入失敗
        }
     }
    
    
    
    /*
     * 查詢某個會員(by Id)
     * Post http://140.115.87.155:8080/member/findById/{memberId}
     * 
     * 成功回傳:200跟查詢的會員資料
     */
    @GetMapping("/findById/{memberId}")
    public ResponseEntity<Member> getMemberById(@PathVariable("memberId") int memberId){
        return new ResponseEntity<Member>(service.getMemberById(memberId),HttpStatus.OK);
    }

    
    /*
     * 查詢某個會員的preferred_career
     * Get http://140.115.87.155:8080/member/getMemberCareerInfo/{memberId}
     * 
     * 成功回傳:200跟查詢的careerId跟careerTitle
     */
    @GetMapping("/getMemberCareerInfo/{memberId}")
    public ResponseEntity<LinkedHashMap<String, Object>> getMemberCareerInfo(@PathVariable("memberId") int memberId) {
        // 根据会员ID从数据库中获取会员信息
        Member member = service.getMemberById(memberId);

        if (member != null) {
            // 创建一个有序的LinkedHashMap，以确保字段顺序
            LinkedHashMap<String, Object> careerInfoMap = new LinkedHashMap<>();
            
            // 查询并获取career1的名称
            Career career1 = cs.getCareerById(member.getCareerId1());
            careerInfoMap.put("career1Id", member.getCareerId1());
            careerInfoMap.put("career1Name", career1 != null ? career1.getTitle() : null);
            
            // 查询并获取career2的名称
            Career career2 = cs.getCareerById(member.getCareerId2());
            careerInfoMap.put("career2Id", member.getCareerId2());
            careerInfoMap.put("career2Name", career2 != null ? career2.getTitle() : null);
            
            // 查询并获取career3的名称
            Career career3 = cs.getCareerById(member.getCareerId3());
            careerInfoMap.put("career3Id", member.getCareerId3());
            careerInfoMap.put("career3Name", career3 != null ? career3.getTitle() : null);

            // 返回包含career信息的成功响应
            return ResponseEntity.ok(careerInfoMap);
        } else {
            // 如果找不到会员，返回相应的错误响应
            return ResponseEntity.notFound().build();
        }
    }

    
    
    /*
     * 查詢某會員的大頭貼
     * Get http://140.115.87.155:8080/member/getImage/{memberId}
     * 
     * 成功回傳:200跟查詢的會員大頭貼檔名
     */
    @GetMapping("/getImage/{memberId}")
    public ResponseEntity<String> getImage(@PathVariable("memberId") int memberId) {
    	String url = "http://140.115.87.132:8080/uploads/user-profiles/";
    	String profilePhotoPath = service.getProfilePhotoPath(memberId);
        if (profilePhotoPath != null) {
            return ResponseEntity.ok(url+profilePhotoPath);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    /*
     * 修改會員(by Id)
     * Put http://140.115.87.155:8080/member/updateById/{memberId}
     * {"name":"","email":"","password":""}
     * 成功回傳:200跟修改後的會員資料
     */
    @PutMapping("/updateById/{memberId}")
    public  ResponseEntity<Member> updateMemberById(@PathVariable("memberId") int memberId, @RequestBody Member member){
        return new ResponseEntity<Member>(service.updateMemberById(member, memberId),HttpStatus.OK); 
    }
    
    /*
     * 修改會員選擇的類別
     * Put http://140.115.87.155:8080/member/updatePreferredCareer/{memberId}
     * {"careerId1":"","careerId2":"","careerId3":""}
     * 成功回傳:200跟修改後的會員資料
     */
    @PutMapping("/updatePreferredCareer/{memberId}")
    public  ResponseEntity<Member> updatePreferredCareer(@PathVariable("memberId") int memberId, @RequestBody Map<String, Integer> careerIds){
    	// 根据memberId从数据库中获取已有的Member对象
        Member existingMember = service.getMemberById(memberId);

        // 检查并更新careerId1字段
        if (careerIds.containsKey("careerId1")) {
            existingMember.setCareerId1(careerIds.get("careerId1"));
        }

        // 检查并更新careerId2字段
        if (careerIds.containsKey("careerId2")) {
            existingMember.setCareerId2(careerIds.get("careerId2"));
        }

        // 检查并更新careerId3字段
        if (careerIds.containsKey("careerId3")) {
            existingMember.setCareerId3(careerIds.get("careerId3"));
        }

        // 保存更新后的Member对象到数据库
        Member savedMember = service.saveMember(existingMember);

        // 返回成功响应，包括更新后的会员数据
        return ResponseEntity.ok(savedMember);

    }
    
    
    /*
     * 修改會員大頭貼
     * Post http://140.115.87.155:8080/member/uploadImage/{memberId}
     * param=file
     * 成功回傳:200跟大頭貼的檔名
     */
    private static final String UPLOAD_DIR = "D:/eclipse-workspace/demo/src/main/resources/static/uploads/user-profiles/";

    @PostMapping("/uploadImage/{memberId}")
    public ResponseEntity<String> uploadImage(@PathVariable("memberId") int memberId, @RequestParam("file") MultipartFile file) {
        try {
             // 1. 從資料庫中獲取舊照片的路徑
             Optional<Member> optionalMember = memberRepository.findById(memberId);
             if (!optionalMember.isPresent()) {
                 return ResponseEntity.notFound().build();
             }

             Member member = optionalMember.get();
             String oldPhotoPath = member.getProfileImage();
             System.out.println(oldPhotoPath);
             // 2. 刪除舊大頭貼
             if (!oldPhotoPath.equals("default.png")) {
            	 System.out.println("開始");
            	 Path oldPhotoFilePath = Paths.get(UPLOAD_DIR).resolve(oldPhotoPath);
                 File oldPhotoFile = oldPhotoFilePath.toFile();
            	    if (oldPhotoFile.exists() && oldPhotoFile.isFile()) {
            	        if (oldPhotoFile.delete()) {
            	            System.out.println("舊大頭貼已刪除");
            	        } else {
            	            System.out.println("無法刪除舊大頭貼");
            	        }
            	    }
             } else { 
            	 System.out.println("原始大頭貼為預設值");
             }
            // Generate unique filename
            String fileName = memberId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            fileName = fileName.toLowerCase(); // 將檔案名稱轉換成小寫

            // Save the file to the server
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);
            Files.write(filePath, file.getBytes());
            // Save the file path or URL to the user's profileImage field in the database
            service.updateMemberProfile(memberId, fileName);
            String url = "http://140.115.87.132:8080/uploads/user-profiles/";
            
            return ResponseEntity.ok(url+fileName); // 返回更新後的照片檔名
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
        }
    }

/* 
    // 查詢所有會員
    // build get all members REST API
    // http://localhost:8080/api/members/findAll
    @GetMapping("/findAll")
    public List<Member> getAllMembers(){

        return service.getAllMembers();     // 回傳所有會員的資料
    }
    
    // 查詢某個會員(by name)
    // build get member by name REST API
    // http://localhost:8080/api/members/findByName?name=[name]
    @GetMapping("/findByName")
    public List<Member> getMemberByName(@RequestParam("name") String keyword) {
        List<Member> members = member_Repository.findByName(keyword);
        return members;     // 回傳查詢會員的資料
    }

    // 刪除會員(by Id)
    // build delete member REST API
    // http://localhost:8080/api/members/deleteById/[memberId]
    @DeleteMapping("/deleteById/{memberId}")
    public ResponseEntity<String> deleteMemberById(@PathVariable("memberId") int memberId){

        //delete member from DB
        service.deleteMemberById(memberId);

        return new ResponseEntity<String>("Employee deleted successfully!",HttpStatus.OK);      // 回傳刪除結果
    }

    // 刪除會員(by name)
    // build delete member REST API
    // http://localhost:8080/api/members/deleteByName/[name]
    @DeleteMapping("/deleteByName/{name}")
    public ResponseEntity<String> deleteMemberByName(@PathVariable("name") String name){

        //delete member from DB
        service.deleteMemberByName(name);

        return new ResponseEntity<String>("Employee deleted successfully!",HttpStatus.OK);      // 回傳刪除結果
    }
*/

}
