/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.auth.manager

import com.fangda.erp.auth.db.dao.UserDao
import com.fangda.erp.authlibs.principal.FangdaPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author yuhb
 * @date 2020/9/1
 */
@Service
class RmmsUserManager @Autowired constructor(private val userDao: UserDao) {
    fun getRmmsUserByAccount(account: String): FangdaPrincipal {
        val userDO = userDao.selectByAccount(account)
        return FangdaPrincipal(userDO.username, "{noop}${userDO.password}").apply {
            userId = userDO.opId
            depId = userDO.dpId
            telephone = userDO.telephone
        }
    }
}