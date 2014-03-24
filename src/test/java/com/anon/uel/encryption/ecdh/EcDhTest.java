package com.anon.uel.encryption.ecdh;

import static org.junit.Assert.*;

import java.security.KeyPair;

import org.bouncycastle.util.encoders.Base64;
import org.junit.Before;
import org.junit.Test;

import com.anon.uel.encryption.SecurityProviderInitializer;
import com.anon.uel.encryption.ec.EllipticCurveAsymmetricKeyGenerator;
import com.anon.uel.encryption.ec.dh.EcDhAgreementGenerator;

public class EcDhTest {
	
	private EllipticCurveAsymmetricKeyGenerator keygen;
	
	private EcDhAgreementGenerator agreementGenerator;
	
	@Before
	public void setup() {
		SecurityProviderInitializer.initializeSecurityProviders();
		keygen = EllipticCurveAsymmetricKeyGenerator.getInstance("ECDH");
		agreementGenerator = new EcDhAgreementGenerator();
	}
	
	@Test
	public void testGenerateKeyPair() {
		KeyPair kp = keygen.generateKeyPair();
		assertTrue(kp.getPrivate().getEncoded().length >= 64);
		assertTrue(kp.getPublic().getEncoded().length >= 32);
	}
	
	@Test
	public void testSharedSecretGeneration() {
		KeyPair tom = keygen.generateKeyPair();
		KeyPair jerry = keygen.generateKeyPair();
		
		byte[] tomToJerry = agreementGenerator.generateSharedSecret(tom.getPrivate().getEncoded(), jerry.getPublic().getEncoded());
		byte[] jerryToTom = agreementGenerator.generateSharedSecret(jerry.getPrivate().getEncoded(), tom.getPublic().getEncoded());
		
		assertArrayEquals(tomToJerry, jerryToTom);
		assertTrue(tomToJerry.length >= 32);
	}

}
