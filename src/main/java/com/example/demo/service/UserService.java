package com.example.demo.service;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.example.demo.Repository.ArticleRepository;
import com.example.demo.Repository.MessageRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.auth.JwtUtil;
import com.example.demo. cryptography.AESUtil;
import com.example.demo.entity.Article;
import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.entity.response.LoginManagerResponse;
import com.example.demo.entity.response.UnifiedArticleListResponse;
import com.example.demo.entity.response.UnifiedArticleResponse;
import com.example.demo.entity.response.UnifiedResponse;
import com.example.demo.exception.GlobalException;
import com.example.demo.exceptionHandler.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.Key;

@Service
public class UserService {

@Autowired
UserRepository userRepository;

@Autowired
ArticleRepository articleRepository;

@Autowired
JwtUtil jwtUtil;

@Autowired
ObjectMapper mapper;

@Autowired
MessageRepository messagesRepository;

@Value("${spring.data.specifiedPassword}")
String specifiedPassword;

@Value("${spring.data.saltValue}")
String saltValue;

@Value("${spring.data.expireTime}")
Long expireTime;

@Value("${spring.data.tokenSecret}")
String tokenSecret;

/*判斷使用者 OR 文章 OR 留言*/

//依據account找出此username的資料(單一帳號)
public User findUserByAccount(String account) {
	return userRepository.findUserByAccount(account);
}

//列出為此帳號的表
public List<User> findUsersByAccount(String account){
	return userRepository.findByAccount(account);
}


/*回應*/

//一般管理成功
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

/*文章*/

//檢查文章狀態
public boolean articleExists(int articleId) {
	if(!(articleRepository.findArticlesById(articleId).isEmpty())) {
		Article article = articleRepository.findArticleById(articleId);
		int status = article.getStatus();
		if(status==0) {
			return true;
		}
		//代表文章為已刪除
		else if(status==1) {
			throw new GlobalException(HttpStatus.NOT_FOUND,ErrorCode.ARTICLE_ALREADY_DELETE,"文章已刪除");
		}
		//代表文章鎖定中
		else {
			throw new GlobalException(HttpStatus.UNAUTHORIZED,ErrorCode.ARTICLE_LOCK,"文章鎖定中");
		}
	}
	//代表文章為不存在db
	else {
		throw new GlobalException(HttpStatus.NOT_FOUND,ErrorCode.ARTICLE_NOTFOUND,"無此文章");
	}
}

//檢查文章狀態(admin)
public boolean articleStatus(int articleId) {
	if(!(articleRepository.findArticlesById(articleId).isEmpty())) {
		if(!articleRepository.articleStatus1IfExists(articleId)) {
			Article article = articleRepository.findArticleById(articleId);
			int status = article.getStatus();
			if(status==0 || status==2) {
				return true;
			}
			else {
				throw new GlobalException(HttpStatus.NOT_FOUND,ErrorCode.ARTICLE_ALREADY_DELETE,"文章已刪除");
			}
		}
		else {
			throw new GlobalException(HttpStatus.NOT_FOUND,ErrorCode.ARTICLE_ALREADY_DELETE,"文章已刪除");
		}
	}
	//代表文章為不存在db
	else {
		throw new GlobalException(HttpStatus.NOT_FOUND,ErrorCode.ARTICLE_NOTFOUND,"無此文章");
	}
}

//單一查詢
//抓取任何狀態的文章
public Article getArticle(int role,int articleId) {
	if(!(articleRepository.findArticlesById(articleId).isEmpty())) {
		Article article = new Article();
		article = articleRepository.findArticleById(articleId);
		int status = article.getStatus();
		//normal account
		if(role==0){
			if(status==0) {
				article = articleRepository.findArticleByNormal(articleId);
				article.setMessage_list(messagesRepository.findMessgesByNormal(articleId));
			}
			//代表文章為已刪除
			else if(status==1) {
				throw new GlobalException(HttpStatus.NOT_FOUND,ErrorCode.ARTICLE_ALREADY_DELETE,"文章已刪除");
			}
			//代表文章鎖定中
			else {
				throw new GlobalException(HttpStatus.UNAUTHORIZED,ErrorCode.ARTICLE_LOCK,"文章鎖定中");
			}
		}
		//若為admin直接輸出aritlce
		return article;
	}
	//代表文章為不存在db
	else {
		throw new GlobalException(HttpStatus.NOT_FOUND,ErrorCode.ARTICLE_NOTFOUND,"無此文章");
	}
}

//查詢所有文章
public Object getArticleList(String account,int role){
	List<Article> articleList = new ArrayList<>();
	List<Message> messageList = new ArrayList<>();
	if(role==0) {
		articleList = articleRepository.findArticleListByNormal();
		for(Article ac : articleList) {
			ac.setMessage_list(messageList);
		}
		if(articleList.isEmpty()) {
			return UnifiedManageResponse("0000","query success",0,"無文章顯示");
		}
	}
	else {
		articleList = articleRepository.findAll();
		for(Article ac : articleList) {
			ac.setMessage_list(messageList);
		}
		if(articleList.isEmpty()) {
			return UnifiedManageResponse("0000","query success",0,"無文章顯示");
		}
	}
	
	return ArticleListResponse("0000","query success",articleList.size(),articleList);
}

//取得使用者發布文章
public Object getArticleListByAccount(String account,int role){
	int userId = userRepository.findUserByAccount(account).getId();
	List<Article> articleList = new ArrayList<>();
	if(role==0) {
		articleList = articleRepository.findArticleListByNormalAccount(userId);
		if(articleList.isEmpty()) {
			return UnifiedManageResponse("0000","query success",0,"無文章顯示");
		}
		else {
			for(Article ac : articleList) {
				List<Message> messageList = messagesRepository.findMessgesByNormal(ac.getId());
				ac.setMessage_list(messageList);
			}
		}
	}
	else {
		articleList = articleRepository.findArticleListByAdminAccount(userId);
		if(articleList.isEmpty()) {
			return UnifiedManageResponse("0000","query success",0,"無文章顯示");
		}
	}
	return ArticleListResponse("0000","query success",articleList.size(),articleList);
}

/*留言*/

//檢查留言狀態(與文章狀態同步)
public boolean messageExists(int messageId) {
	if(!(messagesRepository.findMessagesById(messageId).isEmpty())) {
		Message message = messagesRepository.findMessageById(messageId);
		int messageStatus = message.getStatus();
		
		//代表留言存在
		if(messageStatus==0) {
			return true;
		}
		else if(messageStatus==2) {
			Article article = message.getArticle();
			if(article.getStatus()==0) {
				throw new GlobalException(HttpStatus.UNAUTHORIZED,ErrorCode.MESSAGE_LOCK,"留言鎖定中");
			}
			else if(article.getStatus()==1){
				throw new GlobalException(HttpStatus.NOT_FOUND,ErrorCode.ARTICLE_ALREADY_DELETE,"文章已刪除");
			}
			else {
				throw new GlobalException(HttpStatus.UNAUTHORIZED,ErrorCode.ARTICLE_LOCK,"文章鎖定中");
			}		
		}
		//留言不存在 status=1
		else {
			throw new GlobalException(HttpStatus.NOT_FOUND,ErrorCode.MESSAGE_ALREADY_DELETE,"留言已刪除");
		}
	}
	else {
		throw new GlobalException(HttpStatus.NOT_FOUND,ErrorCode.MESSAGE_NOTFOUND,"無此留言");
	}	
}

//檢查留言狀態(留言上鎖用)
public String messageStatus(int messageId) {
	if(!(messagesRepository.findMessagesById(messageId).isEmpty())) {
		Message message = messagesRepository.findMessageById(messageId);
		int messageStatus = message.getStatus();
		
		//代表留言存在
		if(messageStatus==0) {
			return "存在";
		}
		else if(messageStatus==2) {
			Article article = message.getArticle();
			if(article.getStatus()==0) {
				return "E202";
			}	
			else if(article.getStatus()==2){
				throw new GlobalException(HttpStatus.UNAUTHORIZED,ErrorCode.ARTICLE_LOCK,"文章鎖定中");
			}
			else {
				throw new GlobalException(HttpStatus.NOT_FOUND,ErrorCode.ARTICLE_ALREADY_DELETE,"文章已刪除");
			}
		}
		//留言不存在 status=1
		else {
			throw new GlobalException(HttpStatus.NOT_FOUND,ErrorCode.MESSAGE_ALREADY_DELETE,"留言已刪除");
		}
	}
	else {
		throw new GlobalException(HttpStatus.NOT_FOUND,ErrorCode.MESSAGE_NOTFOUND,"無此留言");
	}	
}

/*使用者管理*/

//檢查建立者與使用者帳號
public boolean compare(User user, String account) {
	if(user.getAccount().equals(account)) {
		return true;
	}
	else {
		throw new GlobalException(HttpStatus.UNAUTHORIZED,ErrorCode.PERMISSION_ERROR,"權限不足");
	}
}

//取的帳號種類(若需取得claims中指定的值)
//public Integer accountType(String token) throws JsonProcessingException, JSONException {
//	//以.進行拆解成字串群組
//	String[] split_string = token.split("\\.");
//	//第二個字串組為body
//	String base64EncodedBody = split_string[1];
//	Base64 base64Url = new Base64(true);
//	//運用base64進行解碼
//	String body = new String(base64Url.decode(base64EncodedBody));
//	//將body轉換成json物件
//	JSONObject j = new JSONObject(body);
//	//取出key為role的value
//	int role = (Integer) j.get("role");
//	return role;
//}

/*登入、文章、留言管理*/

//註冊(將使用者資料儲存至資料庫(密碼進行加密))
//0:此帳號已存在 1:註冊成功 
public UnifiedResponse register(String account, String password) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

//	雜湊加密法
//	MD5Util process1 = new MD5Util();
//	String cipher = process1.md5(password);
	
