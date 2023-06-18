package com.example.demo.junit;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DomainUtilsTest {
	
	private String domain;
	
	private boolean expected;
	
	//參數依照建構子輸入
	public DomainUtilsTest(String domain,boolean expected) {
		super();
		this.domain=domain;
		this.expected=expected;
	}
	
	//建立要執行的參數
	@Parameters(name = "{index} : {0} = {1}")
	public static Iterable<Object[]> Parameter(){
		return Arrays.asList(new Object[][] {
			{"google.com",true},
			{"-gitbook.com",false},
			{"facebook-.com",false}
		});
	}
	
	@Test
	public void TestDomainUitl() {
		assertEquals( expected,DomainUtils.isValidDomainName(domain));
	}
	
}
