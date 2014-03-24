package com.anon.uel.digest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Sha256DigestTest {
	
	private Sha256Digest digest;
	
	@Before
	public void setup() {
		digest = new Sha256Digest();
	}
	
	@Test
	public void testDigest() {
		byte[] content = new byte[] {48, 57, 48, 19, 6, 7, 42, -122, 72, -50, 61, 2, 1, 6, 8, 42, -122, 72, -50, 61, 3, 1, 7, 3, 34, 0, 3, 122, 117, -59, -117, -54, -93, 106, -71, 112, 94, 111, 15, 59, -10, -31, -60, -23, -49, 56, -95, -48, 98, -9, 54, 15, 52, -34, -91, 125, -76, -49, -73};
		byte[] hash = digest.digest(content);
		assertEquals(32, hash.length);
	}

}
