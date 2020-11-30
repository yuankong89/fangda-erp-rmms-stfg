/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.authlibs.auth

import com.fangda.erp.authlibs.principal.FangdaPrincipal
import org.springframework.security.authentication.AbstractAuthenticationToken

/**
 * @author yuhb
 * @date 2020/6/8
 */
class FangdaUsernameAuthenticationToken(private val principal: FangdaPrincipal) : AbstractAuthenticationToken(null) {
    init {
        isAuthenticated = true
    }

    override fun getCredentials(): Any = ""
    override fun getPrincipal(): Any = principal
}