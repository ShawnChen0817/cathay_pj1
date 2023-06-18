package com.example.demo.aop;

import java.security.Key;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.auth.JwtUtil;
import com.example.demo.exception.GlobalException;
import com.example.demo.exceptionHandler.ErrorCode;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Aspect
@Order(1)
public class LogAspect2 {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Value("${spring.data.tokenSecret}")
	String tokenSecret;
	
	public double beginTime;
	public double endTime;
	private static final Logger logger 
	  = LoggerFactory.getLogger(LogAspect2.class);
	
	//@Pointcut(設置joinpoint所在位置)	
	@Pointcut("@annotation(UserInfo)")
	public void userInfo() {		
	}
	
	@Pointcut("@annotation(VerifyToken)")
	public void verify() {
		
	}
	
	@Around(value = "userInfo()")
	public Object getUserInfo(ProceedingJoinPoint joinPoint) throws Throwable{
		//程式執行開始
		Object result = joinPoint.proceed();
		
		String jsonString = mapper.writeValueAsString(result);
		
		JSONObject j = new JSONObject(jsonString);
		
		logger.info("當前登入者資訊: " + j);
		
		return result;
	}
	
	@Around(value= "verify()")
	public Object verifyToken(ProceedingJoinPoint joinPoint) throws Throwable{
		
		//執行程式之前
		Object[] args = joinPoint.getArgs();
		
		String jsonString = mapper.writeValueAsString(args[0]);
		
		JSONObject j = new JSONObject(jsonString);
		
		String token = (String) j.get("token");
		
		//當登入token為空時，應該跳參數錯誤(MOCK時)
		if("".equals(token)) {
			throw new GlobalException(HttpStatus.BAD_REQUEST,ErrorCode.PARAM_ERROR,"參數錯誤");
		}
		
		Key secretKey = jwtUtil.generateKey(tokenSecret);
		
		try {
			jwtUtil.parseJWT(token, secretKey);
			logger.info("驗證成功");
		} catch (JWTVerificationException e) {
			// TODO Auto-generated catch block
			throw new GlobalException(HttpStatus.UNAUTHORIZED,ErrorCode.VERIFY_ERROR,"驗證失敗");
		}
		
		//程式執行開始
		Object result = joinPoint.proceed();
		return result;
	}
	
}
