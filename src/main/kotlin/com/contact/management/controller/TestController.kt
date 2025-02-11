package com.contact.management.controller

import com.contact.management.dto.LoginRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/test")
    fun test(): String{
        return "ok";
    }

    @PostMapping("/api/test/user")
//    @PreAuthorize("hasRole('ROLE_USER')")
    fun userTest(@RequestBody loginRequest: LoginRequest): LoginRequest {
        return loginRequest
    }

    @PostMapping("/api/test/admin")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun adminTest(@RequestBody loginRequest: LoginRequest): LoginRequest {
        return loginRequest
    }

}