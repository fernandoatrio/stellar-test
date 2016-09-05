package com.fatrio.stellar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.stellar.sdk.CreateAccountOperation;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Memo;
import org.stellar.sdk.Server;
import org.stellar.sdk.Transaction;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.AccountResponse.Balance;

import com.fatrio.account.Account;

@Service
public class StellarService {
	final static private Logger logger = LoggerFactory.getLogger(StellarService.class);  
	private static final String TESTNET = "https://horizon-testnet.stellar.org";
	private static final String FRIENDBOT = "https://horizon-testnet.stellar.org/friendbot?addr=%s"; 

	public KeyPair createAccount() {		
		KeyPair pair = KeyPair.random();
		logger.info(String.format("AccountId: %s, SecretSeed: %s", pair.getAccountId(), String.valueOf(pair.getSecretSeed())));		
		try (
				CloseableHttpClient httpclient = HttpClients.createDefault();
				CloseableHttpResponse resp = httpclient.execute(new HttpGet(String.format(FRIENDBOT, pair.getAccountId())));
			) {
			//this.printResponse(resp);
			return pair; 
		} catch (IOException e) {
			logger.error("Error creating Stellar account", e);
		}
		
		return null;
	}

	private void printResponse(CloseableHttpResponse resp) throws IOException {
		for (Header header : resp.getAllHeaders())
			System.out.println(header);
		BufferedReader reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
		reader.lines().forEach(System.out::println);
	}

	public AccountResponse account(KeyPair pair) throws IOException {
		Server server = new Server(TESTNET);
		return server.accounts().account(pair);
	}

	public void loadBalanceInformation(Iterable<Account> accounts) throws IOException {
		for (Account account : accounts) {
			KeyPair pair = KeyPair.fromAccountId(account.getStellarAccountId());
			AccountResponse stellarAccount = this.account(pair);
			Balance balance = stellarAccount.getBalances()[0]; // Assume only native currency
			account.setStellarBalance(balance.getBalance());
		}
	}

	public void transfer(Account source, Account destination, Long amount) {
	    KeyPair srcPair = KeyPair.fromAccountId(source.getStellarAccountId());
	    KeyPair destPair = KeyPair.fromAccountId(destination.getStellarAccountId());
		
	    org.stellar.sdk.Account account = new org.stellar.sdk.Account(srcPair, 2908908335136768L);
	    Transaction transaction = new Transaction.Builder(account)
	            .addOperation(new CreateAccountOperation.Builder(destPair, Long.toString(amount)).build())
	            .build();
	    
	    transaction.sign(srcPair);
	}

}
