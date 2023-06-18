package com.example.demo.junit;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controller.UserController;
import com.example.demo.service.UserService;

//@WebMvcTest(UserController.class)
@RunWith(Parameterized.class)
public class CalculateTest {

	private int expectedResult;
	private int firstNumber;
	private int secondNumber;
	private Calculate add;
	
	@MockBean
	private UserService service;
	
	private static final Logger logger = LoggerFactory.getLogger(CalculateTest.class);

	public CalculateTest(int firstNumber, int secondNumber,int expectedResult) {
		super();
		this.firstNumber = firstNumber;
		this.secondNumber = secondNumber;
		this.expectedResult = expectedResult;
	}
	@Before
	public void initialize() {
		add = new Calculate();
	}
	
	@Parameterized.Parameters(name = "{index} : add({0}+{1}) = {2} ")
	public static Collection<Object[]> addedNumbers() {
		return Arrays.asList(new Object[][] { { 1, 2, 3 }, { 2, 3, 5 },
				{ 3, 4, 7 }, { 4, 5, 9 } });
	}

	@Test
	public void sum() {
		logger.info("Addition with parameters : " + firstNumber + " and " + secondNumber);
		
		assertEquals(expectedResult, add.sum(firstNumber, secondNumber));
	}
	
//	@Test
//	public void testMockito() throws Exception{
//		Mockito.when(service.getHello()).thenReturn("hello");
//	}

}
