package com.example.demo.entity.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class MessageRequest {
	
	@NotBlank
	private String token;
	
	@NotNull(message="文章id不為空")
	private Integer article_id;
	
	@NotBlank(message="留言內容不為空")
	@Pattern(regexp="^((?!=|\\+|-|@|>|<|%).)((?!>|<|%).){0,127}$")
	private String message_content;
	
	public MessageRequest(String token, Integer article_id,String message_content) {
		super();
		this.token = token;
		this.article_id = article_id;
		this.message_content = message_content;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public Integer getArticle_id() {
		return article_id;
	}

	public void setArticle_id(Integer article_id) {
		this.article_id = article_id;
	}

	public String getMessage_content() {
		return message_content;
	}

	public void setMessage_content(String message_content) {
		this.message_content = message_content;
	}
	
}
