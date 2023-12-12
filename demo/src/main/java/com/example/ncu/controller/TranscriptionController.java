package com.example.ncu.controller;

import com.example.ncu.domain.InterviewResult;
import com.example.ncu.repository.InterviewResultRepository;
import com.example.ncu.service.InterviewResultService;
import com.example.ncu.service.OpenAiChatBiz;
import com.example.ncu.service.TranscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.InputFormatException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transcribe")
public class TranscriptionController {

    @Autowired
    private TranscriptionService transcriptionService;
    @Autowired
    private OpenAiChatBiz openAiChatBiz;
    @Autowired
    private InterviewResultRepository interviewResultRepository;
    @Autowired
    private InterviewResultService interviewResultService;

    String UPLOAD_DIR = "src/main/resources/static/uploads/";
    String MP3_DIR = "src/main/resources/static/uploads/audioFile/"; // 添加 MP3 文件保存目录

    @SuppressWarnings("unchecked")
	@PostMapping("/speechtotext/{interview_id}")
    public String transcribeAudio(@PathVariable("interview_id") Integer interview_id, @RequestParam("file") MultipartFile[] audioFiles) throws IOException, IllegalArgumentException, InputFormatException, EncoderException {
        System.out.println("收到aac");
        System.out.println(audioFiles.length);
        InterviewResult existingInterviewResult = interviewResultRepository.findById(interview_id)
                .orElseThrow(() -> new com.example.ncu.Exception.ResourceNotFoundException("InterviewResult", "interviewId", interview_id));

        List<String> transcriptions = new ArrayList<>();
        List<String> aacFilePaths = new ArrayList<>(); // 存储 AAC 文件路径的集合

        InterviewResult interviewResult = new InterviewResult();
        interviewResult.setInterviewId(interview_id);

        Integer y = 1;
        Map<String, Object> audioToTextMap;
        for (MultipartFile audioFile : audioFiles) {
            try {
            	// 生成唯一的文件名，确保不会重复覆盖已有的文件
                String mp3FileName = interview_id + "_" + System.currentTimeMillis() + ".mp3";
                Path mp3FilePath = Paths.get(MP3_DIR).resolve(mp3FileName);
                Path baseDir = Paths.get(MP3_DIR); // 基礎路徑

                Path relativePath = baseDir.relativize(mp3FilePath); // 計算相對路徑
                String relativePathStr = relativePath.toString(); // 轉換為字串
                System.out.println("Relative MP3 File Path: " + relativePathStr);

                // 存储 AAC 文件
                String aacFileName = interview_id + "_" + System.currentTimeMillis() + ".aac";
                Path aacFilePath = Paths.get(UPLOAD_DIR).resolve(aacFileName);
                
                // 将 MultipartFile 存储为 AAC 文件
                File aacInputFile = convertMultipartFileToFile(audioFile, aacFilePath.toFile());
                // 将 AAC 文件路径添加到列表
                aacFilePaths.add(aacFilePath.toString());

                // Convert AAC to MP3
                File mp3OutputFile = mp3FilePath.toFile();
                convertAacToMp3(aacInputFile, mp3OutputFile);
                transcriptionService.SaveAnswerAudio(y, interview_id, relativePathStr);
                // 文本
                // 将音频文件路径传递给文本转录服务
                String AudioToText = transcriptionService.transcribeAudio(mp3FilePath.toString());

                // 将换行符号 \n 取代为空格
                String TextReplace = AudioToText.replace("\n", " ");

                // 将 String 格式转换成 Map<String, Object> 格式
                String audioToText = "{\"answerText" + y + "\" : \"" + TextReplace + "\"}"; // 文字文件
                y++;

                // 将文字文件保存到数据库中
                ObjectMapper TextMapper = new ObjectMapper();
                audioToTextMap = TextMapper.readValue(audioToText, Map.class);
                transcriptionService.SaveAnswerText(interview_id, audioToTextMap);

                // 将 TextReplace 存储到数组中
                transcriptions.add(TextReplace);
                System.out.println(transcriptions);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 取得问题和回答文本
        List<String> questionAndAnswer = interviewResultService.getQuestionAnswerByInterviewId(interview_id);
        System.out.println("\n[Question and Answer]\n" + questionAndAnswer);

        // 取得分数和建议
        String scoreSuggestion = openAiChatBiz.suggest(questionAndAnswer);
        System.out.println("\n[ChatGPT回覆]" + scoreSuggestion);

        interviewResultService.updateScoreSuggestion(existingInterviewResult, scoreSuggestion);

        return scoreSuggestion;
    }

    
    public static void convertAacToMp3(File aacInputFile, File mp3OutputFile) throws IOException, IllegalArgumentException, InputFormatException, EncoderException {
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(64000); // 设置MP3比特率

        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp3");
        attrs.setAudioAttributes(audio);

        Encoder encoder = new Encoder();
        encoder.encode(new MultimediaObject(aacInputFile), mp3OutputFile, attrs);
    }

    // 将MultipartFile转换为File
    private File convertMultipartFileToFile(MultipartFile multipartFile, File targetFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(targetFile)) {
            fos.write(multipartFile.getBytes());
        }
        return targetFile;
    }

}
