package org.example.smartjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SmartJobApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartJobApplication.class, args);
	}

}
