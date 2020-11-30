package com.fangda.erp.rmms.stfg.dto.unload

/**
 * @author yuhb
 * @date 2020/11/23
 */
class UnloadedMachineDto {
    var rawMachineId: String = ""
    var liscenceNo: String = ""
    var flowNo: String = ""
    var rawCode: String = ""
    var levelCode: String = ""
    var rawName: String = ""

    var grossWeight: String = ""
    var grossWeightTime: String = ""

    var selfChecked: Boolean = false
    var unloadTime: String = ""
}