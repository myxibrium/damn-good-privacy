package com.anon.dgp.encryption;

public class DgpDecryptionRequest {
	
	private String myPrivateKey;
	
	private String theirPublicKey;
	
	private String message;

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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
