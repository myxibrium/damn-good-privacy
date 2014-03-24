package com.anon.uel.encryption.ec;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;

import com.anon.uel.encryption.AsymmetricKeyGenerator;
import com.anon.uel.encryption.ec.dh.EcKeyConstants;

public class EllipticCurveAsymmetricKeyGenerator implements AsymmetricKeyGenerator {
	
	private String curveName;
	
	private String algName;
	
	private String provider;
	
	public static EllipticCurveAsymmetricKeyGenerator getInstance(String type) {
		if (type.equals("ECDH")) {
			return new EllipticCurveAsymmetricKeyGenerator(EcKeyConstants.CURVE_NAME, EcKeyConstants.DH_ALG_NAME, EcKeyConstants.PROVIDER);
		} else if (type.equals("ECDSA")) {
			return new EllipticCurveAsymmetricKeyGenerator(EcKeyConstants.CURVE_NAME, EcKeyConstants.DSA_ALG_NAME, EcKeyConstants.PROVIDER);
		} else {
			return null;
		}
	}
	
	public EllipticCurveAsymmetricKeyGenerator(String curveName, String algName, String provider) {
		this.curveName = curveName;
		this.algName = algName;
		this.provider = provider;
	}

	public KeyPair generateKeyPair() {
		try {
			ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(curveName);
			KeyPairGenerator g = KeyPairGenerator.getInstance(algName, provider);
			g.initialize(ecSpec, SecureRandom.getInstance("SHA1PRNG"));
			return g.generateKeyPair();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
