package com.fangda.erp.rmms.stfg.bo.accept

import com.fangda.erp.rmms.stfg.bo.Machine
import java.time.LocalDateTime

/**
 * @author yuhb
 * @date 2020/11/25
 */
class AcceptedMachine : Machine {
    override var rawMachineId: String = ""
    override var liscenceNo: String = ""
    override var flowNo: String = ""
    override var rawCode: String = ""
    override var levelCode: String = ""
    override var rawName: String = ""

    // 磅单
    var grossWeight: Double = Double.MIN_VALUE
    lateinit var grossWeightTime: LocalDateTime

    // 卸车人员备注(部分压块有数据)
    var unloadRemark: String = ""

    // 自我检查
    var selfChecked: Boolean = false
}