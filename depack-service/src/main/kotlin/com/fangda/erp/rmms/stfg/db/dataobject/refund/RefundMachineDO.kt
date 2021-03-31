package com.fangda.erp.rmms.stfg.db.dataobject.refund

import java.time.LocalDateTime

/**
 * @author yuhb
 * @date 2021/3/10
 */
class RefundMachineDO {
    var rawMachineId: String? = null
    var liscenceNo: String? = null
    var flowNo: String? = null
    var refundDate: LocalDateTime? = null
    var refundOper: String? = null
    var refundReason: String? = null
}