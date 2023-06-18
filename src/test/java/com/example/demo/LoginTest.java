package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.stream.Stream;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.entity.request.UserRequest;
import com.example.demo.exception.GlobalException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class) 
@SpringBootTest(classes = SpringBootLoginTestApplication.class)
@AutoConfigureMockMvc
public class LoginTest {
	
	@Autowired 
	private MockMvc mvc;
	
	/*註冊*/
	
	//1.參數錯誤 2.帳號已註冊
	@DisplayName("註冊失敗")
	@ParameterizedTest
	@CsvSource({"null,10431123aqz!,E000","aqz10431123,10431123aqz!,E002"})
	public void registerError(String account,String password,String status) throws Exception{
		String response = mvc.perform(MockMvcRequestBuilders
					.post("/register")
					.content(asJsonString(new UserRequest(account,password)))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
				.andExpect(exception -> assertTrue(exception.getResolvedException() instanceof GlobalException))
				.andReturn().getResponse().getContentAsString();
		assertEquals(status,getStatus(response));
	}
	
	/*登入*/
	
	//1.參數錯誤 2.無此帳號 3.帳號或密碼錯誤
	@DisplayName("登入失敗")
	@ParameterizedTest
	//@CsvSource({"null,10431123aqz!,E000","aqz10431,aqz10431123!,E001","aqz10431123,aqz10431123!,E003"})
	//@MethodSource("input")
	//@EnumSource(Input.class)
	@ArgumentsSource(Argument.class)
	public void loginError(/*@ConvertWith(ToStringArgumentConverter.class)String input*/String account,String password,String status) throws Exception{
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/login")
				.content(asJsonString(new UserRequest(account,password)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
			.andExpect(exception -> assertTrue(exception.getResolvedException() instanceof GlobalException))
			.andReturn().getResponse().getContentAsString();
		assertEquals(status,getStatus(response));
	} 

//	static class ToStringArgumentConverter extends SimpleArgumentConverter{
//
//		@Override
//		protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
//			// TODO Auto-generated method stub
//			assertEquals(String.class, targetType, "Can only convert to String");
//			return String.valueOf(source);
//		}
//		
//	}
	
	static class Argument implements ArgumentsProvider {

		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
			// TODO Auto-generated method stub
			return Stream.of(
					Arguments.of("","","E000"),
					Arguments.of("aqz10431123","","E000"),
					Arguments.of("","aqz10431123!","E000"),
					Arguments.of("aqz1043","aqz10431123!","E001"),
					Arguments.of("aqz10431123","aqz10431123!","E003")
				);
		}
	}
	
	static Stream<Arguments> input(){
		return Stream.of(
				Arguments.of("","10431123aqz!","E000"),
				Arguments.of("aqz10431","aqz10431123!","E001"),
				Arguments.of("aqz10431123","aqz10431123!","E003"));
	} 
	
	public static String getStatus(String response) throws JSONException {
		JSONObject j = new JSONObject(response);
		String status = (String) j.get("status");
		return status;
	}
	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
