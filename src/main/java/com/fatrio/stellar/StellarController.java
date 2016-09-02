package com.fatrio.stellar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	public String accountsBalance(Model model, Pageable page) {
		Page<Account> accounts = this.accountService.findAll(page);
		model.addAttribute("accounts", accounts);
		return "stellar/balance";
	}
}
