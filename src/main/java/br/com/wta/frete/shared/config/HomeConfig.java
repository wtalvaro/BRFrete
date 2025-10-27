package br.com.wta.frete.shared.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class HomeConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(@NonNull ViewControllerRegistry registry) {
		WebMvcConfigurer.super.addViewControllers(registry);
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/home").setViewName("home");
	}
}
