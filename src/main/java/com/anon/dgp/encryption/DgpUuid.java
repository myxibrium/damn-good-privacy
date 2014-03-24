package com.anon.dgp.encryption;

import org.bouncycastle.util.encoders.Base64;

import com.anon.uel.encryption.keys.GenericKeyGenerator;

public class DgpUuid {
	
	public static String generateUuid() {
		byte[] halfUuidBytes = new GenericKeyGenerator().generateKey(120);
		return new String(Base64.encode(halfUuidBytes));
	}

}
