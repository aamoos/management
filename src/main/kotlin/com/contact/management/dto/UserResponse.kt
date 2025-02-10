package com.contact.management.dto

import com.contact.management.entity.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import org.springframework.security.crypto.password.PasswordEncoder

data class UserResponse(
    val id: Long?= null,
    val name: String,
    val age: Int,
    val email: String
){
    companion object {
        fun from(user: User) = UserResponse(
            id = user.id,
            name = user.name,
            age = user.age,
            email = user.email
        )
    }
}