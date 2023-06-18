//package com.example.demo.auth;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.example.demo.service.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import io.jsonwebtoken.Claims;
//
//import static org.springframework.http.HttpHeaders.AUTHORIZATION;
//import static org.springframework.http.HttpStatus.FORBIDDEN;
//import static org.springframework.http.HttpStatus.UNAUTHORIZED;
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//public class AuthorizationCheckFilter extends OncePerRequestFilter{
//	@Autowired
//	UserService userService;
//	
//	@Override
//	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
//			throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		JwtUtil jwtUtil = new JwtUtil();
//		//若api非登入則進行攔截
//		if(!(req.getServletPath().equals("/login"))) {
//			String authHeader =  req.getHeader(AUTHORIZATION);
//			//System.out.println("1");
//           if(authHeader!=null) {
//        	   try {
//        		   String accessToken = authHeader.replace("Bearer ","");
//        		   Claims claims = jwtUtil.parseJWT(accessToken);
//        		   System.out.println("JWT payload: " + claims );
//        		   chain.doFilter(req, res);
//        	   }
//        	   catch(Exception e) {
//        		   System.err.print("Error: " + e);
//        		   res.setStatus(FORBIDDEN.value());
//        		   Map<String,String> err = new HashMap<>();
//        		   err.put("jwt_error", e.getMessage());
//        		   res.setContentType(APPLICATION_JSON_VALUE);
//        		   ObjectMapper mapper = new ObjectMapper();
//        		   mapper.writeValue(res.getOutputStream(),err);
//        	   }
//        	 
//           }
//           else{
//        	   res.setStatus(UNAUTHORIZED.value());
//           }
//		}else {
//			chain.doFilter(req, res);
//		}
//	}
////		 if(!req.getServletPath().equals("/login")){
////			 JwtUtil jwtUtil = new JwtUtil();
////             String authHeader =  req.getHeader(AUTHORIZATION);
////             
////             //以jjwt驗證token，只要驗證成功就放行
////             //驗證失敗會拋exception，直接將錯誤訊息傳回
////             if(authHeader!= null){
////                 try{
////                	 String accessToken = authHeader.replace("Bearer ","");
////                	 Claims claims = jwtUtil.parseJWT(accessToken);
////
////                	 System.out.println("JWT payload:"+claims.toString());
////
////                	 chain.doFilter(req, res);
////                 
////                 }catch(Exception e){
////                     System.err.println("Error : "+e);
////                     res.setStatus(FORBIDDEN.value());
////                     
////                     Map<String, String> err = new HashMap<>();
////                     err.put("jwt_err", e.getMessage());
////                     res.setContentType(APPLICATION_JSON_VALUE);
////                     new ObjectMapper().writeValue(res.getOutputStream(), err);
////                 }
////             }else{
////                 res.setStatus(UNAUTHORIZED.value());
////             }
////         }else{
////             chain.doFilter(req, res);
////         }
//// 
////}
//}
