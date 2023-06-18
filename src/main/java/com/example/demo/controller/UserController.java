package com.example.demo.controller;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.aop.TimeOperation;
import com.example.demo.aop.UserInfo;
import com.example.demo.aop.VerifyToken;
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
import com.example.demo.entity.response.UnifiedResponse;
import com.example.demo.exception.GlobalException;
import com.example.demo.exceptionHandler.ErrorCode;
import com.example.demo.service.UserService;

@RestController
public class UserController {
	
	/*個人想法*/
	
	/*比重: 1.使用者無權限(真) 
	 		2.無文章(真) 
	 		3.無文章(狀態) 
	 		4.無權限(輸入帳號有權限，但修改訊息不符和建立者帳號)*/
	
	/*dologin API代表為登入後才能使用的功能*/
	//1.需要登入成功後才能得到發布文章的許可權
	//2.需要有許可權才可以發布文章、留言
	//3.登出後則無發表文章、留言許可權
	//4.登入成功後可即可進行發文、留言
	//6.發文結束後，若需更改或刪除文章、留言，需為自己或是最高權限帳號
	//7.刪除文章、留言則以狀態呈現，1:代表刪除，此時只有最高權限者可看到所有文章、留言 0:代表所有使用者接能夠看到文章、留言
	//8.單一查詢文章且包含此文章留言
	
	/*延伸 : AOP實作*/
	//進行文章管理 須先進行登入，若無則輸出請先登入再繼續
	//進行留言管理 須先進行登入，若無則輸出請先登入再繼續
	
	@Autowired
	UserService userService;
/*註冊、登入、登出*/	
	
	//註冊帳號密碼
	@TimeOperation
	@PostMapping("/register")
	//@RequestBody抓取我們在Postman設置的請求數據
	public UnifiedResponse register(@Valid @RequestBody UserRequest request,BindingResult result) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, IOException{
			
		if(result.hasErrors()) {
			throw new GlobalException(HttpStatus.BAD_REQUEST,ErrorCode.PARAM_ERROR,"參數錯誤");
		}
		else {
			String account = request.getAccount();
			String password = request.getPassword();
			return userService.register(account, password);
		}	
	}
		
	//以帳號密碼登入
	@UserInfo
	@TimeOperation
	@PostMapping("/login")
	public UnifiedResponse login(@Valid @RequestBody UserRequest request,BindingResult result) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException{
			
		if(result.hasErrors()) {
			throw new GlobalException(HttpStatus.BAD_REQUEST,ErrorCode.PARAM_ERROR,"參數錯誤");
		}
		else {
			String account = request.getAccount();
			String password = request.getPassword();
			return userService.login(account, password);
		}
	}
	
/*文章功能*/
	
	//發布文章
	@TimeOperation
	@VerifyToken
	@PostMapping("/postArticle")
	public UnifiedResponse postArticle(@Valid @RequestBody UserAndArticleRequest request,BindingResult result) throws Exception{
		if(result.hasErrors()) {
			throw new GlobalException(HttpStatus.BAD_REQUEST,ErrorCode.PARAM_ERROR,"參數錯誤");
		}
		else {
			String token = request.getToken();
			String articleContent = request.getArticle_content();
			return userService.postArticle(token,articleContent);
		}
	}
	
	//更新文章內容
	@TimeOperation
	@VerifyToken
	@PostMapping("/updateArticle")
	public UnifiedResponse renewArticle(@Valid @RequestBody UpdateArticleRequest request,BindingResult result) throws Exception {

		if(result.hasErrors()) {
			throw new GlobalException(HttpStatus.BAD_REQUEST,ErrorCode.PARAM_ERROR,"參數錯誤");
		}
		else {
			String token = request.getToken();
			Integer articleId = request.getId();
			String articleContent = request.getArticle_content();
			return userService.renewArticle(token, articleId, articleContent);
		}	
	}
	
	//刪除文章內容(以狀態表示)
	@TimeOperation
	@VerifyToken
	@PostMapping("/deleteArticle")
	public UnifiedResponse deleteArticle(@Valid @RequestBody DeleteArticleRequest request,BindingResult result) throws Exception{
		
		if(result.hasErrors()) {
			throw new GlobalException(HttpStatus.BAD_REQUEST,ErrorCode.PARAM_ERROR,"參數錯誤");
		}
		else {
			String token = request.getToken();
			Integer articleId = request.getId();
			return userService.deleteArticle(token,articleId);
		}
	}
	
	//查看所有文章列表
	@TimeOperation
	@VerifyToken
	@PostMapping("/queryArticleList")
	public Object queryArticleList(@Valid @RequestBody CheckArticleRequest request,BindingResult result) throws Exception{

		if(result.hasErrors()) {
			throw new GlobalException(HttpStatus.BAD_REQUEST,ErrorCode.PARAM_ERROR,"參數錯誤");
		}
		else {
			String token = request.getToken();
			return userService.queryAllArticle(token);
		}
		
	}
	
