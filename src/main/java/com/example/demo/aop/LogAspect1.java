package com.example.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Aspect
@Order(0)
public class LogAspect1 {
	
	public double beginTime;
	public double endTime;
	private static final Logger logger 
	  = LoggerFactory.getLogger(LogAspect1.class);
	
	@Pointcut(value="@annotation(com.example.demo.aop.TimeOperation)")
	public void TimeOperation() {
	}
	
	@Around(value="TimeOperation()")
	public Object operationTime(ProceedingJoinPoint point) throws Throwable {
		//pointcut前
		this.beginTime=System.currentTimeMillis();
		logger.info("進入controller時間: "+beginTime);

		//程式執行
		Object result = point.proceed();

	    //pointcut後
		this.endTime=System.currentTimeMillis();
		logger.info("離開controller時間: "+endTime);
		
		logger.info("在controller花費時間: " + (endTime-beginTime));
		return result;
	}

}
