package com.anon.uel.encryption;

import java.security.Provider;
import java.security.Security;

import org.junit.Before;
import org.junit.Test;


public class CheckSystemCiphers {
	
	@Before
	public void setup() {
		SecurityProviderInitializer.initializeSecurityProviders();
	}
	
	@Test
	public void printAllProviders() {
		for (Provider provider : Security.getProviders()) {
			System.out.println("Provider: " + provider.getName());
			for (Provider.Service service : provider.getServices()) {
                System.out.println("  Algorithm: " + service.getAlgorithm() + " Type: " + service.getType());
            }
		}
	}

}
