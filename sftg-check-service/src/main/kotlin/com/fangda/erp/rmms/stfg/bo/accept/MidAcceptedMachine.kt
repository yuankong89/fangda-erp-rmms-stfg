package com.fangda.erp.rmms.stfg.bo.accept

import com.fangda.erp.rmms.stfg.bo.Machine
import java.time.LocalDateTime

/**
 * @author yuhb
 * @date 2020/11/25
 */
class MidAcceptedMachine : Machine {
    override var rawMachineId: String = ""
    override var liscenceNo: String = ""
    override var flowNo: String = ""
    override var rawCode: String = ""
    override var levelCode: String = ""
    override var rawName: String = ""

    lateinit var submitTime: LocalDateTime
    lateinit var acceptTime: LocalDateTime
    var acceptOperator: String = ""
}