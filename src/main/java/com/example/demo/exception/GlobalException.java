package com.example.demo.exception;

import org.springframework.http.HttpStatus;

import com.example.demo.exceptionHandler.ErrorCode;

public class GlobalException extends RuntimeException {
	
	private String errorCode;
	
	private String msg;
	
	private HttpStatus status;
	
	public GlobalException(HttpStatus status,ErrorCode error,String returnValue) {
		//若是回傳資料等於自訂Enum的Error Message
		if(returnValue.equals(error.getErrorMessage())) {
			this.errorCode = error.getErrorCode();
			this.msg = error.getErrorMessage();
			this.status = status;
		}
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
}
