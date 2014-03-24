package com.anon.uel.encoding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.encoders.Base64;

import com.anon.dgp.encoding.DgpEncoded;

public class Decoder {
	
	private static final Pattern capFinder = Pattern.compile("-----(?:BEGIN|END )([^-]+)-----");
	private static final Pattern propFinder = Pattern.compile("^(.+):(.+)$");
	
	public DgpEncoded getContent(String encoded) {
		DgpEncoded key = null;
		Scanner scan = new Scanner(encoded);
		boolean endOfProps = false;
		String propString = "";
		String sectionString = "";
		while(scan.hasNextLine()) {
			String line = scan.nextLine().trim();
			Matcher capMatcher = capFinder.matcher(line);
			if (capMatcher.matches() && key == null) {
				key = new DgpEncoded(capMatcher.group(1));
			} else if (StringUtils.isNotBlank(line)) {
				if (!endOfProps) {
					propString += line;
				} else {
					sectionString += line;
				}
			} else {
				endOfProps = true;
			}
		}
		scan.close();
		String[] sections = StringUtils.split(sectionString, ';');
		key.addAllSections(parseSections(sections));
		key.addAllProps(parseProps(StringUtils.split(propString, ';')));
		return key;
	}
	
	public List<Prop> parseProps(String[] propStrings) {
		List<Prop> props = new ArrayList<Prop>();
		for (int i=0; i<propStrings.length; i++) {
			String sectString = propStrings[i];
			Matcher sectMatcher = propFinder.matcher(sectString);
			if (sectMatcher.matches()) {
				Prop prop = new Prop();
				prop.setName(sectMatcher.group(1));
				prop.setValue(sectMatcher.group(2));
				props.add(prop);
			}
		}
		return props;
	}
	
	public List<Section> parseSections(String[] sectStrings) {
		List<Section> sections = new ArrayList<Section>();
		for (int i=0; i<sectStrings.length; i++) {
			String sectString = sectStrings[i];
			Matcher sectMatcher = propFinder.matcher(sectString);
			if (sectMatcher.matches()) {
				Section section = new Section();
				section.setTitle(sectMatcher.group(1));
				section.setContent(Base64.decode(sectMatcher.group(2).getBytes()));
				sections.add(section);
			}
		}
		return sections;
	}

}
