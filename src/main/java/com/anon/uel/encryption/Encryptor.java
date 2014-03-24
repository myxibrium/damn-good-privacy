package com.anon.uel.encryption;

import java.io.InputStream;

public interface Encryptor {
	
	public byte[] encrypt(byte[] plainText, byte[] privateKey);
	
	public byte[] decrypt(byte[] cipherText, byte[] privateKey);

}
