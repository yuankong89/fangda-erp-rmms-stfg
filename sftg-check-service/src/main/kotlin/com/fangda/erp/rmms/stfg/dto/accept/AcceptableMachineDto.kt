package com.fangda.erp.rmms.stfg.dto.accept

/**
 * @author yuhb
 * @date 2020/11/23
 * 可以验收的车辆
 */
open class AcceptableMachineDto {
    var daySort: String = ""
    var rawMachineId: String = ""
    var liscenceNo: String = ""
    var flowNo: String = ""
    var rawCode: String = ""
    var levelCode: String = ""
    var rawName: String = ""
    var grossWeight: String = ""
    var grossWeightTime: String = ""
    var preRawLevel: String = ""
}