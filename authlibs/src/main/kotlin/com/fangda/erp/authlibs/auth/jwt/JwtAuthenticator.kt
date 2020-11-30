/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.authlibs.auth.jwt

import com.fangda.erp.authlibs.auth.Authenticator
import com.fangda.erp.authlibs.exception.BadJwtTokenException
import com.fangda.erp.authlibs.principal.FangdaPrincipal
import com.fangda.erp.authlibs.utils.JwtUtils
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.util.StringUtils
import reactor.core.publisher.Mono

/**
 * @author yuhb
 * @date 2020/7/10
 */
class JwtAuthenticator : Authenticator {
    override fun authenticate(request: ServerHttpRequest): Mono<FangdaPrincipal> {
        return Mono.just(request)
            .flatMap(this::getAuthenticateHeader)
            .switchIfEmpty(Mono.error(BadCredentialsException("jwt-没有验证头")))
            .filter { StringUtils.startsWithIgnoreCase(it, BEARER) }
            .switchIfEmpty(Mono.error(BadCredentialsException("不是bearer验证")))
            .map { it.substring(BEARER.length, it.length) }
            .flatMap { Mono.fromCallable { JwtUtils.validateJWT(it) } }
            .onErrorMap(BadJwtTokenException::class.java) { BadCredentialsException(it.message) }
            .switchIfEmpty(Mono.error(BadCredentialsException("Jwt数据格式错误")))
            .flatMap {
                Mono.just(FangdaPrincipal(it.getClaim("username").asString()).apply {
                    this.userId = it.getClaim("userId").asString()
                    this.depId = it.getClaim("depId").asString()
                    this.telephone = it.getClaim("telephone").asString()
                })
            }
    }

    companion object {
        const val BEARER = "bearer "
    }
}