/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.authlibs.auth

import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * Basic 或者 Bearer(jwt) 验证
 * @author yuhb
 * @date 2020/7/10
 */
class ReactiveAuthenticationServerContextRepository : ServerSecurityContextRepository {

    val authenticator = FangdaAuthenticator()

    /**
     * 不同的系统通过[authoritiesRetriever]来获取用户在系统中的权限
     */
    private var authoritiesRetriever: AuthoritiesRetriever? = null

    constructor() {

    }

    constructor(authoritiesRetriever: AuthoritiesRetriever) {
        this.authoritiesRetriever = authoritiesRetriever
    }

    fun setAuthorizationRetrieve(authoritiesRetriever: AuthoritiesRetriever) {
        this.authoritiesRetriever = authoritiesRetriever
    }

    override fun save(exchange: ServerWebExchange, context: SecurityContext): Mono<Void> {
        return Mono.empty()
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        return Mono.just(exchange.request)
            .flatMap(authenticator::authenticate)
            .doOnNext {
                // 获取权限
                if (authoritiesRetriever != null) {
                    it.setAuthorities(authoritiesRetriever!!.readPrincipalAuthorities(it))
                }
            }
            .flatMap {
                val securityContext = SecurityContextImpl()
                securityContext.authentication = FangdaUsernameAuthenticationToken(it)
                Mono.just(securityContext)
            }
    }
}