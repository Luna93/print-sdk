package com.service.print;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class PrintApplication {

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(PrintApplication.class);
	    builder.headless(false).run(args);
//		SpringApplication.run(PrintApplication.class, args);
	}

}
