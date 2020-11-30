package com.fangda.erp.rmms.stfg.bo.unload

import com.fangda.erp.rmms.stfg.bo.Machine
import java.time.LocalDateTime

/**
 * @author yuhb
 * @date 2020/11/25
 */
class UnloadedMachine : Machine {
    override var rawMachineId: String = ""
    override var liscenceNo: String = ""
    override var flowNo: String = ""
    override var rawCode: String = ""
    override var levelCode: String = ""
    override var rawName: String = ""

    // 磅单
    var grossWeight: Double = Double.MIN_VALUE
    lateinit var grossWeightTime: LocalDateTime

    // 自我检查
    var selfChecked: Boolean = false

    // 验收时间
    lateinit var unloadTime: LocalDateTime
}