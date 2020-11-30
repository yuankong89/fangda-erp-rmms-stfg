/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.authlibs.utils

import com.fangda.erp.authlibs.principal.FangdaPrincipal
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication

/**
 * @author yuhb
 * @date 2020/6/8
 */
object AuthenticationUtils {
    fun getFangdaPrincipalOrNull(authentication: Authentication): FangdaPrincipal? {
        return if (authentication.principal != null && authentication.principal is FangdaPrincipal) {
            authentication.principal as FangdaPrincipal
        } else {
            null
        }
    }

    fun getFangdaPrincipalOrThrow(authentication: Authentication): FangdaPrincipal {
        return if (authentication.principal != null && authentication.principal is FangdaPrincipal) {
            authentication.principal as FangdaPrincipal
        } else {
            throw BadCredentialsException("找不到方大账户!")
        }
    }
}