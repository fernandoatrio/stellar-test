package com.fatrio.async;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AsyncController {
	
	Logger log = LoggerFactory.getLogger(AsyncController.class);
	
	@Autowired
	AsyncService asyncService;

	@RequestMapping(value = "/async", method = RequestMethod.GET)
	public String handleAsync(Model model) throws InterruptedException, ExecutionException {
		log.info("Starting " + this.toString());
		Integer count = this.asyncService.value();		
		log.info("Hello!! " + count + " - " + this.asyncService.toString());
		model.addAttribute("count", count);
		return "async/async";
	}
}
