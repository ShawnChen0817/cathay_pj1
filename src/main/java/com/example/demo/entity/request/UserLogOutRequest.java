package com.example.demo.entity.request;

import javax.validation.constraints.NotBlank;

public class UserLogOutRequest {
	
	@NotBlank
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
