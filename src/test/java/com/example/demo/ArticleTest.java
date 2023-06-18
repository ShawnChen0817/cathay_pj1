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

import com.example.demo.entity.request.CheckArticleRequest;
import com.example.demo.entity.request.CheckArticlesRequest;
import com.example.demo.entity.request.CheckUserArticleRequest;
import com.example.demo.entity.request.DeleteArticleRequest;
import com.example.demo.entity.request.LockManagerRequest;
import com.example.demo.entity.request.UpdateArticleRequest;
import com.example.demo.entity.request.UserAndArticleRequest;
import com.example.demo.exception.GlobalException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootLoginTestApplication.class)
@AutoConfigureMockMvc
public class ArticleTest {
	
	@Autowired 
	private MockMvc mvc;

	private static String admin;
	private static String normal;
	
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
	
	/*文章*/
	
	//發布
	@DisplayName("文章發布失敗")
	@ParameterizedTest
	@MethodSource("postInput")
	public void postError(String token,String content,String status) throws Exception {
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/postArticle")
				.content(asJsonString(new UserAndArticleRequest(token,content)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(exception -> assertTrue(exception.getResolvedException() instanceof GlobalException))
			.andReturn().getResponse().getContentAsString();
		Assert.assertEquals(status,getStatus(response));
	}
	
	//發布arguments
	static Stream<Arguments> postInput() {
		return Stream.of(
				Arguments.of("","haha","E000"),//參數錯誤(token為空)
				Arguments.of(normal,"=,","E000"),
				Arguments.of("1","haha","E302"));//token驗證失敗
	}
	
	//文章更新
	@DisplayName("文章更新失敗")
	@ParameterizedTest
	@MethodSource("updateInput")
	public void updateArgError(String token,int articleId,String content,String status) throws Exception {
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/updateArticle")
				.content(asJsonString(new UpdateArticleRequest(token,articleId,content)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(exception -> assertTrue(exception.getResolvedException() instanceof GlobalException))
			.andReturn().getResponse().getContentAsString();
		Assert.assertEquals(status,getStatus(response));
	}
	
	//更新arguments
	static Stream<Arguments> updateInput() {
		return Stream.of(
				Arguments.of(normal,10,"=?","E000"),//參數錯誤(內容不符規則)
				Arguments.of(normal,25,"haha","E100"),//文章不存在
				Arguments.of(normal,1,"haha","E101"),//文章已刪除
				Arguments.of(normal,5,"haha","E102"),//文章鎖定中
				Arguments.of(normal,9,"haha","E301"),//與建立者不符
				Arguments.of(admin,9,"haha","E301")//管理者帳號無法修改起他使用者文章內容
				);
	}
	
	//文章刪除
	@DisplayName("文章刪除失敗")
	@ParameterizedTest
	@MethodSource("deleteInput")
	public void deleteArgError(String token,int articleId,String status) throws Exception {
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/deleteArticle")
				.content(asJsonString(new DeleteArticleRequest(token,articleId)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(exception -> assertTrue(exception.getResolvedException() instanceof GlobalException))
			.andReturn().getResponse().getContentAsString();
		Assert.assertEquals(status,getStatus(response));
	}
	
	//刪除arguments
	static Stream<Arguments> deleteInput() {
		return Stream.of(
				Arguments.of("",10,"E000"),//參數錯誤(內容不符規則)
				Arguments.of(normal,25,"E100"),//文章不存在
				Arguments.of(normal,1,"E101"),//文章已刪除
				Arguments.of(normal,5,"E102"),//文章鎖定中
				Arguments.of(normal,9,"E301")//與建立者不符
				);
	}
	
	//查詢所有文章(參數錯誤)
	@DisplayName("查詢全部文章錯誤")
	@ParameterizedTest
	@MethodSource("queryArticleListInput")
	public void queryArticleListError(String token,String status) throws Exception {
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/queryArticleList")
				.content(asJsonString(new CheckArticleRequest(token,"")))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(exception -> assertTrue(exception.getResolvedException() instanceof GlobalException))
			.andReturn().getResponse().getContentAsString();
		Assert.assertEquals(status,getStatus(response));
	}
	
	//全部查詢arguments
	static Stream<Arguments> queryArticleListInput() {
		return Stream.of(
				Arguments.of("","E000")//參數錯誤(內容不符規則)
			);
	}

	//單一查詢
	@DisplayName("查詢單一文章失敗")
	@ParameterizedTest
	@MethodSource("querySpecifiedArticleInput")
	public void querySpecifiedArticleError(String token,int articleId,String status) throws Exception {
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/querySpecifiedArticle")
				.content(asJsonString(new CheckArticlesRequest(token,articleId)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(exception -> assertTrue(exception.getResolvedException() instanceof GlobalException))
			.andReturn().getResponse().getContentAsString();
		Assert.assertEquals(status,getStatus(response));
	}
	
	//單一查詢arguments
	static Stream<Arguments> querySpecifiedArticleInput() {
		return Stream.of(
				Arguments.of("",25,"E000"),//參數錯誤
				Arguments.of(normal,25,"E100"),//文章不存在
				Arguments.of(normal,2,"E101"),//文章已刪除
				Arguments.of(normal,5,"E102"),//文章已鎖定
				Arguments.of(admin,25,"E100")//文章不存在
			);
	}
	
	//查詢使用者文章
	@DisplayName("查詢使用者文章失敗")
	@ParameterizedTest
	@MethodSource("queryUserArticleListInput")
	public void queryUserArticleListError(String token,String status) throws Exception{
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/queryUserArticle")
				.content(asJsonString(new CheckUserArticleRequest(token,"")))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(exception -> assertTrue(exception.getResolvedException() instanceof GlobalException))
			.andReturn().getResponse().getContentAsString();
		Assert.assertEquals(status,getStatus(response));	
	}
	
	//查詢使用者文章arguments
	static Stream<Arguments> queryUserArticleListInput() {
		return Stream.of(
				Arguments.of("","E000")//參數錯誤
			);
	}

	//上鎖文章
	@DisplayName("鎖定文章失敗")
	@ParameterizedTest
	@MethodSource("lockArticleInput")
	public void lockArticleError(String token,int articleId,String status) throws Exception {
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/lockSpecifiedArticle")
				.content(asJsonString(new LockManagerRequest(token,articleId)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(exception -> assertTrue(exception.getResolvedException() instanceof GlobalException))
			.andReturn().getResponse().getContentAsString();
		Assert.assertEquals(status,getStatus(response));		
	}
	
	//上鎖文章arguments
	static Stream<Arguments> lockArticleInput() {
		return Stream.of(
				Arguments.of("",1,"E000"),//參數錯誤
				Arguments.of(normal,1,"E301"),//權限不足
				Arguments.of(admin,25,"E100"),//無此文章
				Arguments.of(admin,2,"E101")//文章已刪除
			);
	}
	
	//解鎖文章
	@DisplayName("解鎖文章失敗")
	@ParameterizedTest
	@MethodSource("unlockArticleInput")
	public void unlockArticleError(String token,int articleId,String status) throws Exception {
		String response = mvc.perform(MockMvcRequestBuilders
				.post("/lockSpecifiedArticle")
				.content(asJsonString(new LockManagerRequest(token,articleId)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(exception -> assertTrue(exception.getResolvedException() instanceof GlobalException))
			.andReturn().getResponse().getContentAsString();
		Assert.assertEquals(status,getStatus(response));		
	}
	
	//上鎖文章arguments
	static Stream<Arguments> unlockArticleInput() {
		return Stream.of(
				Arguments.of("",1,"E000"),//參數錯誤
				Arguments.of(normal,1,"E301"),//權限不足
				Arguments.of(admin,25,"E100"),//無此文章
				Arguments.of(admin,2,"E101")//文章已刪除
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
