package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.entity.Article;
import com.example.demo.entity.request.CheckArticleRequest;
import com.example.demo.entity.request.CheckArticlesRequest;
import com.example.demo.entity.request.CheckUserArticleRequest;
import com.example.demo.entity.request.DeleteArticleRequest;
import com.example.demo.entity.request.DeleteMessageRequest;
import com.example.demo.entity.request.LockManagerRequest;
import com.example.demo.entity.request.MessageRequest;
import com.example.demo.entity.request.UpdateArticleRequest;
import com.example.demo.entity.request.UpdateMessageRequest;
import com.example.demo.entity.request.UserAndArticleRequest;
import com.example.demo.entity.request.UserRequest;
import com.example.demo.entity.response.UnifiedArticleListResponse;
import com.example.demo.entity.response.UnifiedArticleResponse;
import com.example.demo.entity.response.UnifiedResponse;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class) 
@SpringBootTest(classes = SpringBootLoginTestApplication.class)
@AutoConfigureMockMvc
public class TestSuccess {
	
	@MockBean
	private UserService service;
	
	@Autowired
	private MockMvc mvc;
	
	private static String admin;
	private static String normal;
	
	@BeforeAll
	static void getToken() {
		admin = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoiYWRtaW4iLCJleHAiOjE2ODk1MTQ3MjEsImlhdCI6MTY4MDUxNDcyMX0.4QfQ9QD1GPXZBGTOQ-qUnq1899F4vzJcvkwyMplOKzs" ;
		normal = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwIiwiaXNzIjoiYXF6MTA0MzExMjMiLCJleHAiOjE2ODk1MTQ3MDEsImlhdCI6MTY4MDUxNDcwMX0.e2DclrC8_YsTaMNeYAdGTrj41KBh76MomoTwUp8aNNg";
	}
	
	//自訂回應
	public UnifiedResponse UnifiedManageResponse(String status,String message,Integer total,String data) {
		UnifiedResponse response = new UnifiedResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setSuccess(true);
		response.setTotal(total);
		response.setData(data);
		return response;
	}
	
	//文章管理成功(回應文章列表)
	public UnifiedArticleListResponse ArticleListResponse(String status,String message,Integer total,List<Article> articleList) {
		UnifiedArticleListResponse response = new UnifiedArticleListResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setSuccess(true);
		response.setTotal(total);
		response.setArticle_list(articleList);
		return response;
	}

	//文章管理成功(回應單筆文章+底下留言)
	public UnifiedArticleResponse SpecifiedArticleResponse(String status,String message,Integer total,Article article) {
		UnifiedArticleResponse response = new UnifiedArticleResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setSuccess(true);
		response.setTotal(total);
		response.setArticle(article);
		return response;
	}
	
	//註冊成功
	@Test
	public void registerSuccess() throws Exception {
		//自訂service.register(aqz1043112與Aqz10431123!)時輸出為UnifiedManageResponse("0000","register success",0,null)
		Mockito.when(service.register("aqz1043112", "Aqz10431123!")).thenReturn(UnifiedManageResponse("0000","register success",0,null));
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/register")
				.content(asJsonString(new UserRequest("aqz1043112","Aqz10431123!")))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse().getContentAsString();
		//System.out.println(response);
		assertAll(
				() -> assertEquals("0000",getStatus(response)),
				() -> assertEquals("register success",getMsg(response))
			);
	}
	
	//登入成功
	@Test 
	public void loginSuccess() throws Exception{
		Mockito.when(service.login("aqz10431123", "Aqz10431123!")).thenReturn(UnifiedManageResponse("0000","login success",0,null));
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/login")
				.content(asJsonString(new UserRequest("aqz10431123","Aqz10431123!")))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse().getContentAsString();
		assertAll(
				() -> assertEquals("0000",getStatus(response)),
				() -> assertEquals("login success",getMsg(response))
			);
		
	}
	
	//文章發布成功
	@Test 
	public void postArticleSuccess() throws Exception{
		Mockito.when(service.postArticle(normal, "ha")).thenReturn(UnifiedManageResponse("0000","post success",0,null));
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/postArticle")
				.content(asJsonString(new UserAndArticleRequest(normal,"ha")))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse().getContentAsString();
		assertEquals("0000",getStatus(response));
		assertEquals("post success",getMsg(response));
	}
	
	//文章修改成功
	@Test 
	public void updateArticleSuccess() throws Exception{
		Mockito.when(service.renewArticle(normal,10,"ha")).thenReturn(UnifiedManageResponse("0000","update success",0,null));
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/updateArticle")
				.content(asJsonString(new UpdateArticleRequest(normal,10,"ha")))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse().getContentAsString();
		assertAll(
				() -> assertEquals("0000",getStatus(response)),
				() -> assertEquals("update success",getMsg(response))
			);
	}
	
