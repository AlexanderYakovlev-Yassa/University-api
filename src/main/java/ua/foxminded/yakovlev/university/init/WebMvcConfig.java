package ua.foxminded.yakovlev.university.init;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("ua.foxminded.yakovlev.university")
public class WebMvcConfig implements WebMvcConfigurer {
    
	@Bean
	public HttpMessageConverters getMessageConverter() {
		return new HttpMessageConverters();
	}
	
    @Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*");
    }
}