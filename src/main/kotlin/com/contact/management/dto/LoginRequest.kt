package com.contact.management.dto

import jakarta.validation.constraints.*

data class LoginRequest(

    @field:NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @field:Email(message = "올바른 이메일 형식을 입력하세요.")
    val email: String,

    @field:Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}\$",
        message = "비밀번호는 최소 1개 이상의 숫자와 특수문자를 포함해야 합니다."
    )
    val password:String
)