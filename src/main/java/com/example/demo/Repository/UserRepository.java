package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	/*從資料庫取得使用者資料*/
	
	public List<User> findUsersByAccountAndPassword(String account, String password);	
	//依照account和password在資料庫尋找其資料
	public User findByAccountAndPassword(String account, String password);
	
	//依照account在資料庫中尋找資料，並以列表儲存
	public List<User> findByAccount(String account);
	
	//依照account於資料庫中找出此筆資料
	public User findUserByAccount(String account);
	
	/*不從資料庫取得使用者資料*/
	
	/*判斷資料是否存在*/
	
	//利用帳號判斷此帳號是否於資料庫(註冊時判斷)
	@Query(value= "SELECT EXISTS(SELECT 1 FROM user_info ui WHERE ui.account=:account) ",nativeQuery=true)
	public boolean userIfExistsByAccount(@Param("account") String account);

	//利用帳密判斷使用者資料有無於資料庫(登入時判斷)
	@Query(value= "SELECT EXISTS(SELECT 1 FROM user_info ui WHERE ui.account=:account and ui.password=:password) ",nativeQuery=true)
	public boolean userIfExistsByAccountAndPassword(@Param("account") String account, @Param("password") String password);
	
	//是否有帳號符合但密碼不符合的資料(登入時判斷)
	@Query(value= "SELECT EXISTS(SELECT 1 FROM user_info ui WHERE ui.account=:account and ui.password!=:password) ",nativeQuery=true)
	public boolean userAccountMatchButPasswordError(@Param("account") String account, @Param("password") String password);
	
	/*計算資料在資料庫數量*/
	
	//依照帳號去尋找符合輸入的資料有幾個
	public Long countByAccount(String account);
		
	//依照帳密去尋找符合輸入的資料有幾個
	public Long countByAccountAndPassword(String account,String password);
	
	//查詢符合此帳號但密碼(加密)不符合有幾個
	@Query(value="SELECT COUNT(ui) FROM user_info ui WHERE ui.account=:account and ui.password!=:password", nativeQuery = true)
	public long comparePasswordByAccountAndPassword(@Param("account") String account,@Param("password") String password);
}



