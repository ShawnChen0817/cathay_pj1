package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

//One方(for message)
//Many方(for user)
@Entity
@Table(name = "articles")
public class Article{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String article_content;
	
	@ManyToOne
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private User create_user;
	
	private Date create_time;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String update_user;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Date update_time;
	
	private int status;
	
	@OneToMany(cascade= CascadeType.ALL)
	@JoinColumn(name = "article_id")
	@JsonManagedReference
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<Message> message_list; 

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getArticle_content() {
		return article_content;
	}

	public void setArticle_content(String article_content) {
		this.article_content = article_content;
	}

	public User getCreate_user() {
		return create_user;
	}

	public void setCreate_user(User create_user) {
		this.create_user = create_user;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<Message> getMessage_list() {
		return message_list;
	}

	public void setMessage_list(List<Message> message_list) {
		this.message_list = message_list;
	}
	

}
