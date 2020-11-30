package com.fangda.erp.rmms.stfg.db.dataobject

import java.time.LocalDateTime
import kotlin.properties.Delegates

/**
 * @author yuhb
 * @date 2020/11/10
 */
class DePackDataDO {
    var rawMachineId: String by Delegates.notNull()
    var state: Int by Delegates.notNull()
    var createTime: LocalDateTime? = null
    var startTime: LocalDateTime? = null
    var endTime: LocalDateTime? = null
    var creator: String? = null
    var checkOperator: String? = null
    var startOperator: String? = null
}