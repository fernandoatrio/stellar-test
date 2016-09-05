package com.fatrio.stellar;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fatrio.account.Account;
import com.fatrio.account.AccountService;

@Controller
@RequestMapping(value="stellar")
public class StellarController {
	@Autowired
	private StellarService stellarService;
	@Autowired
	private AccountService accountService;
	
	@Transactional
	@RequestMapping(value="account/balance/all", method=RequestMethod.GET)
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	public String accountsBalance(Model model, Pageable page) throws IOException {
		Page<Account> accounts = this.accountService.findAll(page);
		this.stellarService.loadBalanceInformation(accounts);		
		model.addAttribute("accounts", accounts);
		return "stellar/balance";
	}
	
	@Transactional
	@RequestMapping(value="account/transfer", method=RequestMethod.POST)
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	public String transfer(Model model, 
						   Pageable page, 
						   @RequestParam String source, 
						   @RequestParam String destination, 
						   @RequestParam Long amount) {
		Specification<Account> hasSourceEmail = this.accountService.hasEmail(source);
		Specification<Account> hasDestinationEmail = this.accountService.hasEmail(destination);
		Account src = this.accountService.findOne(hasSourceEmail);
		Account dst = this.accountService.findOne(hasDestinationEmail);
		this.stellarService.transfer(src, dst, amount);
		
		return "redirect:/stellar/account/balance/all";
	}
}
