package com.contact.management.controller

import com.contact.management.dto.UserDto
import com.contact.management.service.UserService
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
        val user = userService.getUserById(id)
        return user.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createdUser(@RequestBody userDto: UserDto): ResponseEntity<UserDto> {
        val createdUser = userService.createdUser(userDto)
        return ResponseEntity.status(201).body(createdUser) // 201 Created 상태 코드
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody userDto: UserDto): ResponseEntity<UserDto> {
        val updatedUser = userService.updateUser(id, userDto)
        return updatedUser.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build() // 404 상태 코드 반환
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build() // 204 No Content 상태 코드
    }
}