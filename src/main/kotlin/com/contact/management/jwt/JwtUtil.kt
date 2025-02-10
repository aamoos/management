package com.contact.management.jwt

import com.contact.management.exception.CommonException
import com.contact.management.exception.CommonExceptionCode
import com.contact.management.security.CustomUserDto
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.time.ZonedDateTime
import java.util.*

/**
 * [JWT 관련 메서드를 제공하는 클래스]
 */
@Component
class JwtUtil(
    @Value("\${jwt.secret}") secretKey: String,
    @Value("\${jwt.expiration_time}") private val accessTokenExpTime: Long
) {
    private val log: Logger = LoggerFactory.getLogger(JwtUtil::class.java)
    private val key: Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))

    /**
     * Access Token 생성
     * @param user
     * @return Access Token String
     */
    fun createAccessToken(user: CustomUserDto): String {
        return createToken(user, accessTokenExpTime)
    }

    /**
     * JWT 생성
     * @param user
     * @param expireTime
     * @return JWT String
     */
    private fun createToken(user: CustomUserDto, expireTime: Long): String {
        val claims: Claims = Jwts.claims().apply {
            put("id", user.id)
            put("email", user.email)
            put("role", user)
        }

        val now = ZonedDateTime.now()
        val tokenValidity = now.plusSeconds(expireTime)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date.from(now.toInstant()))
            .setExpiration(Date.from(tokenValidity.toInstant()))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    /**
     * Token에서 User ID 추출
     * @param token
     * @return User ID
     */
    fun getUserId(token: String): Long {
        val id = parseClaims(token)["id", Integer::class.java]  // Read the claim as an Integer
        return id?.toLong() ?: throw CommonException(CommonExceptionCode.INVALID_AUTH_TOKEN) // Convert Integer to Long
    }

    /**
     * JWT 검증
     * @param token
     * @return IsValidate
     */
    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            true
        } catch (e: io.jsonwebtoken.security.SecurityException) {
            log.info("Invalid JWT Token", e)
            false
        } catch (e: MalformedJwtException) {
            log.info("Invalid JWT Token", e)
            false
        } catch (e: ExpiredJwtException) {
            log.info("Expired JWT Token", e)
            false
        } catch (e: UnsupportedJwtException) {
            log.info("Unsupported JWT Token", e)
            false
        } catch (e: IllegalArgumentException) {
            log.info("JWT claims string is empty.", e)
            false
        }
    }

    /**
     * JWT Claims 추출
     * @param accessToken
     * @return JWT Claims
     */
    fun parseClaims(accessToken: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }
}