package com.anon.uel.encoding;

import java.util.Arrays;

import org.bouncycastle.util.encoders.Base64;

public class Section {
	
	private static final int encodingWrap = 32;
	
	private String title;
	
	private byte[] content;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public String toString() {
		return String.format("%s:%s;", title, encodeBytes(content));
	}
	
	private String encodeBytes(byte[] bytes) {
		return new String(Base64.encode(bytes));
	}
	
}