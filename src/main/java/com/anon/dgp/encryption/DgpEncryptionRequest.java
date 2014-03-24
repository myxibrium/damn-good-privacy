package com.anon.dgp.encryption;

public class DgpEncryptionRequest {
	
	private String myPrivateKey;
	
	private String theirPublicKey;
	
	private byte[] plainBytes;

	public String getMyPrivateKey() {
		return myPrivateKey;
	}

	public void setMyPrivateKey(String myPrivateKey) {
		this.myPrivateKey = myPrivateKey;
	}

	public String getTheirPublicKey() {
		return theirPublicKey;
	}

	public void setTheirPublicKey(String theirPublicKey) {
		this.theirPublicKey = theirPublicKey;
	}

	public byte[] getPlainBytes() {
		return plainBytes;
	}

	public void setPlainBytes(byte[] plainBytes) {
		this.plainBytes = plainBytes;
	}
	
}
