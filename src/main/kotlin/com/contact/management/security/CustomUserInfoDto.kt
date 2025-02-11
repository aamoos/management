package com.contact.management.security

//내부에서 사용하는 dto
import com.contact.management.entity.RoleType
import com.contact.management.entity.User

data class CustomUserDto(
    val id: Long?= null,
    var password: String,
    val name: String,
    val age: Int,
    val email: String,
    val role: RoleType // Adding role to the DTO
) {
    companion object {
        // Factory method for creating CustomUserDto from User entity
        fun from(user: User): CustomUserDto {
            return CustomUserDto(
                id = user.id,
                password = user.password,
                name = user.name,
                age = user.age,
                email = user.email,
                role = user.role
            )
        }
    }
}
