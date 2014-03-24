package com.anon.uel.encoding;

import org.bouncycastle.util.encoders.Base64;


public class Prop {
	private String name;
	private String value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public byte[] getDecodedValue() {
		return Base64.decode(value);
	}
	public String toString() {
		return String.format("%s:%s;", name, value);
	}
}