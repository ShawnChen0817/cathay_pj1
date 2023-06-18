package com.example.demo.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;

//Many方(for user、article)
@Entity
@Table(name = "messages")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JsonBackReference
	private Article article;
	
	//留言內容
	private String message_content;
	
	//留言是誰建立的
	@ManyToOne
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private User create_user;
	
	//訊息建立時間
	private Date create_time;
	
	//訊息是誰更改的
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String update_user;
	
	//訊息更改時間
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Date update_time;
	
	//留言的狀態(0 or 1)
	//0:正常,1:刪除(刪除後的留言無法被使用者查看，除非為最高權限帳號)
	public int status;

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getMessage_content() {
		return message_content;
	}

	public void setContent(String message_content) {
		this.message_content = message_content;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public User getCreate_user() {
		return create_user;
	}

	public void setCreate_user(User create_user) {
		this.create_user = create_user;
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

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}
	
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Messages: id=" + id + "\n" +
						"message_content=" + message_content + "\n"+
						"create_user=" + create_user + "\n"+
						"create_time=" + create_time + "\n"+
						"update_user=" + update_user + "\n"+
						"article=" + article +
						"update_time=" + update_time  + "\n"+
						"status=" + status + "\n\n";
	}
	
}
