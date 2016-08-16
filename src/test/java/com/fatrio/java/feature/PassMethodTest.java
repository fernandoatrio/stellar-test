package com.fatrio.java.feature;

import java.util.Arrays;

import org.junit.Test;

public class PassMethodTest {
	
	@Test
	public void passMethodAsParameter() {
		Arrays.asList("Hola!", "Hello!", "Bon Soir!").forEach(this::print);
	}
	
	@Test
	public void passStaticMethodAsParameter() {
		Arrays.asList("Hola!", "Hello!", "Bon Soir!").forEach(PassMethodTest::staticPrint);
	}
	
	public static void staticPrint(String s) {
		System.out.println(String.format("Length %d: %s", s.length(), s));
	}
	
	public void print(String s) {
		System.out.println(String.format("Length %d: %s", s.length(), s));
	} 

}
