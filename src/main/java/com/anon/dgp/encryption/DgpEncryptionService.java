package com.anon.dgp.encryption;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import org.bouncycastle.util.encoders.Base64;

import com.anon.dgp.encoding.DgpEncoded;
import com.anon.uel.digest.Sha256Digest;
import com.anon.uel.encoding.Decoder;
import com.anon.uel.encoding.Prop;
import com.anon.uel.encoding.Section;
import com.anon.uel.encryption.ec.dh.EcDhAgreementGenerator;
import com.anon.uel.encryption.ec.dsa.EcDsaSignatureGenerator;
import com.anon.uel.encryption.serpent.SerpentEncryptor;

public class DgpEncryptionService {
	
	public String encrypt(DgpEncryptionRequest request) {
		Decoder decoder = new Decoder();
		
		DgpEncoded theirPublicKey = decoder.getContent(request.getTheirPublicKey());
		if (!publicKeyIsAuthentic(theirPublicKey)) {
			return "PUBLIC KEY IS NOT AUTHENTIC";
		}
		
		DgpEncoded myPrivateKey = decoder.getContent(request.getMyPrivateKey());
		
		List<Section> publicKeys = theirPublicKey.getSections();
		Integer publicKeyIndex = chooseRandomIndex(publicKeys);
		byte[] publicKeyBytes = publicKeys.get(publicKeyIndex).getContent();
		
		List<Section> privateKeys = myPrivateKey.getSections();
		Integer privateKeyIndex = chooseRandomIndex(privateKeys);
		byte[] privateKeyBytes = privateKeys.get(privateKeyIndex).getContent();
		
		byte[] password = new EcDhAgreementGenerator().generateSharedSecret(privateKeyBytes, publicKeyBytes);
		byte[] encryptedMessage = new SerpentEncryptor().encrypt(request.getPlainBytes(), new Sha256Digest().digest(password));
		
		DgpEncoded message = new DgpEncoded("DGP MESSAGE");
		message.addProp("Version", "0.0.0");
		message.addProp("Format", "ECDSA/ECDH/SERPENT/BC");
		message.addProp("Public Index", publicKeyIndex.toString());
		message.addProp("Private Index", privateKeyIndex.toString());
		byte[] myIdentity = myPrivateKey.getPropMap().get("Identity").getDecodedValue();
		byte[] mySignature = new EcDsaSignatureGenerator().sign(encryptedMessage, myIdentity);
		message.addProp("Signature", new String(Base64.encode(mySignature)));
		//encoder.addSection("Key", encryptedKey);
		message.addSection("Message", encryptedMessage);
		return message.finish();
	}
	
	public byte[] decrypt(DgpDecryptionRequest request) {
		Decoder decoder = new Decoder();
		
		DgpEncoded theirPublicKey = decoder.getContent(request.getTheirPublicKey());
		if (!publicKeyIsAuthentic(theirPublicKey)) {
			return "PUBLIC KEY IS NOT AUTHENTIC".getBytes();
		}
		DgpEncoded message = decoder.getContent(request.getMessage());
		
		DgpEncoded myPrivateKey = decoder.getContent(request.getMyPrivateKey());
		
		if (!messageIsAuthentic(theirPublicKey, message)) {
			return "MESSAGE DOESN'T MATCH PUBLIC KEY".getBytes();
		}
		
		List<Section> publicKeys = theirPublicKey.getSections();
		int publicIndex = Integer.parseInt(message.getPropMap().get("Private Index").getValue());
		byte[] publicKeyBytes = publicKeys.get(publicIndex).getContent();
		
		List<Section> privateKeys = myPrivateKey.getSections();
		int privateIndex = Integer.parseInt(message.getPropMap().get("Public Index").getValue());
		byte[] privateKeyBytes = privateKeys.get(privateIndex).getContent();

		byte[] password = new EcDhAgreementGenerator().generateSharedSecret(privateKeyBytes, publicKeyBytes);
		byte[] plainText = new SerpentEncryptor().decrypt(message.getSections().get(0).getContent(), new Sha256Digest().digest(password));
		
		return plainText;
	}
	
	private boolean messageIsAuthentic(DgpEncoded theirPublicKey, DgpEncoded message) {
		byte[] messageBytes = message.getSections().get(0).getContent();
		byte[] identity = theirPublicKey.getPropMap().get("Identity").getDecodedValue();
		byte[] signature = message.getPropMap().get("Signature").getDecodedValue();
		return new EcDsaSignatureGenerator().verify(messageBytes, identity, signature);
	}
	
	private boolean publicKeyIsAuthentic(DgpEncoded theirPublicKey) {
		String publicSignKey = theirPublicKey.getPropMap().get("Identity").getValue();
		String signature = theirPublicKey.getPropMap().get("Signature").getValue();
		String author = theirPublicKey.getPropMap().get("Author").getValue();
		String testString = author + ";";
		
		for (Section section : theirPublicKey.getSections()) {
			testString += new String(Base64.encode(section.getContent())) + ";";
		}
		return new EcDsaSignatureGenerator().verify(testString.getBytes(), Base64.decode(publicSignKey), Base64.decode(signature));
	}
	
	private int chooseRandomIndex(List list) {
		try {
			double rand = SecureRandom.getInstance("SHA1PRNG").nextDouble();
			return (int) Math.floor(((double)list.size()) * rand);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
