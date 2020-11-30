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
 * @date 2020/7/10
 */
class UsernameAuthenticationToken(private val principal: FangdaPrincipal?) : AbstractAuthenticationToken(null) {
    init {
        this.isAuthenticated = principal != null
    }

    override fun getCredentials(): Any = ""
    override fun getPrincipal(): Any = principal ?: "anonymous"
}