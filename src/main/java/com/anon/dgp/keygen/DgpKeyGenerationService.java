package com.anon.dgp.keygen;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.util.encoders.Base64;

import com.anon.dgp.encoding.DgpEncoded;
import com.anon.dgp.encryption.DgpUuid;
import com.anon.uel.encoding.EncodingUtils;
import com.anon.uel.encryption.AsymmetricKeyGenerator;
import com.anon.uel.encryption.ec.EllipticCurveAsymmetricKeyGenerator;
import com.anon.uel.encryption.ec.dh.EcDhAgreementGenerator;
import com.anon.uel.encryption.ec.dsa.EcDsaSignatureGenerator;

public class DgpKeyGenerationService {
	
	private static final int NUM_KEY_PAIRS = 25;
	
	AsymmetricKeyGenerator ecDhKeygen = EllipticCurveAsymmetricKeyGenerator.getInstance("ECDH");
	AsymmetricKeyGenerator ecDsaKeygen = EllipticCurveAsymmetricKeyGenerator.getInstance("ECDSA");
	
	public DgpKeyGenerationResponse generateKeys() {
		
		DgpEncoded publicEncoder = new DgpEncoded("DGP PUBLIC KEYS");
		publicEncoder.addProp("Version", "0.0.0");

		DgpEncoded privateEncoder = new DgpEncoded("DGP PRIVATE KEYS");
		privateEncoder.addProp("Version", "0.0.0");
		
		String publicSignatureBody = "";
		
		KeyPair signKeyPair = ecDsaKeygen.generateKeyPair();
		String authorUuid = DgpUuid.generateUuid();
		publicEncoder.addProp("Author", authorUuid);
		privateEncoder.addProp("Author", authorUuid);
		String publicIdentity = EncodingUtils.encodeBase64(signKeyPair.getPublic().getEncoded());
		publicEncoder.addProp("Identity", publicIdentity);
		String privateIdentity = EncodingUtils.encodeBase64(signKeyPair.getPrivate().getEncoded());
		privateEncoder.addProp("Identity", privateIdentity);
		
		publicSignatureBody += authorUuid + ";";
		
		for (int i=0; i<NUM_KEY_PAIRS; i++) {
			KeyPair keyPair = ecDhKeygen.generateKeyPair();
			String index = ((Integer)i).toString();
			
			byte[] publicKeyEncoded = keyPair.getPublic().getEncoded();
			publicSignatureBody += new String(Base64.encode(publicKeyEncoded)) + ";";
			publicEncoder.addSection(index, publicKeyEncoded);
			privateEncoder.addSection(index, keyPair.getPrivate().getEncoded());
		}
		
		publicEncoder.addProp("Signature", generateSignature(signKeyPair, publicSignatureBody));
		
		DgpKeyGenerationResponse response = new DgpKeyGenerationResponse();
		response.setPublicKeys(publicEncoder.finish());
		response.setPrivateKeys(privateEncoder.finish());
		return response;
	}

	private String generateSignature(KeyPair signKeyPair, String body) {
		return EncodingUtils.encodeBase64(new EcDsaSignatureGenerator().sign(body.getBytes(), signKeyPair.getPrivate().getEncoded()));
	}

}
