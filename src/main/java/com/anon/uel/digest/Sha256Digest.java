package com.anon.uel.digest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Sha256Digest {
	
	public byte[] digest(byte[] content) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(content);
			return md.digest();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
