package com.anon.dgp.encoding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.util.encoders.Base64;

import com.anon.uel.encoding.Prop;
import com.anon.uel.encoding.Section;


public class DgpEncoded {
	
	private static final int ENCODING_WRAP = 64;
	
	private String title;
	
	private Map<String,Prop> propMap;
	
	private List<Section> sections;
	
	public DgpEncoded(String title) {
		this.title = title;
		propMap = new LinkedHashMap<String,Prop>();
		sections = new ArrayList<Section>();
	}
	
	public void addProp(String name, String value) {
		Prop prop = new Prop();
		prop.setName(name);
		prop.setValue(value);
		addProp(prop);
	}
	
	public void addProp(Prop prop) {
		propMap.put(prop.getName(), prop);
	}
	
	public void addSection(String title, byte[] bytes) {
		Section section = new Section();
		section.setTitle(title);
		section.setContent(bytes);
		sections.add(section);
	}
	
	public void addAllSections(List<Section> sections) {
		this.sections.addAll(sections);
	}
	
	public void addAllProps(List<Prop> props) {
		for (Prop prop : props) {
			addProp(prop);
		}
	}
	
	public String finish() {
		String body = String.format("-----BEGIN %s-----\r\n", title);
		
		body += propsToString();
		body += sectionsToString();
		
		body += "\r\n";
		body += String.format("-----END %s-----\r\n", title);
		
		return body;
	}
	
	private String sectionsToString() {
		String stringsSection = "";
		for (Section section : sections) {
			stringsSection += section.toString();
		}
		return wrap(stringsSection);
	}

	private String propsToString() {
		String propsString = "";
		for (Prop prop : propMap.values()) {
			propsString += prop.toString();
		}
		return wrap(propsString) + "\r\n";
	}
	
	public String wrap(String stringToWrap) {
		String result = "";
		char[] characters = stringToWrap.toCharArray();
		for (int i=0; i<characters.length; i+=ENCODING_WRAP) {
			int to = i+ENCODING_WRAP > characters.length ? characters.length : i+ENCODING_WRAP;
			result += new String(Arrays.copyOfRange(characters, i, to)) + "\r\n";
		}
		return result;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}
	
	public Map<String, Prop> getPropMap() {
		return propMap;
	}
	
}
