package com.contact.management.audit

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import java.util.Optional

@Configuration
class AuditorAwareConfig {
    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return AuditorAware { Optional.of("system") } // 실제 환경에서는 SecurityContext에서 가져오기
    }
}