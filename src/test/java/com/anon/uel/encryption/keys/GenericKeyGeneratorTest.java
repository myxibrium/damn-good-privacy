package com.anon.uel.encryption.keys;

import static org.junit.Assert.*;

import org.junit.Test;

public class GenericKeyGeneratorTest {
	
	@Test
	public void testGenerateKey() {
		byte[] bytes = new GenericKeyGenerator().generateKey(256);
		assertEquals(32, bytes.length);
	}
}
