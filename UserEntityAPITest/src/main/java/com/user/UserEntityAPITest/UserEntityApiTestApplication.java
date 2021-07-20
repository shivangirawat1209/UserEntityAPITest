package com.user.UserEntityAPITest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class UserEntityApiTestApplication {


	public static void main(String[] args) {
		SpringApplication.run(UserEntityApiTestApplication.class, args);
	}

}