	//單一查詢文章(且包含文章中的留言)
	@TimeOperation
	@VerifyToken
	@PostMapping("/querySpecifiedArticle")
	public Object checkSpecifiedArticle(@Valid @RequestBody CheckArticlesRequest request,BindingResult result) throws Exception{

		if(result.hasErrors()) {
			throw new GlobalException(HttpStatus.BAD_REQUEST,ErrorCode.PARAM_ERROR,"參數錯誤");
		}
		else {
			String token = request.getToken();
			Integer articleId = request.getId();
			return userService.querySpecifiedArticle(token, articleId);
		}
	}
	
	//查詢使用者所有貼文
	@TimeOperation
	@VerifyToken
	@PostMapping("/queryUserArticle")
	public Object querySpecifiedUserArticle(@Valid @RequestBody CheckUserArticleRequest request,BindingResult result) throws Exception{
		
		if(result.hasErrors()) {
			throw new GlobalException(HttpStatus.BAD_REQUEST,ErrorCode.PARAM_ERROR,"參數錯誤");
		}
		else {
			String token = request.getToken();
			return userService.querySpecifiedUserArticle(token);
		}
	}
	
	//鎖定貼文(只有管理者有權限)
	@TimeOperation
	@VerifyToken
	@PostMapping("/lockSpecifiedArticle")
	public UnifiedResponse lockSpecifiedArticle(@Valid @RequestBody LockManagerRequest request,BindingResult result) throws Exception{

		if(result.hasErrors()) {
			throw new GlobalException(HttpStatus.BAD_REQUEST,ErrorCode.PARAM_ERROR,"參數錯誤");
		}
		else {
			String token = request.getToken();
			Integer articleId = request.getId();
			return userService.lockArticle(token, articleId);
		}	
	}
	
	//解鎖貼文(只有管理者有權限)
	@TimeOperation
	@VerifyToken
	@PostMapping("/unlockSpecifiedArticle")
	public UnifiedResponse unlockSpecifiedArticle(@Valid @RequestBody LockManagerRequest request,BindingResult result) throws Exception{
		if(result.hasErrors()) {
			throw new GlobalException(HttpStatus.BAD_REQUEST,ErrorCode.PARAM_ERROR,"參數錯誤");
		}
		else {
			String token = request.getToken();
			Integer articleId = request.getId();
			return userService.unlockArticle(token, articleId);
		}
	}
/*留言功能*/
	
	//對文章進行留言
	@TimeOperation
	@VerifyToken
	@PostMapping("/postMessage")
	public UnifiedResponse postMessage(@Valid @RequestBody MessageRequest request,BindingResult result) throws Exception{			
		if(result.hasErrors()) {
			throw new GlobalException(HttpStatus.BAD_REQUEST,ErrorCode.PARAM_ERROR,"參數錯誤");
		}
		else {
			String messageContent = request.getMessage_content();
			Integer articleId = request.getArticle_id();
			String token = request.getToken();
			return userService.postMessage(token, articleId, messageContent);
		}
	}
	
	//對留言進行更改
	@TimeOperation
	@VerifyToken
	@PostMapping("/updateMessage")
	public UnifiedResponse renewMessage(@Valid @RequestBody UpdateMessageRequest request,BindingResult result) throws Exception{

		if(result.hasErrors()) {
			throw new GlobalException(HttpStatus.BAD_REQUEST,ErrorCode.PARAM_ERROR,"參數錯誤");
		}
		else {
			//更新此留言的帳號
			String token = request.getToken();
			//更新留言的id
			Integer messageId = request.getId();
			String messageContent = request.getMessage_content();
			return userService.renewMessage(token, messageId, messageContent);
		}
	}
	
	//對留言進行刪除
	@TimeOperation
	@VerifyToken
	@PostMapping("/deleteMessage")
	public UnifiedResponse deleteMessage(@Valid @RequestBody DeleteMessageRequest request,BindingResult result) throws Exception{
		
		if(result.hasErrors()) {
			throw new GlobalException(HttpStatus.BAD_REQUEST,ErrorCode.PARAM_ERROR,"參數錯誤");
		}
		else {
			String token = request.getToken();
			Integer messageId = request.getId();
			return userService.deleteMessage(token, messageId);
		}
	}
	
	//鎖定留言(只有管理者有權限)
	@TimeOperation
	@VerifyToken
	@PostMapping("/lockSpecifiedMessage")
	public UnifiedResponse lockSpecifiedMessage(@Valid @RequestBody LockManagerRequest request,BindingResult result) throws Exception{

		if(result.hasErrors()) {
			throw new GlobalException(HttpStatus.BAD_REQUEST,ErrorCode.PARAM_ERROR,"參數錯誤");
		}
		else {
			String token = request.getToken();
			Integer messageId = request.getId();
			return userService.lockMessage(token, messageId);
		}	
	}
	
	//解鎖留言(只有管理者有權限)
	@TimeOperation
	@VerifyToken
	@PostMapping("/unlockSpecifiedMessage")
	public UnifiedResponse unlockSpecifiedMessage(@Valid @RequestBody LockManagerRequest request,BindingResult result) throws Exception{

		if(result.hasErrors()) {
			throw new GlobalException(HttpStatus.BAD_REQUEST,ErrorCode.PARAM_ERROR,"參數錯誤");
		}
		else {
			String token = request.getToken();
			Integer messageId = request.getId();
			return userService.unlockMessage(token, messageId);
		}	
	}
	
}
