package api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.researchspace.figshare.impl.FigshareTemplate;
/**
 * Test configuration. REquirs personal token set as system property e.g. -DfigshareToken=XXX 
 * @author rspace
 *
 */
@Configuration
public class SpringTestConfig {
	
	@Autowired Environment env;
	
	@Bean
	FigshareTemplate FigshareTemplate (){
		FigshareTemplate ft =  new FigshareTemplate();
		ft.setPersonalToken(env.getProperty("figshareToken"));
		return ft;
	}
	
}
