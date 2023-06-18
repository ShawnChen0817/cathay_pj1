package com.example.demo.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.jsonwebtoken.Claims;

public class AuthResponse {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String token;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Claims claims;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Claims getClaims() {
		return claims;
	}

	public void setClaims(Claims claims) {
		this.claims = claims;
	}
	
	
}
