package com.chhei.mall.mall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

@Configuration
public class MallCorsConfiguration {
	@Bean
	public CorsWebFilter corsWebFilter(){
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		configuration.addAllowedOriginPattern("*");
		configuration.setAllowCredentials(true);
		source.registerCorsConfiguration("/**",configuration);
		return new CorsWebFilter(source);
	}
}
