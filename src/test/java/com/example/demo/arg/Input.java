package com.example.demo.arg;

public enum Input {
	USER_INFO1("aqz10431123","aqz10431123!","E003"),
	USER_INFO2("","aqz10431123!","E000");
	private String account;
	private String password;
	private String status;
	Input(String account, String password,String status) {
		this.account = account;
		this.password=password;
		this.status=status;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
