package com.example.demo.entity.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UpdateMessageRequest {
	
	@NotBlank
	private String token;
	
	@NotNull(message="留言id不為空")
	private Integer id;
	
	@NotBlank(message="留言內容不為空")
	@Pattern(regexp="^((?!=|\\+|-|@|>|<|%).)((?!>|<|%).){0,127}$")
	private String message_content;
	
	public UpdateMessageRequest(String token, Integer id,String message_content) {
		super();
		this.token = token;
		this.id = id;
		this.message_content = message_content;
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
	public String getMessage_content() {
		return message_content;
	}

	public void setMessage_content(String message_content) {
		this.message_content = message_content;
	}	
}
