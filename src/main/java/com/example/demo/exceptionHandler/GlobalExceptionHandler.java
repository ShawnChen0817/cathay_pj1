package com.example.demo.exceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.response.UnifiedResponse;
import com.example.demo.exception.*;

//若發生運行錯誤則跑到以下程式
@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {
	
	//系統錯誤(Response)
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> Excpetion(Exception exception){
	      Logger myLogger = LoggerFactory.getLogger(exception.getClass().getName());
	      myLogger.error("Global Exception Handler. Message: {}", exception.getMessage());
	 
	      UnifiedResponse response = new UnifiedResponse();
	      response.setStatus("E400");
	      response.setMessage("系統錯誤");
	      response.setSuccess(false);
	      response.setTotal(0);
	      response.setData(null);
	 
	     // 回覆一個Response實體
	     return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
	//自訂運行錯誤(Response)
	@ResponseBody
	//統一處理GlobalException例外
	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<Object> GlobalExcpetion(GlobalException exception){
	      Logger myLogger = LoggerFactory.getLogger(exception.getClass().getName());
	      myLogger.error("Global Exception Handler. Message: {}", exception.getMsg());
	      myLogger.error("Global Exception Handler. Error code: {}", exception.getErrorCode());
	        
	      UnifiedResponse response = new UnifiedResponse();
	      response.setStatus(exception.getErrorCode());
	      response.setMessage(exception.getMsg());
	      response.setSuccess(false);
	      response.setTotal(0);
	      response.setData(null);
	      // 回覆一個Response實體
	      return new ResponseEntity<>(response,exception.getStatus());
	}
}
