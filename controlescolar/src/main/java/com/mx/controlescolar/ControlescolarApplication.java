package com.mx.controlescolar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ControlescolarApplication 
//implements CommandLineRunner
{


	/* 
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	public ControlescolarApplication(){
		
		this.passwordEncoder = new BCryptPasswordEncoder();
	}*/
	
//	@Override
//	public void run(String... args) throws Exception {
//		
//			System.out.println(passwordEncoder.encode("Chicharito12#"));
//		
//	}
	public static void main(String[] args) {
		SpringApplication.run(ControlescolarApplication.class, args);
		
	}

}
