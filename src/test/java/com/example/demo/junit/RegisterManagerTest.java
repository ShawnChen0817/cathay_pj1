package com.example.demo.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.entity.request.UserRequest;
import com.example.demo.exception.GlobalException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(Parameterized.class)
@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
public class RegisterManagerTest {
	
	private String account;
	
	private String password;
	
	private String status;
	
	@Autowired
	private MockMvc mockMvc;
	
	private TestContextManager testContextManager; 
	
	@Before
    public void setUp() throws Exception {
        testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);
    }
	
	public RegisterManagerTest(String account,String password,String status) {
		this.account = account;
		this.password = password;
		this.status = status;
	}
	
	@Parameters(name = "{index} : account= {0} , password= {1}")
	public static Iterable<Object[]> parameters(){
		return Arrays.asList(new Object[][]{
			{"","Aqz104311234!","E000"},
			{"aqz10431123","Aqz104311234!","E002"}
		});
	} 
	
	@Test
	public void RegisterTest() throws Exception {
		String response = mockMvc.perform(MockMvcRequestBuilders.post("/register")
				.content(asJsonString(new UserRequest(account,password)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(exception -> assertTrue(exception.getResolvedException() instanceof GlobalException))
			.andReturn().getResponse().getContentAsString();
        assertEquals(status,getStatus(response));
	} 
	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static String getStatus(String jsonString) throws JSONException {
		JSONObject j = new JSONObject(jsonString);
		String status = j.getString("status");
		return status;
	}
}
