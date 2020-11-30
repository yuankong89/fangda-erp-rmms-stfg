/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.authlibs.principal

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * @author yuhb
 * @date 2020/6/8
 */
class FangdaPrincipal(private var username: String, private var password: String) : UserDetails {
    constructor(username: String) : this(username, "")

    var userId: String = ""
    var depId: String = ""
    var telephone: String = ""

    private var authorities: MutableList<GrantedAuthority> = mutableListOf()
    private var enabled: Boolean = true
    private var credentialsNonExpired: Boolean = true
    private var accountNonExpired: Boolean = true
    private var accountNonLocked: Boolean = true

    override fun getUsername(): String = username

    override fun getPassword(): String = password

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities

    fun setAuthorities(authorities: Collection<GrantedAuthority>) = this.authorities.addAll(authorities)

    override fun isEnabled(): Boolean = enabled

    override fun isCredentialsNonExpired(): Boolean = credentialsNonExpired

    override fun isAccountNonExpired(): Boolean = accountNonExpired

    override fun isAccountNonLocked(): Boolean = accountNonLocked

}