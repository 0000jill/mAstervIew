package com.example.ncu.service;

import com.example.ncu.domain.InterviewResult;
//import com.google.common.collect.Maps;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
//import com.theokanning.openai.completion.CompletionRequest.CompletionRequestBuilder;
import com.theokanning.openai.completion.CompletionResult;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;



@Slf4j
@Service
public class OpenAiChatBiz {
	
	@Value("${open.ai.model}")
	private String openAiModel;
	@Autowired
	private OpenAiService openAiService;
	@Autowired
	private InterviewResultService interviewResult_service;
	
	
	// 回傳未切割的生成好的問題
	public String chat(Map<String, Object> responseMap) {
		String promptText ="我們要做一個面試的app，而你是一位資深的人資。\n" +
                "請根據提供的履歷及面試的職位提出你會想問的5個問題。\n" +
                "請你判斷面試者的履歷裡的專長及經歷是否和面試的職位相關，如果無關則5題當中有1題需問「你的專長和經歷和你所要面試的職位無關，可以請你分享為什麼會想面試這個職位呢？」，如果相符則不需要問這個問題。\n" +
                "問題以「1.您的專長和經歷和您所要面試的職位無關，為什麼您會想面試這個職位呢？」這種形式回傳，總共回傳5個問題即可。"+responseMap;
		CompletionRequest completionRequest = CompletionRequest.builder()
				.prompt(promptText)
				.model(openAiModel)
				.echo(false)
				.temperature(0.7)
				.topP(1d)
				.frequencyPenalty(0d)
				.presencePenalty(0d)
				.maxTokens(1000)
				.build();
		CompletionResult completionResult = openAiService.createCompletion(completionRequest);
		String text = completionResult.getChoices().get(0).getText();
		System.out.println("指令: "+promptText);
		System.out.println("面試問題: "+text);
        return text; 
	}

	// 把生成的問題拆開存進陣列 回傳陣列
	public String[] splitText(String text) {
		// 將 text 字符串按換行符拆分
	    String[] ans = text.split("\\n");
        // 儲存處理好的文字
        StringBuilder questionsBuilder = new StringBuilder();
        // 把編號跟.處理掉
        for (String a : ans) {
            int dotIndex = a.indexOf(':');
            if (dotIndex != -1) {
                String questionText = a.substring(dotIndex + 1).trim();
                questionsBuilder.append(questionText).append("\n");
            }
        }
        // 將處理好的文字按換行符拆分成陣列
        String[] questionsArray = questionsBuilder.toString().split("\\n");
        return questionsArray;// 
		
	}
	
	// 把問題拿去生成影片 回傳影片Id
	public String[] generateId(String [] questionsArray){
		String[] talkId = new String[questionsArray.length]; // 用來儲存 create_talk 回傳的talkId
		
        for (int i = 0; i < questionsArray.length; i++) {  // 把五個問題拿去生成影片 回傳talkId
            talkId[i] = did.create_talk(questionsArray[i]);
        }
        
        return talkId;
        //return downloadVideo(url); // 回傳result_url
	}
	
	// 回傳影片URL
		public ResponseEntity<String[]> generateURL(String [] Id) {
			String[] url = new String[Id.length]; // 用來儲存 create_talk 回傳的talkId
			for (int i = 0; i < Id.length; i++) {  //拿五個result_url
	            url[i] = did.get_talk(Id[i]);
	        }
			return downloadVideo(url); // 回傳result_url
			
		}
	
	
	// 把result_url裡的影片下載到本機 回傳result_url
	//private static final String UPLOAD_DIR = System.getProperty("user.dir") + "src/main/resources/static/uploads/video/";
	private static final String UPLOAD_DIR = "src/main/resources/static/uploads/video/";
	public static ResponseEntity<String[]> downloadVideo(String[] videoUrls) {
	    for (String videoUrl : videoUrls) {
	        try {
	            URL url = new URL(videoUrl);
	            InputStream inputStream = url.openStream();

	            // 生成一個簡短的唯一檔案名稱，使用時間戳
	            long timestamp = System.currentTimeMillis();
	            String videoFileName = "video_" + timestamp + ".mp4";

	            // Save the file to the server
	            String filePath = UPLOAD_DIR + videoFileName;

	            //String filePath = "C:/Users/user/Downloads/" + videoFileName;
	            OutputStream outputStream = new FileOutputStream(filePath);
	            
	            byte[] buffer = new byte[4096];
	            int bytesRead;
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                outputStream.write(buffer, 0, bytesRead);
	            }

	            inputStream.close();
	            outputStream.close();
	            System.out.println("Video downloaded and saved: " + filePath);
	        } catch (Exception e) {
	            System.out.println("Error downloading video: " + e.getMessage());
	        }
	    }

