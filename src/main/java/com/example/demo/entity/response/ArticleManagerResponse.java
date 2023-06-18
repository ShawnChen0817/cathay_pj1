package com.example.demo.entity.response;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONType;
import com.example.demo.entity.Article;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JSONType(orders= {"error","message","account","result","article_list","article"})
public class ArticleManagerResponse {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String status;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String message;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String user;
	
	@JsonProperty(value="data")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String result;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Article> article_list;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Article article;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public List<Article> getArticle_list() {
		return article_list;
	}

	public void setArticle_list(List<Article> article_list) {
		this.article_list = article_list;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
