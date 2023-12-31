package com.example.ncu;

import com.theokanning.openai.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
public class OpenAiConfiguration {
	
	@Value("${open.ai.key}")
	private String openAiKey;
	
	@Value("${open.ai.request.timeout}")
	private long timeout;
	
	@Bean
	public OpenAiService openAiService() {
		return new OpenAiService(openAiKey, Duration.ofSeconds(timeout));
		
	}
	
}
