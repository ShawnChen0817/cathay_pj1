package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
	
	public Message findMessageById(int id);
	
	public List<Message> findAll();
	
	public List<Message> findMessagesById(int id);
	/*message因文章被刪除，故也刪除*/
	@Transactional()
	@Modifying()
	@Query(value="UPDATE messages SET status=1 WHERE article_id=:article_id",nativeQuery=true)
	public List<Message> deleteMessageByArticleId(@Param("article_id") Integer article_id);
	
	/*normal過濾留言*/
	@Query(value="SELECT * FROM messages msg WHERE msg.article_id =:article_id and msg.status=0" ,nativeQuery=true)
	public List<Message> findMessgesByNormal(@Param("article_id") Integer article_id);
	
	/*message上鎖(因為article)*/
	@Transactional()
	@Modifying()
	@Query(value="UPDATE messages SET status=2 WHERE article_id=:articleId and (status=2 or status=0)",nativeQuery = true)
	public void lockMessageByArticleId(@Param("articleId") int articleId);
	
	/*message解鎖(因為article)*/
	@Transactional()
	@Modifying()
	@Query(value="UPDATE messages SET status=0 WHERE article_id=:articleId and (status=2 or status=0)",nativeQuery = true)
	public void unlockMessageByArticleId(@Param("articleId") int articleId);
	
	/*message上鎖*/
	@Transactional()
	@Modifying()
	@Query(value="UPDATE messages SET status=2 WHERE id=:messageId and (status=2 or status=0)",nativeQuery = true)
	public void lockMessageByMessageId(@Param("messageId") int messageId);
	
	/*message解鎖*/
	@Transactional()
	@Modifying()
	@Query(value="UPDATE messages SET status=0 WHERE id=:messageId and (status=2 or status=0)",nativeQuery = true)
	public void unlockMessageByMessageId(@Param("messageId") int messageId);
}	
