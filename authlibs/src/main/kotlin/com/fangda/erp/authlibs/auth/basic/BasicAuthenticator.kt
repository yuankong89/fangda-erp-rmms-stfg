/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.authlibs.auth.basic

import com.fangda.erp.authlibs.principal.FangdaPrincipal
import com.fangda.erp.authlibs.auth.Authenticator
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.util.Base64Utils
import org.springframework.util.StringUtils
import reactor.core.publisher.Mono

/**
 * @author yuhb
 * @date 2020/7/10
 */
class BasicAuthenticator(private val basicUserRepository: ReactiveUserDetailsService) : Authenticator {
    private val passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
    override fun authenticate(request: ServerHttpRequest): Mono<FangdaPrincipal> {
        return Mono.just(request)
            .flatMap(this::getAuthenticateHeader)
            .switchIfEmpty(Mono.error(BadCredentialsException("basic-没有验证头")))
            .filter { StringUtils.startsWithIgnoreCase(it, BASIC) }
            .switchIfEmpty(Mono.error(BadCredentialsException("非basic验证")))
            .map { it.substring(BASIC.length, it.length) }
            .flatMap(this::handleToke)
            .flatMap { validate(it[0], it[1]) }
    }

    private fun handleToke(basicToken: String): Mono<List<String>> {
        return Mono.just(basicToken)
            .map { String(Base64Utils.decodeFromString(it)) }
            .map { it.split(":") }
            .filter { it.size == 2 }
            .switchIfEmpty(Mono.error(BadCredentialsException("basic登陆数据错误")))
    }

    private fun validate(username: String, password: String): Mono<FangdaPrincipal> {
        return basicUserRepository.findByUsername(username)
            .filter { passwordEncoder.matches(password, it.password) }
            .switchIfEmpty(Mono.error(BadCredentialsException("密码错误")))
            .map { FangdaPrincipal(username) }
    }

    companion object {
        const val BASIC = "basic "
    }
}