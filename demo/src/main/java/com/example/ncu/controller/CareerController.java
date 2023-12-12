package com.example.ncu.controller;

import com.example.ncu.domain.Career;
import com.example.ncu.service.CareerService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/career")
public class CareerController {

    private CareerService service;

    public CareerController(CareerService service) {
        super();
        this.service = service;
    }

    
    /*
     * 查詢所有面試類別
     * Get http://140.115.87.155:8080/career/findAll
     * 前端要傳遞的值:無
     * 成功回傳:200跟所有面試類別(總共19個
     */
    @GetMapping("/findAll")
    public ResponseEntity <List<Career>> getAllCareers(){
    	// 创建响应标头对象
        HttpHeaders headers = new HttpHeaders();

        // 设置自定义标头，例如 Content-Type
        headers.add("Content-Type", "application/json; charset=UTF-8");

        // 返回带有自定义标头的响应实体
        return ResponseEntity.ok()
            .headers(headers) // 设置响应标头
            .body(service.getAllCareers()); // 设置响应主体内容
    }

    
    /*
     * 查詢某個面試類別
     * Get http://140.115.87.155:8080/career/findById/{careerId}
     * 前端要傳遞的值:無
     * 成功回傳:200跟某個面試類別
     */
    @GetMapping("/findById/{careerId}")
    public ResponseEntity<Career> getCareerById(@PathVariable("careerId") int careerId){
        return new ResponseEntity<Career>(service.getCareerById(careerId),HttpStatus.OK);
    }

}