package com.anon.uel.encryption.serpent;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

import com.anon.uel.encryption.SecurityProviderInitializer;
import com.anon.uel.encryption.serpent.SerpentEncryptor;

public class SerpentEncryptorTest {
	
	private SerpentEncryptor encryptor;
	
	@Before
	public void setup() {
		SecurityProviderInitializer.initializeSecurityProviders();
		encryptor = new SerpentEncryptor();
	}
	
	@Test
	public void testEncryptString() {
		byte[] encrypted = encryptor.encrypt(new byte[] {25, 16, 0, 55}, new byte[32]);
		byte[] expected = new byte[] {-43, 37, -4, -56, 108, 90, -66, -102, -77, 17, -125, -88, 37, 114, -87, 33};
		assertArrayEquals(expected, encrypted);
	}

}
