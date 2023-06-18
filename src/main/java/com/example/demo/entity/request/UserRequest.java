package com.example.demo.entity.request;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

public class UserRequest {

	@Length(min=5,max=15)
	private String account;
	
	//設置密碼由字母與數字組成，且須超過8位數
	@Pattern(regexp="^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[~!@#$%^&])[\\da-zA-Z~!@#$%^&]{9,}$", message = "請依照規則註冊(英文 + 數字 + 特殊位元且需超過8位數)")
	private String password;
	

	public UserRequest( String account,String password) {
		super();
		this.account = account;
		this.password = password;
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

	
}
