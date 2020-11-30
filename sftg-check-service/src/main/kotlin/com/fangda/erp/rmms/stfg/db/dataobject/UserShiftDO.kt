package com.fangda.erp.rmms.stfg.db.dataobject

import kotlin.properties.Delegates

/**
 * @author yuhb
 * @date 2020/11/24
 */
class UserShiftDO {
    var userId: String by Delegates.notNull()
    var role: Int by Delegates.notNull()
    var shift: Int by Delegates.notNull()
}