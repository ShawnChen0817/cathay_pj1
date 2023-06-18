package com.example.demo;

import static org.junit.Assert.assertTrue;

import java.util.stream.Stream;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.entity.request.DeleteMessageRequest;
import com.example.demo.entity.request.LockManagerRequest;
import com.example.demo.entity.request.MessageRequest;
import com.example.demo.entity.request.UpdateMessageRequest;
import com.example.demo.exception.GlobalException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootLoginTestApplication.class)
@AutoConfigureMockMvc
public class MessageTest {

	@Autowired 
	private MockMvc mvc;
	
	/*admin and normal*/
	private static String admin; 
	
	private static String normal;
	
	/*留言*/
	@BeforeAll
	static void getToken() {
		admin = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoiYWRtaW4iLCJleHAiOjE2ODk1MTQ3MjEsImlhdCI6MTY4MDUxNDcyMX0.4QfQ9QD1GPXZBGTOQ-qUnq1899F4vzJcvkwyMplOKzs" ;
		normal = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwIiwiaXNzIjoiYXF6MTA0MzExMjMiLCJleHAiOjE2ODk1MTQ3MDEsImlhdCI6MTY4MDUxNDcwMX0.e2DclrC8_YsTaMNeYAdGTrj41KBh76MomoTwUp8aNNg";
	}
	
	@AfterAll
	static void clearToken() {
		admin = "";
		normal = "";
	}
	
	//發布
	@DisplayName("留言發布失敗")
	@ParameterizedTest
	@MethodSource("postInput")
	public void postArgError(String token,int articleId,String content,String status) throws Exception {
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/postMessage")
				.content(asJsonString(new MessageRequest(token,articleId,content)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(exception -> assertTrue(exception.getResolvedException() instanceof GlobalException))
			.andReturn().getResponse().getContentAsString();
		Assert.assertEquals(status,getStatus(response));
	}
	
	//發布arguments
	static Stream<Arguments> postInput(){
		return Stream.of(
					Arguments.of(normal,5,"=?","E000"), //內容格式錯誤
					Arguments.of("15515151515",25,"Yeah","E302"), //token驗證失敗
					Arguments.of(normal,25,"Yeah","E100"), //文章不存在
					Arguments.of(normal,2,"Yeah","E101"), //文章已刪除
					Arguments.of(normal,5,"Yeah","E102") //文章鎖定中
				);
	}
	
	//留言更新
	@DisplayName("留言更新失敗")
	@ParameterizedTest
	@MethodSource("updateInput")
	public void updateError(String token,int messageId,String content,String status) throws Exception {

		String response = mvc.perform(MockMvcRequestBuilders
				.post("/updateMessage")
				.content(asJsonString(new UpdateMessageRequest(token,messageId,content)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(exception -> assertTrue(exception.getResolvedException() instanceof GlobalException))
			.andReturn().getResponse().getContentAsString();
		Assert.assertEquals(status,getStatus(response));
	}
	
	static Stream<Arguments> updateInput(){
		return Stream.of(
				Arguments.of(normal,5,"=?","E000"), //內容格式錯誤
				Arguments.of(normal,25,"Yeah","E200"), //留言不存在
				Arguments.of(normal,5,"Yeah","E201"), //留言已刪除
				Arguments.of(normal,7,"Yeah","E102"), //文章鎖定中
				Arguments.of(normal,12,"Yeah","E202"), //留言鎖定中
				Arguments.of(normal,11,"Yeah","E301"), //與建立者不符
				Arguments.of(admin,8,"Yeah","E301") //admin不能隨意修改內容
			);
	}
	
	
	//留言刪除
	@DisplayName("留言刪除失敗")
	@ParameterizedTest
	@MethodSource("deleteInput")
	public void deleteArgError(String token,int messageId,String status) throws Exception {
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/deleteMessage")
				.content(asJsonString(new DeleteMessageRequest(token,messageId)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(exception -> assertTrue(exception.getResolvedException() instanceof GlobalException))
			.andReturn().getResponse().getContentAsString();
		Assert.assertEquals(status,getStatus(response));
	}
	
	static Stream<Arguments> deleteInput(){
		return Stream.of(
				Arguments.of("",1,"E000"), //內容格式錯誤
				Arguments.of(normal,25,"E200"), //留言不存在
				Arguments.of(normal,5,"E201"), //留言已刪除
				Arguments.of(normal,7,"E102"), //文章鎖定中
				Arguments.of(normal,12,"E202"), //留言鎖定中
				Arguments.of(normal,11,"E301") //與建立者不符
			);
	}
	
	/*管理者功能 留言上鎖+解鎖*/
	
	//留言上鎖	
	@DisplayName("上鎖留言失敗")
	@ParameterizedTest
	@MethodSource("lockInput")
	public void lockMessagebutNotAdmin(String token,int messageId,String status) throws Exception {
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/lockSpecifiedMessage")
				.content(asJsonString(new LockManagerRequest(token,messageId)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(exception -> assertTrue(exception.getResolvedException() instanceof GlobalException))
			.andReturn().getResponse().getContentAsString();
		Assert.assertEquals(status,getStatus(response));
	}
	
	static Stream<Arguments> lockInput(){
		return Stream.of(
				Arguments.of("",1,"E000"), //參數錯誤
				Arguments.of(normal,7,"E301"), //normal無權限
				Arguments.of(admin,90,"E200"), //無此留言
				Arguments.of(admin,2,"E201"), //留言已刪除
				Arguments.of(admin,7,"E102") //文章鎖定中
			);
	}

	
	//留言解鎖
	@DisplayName("解鎖留言失敗")
	@ParameterizedTest
	@MethodSource("unlockInput")
	public void unlockMessagebutNotAdmin(String token,int messageId,String status) throws Exception {
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/unlockSpecifiedMessage")
				.content(asJsonString(new LockManagerRequest(token,messageId)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(exception -> assertTrue(exception.getResolvedException() instanceof GlobalException))
			.andReturn().getResponse().getContentAsString();
		Assert.assertEquals(status,getStatus(response));
	}
	
	static Stream<Arguments> unlockInput(){
		return Stream.of(
				Arguments.of("",1,"E000"), //參數錯誤
				Arguments.of(normal,7,"E301"), //normal無權限
				Arguments.of(admin,90,"E200"), //無此留言
				Arguments.of(admin,2,"E201"), //留言已刪除
				Arguments.of(admin,7,"E102") //文章鎖定中
			);
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
