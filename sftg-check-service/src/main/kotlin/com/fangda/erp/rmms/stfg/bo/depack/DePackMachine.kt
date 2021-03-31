package com.fangda.erp.rmms.stfg.bo.depack

import com.fangda.erp.rmms.stfg.bo.Machine

/**
 * @author yuhb
 * @date 2020/11/11
 */
class DePackMachine : Machine {
    override var rawMachineId: String = ""
    override var liscenceNo: String = ""
    override var flowNo: String = ""
    override var rawCode: String = ""
    override var levelCode: String = ""
    override var rawName: String = ""

    var state: DePackMachineEnum = DePackMachineEnum.NEED

    enum class DePackMachineEnum constructor(val value: Int, val stateStr: String) {
        NEED(1, "待解包"),
        START(2, "解包中"),
        END(3, "完成解包"),
        PASS(0, "审核通过");

        companion object {
            fun valueOf(value: Int): DePackMachineEnum {
                return values().find { it.value == value }!!
            }
        }
    }
}