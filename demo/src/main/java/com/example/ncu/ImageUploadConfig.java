package com.example.ncu;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageUploadConfig implements WebMvcConfigurer{
	
    // file:絕對路徑
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry r) {
		r.addResourceHandler("/uploads/**").addResourceLocations("file:C:/Users/user/eclipse-workspace/demo/src/main/resources/static/uploads/");
	}


}