	//AES加密
	AESUtil process = new AESUtil();
	if(!userRepository.userIfExistsByAccount(account)) {
		User user = new User();
		user.setAccount(account);
		
		//判斷正確後再進行加密的動作
		SecretKey key = process.getKeyFromPassword(specifiedPassword,saltValue);
		String cipher = process.encrypt(password, key);
		user.setPassword(cipher);
		if("admin".equals(account)) {
			user.setAccount_type(1);
		}	
		userRepository.save(user);
		return UnifiedManageResponse("0000","register success",0,null);
	}
	else {
		throw new GlobalException(HttpStatus.UNAUTHORIZED,ErrorCode.ACCOUNT_REGISTERD,"帳號已註冊");
	}
}

//登入(以username與password尋找資料庫中的此筆資料)
//抓取資料庫此使用者的密碼進行解密
//0:無此帳號 1:帳號登入成功 2:帳號已登入 3:密碼錯誤
public UnifiedResponse login(String account, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException{

	AESUtil process = new AESUtil();
	
	//加密
	SecretKey key = process.getKeyFromPassword(specifiedPassword,saltValue);
	String cipher = process.encrypt(password,key);
	LoginManagerResponse response = new LoginManagerResponse();
	
	//有此使用者資料
	if(userRepository.userIfExistsByAccountAndPassword(account, cipher)) {
		
		//token可以重複取得(故不須製作此帳號已登入限制)
		User user = userRepository.findByAccountAndPassword(account,cipher);
		Key secretKey = jwtUtil.generateKey(tokenSecret);
		String token = jwtUtil.getToken(user, null,expireTime,secretKey);
		return UnifiedManageResponse("0000","login success",1,token);
	}
	
	//無此使用者資料
	else {
		
		//在帳號符合時，密碼錯誤的情況有幾個
		if(userRepository.userAccountMatchButPasswordError(account, cipher)) {
			throw new GlobalException(HttpStatus.UNAUTHORIZED,ErrorCode.LOGIN_ERROR,"帳號或密碼錯誤");
		}
		else {
			throw new GlobalException(HttpStatus.NOT_FOUND,ErrorCode.ACCOUNT_NOTFOUND,"無此帳號");
		}	
	}
}

/*文章*/

//文章發布(成功登入後，進行發文，將其內容存至資料庫)
//0:無此帳號 1:發布成功 2:未登入
//情境: 驗證成功的token會被視為已登入 ， 若無則「E302」
public UnifiedResponse postArticle(String token ,String articleContent) throws Exception{
	Article article = new Article();
	Date now = new Date();
	String account = JWT.decode(token).getIssuer();
	User user = userRepository.findUserByAccount(account);
	article.setArticle_content(articleContent);
	article.setCreate_user(user);
	article.setCreate_time(now);
	articleRepository.save(article);
	return UnifiedManageResponse("0000","post success",0,null);
}

//更改文章 0:無此帳號 1:更改成功 2.請先登入(真無權限) 3.更改失敗(比對資料後無權限) 4:文章已鎖定 5:無此文章   
public UnifiedResponse renewArticle(String token, Integer articleId, String articleContent) throws Exception {
	
	UnifiedResponse response = new UnifiedResponse();
	String account = JWT.decode(token).getIssuer();
	if(articleExists(articleId)) {
		Article article = articleRepository.findArticleById(articleId);
		User user = article.getCreate_user();	
		Date time = new Date();
		//若與建立者符合(管理者不可以隨意更改文章內容)
		if(compare(user,account)){
			article.setUpdate_user(account);
			article.setArticle_content(articleContent);
			article.setUpdate_time(time);
			articleRepository.save(article);
			response = UnifiedManageResponse("0000","update success",0,null);
		}
	}
	return response;
}

//刪除文章(以狀態表示) 0:無此文章 1:刪除成功 2:請先登入(真無權限) 3:刪除失敗(與使用者不符) 4:文章已鎖定 5:無此文章
public UnifiedResponse deleteArticle(String token, Integer articleId) throws Exception {
	UnifiedResponse response = new UnifiedResponse();
	String account = JWT.decode(token).getIssuer();
	int role = Integer.valueOf(JWT.decode(token).getSubject());
	if(articleExists(articleId)) {
		Article article = articleRepository.findArticleById(articleId);
		User user = article.getCreate_user();	
		Date time = new Date();
		//若是文章內容違反規定，管理者可以刪除文章
		if(compare(user,account) || role==1) {
			article.setUpdate_user(account);
			article.setUpdate_time(time);
			article.setStatus(1);			
			
			//若文章刪除，則下方留言一併刪除
			messagesRepository.deleteMessageByArticleId(articleId);
			response = UnifiedManageResponse("0000","delete success",0,null);
		}
	}
	return response;
}

//所有文章顯示 0.無文章顯示 1.所有文章 2.部分文章(存在狀態) 3.請先登入 
public Object queryAllArticle(String token) throws Exception{
	String account = JWT.decode(token).getIssuer();
	int role = Integer.valueOf(JWT.decode(token).getSubject());
	return getArticleList(account,role);
}

//單一查詢此文章(包含此文章留言) 
public Object querySpecifiedArticle(String token,Integer articleId) throws Exception{
	int role = Integer.valueOf(JWT.decode(token).getSubject());
	Article article = getArticle(role,articleId);
	return SpecifiedArticleResponse("0000","query success",1,article);
}

/*查詢使用者所發布的貼文*/
//0:無此帳號 1:查詢成功 2:未登入 3:無文章
public Object querySpecifiedUserArticle(String token) throws Exception {
	String account = JWT.decode(token).getIssuer();
	int role = Integer.valueOf(JWT.decode(token).getSubject());
	return getArticleListByAccount(account,role);
}

//鎖定文章內容
//0:無此帳號 1:鎖定成功 2:未登入 3:無權限 4:文章已鎖定 5:文章不存在
public UnifiedResponse lockArticle(String token,Integer articleId) throws Exception {
	UnifiedResponse response = new UnifiedResponse();
	int role = Integer.valueOf(JWT.decode(token).getSubject());
	if(role==1) {
		if(articleStatus(articleId)) {
			articleRepository.lockArticle(articleId);
			messagesRepository.lockMessageByArticleId(articleId);
			response = UnifiedManageResponse("0000","lock success",0,null);
		}
	}
	else {
		throw new GlobalException(HttpStatus.UNAUTHORIZED,ErrorCode.PERMISSION_ERROR,"權限不足");
	}	
	return response;
}

//解鎖文章內容
//0:無此帳號 1:解鎖成功 2:未登入 3:無權限 4:文章無鎖定 5:文章不存在
public UnifiedResponse unlockArticle(String token,Integer articleId) throws Exception {
	int role = Integer.valueOf(JWT.decode(token).getSubject());
	UnifiedResponse response = new UnifiedResponse();
	if(role==1) {
		if(articleStatus(articleId)) {
			articleRepository.unlockArticle(articleId);
			messagesRepository.unlockMessageByArticleId(articleId);
			response = UnifiedManageResponse("0000","unlock success",0,"解鎖成功");
		}
	}
	else {
		throw new GlobalException(HttpStatus.FORBIDDEN,ErrorCode.PERMISSION_ERROR,"權限不足");
	}	
	return response;
}

/*留言*/

//對文章進行留言 0:無此帳號 1:留言成功 2:請先登入(使用者無權限) 3:文章已鎖定 4:無此文章 
public UnifiedResponse postMessage(String token,Integer articleId, String messageContent) throws Exception {
	UnifiedResponse response = new UnifiedResponse();
	Message message = new Message();
	Date time = new Date();
	String account = JWT.decode(token).getIssuer();
	if(articleExists(articleId)) {
		Article article = articleRepository.findArticleById(articleId);
		message.setContent(messageContent);
		//設置article_id為文章的id 
		message.setArticle(article);
		//將create_user設為此帳號的id
		message.setCreate_user(userRepository.findUserByAccount(account));
		message.setCreate_time(time);
		messagesRepository.save(message);
		response = UnifiedManageResponse("0000","post success",0,null);
	}
	return response;
}

//比重: 1.使用者無權限(真) 2.無文章(真) 3.無文章(狀態) 4.無權限(輸入帳號有權限，但修改訊息不符和建立者帳號)
//對留言進行更改(只有自己或是最高權限帳號可更改) 0:無此帳號 1:留言更改成功 2:無更改權限(未登入) 3:與建立者不符 4:無此留言
public UnifiedResponse renewMessage(String token,Integer messageId,String messageContent) throws Exception {
	UnifiedResponse response = new UnifiedResponse();
	String account = JWT.decode(token).getIssuer();
	if(messageExists(messageId)) {
		Message message = messagesRepository.findMessageById(messageId);
		User user = message.getCreate_user();
		Date time = new Date();
		
		//管理者不可以隨意更改使用者留言內容
		if(compare(user,account)) {
			message.setUpdate_user(account);
			message.setContent(messageContent);
			message.setUpdate_time(time);
			messagesRepository.save(message);
			response = UnifiedManageResponse("0000","update success",0,null);
		}
	}
	return response;
}

//對留言進行刪除(不過是狀態之間的轉換) 0:無此訊息 1:留言刪除成功 2:無更改權限 3:與建立者不符
public UnifiedResponse deleteMessage(String token, Integer messageId) throws Exception {

	String account = JWT.decode(token).getIssuer();
	int role = Integer.valueOf(JWT.decode(token).getSubject());
	UnifiedResponse response = new UnifiedResponse();
	if(messageExists(messageId)) {
		Message message = messagesRepository.findMessageById(messageId);
		User user = message.getCreate_user();
		Date time = new Date();
		
		//若是留言違反規定，管理者可以刪除此留言
		if(compare(user,account) || role==1) {
			message.setUpdate_user(account);
			message.setUpdate_time(time);
			message.setStatus(1);
			messagesRepository.save(message);
			response = UnifiedManageResponse("0000","delete success",0,null);
		}
	}
	return response;
}

//admin進行留言上鎖
public UnifiedResponse lockMessage(String token,Integer messageId) throws Exception {
	UnifiedResponse response = new UnifiedResponse();
	int role = Integer.valueOf(JWT.decode(token).getSubject());
	if(role==1) {
		String messageStatus = messageStatus(messageId);
		if("存在".equals(messageStatus) || "E202".equals(messageStatus)) {
			
			//將留言變為上鎖狀態
			messagesRepository.lockMessageByMessageId(messageId);
			response = UnifiedManageResponse("0000","lock success",0,null);
		}
	}
	else {
		throw new GlobalException(HttpStatus.UNAUTHORIZED,ErrorCode.PERMISSION_ERROR,"權限不足");
	}			
	return response;
}

//admin進行留言解鎖
public UnifiedResponse unlockMessage(String token,Integer messageId) throws Exception {
	UnifiedResponse response = new UnifiedResponse();
	int role = Integer.valueOf(JWT.decode(token).getSubject());
	if(role==1) {
		String messageStatus = messageStatus(messageId);
		if("存在".equals(messageStatus) || "E202".equals(messageStatus)) {
			
			//將留言變為解鎖狀態
			messagesRepository.unlockMessageByMessageId(messageId);
			response = UnifiedManageResponse("0000","unlock success",0,null);
			
		}
	}
	else {
		throw new GlobalException(HttpStatus.UNAUTHORIZED,ErrorCode.PERMISSION_ERROR,"權限不足");
	}			
	return response;
}

}
