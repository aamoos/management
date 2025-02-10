package com.contact.management.controller

import com.contact.management.dto.LoginRequest
import com.contact.management.dto.SignUpRequest
import com.contact.management.dto.UserResponse
import com.contact.management.dto.UserUpdateRequest
import com.contact.management.service.QuerydslUserService
import com.contact.management.service.UserService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import kotlin.math.log

@RestController
@RequestMapping("/api/users")
class UserController(
     private val userService: UserService
    ,private val querydslUserService: QuerydslUserService
) {

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserResponse>> {
        val users = userService.getAllUsers()
        return ResponseEntity.ok(users)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(userService.getUserById(id))
    }

    @PostMapping
    fun createUser(@Valid @RequestBody request: SignUpRequest): ResponseEntity<UserResponse> {
        val createdUser = userService.createdUser(request)
        return ResponseEntity
            .status(HttpStatus.CREATED) // 201 Created
            .header(HttpHeaders.LOCATION, "/api/users/${createdUser.id}") // 생성된 리소스의 URI 반환
            .body(createdUser)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long,@Valid @RequestBody request: UserUpdateRequest): ResponseEntity<UserResponse> {
        val updatedUser = userService.updateUser(id, request)
        return ResponseEntity.ok(updatedUser)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build() // 204 No Content
    }

    @GetMapping("/paged")
    fun getUsersWithPaging(pageable: Pageable, @RequestParam searchVal: String?): Page<UserResponse> {
        // 페이징된 사용자 목록 반환
        return querydslUserService.getUsersWithPaging(pageable, searchVal)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<String>{
        val token = userService.login(loginRequest)
        return ResponseEntity.ok(token)
    }
}