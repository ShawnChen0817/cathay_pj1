package com.example.demo.auth;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.Repository.UserRepository;
import com.example.demo.entity.User;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	@Autowired
	public UserRepository userRepo;
	
	public static Key generateKey(String tokenSecret) {
		byte[] encodeKey = Decoders.BASE64.decode(tokenSecret);
		Key key = Keys.hmacShaKeyFor(encodeKey);
		return key;
	}
	//建立token
	public String getToken(User user,Long ttlMillis,long expireTime,Key secretKey) {
		if(ttlMillis == null) {
			ttlMillis=expireTime;
		}	
		String token = JWT.create()
				.withSubject(String.valueOf(user.getAccount_type()))
				.withIssuer(user.getAccount())
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis()+expireTime))
				.sign(Algorithm.HMAC256(String.valueOf(secretKey)));
		return token;
	}
	
	//解析JWT
	public DecodedJWT parseJWT(String token,Key secretkey) throws Exception{
		JWTVerifier jwtVerifier = JWT
								.require(Algorithm.HMAC256(String.valueOf(secretkey)))
								.build();
		return jwtVerifier.verify(token);
	}
//	//建立JWT
//	public String createJWT(User user,Long ttlMillis,long expireTime,Key secretkey) {;
//		long nowMills = System.currentTimeMillis();
//		Date now = new Date(nowMills);
//		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//		if(ttlMillis == null) {
//			ttlMillis=expireTime;
//		}
//		
//		//過期時間點 = 目前 + 過期時間
//		long expMills = nowMills + ttlMillis;
//		Date expDate = new Date(expMills);
//		
//		JwtBuilder builder = Jwts.builder()
//				.setSubject(String.valueOf(user.getId()))
//				.setIssuer(user.getAccount())
//				.setIssuedAt(now)
//				.claim("role", user.getAccount_type())
//				.signWith(secretkey,signatureAlgorithm)
//				.setExpiration(expDate);
//				
//		return builder.compact();//最後使用compact()生成
//	}
//	
//	public Claims decodeJWT(String token,Key secretkey) throws Exception{
//		Claims claims =  Jwts
//				.parserBuilder()
//				.setSigningKey(secretkey)
//				.build()
//				.parseClaimsJws(token).getBody();
//		return claims;
//	}
}
