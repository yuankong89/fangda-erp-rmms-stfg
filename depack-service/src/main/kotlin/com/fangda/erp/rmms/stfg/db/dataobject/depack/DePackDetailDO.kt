package com.fangda.erp.rmms.stfg.db.dataobject.depack

import kotlin.properties.Delegates

/**
 * @author yuhb
 * @date 2020/11/11
 */
class DePackDetailDO {
    var rawMachineId: String by Delegates.notNull()
    var dataNo: String by Delegates.notNull()
    var dataName: String by Delegates.notNull()
    var dataValue: Double by Delegates.notNull()
}