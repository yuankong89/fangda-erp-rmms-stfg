/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.auth.web

import com.fangda.erp.auth.dto.UserDto
import com.fangda.erp.authlibs.principal.FangdaPrincipal
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * 提供用户信息
 * @author yuhb
 * @date 2020/6/3
 */
@RestController
@RequestMapping("/user")
class UserController {
    @GetMapping("")
    fun getUserInfo(authentication: Authentication): Mono<UserDto> {
        if (authentication.principal is FangdaPrincipal) {
            return Mono.just(authentication.principal as FangdaPrincipal)
                .map {
                    UserDto().apply {
                        this.userId = it.userId
                        this.username = it.username
                        this.depId = it.depId
                        this.telephone = it.telephone
                    }
                }
        }
        return Mono.empty()
    }
}