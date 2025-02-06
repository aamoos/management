package com.contact.management.controller

import com.contact.management.dto.UserDto
import com.contact.management.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserDto>> {
        val users = userService.getAllUsers()
        return ResponseEntity.ok(users)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.getUserById(id))
    }

    @PostMapping
    fun createUser(@Valid @RequestBody userDto: UserDto): ResponseEntity<UserDto> {
        val createdUser = userService.createdUser(userDto)
        return ResponseEntity
            .status(HttpStatus.CREATED) // 201 Created
            .header(HttpHeaders.LOCATION, "/api/users/${createdUser.id}") // 생성된 리소스의 URI 반환
            .body(createdUser)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long,@Valid @RequestBody userDto: UserDto): ResponseEntity<UserDto> {
        val updatedUser = userService.updateUser(id, userDto)
        return ResponseEntity.ok(updatedUser)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build() // 204 No Content
    }
}