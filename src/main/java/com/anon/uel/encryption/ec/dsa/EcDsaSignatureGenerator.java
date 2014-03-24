package com.anon.uel.encryption.ec.dsa;

import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.anon.uel.encryption.ec.dh.EcKeyConstants;

public class EcDsaSignatureGenerator {
	
	public byte[] sign(byte[] message, byte[] privateKey) {
		try {
			Signature signature = Signature.getInstance(EcKeyConstants.DSA_ALG_NAME);
			
			KeyFactory fact = KeyFactory.getInstance(EcKeyConstants.DSA_ALG_NAME, EcKeyConstants.PROVIDER);

			signature.initSign(fact.generatePrivate(new PKCS8EncodedKeySpec(privateKey)));
			signature.update(message);
			
			return signature.sign();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean verify(byte[] message, byte[] publicKey, byte[] sigBytes) {
		try {
			Signature signature = Signature.getInstance(EcKeyConstants.DSA_ALG_NAME);
			KeyFactory fact = KeyFactory.getInstance(EcKeyConstants.DSA_ALG_NAME, EcKeyConstants.PROVIDER);
			signature.initVerify(fact.generatePublic(new X509EncodedKeySpec(publicKey)));
			signature.update(message);
			return signature.verify(sigBytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
