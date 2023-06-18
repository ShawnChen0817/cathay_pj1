package com.example.demo.entity.request;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckUserArticleRequest {

	@NotBlank
	@JsonProperty("token")
	private String token;
	
	public CheckUserArticleRequest(String token,String status) {
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
