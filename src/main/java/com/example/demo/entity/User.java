package com.example.demo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

//one方(for article、message)
@Entity
@Table(name = "user_info")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String account;
	
	@JsonIgnore
	private String password;
	
	@JsonIgnore
	private int account_type;
	
	//一個使用者可以有多篇文章與留言
	@OneToMany(cascade= CascadeType.ALL)
	@JoinColumn(name = "create_user_id")
	@JsonIgnore
	private List<Article> article_list;
	
	@OneToMany(cascade= CascadeType.ALL)
	@JoinColumn(name = "create_user_id")
	@JsonIgnore
	private List<Message> message_list; 
		
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public int getAccount_type() {
		return account_type;
	}

	public void setAccount_type(int account_type) {
		this.account_type = account_type;
	}

	public List<Article> getArticle_list() {
		return article_list;
	}

	public void setArticle_list(List<Article> article_list) {
		this.article_list = article_list;
	}

	
	
}
