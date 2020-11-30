package com.fangda.erp.rmms.stfg.db.query

import java.time.LocalDateTime

/**
 * @author yuhb
 * @date 2020/11/11
 */
class DePackMachineQuery {
    var stateIn: List<String>? = null
    var supplierId: String? = null
    var startTime: LocalDateTime? = null
    var endTime: LocalDateTime? = null
}