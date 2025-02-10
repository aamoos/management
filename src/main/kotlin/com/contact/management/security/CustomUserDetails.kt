package com.contact.management.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    private val member: CustomUserDto // Change from CustomUserInfoDto to CustomUserDto
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        // Assuming role is part of the member object, you can change accordingly
        val roles = listOf("ROLE_USER") // Or map it to a member role field if available
        return roles.map { SimpleGrantedAuthority(it) }
    }

    override fun getPassword(): String = member.password // Ensure the DTO has password if needed

    override fun getUsername(): String = member.id.toString() // or member.name if that's preferred

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}