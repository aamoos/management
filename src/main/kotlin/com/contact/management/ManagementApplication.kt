package com.contact.management

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing


@SpringBootApplication
@EnableJpaAuditing
class ManagementApplication

fun main(args: Array<String>) {
	runApplication<ManagementApplication>(*args)
	val dotenv = Dotenv.load()
	System.out.println("SPRING_PROFILES_ACTIVE: " + dotenv.get("SPRING_PROFILES_ACTIVE"));
	System.out.println("SECRET: " + dotenv.get("SECRET"));
}
