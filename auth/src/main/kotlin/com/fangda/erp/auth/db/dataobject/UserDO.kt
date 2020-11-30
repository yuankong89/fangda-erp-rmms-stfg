/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.auth.db.dataobject

import kotlin.properties.Delegates

/**
 * @author yuhb
 * @date 2020/9/1
 */
class UserDO {
    var username: String by Delegates.notNull()
    var opId: String by Delegates.notNull()
    var dpId: String by Delegates.notNull()
    var account: String by Delegates.notNull()
    var password: String by Delegates.notNull()
    var telephone: String by Delegates.notNull()
}