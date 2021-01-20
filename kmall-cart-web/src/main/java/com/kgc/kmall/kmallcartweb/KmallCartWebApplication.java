package com.kgc.kmall.kmallcartweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.kgc.kmall")
@SpringBootApplication
public class KmallCartWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(KmallCartWebApplication.class, args);
	}

}
