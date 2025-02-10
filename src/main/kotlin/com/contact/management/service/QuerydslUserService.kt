package com.contact.management.service

import com.contact.management.dto.UserResponse
import com.contact.management.entity.QUser
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class QuerydslUserService(
    private val queryFactory: JPAQueryFactory
) {

    @Transactional(readOnly = true)
    fun getUsersWithPaging(pageable: Pageable, searchVal: String?): Page<UserResponse> {
        val qUser = QUser.user
        var predicate: BooleanExpression = qUser.isNotNull

        searchVal?.let {
            predicate = predicate.and(qUser.name.containsIgnoreCase(searchVal))
        }

        // JPAQueryFactory를 사용하여 쿼리 작성
        val query = queryFactory
            .selectFrom(qUser)
            .where(predicate)
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
            .where(predicate)
        val total = countQuery.fetchOne() ?: 0L // fetchOne()으로 단일 값 가져오기

        // Page 객체로 반환
        val userDtos = users.map { UserResponse(it.id, it.name, it.age, it.email) }
        return PageImpl(userDtos, pageable, total)
    }

}