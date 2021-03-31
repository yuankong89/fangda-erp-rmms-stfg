package com.fangda.erp.rmms.stfg.db.dataobject.refund

import kotlin.properties.Delegates

/**
 * @author yuhb
 * @date 2020/11/11
 */
class RefundCheckPhotoDO {
    var rawMachineId: String by Delegates.notNull()
    var no: Int by Delegates.notNull()
    var photo: ByteArray by Delegates.notNull()
}