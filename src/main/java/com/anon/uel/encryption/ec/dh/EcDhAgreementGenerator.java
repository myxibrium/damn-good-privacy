package com.anon.uel.encryption.ec.dh;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;

import com.anon.uel.encryption.Encryptor;

public class EcDhAgreementGenerator{
	
	public byte[] generateSharedSecret(byte[] myPrivateKey, byte[] theirPublicKey) {
		try {
			KeyFactory fact = KeyFactory.getInstance(EcKeyConstants.DH_ALG_NAME, EcKeyConstants.PROVIDER);
			PrivateKey privateKey = fact.generatePrivate(new PKCS8EncodedKeySpec(myPrivateKey));
			ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(EcKeyConstants.CURVE_NAME);
			
			KeyAgreement ecdh = KeyAgreement.getInstance(EcKeyConstants.DH_ALG_NAME, EcKeyConstants.PROVIDER);
			ecdh.init(privateKey, ecSpec);

			PublicKey publicKey = fact.generatePublic(new X509EncodedKeySpec(theirPublicKey));
			ecdh.doPhase(publicKey, true);
			
			return ecdh.generateSecret();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

}
