package com.fangda.erp.rmms.stfg.bo.accept

import java.time.LocalDateTime

/**
 * @author yuhb
 * @date 2020/11/25
 */
class AcceptData {
    var rawMachineId: String = ""
    var acceptPos: String = ""
    lateinit var acceptDate: LocalDateTime
    var acceptRemark: String = ""
    lateinit var details: List<AcceptDataDetail>

    var dePackPhotoCount: Int = Int.MIN_VALUE
    var acceptPhotoCount: Int = Int.MIN_VALUE
}