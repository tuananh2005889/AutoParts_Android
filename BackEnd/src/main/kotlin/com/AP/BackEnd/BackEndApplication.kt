package com.AP.BackEnd

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EntityScan(basePackages = ["com.AP.BackEnd.model"])
@EnableJpaRepositories(basePackages = ["com.AP.BackEnd.repository"])
@ComponentScan(basePackages = ["com.AP.BackEnd"])
class BackEndApplication

fun main(args: Array<String>) {
	runApplication<BackEndApplication>(*args)
}