	    return ResponseEntity.status(HttpStatus.CREATED).body(videoUrls);
	}

	public void SaveQuestion(int memberId, int resumeId, String[] questionsArray) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        InterviewResult interviewResult = new InterviewResult();

        interviewResult.setMemberIdInterviewResult(memberId);
        interviewResult.setResumeIdInterviewResult(resumeId);

        for (int x = 0; x < questionsArray.length; x++) {
            if (x == 0) {
                interviewResult.setQuestion1(questionsArray[x]);
            } else if (x == 1) {
                interviewResult.setQuestion2(questionsArray[x]);
            } else if (x == 2) {
                interviewResult.setQuestion3(questionsArray[x]);
            } else if (x == 3) {
                interviewResult.setQuestion4(questionsArray[x]);
            } else if (x == 4) {
                interviewResult.setQuestion5(questionsArray[x]);
            }
        }

        // 存面試問題
        InterviewResult savedQuestion = interviewResult_service.saveInterviewResult(memberId, resumeId, interviewResult);
        System.out.println(savedQuestion);
    }
	public static void main(String[] args) {
	//	downloadVideo(new String[] {"https://d-id-talks-prod.s3.us-west-2.amazonaws.com/auth0%7C643d47d5ae1dd5a7069cb1a9/tlk_97WDgneNcOZT8DlFS0Mbp/1692694947171.mp4?AWSAccessKeyId=AKIA5CUMPJBIK65W6FGA&Expires=1692781350&Signature=DjBy7VwmJ%2F5%2BrjVi7OqNqTIAL%2BE%3D&X-Amzn-Trace-Id=Root%3D1-64e479a6-1d1fb00831f242642c63a73f%3BParent%3D8a2843a4c697185e%3BSampled%3D1%3BLineage%3D6b931dd4%3A0",
	//			"https://d-id-talks-prod.s3.us-west-2.amazonaws.com/auth0%7C643d47d5ae1dd5a7069cb1a9/tlk_ir2xQG3qGeTEU9Fy1wmHE/1692694960704.mp4?AWSAccessKeyId=AKIA5CUMPJBIK65W6FGA&Expires=1692781363&Signature=2F5nPXPgQcafk%2F98Tgckr%2FBAAck%3D&X-Amzn-Trace-Id=Root%3D1-64e479b3-65f2b16e5950d02360ad5ccb%3BParent%3D52997674c8a111f0%3BSampled%3D1%3BLineage%3D6b931dd4%3A0"});
	}
	
	public String suggest(List<String> questionAndAnswer) {

        String promptText = "請根據面試者的回覆個別給出適當的個別建議、一個總評和總分，\n" +
                "個別建議以[Answer X建議： 可以再加強對於不同層面的關鍵技能的說明，尤其是領導力和溝通技巧等方面。] X = 1 ~ 5，\n" +
                "總評以[總評：建議深入闡述領導力和溝通技巧等關鍵技能，並更全面地評估候選人，特別是應屆生。強化員工發展和績效評估知識，提升團隊表現。加強文化差異敏感度，促進協調和共識。您表現卓越，有提升潛力。]，\n" +
                "總分以[評分 = A++]這樣的固定形式回傳。 \n" +
                "A++ = 10, A+ = 9, A = 8, B++ = 7, B+ = 6, B = 5, C++ = 4, C+ = 3, C = 2, F = 1 \n" + questionAndAnswer;

        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(promptText)
                .model(openAiModel)
                .echo(false)
                .temperature(1.0)
                .topP(1d)
                .frequencyPenalty(0d)
                .presencePenalty(0d)
                .maxTokens(2000)
                .build();

        CompletionResult completionResult = openAiService.createCompletion(completionRequest);
        String scoreSuggestion = completionResult.getChoices().get(0).getText();
        System.out.println("\n[PromptText]\n" + promptText);
        return scoreSuggestion;

    }

}
