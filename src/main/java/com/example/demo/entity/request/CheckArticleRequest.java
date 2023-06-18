package com.example.demo.entity.request;

import javax.validation.constraints.NotBlank;

public class CheckArticleRequest {
	
	@NotBlank
	private String token;
	
	public CheckArticleRequest(String token,String status) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
