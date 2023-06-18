package com.example.demo.entity.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UpdateArticleRequest {
	
	@NotBlank
	private String token;
	
	@NotNull(message="文章id不為空")
	private Integer id;
	
	@NotBlank
	@Pattern(regexp="^((?!=|\\+|-|@|>|<|%).)((?!>|<|%).){0,127}$")
	private String article_content;
	
	
	public UpdateArticleRequest(String token,Integer id, String article_content) {
		super();
		this.token = token;
		this.id = id;
		this.article_content = article_content;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setArticle_id(Integer id) {
		this.id = id;
	}

	public String getArticle_content() {
		return article_content;
	}

	public void setArticle_content(String article_content) {
		this.article_content = article_content;
	}

}
