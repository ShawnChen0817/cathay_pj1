package com.example.demo.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UnifiedResponse {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String status;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String message;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private boolean success;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer total;
	
	private String data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
