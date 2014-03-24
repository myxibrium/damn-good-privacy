package com.anon.uel.encoding;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class DecoderTest {
	
	@Test
	public void testDecode() {
		String encoded = "-----BEGIN ECDH PUBLIC KEY-----\r\n"+
				"Version: 0.0.0\r\n" +
				"\r\n" +
				"MDowEwYHKoZIzj0CAQYIKoZIzj0DAQcD\r\n"+
				"IwACANbQRtCWGi08m2QwtwQDVdcPLLJP\r\n"+
				"s6f1UxT2sfHs9RF9\r\n" +
				"\r\n" +
				"-----END ECDH PUBLIC KEY-----";
		Decoder decoder = new Decoder();
		List<Section> sections = decoder.getContent(encoded).getSections();
		assertEquals(1, sections.size());
		assertEquals(60, sections.get(0).getContent().length);
	}

}
