package com.codeex.salarycheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.codeex.salarycheck.proc.csv.CsvBeanProcessor;
import com.codeex.salarycheck.proc.exception.ScProcessException;

@SpringBootApplication
public class ApplicationRunner {

	@Autowired
	CsvBeanProcessor csvBeanProcessor;

	public static void main(String[] args) {
		SpringApplication.run(ApplicationRunner.class, args);
	}

	@Bean
	@Profile("!test")
	public CommandLineRunner runner() {
		
		return args -> {
			String filePath = "";
	
			if (args != null) {
				for (String arg : args) {
					System.out.println(arg);
				}
				filePath = args[0];
				try {
					csvBeanProcessor.process(filePath);
				} catch (ScProcessException e) {
					e.printStackTrace();
				}
			}
			System.exit(0);
		};
	}
}
