/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.authlibs.auth

import com.fangda.erp.authlibs.principal.FangdaPrincipal
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.AuthenticationException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * @author yuhb
 * @date 2020/7/10
 */
class FangdaAuthenticator : Authenticator {
    private val authenticators: MutableList<Authenticator> = mutableListOf()

    override fun authenticate(request: ServerHttpRequest): Mono<FangdaPrincipal> {
        return Flux.fromIterable(authenticators)
            .flatMap { it.authenticate(request) }
            .onErrorContinue(AuthenticationException::class.java) { _, _ -> }
            .next()
    }

    fun addAuthenticator(authenticator: Authenticator) {
        this.authenticators.add(authenticator)
    }
}