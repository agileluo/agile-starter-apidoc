package io.github.agileluo.apidoc.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.agileluo.apidoc.rest.ApiDocAutoGenRest;

@Configuration
public class ApiDocAutoConfiguration {
	
	
	@Bean
	public ApiDocAutoGenRest autoGenApi(){
		return new ApiDocAutoGenRest();
	}
	
	
}
