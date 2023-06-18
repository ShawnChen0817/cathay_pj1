package com.example.demo.junit;

import java.util.regex.Pattern;

public class DomainUtils {
	
	private static Pattern pDomainName;
	
	//建立domain name 命名規則
	private static final String DOMAIN_NAME_PATTERN = "^((?!-)[A-Za-z0-9]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$";
	
	//compile命名規則後建立pattern
	static {
		pDomainName = Pattern.compile(DOMAIN_NAME_PATTERN);
	}
	
	//is valid domain name ?
	public static boolean isValidDomainName(String domainName) {
		return pDomainName.matcher(domainName).find();
	}
}
