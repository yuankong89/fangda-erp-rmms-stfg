package com.fangda.erp.rmms.stfg.bo

import java.time.LocalDateTime

/**
 * @author yuhb
 * @date 2020/11/11
 */
class DePackMachine {
    var rawMachineId: String = ""
    var liscenceNo: String = ""
    var flowNo: String = ""
    var state: DePackMachineEnum = DePackMachineEnum.Need
    var createTime: LocalDateTime? = null

    enum class DePackMachineEnum constructor(val value: Int, val stateStr: String) {
        Need(1, "待解包"),
        Start(2, "解包中"),
        End(3, "完成解包"),
        Pass(0, "通过");

        companion object {
            fun valueOf(value: Int): DePackMachineEnum {
                return DePackMachineEnum.values().find { it.value == value }!!
            }
        }
    }
}