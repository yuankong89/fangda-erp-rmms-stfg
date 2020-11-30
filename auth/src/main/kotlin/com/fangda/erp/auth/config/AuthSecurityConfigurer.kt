/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.auth.config

import com.fangda.erp.auth.manager.RmmsUserManager
import com.fangda.erp.auth.service.FangdaPrincipalUserDetailsServiceImpl
import com.fangda.erp.authlibs.auth.ReactiveAuthenticationServerContextRepository
import com.fangda.erp.authlibs.auth.jwt.JwtAuthenticator
import com.fangda.erp.authlibs.principal.FangdaPrincipal
import com.fangda.erp.authlibs.utils.JwtUtils
import com.fangda.erp.libs.dto.InvokeResultDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import reactor.core.publisher.Mono
import reactor.netty.ByteBufMono

/**
 * @author yuhb
 * @date 2020/6/3
 */
@Configuration
@EnableWebFluxSecurity
class AuthSecurityConfigurer {

    @Autowired
    private lateinit var rmmsUserManager: RmmsUserManager

    @Bean
    fun userDetailsService(): ReactiveUserDetailsService {
        return FangdaPrincipalUserDetailsServiceImpl(rmmsUserManager)
    }

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
            .pathMatchers("/auth/login").permitAll()  //无需进行权限过滤的请求路径
            .anyExchange().authenticated()
            .and()
            .formLogin()
            .loginPage("/auth/login")
            .authenticationSuccessHandler(AuthenticationSuccessHandler())
            .authenticationFailureHandler(AuthenticationFailureHandler())
            .and()
            .logout()
            .logoutUrl("/auth/logout")
            .and()
            .csrf()
            .disable()
            .build()
    }

    class AuthenticationSuccessHandler : ServerAuthenticationSuccessHandler {
        private val mapper = ObjectMapper()
        override fun onAuthenticationSuccess(
            webFilterExchange: WebFilterExchange,
            authentication: Authentication
        ): Mono<Void> {
            return Mono.just(webFilterExchange.exchange.response)
                .doOnNext {
                    it.statusCode = HttpStatus.OK
                    it.headers.contentType = MediaType.APPLICATION_JSON
                }
                .flatMap {
                    if (authentication.principal != null && authentication.principal is FangdaPrincipal) {
                        val fangdaPrincipal = authentication.principal as FangdaPrincipal
                        it.writeAndFlushWith(
                            Mono.just(
                                ByteBufMono.just(
                                    it.bufferFactory().wrap(
                                        mapper.writeValueAsBytes(
                                            InvokeResultDto.successResult(
                                                JwtUtils.createJWT(
                                                    mapOf(
                                                        "userId" to fangdaPrincipal.userId,
                                                        "username" to fangdaPrincipal.username,
                                                        "depId" to fangdaPrincipal.depId,
                                                        "telephone" to fangdaPrincipal.telephone
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    } else {
                        Mono.empty()
                    }
                }
                .then()
        }
    }

    class AuthenticationFailureHandler : ServerAuthenticationFailureHandler {
        private val mapper = ObjectMapper()
        override fun onAuthenticationFailure(
            webFilterExchange: WebFilterExchange,
            exception: AuthenticationException
        ): Mono<Void> {
            return Mono.just(webFilterExchange.exchange.response)
                .doOnNext {
                    it.statusCode = HttpStatus.UNAUTHORIZED
                    it.headers.contentType = MediaType.APPLICATION_JSON
                }
                .flatMap {
                    it.writeAndFlushWith(
                        Mono.just(
                            ByteBufMono.just(
                                it.bufferFactory()
                                    .wrap(
                                        mapper.writeValueAsBytes(InvokeResultDto.failResult("账号或密码错误"))
                                    )
                            )
                        )
                    )
                }
                .then()
        }

    }
}