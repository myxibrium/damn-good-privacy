package com.anon.uel.encryption.serpent;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.anon.uel.encryption.Encryptor;
import com.anon.uel.encryption.keys.GenericKeyGenerator;

public class SerpentEncryptor implements Encryptor{

	private static final int IV_SIZE_BITS = 128;

	@Override
	public byte[] encrypt(byte[] plainText, byte[] privateKey) {
		byte[] iv = new GenericKeyGenerator().generateKey(IV_SIZE_BITS);
		Key key = new SecretKeySpec(privateKey, "SERPENT");
		try {
			Cipher cipher = Cipher.getInstance("Serpent/CBC/PKCS7Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
			return combine(iv, cipher.doFinal(plainText));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public byte[] decrypt(byte[] cipherText, byte[] privateKey) {
		byte[] iv = Arrays.copyOfRange(cipherText, 0, IV_SIZE_BITS/8);
		byte[] body = Arrays.copyOfRange(cipherText, IV_SIZE_BITS/8, cipherText.length);
		Key key = new SecretKeySpec(privateKey, "SERPENT");
		try {
			Cipher cipher = Cipher.getInstance("Serpent/CBC/PKCS7Padding", "BC");
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
			return cipher.doFinal(body);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private byte[] combine(byte[] first, byte[] second) {
		byte[] result = Arrays.copyOf(first, first.length+second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

}
