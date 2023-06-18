package com.example.demo.entity.response;

import java.util.List;

import com.example.demo.entity.Article;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UnifiedArticleListResponse {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String status;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String message;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private boolean success;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer total;
	
	@JsonProperty("data")
	private List<Article> article_list;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<Article> getArticle_list() {
		return article_list;
	}

	public void setArticle_list(List<Article> article_list) {
		this.article_list = article_list;
	}
}
