	package com.v1.sealert.sa;

	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.context.ApplicationContext;
	import org.springframework.scheduling.annotation.EnableScheduling;

	@SpringBootApplication
	@EnableScheduling
	public class SaApplication {

		public static void main(String[] args) {
			ApplicationContext context = SpringApplication.run(SaApplication.class, args);

			NonStatic n = context.getBean(NonStatic.class);

		}

	}
