package com.example.demo;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo. cryptography.AESUtil;

@SpringBootTest
public class TestEncryptDecrypt {
	
	@Test
	void givenString_whenEncrypt_thenSuccess()
	    throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
	    BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeySpecException { 
		String plainText = "www.baeldung.com";

        String password = "baeldung";

        String salt = "12345678";

        SecretKey key = AESUtil.getKeyFromPassword(password, salt);

        String cipherText = AESUtil.encrypt(plainText, key);

       

        System.out.println("cipherText = "+ cipherText);


        SecretKey key2 = AESUtil.getKeyFromPassword(password, salt);

       

        String decryptedCipherText = AESUtil.decrypt(cipherText, key2);

       

        System.out.println("decryptedCipherText = " + decryptedCipherText);
	}
}
