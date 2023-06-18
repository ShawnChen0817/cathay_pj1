package com.example.demo.exceptionHandler;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public enum ErrorCode {
	PARAM_ERROR("E000","參數錯誤"),
	
	ACCOUNT_NOTFOUND("E001","無此帳號"),
	
	ACCOUNT_REGISTERD("E002","帳號已註冊"),
	
	LOGIN_ERROR("E003","帳號或密碼錯誤"),
	
	ARTICLE_NOTFOUND("E100","無此文章"),
	
	ARTICLE_ALREADY_DELETE("E101","文章已刪除"),
	
	ARTICLE_LOCK("E102","文章鎖定中"),
	
	MESSAGE_NOTFOUND("E200","無此留言"),
	
	MESSAGE_ALREADY_DELETE("E201","留言已刪除"),
	
	MESSAGE_LOCK("E202","留言鎖定中"),
	
	//2種情況: 1.與建立者不符 2.Admin帳號才可使用
	PERMISSION_ERROR("E301","權限不足"),
	
	//2種情況: 1.token錯誤 2.token過期
	VERIFY_ERROR("E302","驗證失敗");
	
	private String errorCode;
	private String errorMessage;
	private HttpStatus status;
	ErrorCode(String errorCode, String errorMessage) {
		// TODO Auto-generated constructor stub
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public HttpStatus getStatus() {
		return status;
	}

}
