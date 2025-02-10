package com.contact.management.service

import com.contact.management.dto.LoginRequest
import com.contact.management.dto.SignUpRequest
import com.contact.management.dto.UserResponse
import com.contact.management.dto.UserUpdateRequest
import com.contact.management.entity.QUser
import com.contact.management.entity.User
import com.contact.management.exception.CommonException
import com.contact.management.exception.CommonExceptionCode.EMAIL_ALREADY_EXISTS
import com.contact.management.exception.CommonExceptionCode.USER_NOT_FOUND
import com.contact.management.jwt.JwtUtil
import com.contact.management.repository.UserRepository
import com.contact.management.security.CustomUserDto
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
     private val userRepository: UserRepository
    ,private val queryFactory: JPAQueryFactory
    ,private val encoder: PasswordEncoder
    ,private val jwtUtil: JwtUtil
) {

    @Transactional(readOnly = true)
    fun getAllUsers() : List<UserResponse> {
        return userRepository.findAll().map { UserResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun getUserById(id: Long): UserResponse {
        val user = userRepository.findById(id).orElseThrow{ CommonException(USER_NOT_FOUND) }
        return UserResponse.from(user)
    }

    fun createdUser(request: SignUpRequest): UserResponse {
        userRepository.findByEmail(request.email)?.let {
            throw CommonException(EMAIL_ALREADY_EXISTS)
        }

        val user = userRepository.save(User.from(request, encoder))
        return UserResponse.from(user)
    }

    // 사용자 업데이트
    fun updateUser(id: Long, request: UserUpdateRequest): UserResponse{
        val user = userRepository.findById(id).orElseThrow{ CommonException(USER_NOT_FOUND) }

        user.update(request, encoder)

        return UserResponse.from(user)
    }

    fun deleteUser(id: Long){
        if(!userRepository.existsById(id)){
            throw CommonException(USER_NOT_FOUND)
        }
        userRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun getUsersWithPaging(pageable: Pageable): Page<UserResponse> {
        val qUser = QUser.user

        // JPAQueryFactory를 사용하여 쿼리 작성
        val query = queryFactory
            .selectFrom(qUser)
            .orderBy(qUser.id.asc()) // ID 기준으로 오름차순 정렬 (필요에 따라 변경 가능)

        // 실제 데이터 조회
        val users = query
            .offset(pageable.offset) // 페이지의 시작 인덱스
            .limit(pageable.pageSize.toLong()) // 페이지 크기
            .fetch() // 결과 가져오기

        // 총 개수를 구하는 쿼리 작성 (countQuery)
        val countQuery = queryFactory
            .select(qUser.count()) // 총 개수 계산
            .from(qUser)
        val total = countQuery.fetchOne() ?: 0L // fetchOne()으로 단일 값 가져오기

        // Page 객체로 반환
        val userDtos = users.map { UserResponse.from(it) }
        return PageImpl(userDtos, pageable, total)
    }

    @Transactional(readOnly = true)
    fun login(request: LoginRequest): String {
        val email = request.email
        val password = request.password
        val user = userRepository.findByEmail(email) ?: throw CommonException(USER_NOT_FOUND)

        // 암호화된 password를 디코딩한 값과 입력한 패스워드 값이 다르면 예외를 던짐
        if (!encoder.matches(password, user.password)) {
            throw CommonException(USER_NOT_FOUND)
        }

        val info = CustomUserDto.from(user)
        return jwtUtil.createAccessToken(info)
    }

}