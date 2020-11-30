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

    var state: DePackMachineEnum = DePackMachineEnum.Need

    enum class DePackMachineEnum constructor(val value: Int, val stateStr: String) {
        Need(1, "待解包"), Start(2, "解包中"), END(0, "完成解包");

        companion object {
            fun valueOf(value: Int): DePackMachineEnum {
                return DePackMachineEnum.values().find { it.value == value }!!
            }
        }
    }
}