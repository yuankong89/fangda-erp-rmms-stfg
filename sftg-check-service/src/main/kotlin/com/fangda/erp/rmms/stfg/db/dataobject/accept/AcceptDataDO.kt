package com.fangda.erp.rmms.stfg.db.dataobject.accept

import java.time.LocalDateTime
import kotlin.properties.Delegates

/**
 * @author yuhb
 * @date 2020/11/25
 */
class AcceptDataDO {
    var rawMachineId: String by Delegates.notNull()
    var acceptPos: String by Delegates.notNull()
    var checkTime: LocalDateTime by Delegates.notNull()
    var remark: String? = null
    var operator: String? = null
}