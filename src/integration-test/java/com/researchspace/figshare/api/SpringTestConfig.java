package com.researchspace.figshare.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.researchspace.figshare.impl.FigshareTemplate;
/**
 * Test configuration. Some of these nbeans are configured in main web application at runtime
 * @author rspace
 *
 */
@Configuration
public class SpringTestConfig {
	
	@Bean
	FigshareTemplate FigshareTemplate (){
		return new FigshareTemplate();
	}
	
}
