package com.fangda.erp.rmms.stfg.db.dataobject.unload

import kotlin.properties.Delegates

/**
 * @author yuhb
 * @date 2020/11/11
 */
class UnloadPhotoDO {
    var rawMachineId: String by Delegates.notNull()
    var no: Int by Delegates.notNull()
    var photo: ByteArray by Delegates.notNull()
    val type: Int = 2   // 卸车的type字段默认为2
}