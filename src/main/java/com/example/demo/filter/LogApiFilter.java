package com.example.demo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

//@WebFilter(urlPatterns = "/*", filterName = "logApiFilter")
public class LogApiFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(LogApiFilter.class);
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		chain.doFilter(request,response);
		
		int httpStatus = response.getStatus();
		String httpMethod = request.getMethod();
		String uri = request.getRequestURI();
		String param = request.getQueryString(); //類似 @RequestParam 取得參數
		
		if(param!=null) {
			uri+="?";
		}
		
		logger.info(String.valueOf(httpStatus) + " " + httpMethod + " " + uri );
	}

}
