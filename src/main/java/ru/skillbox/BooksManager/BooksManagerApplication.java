package ru.skillbox.BooksManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BooksManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(BooksManagerApplication.class, args);
	}

}
