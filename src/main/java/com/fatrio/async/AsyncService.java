package com.fatrio.async;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class AsyncService {
	
	Logger log = LoggerFactory.getLogger(AsyncService.class);
	
	private Integer value = 0;
	
	private ExecutorService executor;

    public synchronized Integer value() throws InterruptedException, ExecutionException { 
    	return this.executor.submit(() -> calculate()).get();
    }

	private Integer calculate() throws InterruptedException {
		int val = this.getValue(); 
		this.sleepSeconds(2); 
		this.setValue(val + 1); 
		return val;
	}
    
    private void sleepSeconds(int seconds) throws InterruptedException {
    	Thread.sleep(1000L * seconds);
	}

	public AsyncService() {
    	this.executor = Executors.newSingleThreadExecutor();
    }

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
