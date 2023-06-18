package com.example.demo.entity.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


public class UserAndArticleRequest {
	
	@NotBlank
	private String token;
	
	//不能以=+-@><,.。開頭，且不能包含><%
	@NotBlank
	@Pattern(regexp="^((?!=|\\+|-|@|>|<|%).)((?!>|<|%).){0,127}$")
	private String article_content;
	
	public UserAndArticleRequest(String token, String article_content) {
		super();
		this.token = token;
		this.article_content = article_content;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getArticle_content() {
		return article_content;
	}

	public void setArticle_content(String article_content) {
		this.article_content = article_content;
	}

}
