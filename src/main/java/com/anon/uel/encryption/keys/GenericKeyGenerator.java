package com.anon.uel.encryption.keys;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class GenericKeyGenerator {

	public byte[] generateKey(int bits) {
		SecureRandom random;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
			byte[] keyBytes = new byte[bits / 8];
			random.nextBytes(keyBytes);
			return keyBytes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
