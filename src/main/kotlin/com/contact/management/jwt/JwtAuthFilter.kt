package com.contact.management.jwt

import com.contact.management.security.CustomUserDetailsService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class JwtAuthFilter(
    private val customUserDetailsService: CustomUserDetailsService,
    private val jwtUtil: JwtUtil
) : OncePerRequestFilter() { // OncePerRequestFilter -> 한 번 실행 보장

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authorizationHeader = request.getHeader("Authorization")

        // JWT가 헤더에 있는 경우
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val token = authorizationHeader.substring(7)
            // JWT 유효성 검증
            if (jwtUtil.validateToken(token)) {
                val userId = jwtUtil.getUserId(token)

                // 유저와 토큰 일치 시 userDetails 생성
                val userDetails: UserDetails = customUserDetailsService.loadUserByUsername(userId.toString())

                val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                )
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }

        filterChain.doFilter(request, response) // 다음 필터로 넘기기
    }
}