package com.contact.management

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class ManagementApplication

fun main(args: Array<String>) {
	runApplication<ManagementApplication>(*args)
}
