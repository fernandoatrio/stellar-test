package com.fatrio.stellar;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Ignore;
import org.junit.Test;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.AccountResponse.Balance;

import static org.assertj.core.api.Assertions.assertThat;


public class TestStellarService {
	@Test
	public void createAccount() throws MalformedURLException, IOException {
		StellarService stellarService = new StellarService();
		KeyPair pair = stellarService.createAccount();
		stellarService.account(pair);
	}
	
	@Test
	public void createAccountCheckBalance() throws IOException {
		StellarService stellarService = new StellarService();
		
		KeyPair pair = stellarService.createAccount();
		assertThat(pair).isNotNull();		
		AccountResponse account = stellarService.account(pair);
		assertThat(account).isNotNull();
		
		for (Balance balance : account.getBalances()) {
			System.out.println(balance.getAssetIssuer());
			System.out.println(balance.getAssetCode());
			System.out.println(balance.getAssetType());
			System.out.println(balance.getBalance());
			System.out.println(balance);
		}
	}
	
	@Test
	public void checkAccountBalance() throws IOException {
		StellarService stellarService = new StellarService();
		KeyPair pair = KeyPair.fromSecretSeed("SA2TIHXJ7FKTCLK5JODFAT3W7TWXLUKED7CQNIMMGV7SHHHAU5IRITC5");
		assertThat(pair).isNotNull();		
		AccountResponse account = stellarService.account(pair);
		assertThat(account).isNotNull();
		
		for (Balance balance : account.getBalances()) {
			System.out.println(balance.getAssetIssuer());
			System.out.println(balance.getAssetCode());
			System.out.println(balance.getAssetType());
			System.out.println(balance.getBalance());
			System.out.println(balance);
		}
	}
}
