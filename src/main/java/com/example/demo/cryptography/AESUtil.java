package com.example.demo.cryptography;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class AESUtil {

	//在AES算法，需要3個參數: 輸入數據(這裡指密碼)、密鑰、IV(初始向量)
	
	
	/*產生密鑰有兩種方法*/
	
	//1.密碼應該從加密安全隨機數生成器
	public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }
	
	//2.給定基於設置明碼的派生函數從給定的密碼派生AES密鑰(還需要一個salt value)
	public static SecretKey getKeyFromPassword(String password, String salt)
	        throws NoSuchAlgorithmException, InvalidKeySpecException {
		
			//設定要用哪種密鑰的演算法
	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	        //定義生成AES密鑰方法,迭代次數65536、長度為256位
	        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
	        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
	            .getEncoded(), "AES");
	        return secret;
	    }
	
//	/*初始化向量*/
//	public static IvParameterSpec generateIv() {
//		//使用設定好的 iv 生成 ivSpec
//        byte[] iv = new byte[16];
//        new SecureRandom().nextBytes(iv);
//        return new IvParameterSpec(iv);
//        
//        /*將iv設為固定值*/
//        //byte[] iv = {0,1,0,2,0,3,0,4,0,5,0,6,0,7,0,8};
//        //IvParameterSpec ivsepc = new IvParameterSpec(iv);
//        //return ivspec;
//    }
	
	/*字串加密*/
	public static String encrypt(String input, SecretKey key)
	        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
	        InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
			//根據演算法創建cipher實例
	        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
	        //利用加密模式、密鑰 建立密碼實例
	        cipher.init(Cipher.ENCRYPT_MODE, key);
	        //利用dofinal()來加密輸入字符串
	        byte[] cipherText = cipher.doFinal(input.getBytes());
	        return Base64.getEncoder()
	            .encodeToString(cipherText);
	    }
	
	/*字串解密*/
	public static String decrypt(String cipherText, SecretKey key)
	        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
	        InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
	        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
	        cipher.init(Cipher.DECRYPT_MODE, key);
	        //解密
	        byte[] plainText = cipher.doFinal(Base64.getDecoder()
	            .decode(cipherText));
	        
	        System.out.println(new String(plainText));
	        return new String(plainText);
	    }
	
	/*public static String decrpyted(String cipherText) {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();

	       SimpleStringPBEConfig config = new SimpleStringPBEConfig();

	       config.setPassword("abcdefghijklmnopqrstuvwxyz");

	       config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");

	       config.setKeyObtentionIterations(1000);

	       config.setProviderName("SunJCE");

	       config.setStringOutputType("base64");

	       config.setPoolSize(1);

	       config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");

	       config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");

	       encryptor.setConfig(config);

	       // 請放入與 server 1 約定好的密碼
	       //String encryptedText = encryptor.encrypt("CUBC_KHDProd_P@ssw0rd");
	       // properties 檔設定內容驗證
	       String decryptedTest = encryptor.decrypt("TH6HxNm7r13Hm&#43;XECQeDPSw9KAT47K8DXxlJPHD2NKUUS3jzDO7HSB&#43;beGhjT3UUq&#43;00/TKdu9O3Ffnll96jmw==");
		return decryptedTest;
	}*/
}
