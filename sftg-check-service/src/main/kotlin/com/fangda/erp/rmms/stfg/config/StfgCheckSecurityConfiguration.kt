/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.rmms.stfg.config

import com.fangda.erp.authlibs.auth.ReactiveAuthenticationServerContextRepository
import com.fangda.erp.authlibs.auth.jwt.JwtAuthenticator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

/**
 * @author yuhb
 * @date 2020/6/8
 */
@Configuration
@EnableWebFluxSecurity
class StfgCheckSecurityConfiguration {
    @Bean
    fun reactiveAuthenticationServerContextRepository(): ReactiveAuthenticationServerContextRepository {
        return ReactiveAuthenticationServerContextRepository().apply {
            this.authenticator.addAuthenticator(JwtAuthenticator())
        }
    }

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .securityContextRepository(reactiveAuthenticationServerContextRepository())
            .authorizeExchange()
            .anyExchange().authenticated()
            .and()
            .formLogin().disable()
            .logout().disable()
            .csrf().disable()
            .build()
    }
}