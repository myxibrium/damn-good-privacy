package com.anon.uel.encoding;

import org.bouncycastle.util.encoders.Base64;

public class EncodingUtils {
	
	public static String encodeBase64(byte[] bytes) {
		return new String(Base64.encode(bytes));
	}

}
