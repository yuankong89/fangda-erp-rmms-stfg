package com.fangda.erp.rmms.stfg.bo

import java.time.LocalDateTime

/**
 * @author yuhb
 * @date 2021/3/10
 */
class RefundMachine {
    var rawMachineId: String = ""
    var liscenceNo: String = ""
    var flowNo: String = ""
    var refundDate: LocalDateTime? = null
    var refundOper: String = ""
    var refundReason: String = ""
}