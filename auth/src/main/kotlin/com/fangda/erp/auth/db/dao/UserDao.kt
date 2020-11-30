/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.auth.db.dao

import com.fangda.erp.auth.db.dataobject.UserDO
import org.apache.ibatis.annotations.Mapper

/**
 * @author yuhb
 * @date 2020/9/1
 */
@Mapper
interface UserDao {
    fun selectByAccount(account: String): UserDO
}