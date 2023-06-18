package com.example.demo.entity.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DeleteMessageRequest {
	
	@NotBlank
	private String token;
	
	@NotNull(message="留言id不為空")
	private Integer id;
	
	public DeleteMessageRequest(String token,Integer id) {
		super();
		this.token = token;
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
