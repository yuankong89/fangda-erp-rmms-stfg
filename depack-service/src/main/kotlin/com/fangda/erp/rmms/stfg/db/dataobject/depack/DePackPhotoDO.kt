package com.fangda.erp.rmms.stfg.db.dataobject.depack

import kotlin.properties.Delegates

/**
 * @author yuhb
 * @date 2020/11/11
 */
class DePackPhotoDO {
    var rawMachineId: String by Delegates.notNull()
    var no: Int by Delegates.notNull()
    var photo: ByteArray by Delegates.notNull()
    var type: Int = 3   // 解包的type字段默认为3
}