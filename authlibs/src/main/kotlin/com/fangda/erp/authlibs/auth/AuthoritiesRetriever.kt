/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.authlibs.auth

import com.fangda.erp.authlibs.principal.FangdaPrincipal
import org.springframework.security.core.GrantedAuthority

/**
 * 用户权限读取
 * 每个系统不同的实现，这样用户在每个系统中都有不同的权限
 * @author yuhb
 * @date 2020/6/8
 */
interface AuthoritiesRetriever {
    fun readPrincipalAuthorities(principal: FangdaPrincipal): List<GrantedAuthority>
}