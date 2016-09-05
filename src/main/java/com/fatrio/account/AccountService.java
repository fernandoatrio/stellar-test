package com.fatrio.account;

import java.util.Arrays;
import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccountService implements UserDetailsService {
	
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct	
	protected void initialize() {
		// This is hardcoded to avoid loosing the account information with every app restart (the system is using a memory DB)
		// AccountId: GC4U45NTFCMGT62FC4D6WBQWEBPXRUBWBMQGHHS3KJBSUZMSSR32BJPA, SecretSeed: SA2TIHXJ7FKTCLK5JODFAT3W7TWXLUKED7CQNIMMGV7SHHHAU5IRITC5
		Account user = new Account("user", "demo", "ROLE_USER");
		user.setStellarAccountId("GC4U45NTFCMGT62FC4D6WBQWEBPXRUBWBMQGHHS3KJBSUZMSSR32BJPA");
		user.setStellarSecretSeed("SA2TIHXJ7FKTCLK5JODFAT3W7TWXLUKED7CQNIMMGV7SHHHAU5IRITC5");		
		// AccountId: GASSGK5AG3H4BNIALSBICPSKERP2OIYGECFTWPHNWFTIVZXH2VJMAQN7, SecretSeed: SA7ARASD7VOFTYMW7YMOOPKOCLGUBJR6GDU6OSKSUSZVKNJCUFLIXM6X
		Account admin = new Account("admin", "admin", "ROLE_ADMIN");
		admin.setStellarAccountId("GASSGK5AG3H4BNIALSBICPSKERP2OIYGECFTWPHNWFTIVZXH2VJMAQN7");
		admin.setStellarSecretSeed("SA7ARASD7VOFTYMW7YMOOPKOCLGUBJR6GDU6OSKSUSZVKNJCUFLIXM6X");
		
		for (Account account : Arrays.asList(user, admin))
			save(account);
	}

	@Transactional
	public Account save(Account account) {
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		accountRepository.save(account);
		return account;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findOneByEmail(username);
		if(account == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return createUser(account);
	}
	
	public void signin(Account account) {
		SecurityContextHolder.getContext().setAuthentication(authenticate(account));
	}
	
	private Authentication authenticate(Account account) {
		return new UsernamePasswordAuthenticationToken(createUser(account), null, Collections.singleton(createAuthority(account)));		
	}
	
	private User createUser(Account account) {
		return new User(account.getEmail(), account.getPassword(), Collections.singleton(createAuthority(account)));
	}

	private GrantedAuthority createAuthority(Account account) {
		return new SimpleGrantedAuthority(account.getRole());
	}
	
	@Transactional
	public Page<Account> findAll(Pageable page) {
		return this.accountRepository.findAll(page);
	}
	
	@Transactional
	public Account findOne(Specification<Account> spec) {
		return this.accountRepository.findOne(spec);
	}
	
	@Transactional
	public Slice<Account> find(Specification<Account> specs, Pageable page) {
		return this.accountRepository.findAll(specs, page);
	}
	
	public Specification<Account> hasStellarAccountId(String stellarAccountId) {
		return (root, query, cb) -> cb.equal(root.get(Account_.stellarAccountId), stellarAccountId);
	}
	
	public Specification<Account> hasEmail(String email) {
		return (root, query, cb) -> cb.equal(root.get(Account_.email), email);
	}
	
	public Specification<Account> hasRole(String role) {
		return (root, query, cb) -> cb.equal(root.get(Account_.role), role);
	}

}
