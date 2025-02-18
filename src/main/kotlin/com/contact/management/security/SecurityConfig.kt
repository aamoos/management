package com.contact.management.security

import com.contact.management.jwt.JwtAccessDeniedHandler
import com.contact.management.jwt.JwtAuthFilter
import com.contact.management.jwt.JwtAuthenticationEntryPoint
import com.contact.management.jwt.JwtUtil
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)         //PreAuthorize 사용하기위한
class SecurityConfig(
    private val customUserDetailsService: CustomUserDetailsService,
    private val jwtUtil: JwtUtil,
    private val accessDeniedHandler: JwtAccessDeniedHandler,
    private val authenticationEntryPoint: JwtAuthenticationEntryPoint
) {

    companion object {
        private val AUTH_WHITELIST = arrayOf(
            "/api/users/**", // Allows unrestricted access to this endpoint
            "/",
            "/front/**"
        )
    }

    @Bean
    @ConditionalOnProperty(name = ["spring.h2.console.enabled"], havingValue = "true")
    fun configureH2ConsoleEnable(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web -> web.ignoring().requestMatchers(PathRequest.toH2Console()) }
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    .requestMatchers(*AUTH_WHITELIST).permitAll()
                    .requestMatchers("/api/test/users").hasAuthority("ROLE_USER")   //USER 역할만 접근 허용
                    .requestMatchers("/api/test/admin").hasAuthority("ROLE_ADMIN")  // ADMIN 역할만 접근 허용
                    .anyRequest().authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            }
            .addFilterBefore(JwtAuthFilter(customUserDetailsService, jwtUtil), UsernamePasswordAuthenticationFilter::class.java)
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
