package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.kgc.kmall")
@SpringBootApplication
public class KmallOrderWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(KmallOrderWebApplication.class, args);
	}

}
