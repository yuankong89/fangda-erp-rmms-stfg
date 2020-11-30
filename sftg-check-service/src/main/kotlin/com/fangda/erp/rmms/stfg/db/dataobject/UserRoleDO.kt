package com.fangda.erp.rmms.stfg.db.dataobject

import kotlin.properties.Delegates

/**
 * @author yuhb
 * @date 2020/11/24
 */
class UserRoleDO {
    var userId: String by Delegates.notNull()
    var roleId: String by Delegates.notNull()
    var roleName: String by Delegates.notNull()
}