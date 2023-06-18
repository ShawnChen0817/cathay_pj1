package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer>{
	
	//抓取所有在資料庫的文章
	public List<Article> findAll();
	
	public List<Article> findArticlesById(int id);
	
	public Article findArticleById(int id);
	
	/*計算幾筆資料*/
	
	/*依照帳號種類過濾文章(NORMAL)*/
	
	//全部查詢
	@Query(value="SELECT * FROM articles ac WHERE ac.status=0",nativeQuery = true)
	public List<Article> findArticleListByNormal();
	
	//單一查詢(抓取狀態為0的文章)
	@Query(value="SELECT * FROM articles ac WHERE ac.status=0 and ac.id=:articleId",nativeQuery = true)
	public Article findArticleByNormal(@Param("articleId") Integer articleId);
	
	//查詢使用者發布文章
	@Query(value="SELECT * FROM articles ac WHERE ac.status=0 and ac.create_user_id =:create_user_id",nativeQuery = true)
	public List<Article> findArticleListByNormalAccount(@Param("create_user_id") Integer create_user_id);
	
	/*依照帳號種類過濾文章(ADMIN)*/
	
	//查詢使用者發布文章
	@Query(value="SELECT * FROM articles ac WHERE ac.create_user_id =:create_user_id",nativeQuery = true)
	public List<Article> findArticleListByAdminAccount(@Param("create_user_id") Integer create_user_id);
	
	/*文章上鎖、解鎖*/
	
	//判斷此文章狀態為1是否存在
	@Query(value="SELECT EXISTS(SELECT * FROM articles ac WHERE ac.id=:articleId and ac.status=1)",nativeQuery = true)
	public boolean articleStatus1IfExists(@Param("articleId") int articleId);
	
	//上鎖
	@Transactional()
	@Modifying()
	@Query(value="UPDATE articles SET status=2 WHERE id=:articleId and (status=2 or status=0)",nativeQuery = true)
	public void lockArticle(@Param("articleId") int articleId);
	
	//解鎖
	@Transactional()
	@Modifying()
	@Query(value="UPDATE articles SET status=0 WHERE id=:articleId and (status=2 or status=0)",nativeQuery = true)
	public void unlockArticle(@Param("articleId") int articleId);
	
}
