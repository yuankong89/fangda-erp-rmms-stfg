package com.fangda.erp.rmms.stfg.bo

import kotlin.properties.Delegates

/**
 * @author yuhb
 * @date 2020/11/11
 */
class DePackData {
    var rawMachineId: String = ""
    var dePackPhotoCount: Int = 0
    var details: List<DePackDataDetail> by Delegates.notNull()
}