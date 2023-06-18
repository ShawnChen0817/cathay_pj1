package com.example.demo.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
	
	@Bean 
	public FilterRegistrationBean logApiFilter() {
		
		FilterRegistrationBean<LogApiFilter> bean = new FilterRegistrationBean<LogApiFilter>();
		bean.setFilter(new LogApiFilter());
		bean.addUrlPatterns("/*");
		bean.setName("logApiFilter");
		return bean;
	} 
}
