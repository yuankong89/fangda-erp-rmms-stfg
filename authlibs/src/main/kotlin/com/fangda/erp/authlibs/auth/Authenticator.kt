/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.authlibs.auth

import com.fangda.erp.authlibs.principal.FangdaPrincipal
import org.springframework.http.server.reactive.ServerHttpRequest
import reactor.core.publisher.Mono

/**
 * @author yuhb
 * @date 2020/7/10
 */
interface Authenticator {
    companion object {
        const val DEFAULT_AUTHENTICATION_ATTR_NAME = "Authorization"
    }

    fun authenticate(request: ServerHttpRequest): Mono<FangdaPrincipal>

    fun getAuthenticateHeader(request: ServerHttpRequest): Mono<String> {
        val str = request.headers.getFirst(DEFAULT_AUTHENTICATION_ATTR_NAME)
        return if (str.isNullOrEmpty()) {
            Mono.empty()
        } else {
            Mono.just(str)
        }
    }
}