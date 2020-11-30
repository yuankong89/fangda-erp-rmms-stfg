package com.fangda.erp.rmms.stfg.bo.unload

import com.fangda.erp.rmms.stfg.bo.Machine
import java.time.LocalDateTime

/**
 * @author yuhb
 * @date 2020/11/25
 */
open class UnloadingMachine : Machine {
    override var rawMachineId: String = ""
    override var liscenceNo: String = ""
    override var flowNo: String = ""
    override var rawCode: String = ""
    override var levelCode: String = ""
    override var rawName: String = ""

    // 今日排序
    var daySort: Int = Int.MIN_VALUE

    // 磅单
    var grossWeight: Double = Double.MIN_VALUE
    lateinit var grossWeightTime: LocalDateTime

    // 预报料型
    var preRawLevel: String = ""
}