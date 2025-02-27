package com.autopart.backend.controller

import com.autopart.backend.model.User
import com.autopart.backend.repository.UserRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userRepository: UserRepository) {
//
//    @GetMapping("/test-connection")
//    fun testConnection(): String {
//        return try {
//            userRepository.count() // Kiểm tra số lượng user (nếu DB kết nối thành công)
//            "Database connection successful!"
//        } catch (e: Exception) {
//            "Database connection failed: ${e.message}"
//        }
//    }
//
//    @PostMapping("/add")
//    fun addUser(@RequestBody user: User): User {
//        return userRepository.save(user)
//    }
}
