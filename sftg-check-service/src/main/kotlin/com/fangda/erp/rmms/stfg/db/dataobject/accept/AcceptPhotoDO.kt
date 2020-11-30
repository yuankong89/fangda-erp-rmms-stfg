package com.fangda.erp.rmms.stfg.db.dataobject.accept

import kotlin.properties.Delegates

/**
 * @author yuhb
 * @date 2020/11/11
 */
class AcceptPhotoDO {
    var rawMachineId: String by Delegates.notNull()
    var no: Int by Delegates.notNull()
    var photo: ByteArray by Delegates.notNull()
    val type: Int = 1   // 验收的type字段默认为1
}