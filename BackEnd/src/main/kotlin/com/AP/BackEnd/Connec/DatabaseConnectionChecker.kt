package com.AP.BackEnd

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import jakarta.annotation.PostConstruct
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener


@Component
class DatabaseConnectionChecker {

//    @EventListener(ContextRefreshedEvent::class)
//    fun init() {
//        println("Database connection initialized")
//    }
}