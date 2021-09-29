package com.researchspace.figshare.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.researchspace.figshare.impl.FigshareTemplate;
/**
 * Test configuration. Requires personal token set as system or environment  property e.g. -DfigshareToken=XXX
 * @author rspace
 *
 */
@Configuration
public class SpringTestConfig {
	
	@Autowired Environment env;
	
	@Bean
	FigshareTemplate FigshareTemplate (){
		FigshareTemplate ft =  new FigshareTemplate(env.getProperty("figshareToken"));
		return ft;
	}
	
}
