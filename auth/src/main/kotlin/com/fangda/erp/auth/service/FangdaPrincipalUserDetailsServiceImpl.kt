/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.auth.service

import com.fangda.erp.auth.manager.RmmsUserManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import reactor.core.publisher.Mono

/**
 * @author yuhb
 * @date 2020/6/4
 */
class FangdaPrincipalUserDetailsServiceImpl @Autowired constructor(private val rmmsUserManager: RmmsUserManager) : ReactiveUserDetailsService {
    override fun findByUsername(username: String?): Mono<UserDetails> {
        return Mono.fromCallable {
            if (username != null) {
                rmmsUserManager.getRmmsUserByAccount(username)
            } else {
                throw RuntimeException("用户名为空!")
            }
        }
    }
}