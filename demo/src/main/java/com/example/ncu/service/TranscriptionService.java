package com.example.ncu.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ncu.domain.InterviewResult;
import com.example.ncu.repository.InterviewResultRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;


@Service
public class TranscriptionService {
	
	//ChatGPT APIKey = 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
    private static final String Transcription_API_URL = "https://api.openai.com/v1/audio/transcriptions";

    private final OkHttpClient client = new OkHttpClient();

    @Autowired
    private InterviewResultService interviewResult_Service;
    @Autowired
    private InterviewResultRepository interviewResultRepository;

    public TranscriptionService(InterviewResultService interviewResult_Service) {
        super();
        this.interviewResult_Service = interviewResult_Service;
    }

    public String transcribeAudio(String audioFilePath) {

    	File audioFile = new File(audioFilePath);

        if (!audioFile.exists()) {
            return "Error: File not found";
        }

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", audioFile.getName(),
                        RequestBody.create(MediaType.parse("audio/mp3"), audioFile))
                .addFormDataPart("model", "whisper-1")
                .addFormDataPart("response_format", "text")
                .addFormDataPart("language", "zh")
                .build();

        Request request = new Request.Builder()
                .url(Transcription_API_URL)
                .header("Authorization", "Bearer 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")
                .post(requestBody)
                .build();

        Call call = client.newCall(request);

        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return "Error: " + response.code() + " - " + response.message();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred during transcription.";
        }
    }


    public String SaveAnswerText(int interviewId, Map<String, Object> text) {

        InterviewResult existingInterviewResult = interviewResultRepository.findById(interviewId)
                .orElseThrow(() -> new com.example.ncu.Exception.ResourceNotFoundException("InterviewResult", "interviewId", interviewId));

        InterviewResult savedAnswerText = interviewResult_Service.updateAnswerText(existingInterviewResult, text);

        if (savedAnswerText != null) {
            System.out.print("成功儲存回答文本" + HttpStatus.CREATED.value());
            return String.valueOf(HttpStatus.CREATED);
        } else {
            System.out.print("儲存回答文本失敗" + HttpStatus.INTERNAL_SERVER_ERROR.value());
            return String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    String UPLOAD_DIR = "src/main/resources/static/uploads/audioFile/";

    public void SaveAnswerAudio(Integer y, Integer interviewId, String url) {

		String FilePath = "http://140.115.87.132:8080/uploads/audioFile/" + url;

		InterviewResult existingInterviewResult = interviewResultRepository.findById(interviewId)
		    .orElseThrow(() -> new com.example.ncu.Exception.ResourceNotFoundException("InterviewResult", "interviewId", interviewId));

		if (y == 1) {
			existingInterviewResult.setAudioUrl1(FilePath);  // 將檔案的 URL 路徑存入 InterviewResult
		} else if (y == 2) {
			existingInterviewResult.setAudioUrl2(FilePath);
		} else if (y == 3) {
			existingInterviewResult.setAudioUrl3(FilePath);
		} else if (y == 4) {
			existingInterviewResult.setAudioUrl4(FilePath);
		} else if (y == 5) {
			existingInterviewResult.setAudioUrl5(FilePath);
		}
		// 印出 InterviewResult 的各個屬性值
		System.out.println("目前的y="+y);
        System.out.println("InterviewResult ID: " + existingInterviewResult.getInterviewId());
        System.out.println("Answer Url 1: " + existingInterviewResult.getAudioUrl1());
        System.out.println("Answer Url 2: " + existingInterviewResult.getAudioUrl2());
        System.out.println("Answer Url 3: " + existingInterviewResult.getAudioUrl3());
        System.out.println("Answer Url 4: " + existingInterviewResult.getAudioUrl4());
        System.out.println("Answer Url 5: " + existingInterviewResult.getAudioUrl5());

		interviewResultRepository.save(existingInterviewResult);
    }
}
