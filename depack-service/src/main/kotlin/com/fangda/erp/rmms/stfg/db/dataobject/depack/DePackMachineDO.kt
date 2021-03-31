package com.fangda.erp.rmms.stfg.db.dataobject.depack

import java.time.LocalDateTime
import kotlin.properties.Delegates

/**
 * @author yuhb
 * @date 2020/11/11
 */
class DePackMachineDO {
    var rawMachineId: String by Delegates.notNull()
    var flowNo: String by Delegates.notNull()
    var liscenceNo: String by Delegates.notNull()
    var state: Int by Delegates.notNull()
    var createTime: LocalDateTime? = null
}