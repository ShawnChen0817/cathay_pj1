package com.example.demo.cryptography;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//利用雜湊的方式進行加密
public class MD5Util {
	
	public static String md5(String password) {
		if(password.isEmpty()) {
			return "";
		}
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			byte[] bytes = md5.digest(password.getBytes("UTF-8"));
			String result = "";
			for(byte b : bytes) {
				//將整數(4bytes)轉換成十六進位字串
				String temp = Integer.toHexString(b & 0xff);
				if(temp.length() == 1) {
					temp="0"+temp;
				}
				result += temp;
			}
			return result;
		}catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
}
