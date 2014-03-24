package com.anon.uel.encryption;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SecurityProviderInitializer {
	
	private static boolean isInitialized = false;
	
	public static synchronized void initializeSecurityProviders() {
		if (!isInitialized) {
			Security.addProvider(new BouncyCastleProvider());
			isInitialized = true;
		}
	}

}
