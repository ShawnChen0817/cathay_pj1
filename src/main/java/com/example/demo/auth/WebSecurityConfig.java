//package com.example.demo.auth;
//
//import com.example.demo.auth.AuthorizationCheckFilter;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
//    
//	@Override
//	protected void configure(HttpSecurity http) throws Exception{
//	    http
//
//        	.authorizeRequests()
//        	.antMatchers(HttpMethod.POST, "/login").permitAll()
//            .antMatchers(HttpMethod.POST, "/logout").permitAll()
//            .anyRequest().authenticated()
//            .and()
//            .sessionManagement()
//			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            .and()
//            .addFilterBefore(new AuthorizationCheckFilter(), BasicAuthenticationFilter.class)
//            .csrf().disable();
//
////           .and()
////        .httpBasic();
//		/*http
//			.authorizeRequests()
//			.antMatchers(HttpMethod.POST,"/login").permitAll()
//			.antMatchers(HttpMethod.GET).permitAll()
//			.and()
//			.sessionManagement()
//			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//			.and()
//			.addFilterBefore(new AuthorizationCheckFilter(), BasicAuthenticationFilter.class)
//			.csrf().disable();*/
////		http.csrf().disable(); //取消csrf使用
////		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);   //無狀態的session政策，不使用Http Session
////		http.addFilterBefore(new AuthorizationCheckFilter(), BasicAuthenticationFilter.class);//自定義攔截器
//	}
//}