	//文章刪除成功
	@Test 
	public void deleteArticleSuccess() throws Exception{
		Mockito.when(service.deleteArticle(normal,10)).thenReturn(UnifiedManageResponse("0000","delete success",0,null));
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/deleteArticle")
				.content(asJsonString(new DeleteArticleRequest(normal,10)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse().getContentAsString();
		assertAll(
				() -> assertEquals("0000",getStatus(response)),
				() -> assertEquals("delete success",getMsg(response))
			);	
	}
	
	//全部文章查詢
	@Test 
	public void queryArticleListSuccess() throws Exception{
		Mockito.when(service.queryAllArticle(normal)).thenReturn(ArticleListResponse("0000","query success",0,null));
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/queryArticleList")
				.content(asJsonString(new CheckArticleRequest(normal,"")))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse().getContentAsString();
		assertAll(
				() -> assertEquals("0000",getStatus(response)),
				() -> assertEquals("query success",getMsg(response))
			);
	}
	
	//單一文章查詢
	@Test 
	public void querySpecifiedArticleSuccess() throws Exception{
		Mockito.when(service.querySpecifiedArticle(normal,10)).thenReturn(SpecifiedArticleResponse("0000","query success",1,new Article()));
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/querySpecifiedArticle")
				.content(asJsonString(new CheckArticlesRequest(normal,10)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse().getContentAsString();
		assertAll(
				() -> assertEquals("0000",getStatus(response)),
				() -> assertEquals("query success",getMsg(response))
			);
	}
	
	//使用者文章查詢
	@Test 
	public void queryUserArticleSuccess() throws Exception{
		Mockito.when(service.querySpecifiedUserArticle(normal)).thenReturn(UnifiedManageResponse("0000","query success",0,"無文章顯示"));
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/queryUserArticle")
				.content(asJsonString(new CheckUserArticleRequest(normal,"")))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse().getContentAsString();
		assertAll(
				() -> assertEquals("0000",getStatus(response)),
				() -> assertEquals("query success",getMsg(response))
			);
	}
	
	//文章上鎖
	@Test 
	public void lockArticleSuccess() throws Exception{
		Mockito.when(service.lockArticle(admin,10)).thenReturn(UnifiedManageResponse("0000","lock success",0,null));
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/lockSpecifiedArticle")
				.content(asJsonString(new LockManagerRequest(admin,10)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse().getContentAsString();
		assertAll(
				() -> assertEquals("0000",getStatus(response)),
				() -> assertEquals("lock success",getMsg(response))
			);
	}
	
	//文章解鎖
	@Test 
	public void unlockArticleSuccess() throws Exception{
		Mockito.when(service.unlockArticle(admin,10)).thenReturn(UnifiedManageResponse("0000","unlock success",0,null));
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/unlockSpecifiedArticle")
				.content(asJsonString(new LockManagerRequest(admin,10)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse().getContentAsString();
		assertAll(
				() -> assertEquals("0000",getStatus(response)),
				() -> assertEquals("unlock success",getMsg(response))
			);		
	}
	
	//留言發布
	@Test 
	public void postMessageSuccess() throws Exception{
		Mockito.when(service.postMessage(normal, 10,"ha")).thenReturn(UnifiedManageResponse("0000","post success",0,null));
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/postMessage")
				.content(asJsonString(new MessageRequest(normal,10,"ha")))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse().getContentAsString();
		assertAll(
				() -> assertEquals("0000",getStatus(response)),
				() -> assertEquals("post success",getMsg(response))
			);
	}
	
	//留言修改
	@Test 
	public void updateMessageSuccess() throws Exception{
		Mockito.when(service.renewMessage(normal,10,"ha")).thenReturn(UnifiedManageResponse("0000","update success",0,null));
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/updateMessage")
				.content(asJsonString(new UpdateMessageRequest(normal,10,"ha")))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse().getContentAsString();
		assertAll(
				() -> assertEquals("0000",getStatus(response)),
				() -> assertEquals("update success",getMsg(response))
			);
	}
	
	//留言刪除
	@Test 
	public void deleteMessageSuccess() throws Exception{
		Mockito.when(service.deleteMessage(normal,10)).thenReturn(UnifiedManageResponse("0000","delete success",0,null));
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/deleteMessage")
				.content(asJsonString(new DeleteMessageRequest(normal,10)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse().getContentAsString();
		assertAll(
				() -> assertEquals("0000",getStatus(response)),
				() -> assertEquals("delete success",getMsg(response))
			);
	}
	
	//留言上鎖
	@Test 
	public void lockMessageSuccess() throws Exception{
		Mockito.when(service.lockMessage(admin,10)).thenReturn(UnifiedManageResponse("0000","lock success",0,null));
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/lockSpecifiedMessage")
				.content(asJsonString(new LockManagerRequest(admin,10)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse().getContentAsString();
		assertAll(
				() -> assertEquals("0000",getStatus(response)),
				() -> assertEquals("lock success",getMsg(response))
			);
	}
	
	//留言解鎖
	@Test 
	public void unlockMessageSuccess() throws Exception{
		Mockito.when(service.unlockMessage(admin,10)).thenReturn(UnifiedManageResponse("0000","unlock success",0,null));
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/unlockSpecifiedMessage")
				.content(asJsonString(new LockManagerRequest(admin,10)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse().getContentAsString();
		assertAll(
				() -> assertEquals("0000",getStatus(response)),
				() -> assertEquals("unlock success",getMsg(response))
			);
	}
	
	public static String getStatus(String response) throws JSONException {
		JSONObject j = new JSONObject(response);
		String status = (String) j.get("status");
		return status;
	}
	
	public static String getMsg(String response) throws JSONException {
		JSONObject j = new JSONObject(response);
		String msg = (String) j.get("message");
		return msg;
	}
	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
