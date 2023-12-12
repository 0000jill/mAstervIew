package com.example.ncu.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.ncu.service.ExpertiseService;
import com.example.ncu.service.OpenAiChatBiz;
import com.example.ncu.service.ResumeService;
import com.example.ncu.service.SpecialService;
import com.example.ncu.service.WorkexpService;



@RestController
@RequestMapping(path = "/chat")
public class OpenAiChatApi {
	
	@Autowired
	private OpenAiChatBiz openAiChatBiz;
	@Autowired
    private ResumeService rs;
    @Autowired
    private ExpertiseService es;
    @Autowired
    private SpecialService ss;
    @Autowired 
    private WorkexpService ws;
	
	
	/*
	 * 生成面試Id
	 * Post Get http://140.115.87.155:8080/chat/interviewstart/{resume_id}
	 * 前端要傳遞的值:無
	 * 成功回傳:201跟生成的影片Id*5
	 */
	@PostMapping("/interviewstart/{resume_id}")
	public ResponseEntity<String> openAiChat(@PathVariable("resume_id")Integer resume_id) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
		System.out.println("履歷id: "+resume_id);
		List<Map<String, Object>> r = rs.findById(resume_id);
        List<Map<String, Object>> e = es.findById(resume_id);
        List<Map<String, Object>> s = ss.findById(resume_id);
        List<Map<String, Object>> w = ws.findById(resume_id);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("resume", r);
        responseMap.put("expertise", e);
        responseMap.put("special", s);
        responseMap.put("workexp", w);
		String text = openAiChatBiz.chat(responseMap); //text=未切割的問題
		String [] question = openAiChatBiz.splitText(text); // question[]=切割成五個的問題
        int memberIdForResume = rs.findMemberId(resume_id);
        openAiChatBiz.SaveQuestion(memberIdForResume, resume_id, question); // 把問題存進資料庫
        
        return ResponseEntity.ok("成功");
        //return ResponseEntity.status(HttpStatus.CREATED).body(openAiChatBiz.generateId(question));// 生成影片 回傳影片Id
        
	}
	
	
	// 生成面試影片
	// Post http://140.115.87.155:8080/chat/getResultURL
	// 前端要傳遞的值:五個影片Id
	// 成功回傳:201跟生成的影片網址*5
	@PostMapping("/getResultURL")
	public ResponseEntity<String[]> getResultURL(@RequestBody String[] Id){
		
		return openAiChatBiz.generateURL(Id);  //抓影片Id 回傳影片URL
	}
	

	// Get http://140.115.87.132:8080/chat/getvideo
		@GetMapping("/getvideo")
		public ResponseEntity<List<String>> getVideo() {
		    List<String> videoUrls = new ArrayList<>();

		    // 添加多个视频URL到列表中
		    videoUrls.add("http://140.115.87.132:8080/uploads/video/intro.mp4");
		    videoUrls.add("http://140.115.87.132:8080/uploads/video/first.mp4");
		    videoUrls.add("http://140.115.87.132:8080/uploads/video/second.mp4");
		    videoUrls.add("http://140.115.87.132:8080/uploads/video/third.mp4");
		    videoUrls.add("http://140.115.87.132:8080/uploads/video/fourth.mp4");
		    videoUrls.add("http://140.115.87.132:8080/uploads/video/fifth.mp4");
		    videoUrls.add("http://140.115.87.132:8080/uploads/video/default1.mp4");
		    videoUrls.add("http://140.115.87.132:8080/uploads/video/default2.mp4");

		    // 返回包含多个视频URL的成功响应
		    return ResponseEntity.ok(videoUrls);
		}
	

	@GetMapping("/getclip")
	public HttpResponse<String> get_clip() throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://api.d-id.com/clips"))
			    .header("accept", "application/json")
			    .header("content-type", "application/json")
			    .header("authorization", "Basic Ym1OMU1URXhNREp3Y205cVpXTjBkR1ZoYlRFeVFHZHRZV2xzTG1OdmJROks4Um1SaVVtZVY2WVE1MVJPN3hQYg==")
			    .method("POST", HttpRequest.BodyPublishers.ofString("{\"driver_id\":\"UoaR8NoiQo\",\"script\":{\"type\":\"text\",\"subtitles\":\"false\",\"provider\":{\"type\":\"microsoft\",\"voice_id\":\"zh-TW-YunJheNeural\"},\"ssml\":\"false\",\"input\":\"你好，我是面試官。我會問你幾個問題，請你根據經驗來回答，那現在就開始面試囉!\"},\"config\":{\"result_format\":\"mp4\"},\"presenter_config\":{\"crop\":{\"type\":\"rectangle\"}},\"presenter_id\":\"rian-bYZilf8Qr9\"}"))
			    .build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
			return response;
	}
	
}
